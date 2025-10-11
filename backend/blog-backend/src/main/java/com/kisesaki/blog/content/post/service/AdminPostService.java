package com.kisesaki.blog.content.post.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.markdown.MarkdownService;
import com.kisesaki.blog.common.util.SlugGenerator;
import com.kisesaki.blog.content.category.entity.Categories;
import com.kisesaki.blog.content.category.mapper.CategoriesMapper;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminCreatePostRequest;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostBatchDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostQueryDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostStatsDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostStatusDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminUpdatePostRequest;
import com.kisesaki.blog.content.post.entity.PostTags;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostTagsMapper;
import com.kisesaki.blog.content.post.mapper.PostsMapper;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章管理服务 - 管理员专用
 * 提供管理员级别的文章管理功能
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminPostService {

    private final PostsMapper postsMapper;
    private final PostTagsMapper postTagsMapper;
    private final CategoriesMapper categoriesMapper;
    private final UserMapper userMapper;
    private final MarkdownService markdownService;

    /**
     * 获取所有文章列表（管理员视图）
     *
     * @param params 查询参数
     * @return 文章列表
     */
    public PageResponse<AdminPostQueryDto.AdminPostListResponse> getAdminPostsList(
            AdminPostQueryDto.AdminPostListParams params) {
        log.info("管理员获取文章列表：{}", params);

        // 计算总数
        long totalCount = postsMapper.countAdminPosts(params);

        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        // 创建分页对象
        Page<AdminPostQueryDto.AdminPostListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize());

        // 执行分页查询
        page = postsMapper.selectAdminPostsPage(page, params);

        return PageResponse.of(page.getRecords(), totalCount, params.getPageable());
    }

    /**
     * 管理员创建文章（可指定作者）
     *
     * @param request 创建请求
     * @return 创建的文章ID
     */
    @Transactional
    public Long createPostAsAdmin(AdminCreatePostRequest request) {
        log.info("管理员创建文章：{}", request);

        // 验证作者存在
        User author = userMapper.selectById(request.getAuthorId());
        if (author == null) {
            throw BusinessException.notFound("指定的作者");
        }

        // 验证分类存在
        Categories category = categoriesMapper.selectById(request.getCategoryId());
        if (category == null) {
            throw BusinessException.notFound("分类");
        }

        // 生成slug（如果未提供）
        String slug = request.getSlug();
        if (!StringUtils.hasText(slug)) {
            slug = SlugGenerator.generateFromTitle(request.getTitle());
        }

        // 检查slug唯一性
        validateSlugUniqueness(slug, null);

        // 创建文章实体
        Posts post = new Posts();
        post.setAuthorId(request.getAuthorId());
        post.setCategoryId(request.getCategoryId());
        post.setTitle(request.getTitle());
        post.setSlug(slug);
        post.setExcerpt(request.getExcerpt());
        post.setContent(request.getContent());

        // 渲染HTML内容
        if (StringUtils.hasText(request.getContent())) {
            post.setHtmlContent(markdownService.convertToHtml(request.getContent()));
        }

        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setFeaturedImageUrl(request.getFeaturedImageUrl());
        post.setStatus(request.getStatus());
        post.setVisibility(request.getVisibility());
        post.setPassword(request.getPassword());
        post.setIsFeatured(request.getIsFeatured());
        post.setIsTop(request.getIsTop());
        post.setAllowComments(request.getAllowComments());

        // SEO信息处理
        post.setSeoTitle(StringUtils.hasText(request.getSeoTitle()) ? request.getSeoTitle() : request.getTitle());
        post.setSeoDescription(
                StringUtils.hasText(request.getSeoDescription()) ? request.getSeoDescription() : request.getExcerpt());
        post.setSeoKeywords(request.getSeoKeywords());

        post.setScheduledAt(request.getScheduledAt());

        OffsetDateTime now = OffsetDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setLastModifiedAt(now);

        // 如果要立即发布
        if (Boolean.TRUE.equals(request.getPublishNow()) || "published".equals(request.getStatus())) {
            post.setStatus("published");
            post.setPublishedAt(now);
        }

        // 保存文章
        postsMapper.insert(post);
        log.info("管理员创建文章成功，文章ID：{}", post.getId());

        // 保存标签关联
        savePostTags(post.getId(), request.getTagIds());

        return post.getId();
    }

    /**
     * 管理员更新文章
     *
     * @param id      文章ID
     * @param request 更新请求
     * @return 更新结果
     */
    @Transactional
    public void updatePostAsAdmin(Long id, AdminUpdatePostRequest request) {
        log.info("管理员更新文章 {}：{}", id, request);

        // 检查文章是否存在
        Posts existingPost = postsMapper.selectById(id);
        if (existingPost == null) {
            throw BusinessException.postNotFound();
        }

        // 验证新作者（如果指定）
        if (request.getAuthorId() != null) {
            User author = userMapper.selectById(request.getAuthorId());
            if (author == null) {
                throw BusinessException.notFound("指定的作者");
            }
        }

        // 验证分类存在
        if (request.getCategoryId() != null) {
            Categories category = categoriesMapper.selectById(request.getCategoryId());
            if (category == null) {
                throw BusinessException.notFound("分类");
            }
        }

        // 处理slug
        String slug = request.getSlug();
        if (StringUtils.hasText(slug) && !slug.equals(existingPost.getSlug())) {
            validateSlugUniqueness(slug, id);
        }

        // 更新文章
        Posts post = new Posts();
        post.setId(id);

        if (request.getAuthorId() != null) {
            post.setAuthorId(request.getAuthorId());
        }
        if (request.getCategoryId() != null) {
            post.setCategoryId(request.getCategoryId());
        }
        if (StringUtils.hasText(request.getTitle())) {
            post.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(slug)) {
            post.setSlug(slug);
        }
        if (request.getExcerpt() != null) {
            post.setExcerpt(request.getExcerpt());
        }
        if (StringUtils.hasText(request.getContent())) {
            post.setContent(request.getContent());
            post.setHtmlContent(markdownService.convertToHtml(request.getContent()));
        }

        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setFeaturedImageUrl(request.getFeaturedImageUrl());

        if (StringUtils.hasText(request.getStatus())) {
            post.setStatus(request.getStatus());
        }
        if (StringUtils.hasText(request.getVisibility())) {
            post.setVisibility(request.getVisibility());
        }
        post.setPassword(request.getPassword());

        if (request.getIsFeatured() != null) {
            post.setIsFeatured(request.getIsFeatured());
        }
        if (request.getIsTop() != null) {
            post.setIsTop(request.getIsTop());
        }
        if (request.getAllowComments() != null) {
            post.setAllowComments(request.getAllowComments());
        }

        post.setSeoTitle(request.getSeoTitle());
        post.setSeoDescription(request.getSeoDescription());
        post.setSeoKeywords(request.getSeoKeywords());
        post.setScheduledAt(request.getScheduledAt());

        post.setUpdatedAt(OffsetDateTime.now());
        post.setLastModifiedAt(OffsetDateTime.now());

        postsMapper.updateById(post);

        // 更新标签关联
        if (request.getTagIds() != null) {
            savePostTags(id, request.getTagIds());
        }

        log.info("管理员更新文章 {} 成功", id);
    }

    /**
     * 管理员删除文章（软删除）
     *
     * @param id 文章ID
     */
    @Transactional
    public void deletePostAsAdmin(Long id) {
        log.info("管理员删除文章：{}", id);

        Posts existingPost = postsMapper.selectById(id);
        if (existingPost == null) {
            throw BusinessException.postNotFound();
        }

        // 软删除：更新状态为deleted
        Posts post = new Posts();
        post.setId(id);
        post.setStatus("deleted");
        post.setUpdatedAt(OffsetDateTime.now());

        postsMapper.updateById(post);
        log.info("管理员删除文章 {} 成功", id);
    }

    /**
     * 更新文章状态
     *
     * @param id      文章ID
     * @param request 状态更新请求
     */
    @Transactional
    public void updatePostStatus(Long id, AdminPostStatusDto.UpdateStatusRequest request) {
        log.info("管理员更新文章 {} 状态为：{}", id, request.getStatus());

        Posts existingPost = postsMapper.selectById(id);
        if (existingPost == null) {
            throw BusinessException.postNotFound();
        }

        Posts post = new Posts();
        post.setId(id);
        post.setStatus(request.getStatus());
        post.setUpdatedAt(OffsetDateTime.now());

        // 如果状态改为published，设置发布时间
        if ("published".equals(request.getStatus()) && existingPost.getPublishedAt() == null) {
            post.setPublishedAt(OffsetDateTime.now());
        }

        postsMapper.updateById(post);
        log.info("管理员更新文章 {} 状态成功", id);
    }

    /**
     * 设置/取消精选
     *
     * @param id      文章ID
     * @param request 精选设置请求
     */
    @Transactional
    public void setPostFeatured(Long id, AdminPostStatusDto.SetFeaturedRequest request) {
        log.info("管理员设置文章 {} 精选状态为：{}", id, request.getIsFeatured());

        Posts existingPost = postsMapper.selectById(id);
        if (existingPost == null) {
            throw BusinessException.postNotFound();
        }

        Posts post = new Posts();
        post.setId(id);
        post.setIsFeatured(request.getIsFeatured());
        post.setUpdatedAt(OffsetDateTime.now());

        postsMapper.updateById(post);
        log.info("管理员设置文章 {} 精选状态成功", id);
    }

    /**
     * 设置/取消置顶
     *
     * @param id      文章ID
     * @param request 置顶设置请求
     */
    @Transactional
    public void setPostTop(Long id, AdminPostStatusDto.SetTopRequest request) {
        log.info("管理员设置文章 {} 置顶状态为：{}", id, request.getIsTop());

        Posts existingPost = postsMapper.selectById(id);
        if (existingPost == null) {
            throw BusinessException.postNotFound();
        }

        Posts post = new Posts();
        post.setId(id);
        post.setIsTop(request.getIsTop());
        post.setUpdatedAt(OffsetDateTime.now());

        postsMapper.updateById(post);
        log.info("管理员设置文章 {} 置顶状态成功", id);
    }

    /**
     * 转移文章作者
     *
     * @param id      文章ID
     * @param request 作者转移请求
     */
    @Transactional
    public void transferPostAuthor(Long id, AdminPostStatusDto.TransferAuthorRequest request) {
        log.info("管理员转移文章 {} 作者为：{}", id, request.getAuthorId());

        Posts existingPost = postsMapper.selectById(id);
        if (existingPost == null) {
            throw BusinessException.postNotFound();
        }

        // 验证新作者存在
        User newAuthor = userMapper.selectById(request.getAuthorId());
        if (newAuthor == null) {
            throw BusinessException.notFound("指定的作者");
        }

        Posts post = new Posts();
        post.setId(id);
        post.setAuthorId(request.getAuthorId());
        post.setUpdatedAt(OffsetDateTime.now());

        postsMapper.updateById(post);
        log.info("管理员转移文章 {} 作者成功", id);
    }

    /**
     * 批量删除文章
     *
     * @param request 批量删除请求
     */
    @Transactional
    public void batchDeletePosts(AdminPostBatchDto.BatchDeleteRequest request) {
        log.info("管理员批量删除文章：{}", request.getIds());

        if (request.getIds().isEmpty()) {
            throw BusinessException.paramError("文章ID列表不能为空");
        }

        // 批量更新状态为deleted
        LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<Posts>()
                .in(Posts::getId, request.getIds())
                .set(Posts::getStatus, "deleted")
                .set(Posts::getUpdatedAt, OffsetDateTime.now());

        postsMapper.update(null, updateWrapper);
        log.info("管理员批量删除文章成功，共删除 {} 篇", request.getIds().size());
    }

    /**
     * 批量更新状态
     *
     * @param request 批量状态更新请求
     */
    @Transactional
    public void batchUpdateStatus(AdminPostBatchDto.BatchUpdateStatusRequest request) {
        log.info("管理员批量更新文章状态：{} -> {}", request.getIds(), request.getStatus());

        if (request.getIds().isEmpty()) {
            throw BusinessException.paramError("文章ID列表不能为空");
        }

        LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<Posts>()
                .in(Posts::getId, request.getIds())
                .set(Posts::getStatus, request.getStatus())
                .set(Posts::getUpdatedAt, OffsetDateTime.now());

        // 如果状态改为published，需要设置发布时间
        if ("published".equals(request.getStatus())) {
            updateWrapper.set(Posts::getPublishedAt, OffsetDateTime.now());
        }

        postsMapper.update(null, updateWrapper);
        log.info("管理员批量更新文章状态成功，共更新 {} 篇", request.getIds().size());
    }

    /**
     * 批量转移作者
     *
     * @param request 批量作者转移请求
     */
    @Transactional
    public void batchTransferAuthor(AdminPostBatchDto.BatchTransferAuthorRequest request) {
        log.info("管理员批量转移文章作者：{} -> {}", request.getIds(), request.getAuthorId());

        if (request.getIds().isEmpty()) {
            throw BusinessException.paramError("文章ID列表不能为空");
        }

        // 验证新作者存在
        User newAuthor = userMapper.selectById(request.getAuthorId());
        if (newAuthor == null) {
            throw BusinessException.notFound("指定的作者");
        }

        LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<Posts>()
                .in(Posts::getId, request.getIds())
                .set(Posts::getAuthorId, request.getAuthorId())
                .set(Posts::getUpdatedAt, OffsetDateTime.now());

        postsMapper.update(null, updateWrapper);
        log.info("管理员批量转移文章作者成功，共转移 {} 篇", request.getIds().size());
    }

    /**
     * 批量设置精选
     *
     * @param request 批量精选设置请求
     */
    @Transactional
    public void batchSetFeatured(AdminPostBatchDto.BatchSetFeaturedRequest request) {
        log.info("管理员批量设置精选：{} -> {}", request.getIds(), request.getIsFeatured());

        if (request.getIds().isEmpty()) {
            throw BusinessException.paramError("文章ID列表不能为空");
        }

        LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<Posts>()
                .in(Posts::getId, request.getIds())
                .set(Posts::getIsFeatured, request.getIsFeatured())
                .set(Posts::getUpdatedAt, OffsetDateTime.now());

        postsMapper.update(null, updateWrapper);
        log.info("管理员批量设置精选成功，共设置 {} 篇", request.getIds().size());
    }

    /**
     * 批量设置置顶
     *
     * @param request 批量置顶设置请求
     */
    @Transactional
    public void batchSetTop(AdminPostBatchDto.BatchSetTopRequest request) {
        log.info("管理员批量设置置顶：{} -> {}", request.getIds(), request.getIsTop());

        if (request.getIds().isEmpty()) {
            throw BusinessException.paramError("文章ID列表不能为空");
        }

        LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<Posts>()
                .in(Posts::getId, request.getIds())
                .set(Posts::getIsTop, request.getIsTop())
                .set(Posts::getUpdatedAt, OffsetDateTime.now());

        postsMapper.update(null, updateWrapper);
        log.info("管理员批量设置置顶成功，共设置 {} 篇", request.getIds().size());
    }

    /**
     * 获取文章统计数据
     *
     * @return 统计数据
     */
    public AdminPostStatsDto.PostStatsResponse getPostStats() {
        log.info("管理员获取文章统计数据");

        // 使用专门的统计查询方法
        return postsMapper.getPostStats();
    }

    // ========== 私有辅助方法 ==========

    /**
     * 验证slug唯一性
     */
    private void validateSlugUniqueness(String slug, Long excludeId) {
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<Posts>()
                .eq(Posts::getSlug, slug);

        if (excludeId != null) {
            queryWrapper.ne(Posts::getId, excludeId);
        }

        if (postsMapper.selectCount(queryWrapper) > 0) {
            throw BusinessException.slugAlreadyExists(slug);
        }
    }

    /**
     * 保存文章标签关联
     */
    private void savePostTags(Long postId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }

        // 先删除现有关联
        LambdaQueryWrapper<PostTags> deleteWrapper = new LambdaQueryWrapper<PostTags>()
                .eq(PostTags::getPostId, postId);
        postTagsMapper.delete(deleteWrapper);

        // 插入新关联
        for (Long tagId : tagIds) {
            PostTags postTag = new PostTags();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTagsMapper.insert(postTag);
        }
    }
}
