package com.kisesaki.blog.content.post.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.markdown.MarkdownService;
import com.kisesaki.blog.common.util.SlugGenerator;
import com.kisesaki.blog.content.category.entity.Categories;
import com.kisesaki.blog.content.category.mapper.CategoriesMapper;
import com.kisesaki.blog.content.post.dto.BasePostDto;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostResponse;
import com.kisesaki.blog.content.post.dto.PostCommand.UpdatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.UpdatePostResponse;
import com.kisesaki.blog.content.post.entity.PostTags;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostTagsMapper;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCommandService {

    private final PostsMapper postsMapper;
    private final PostTagsMapper postTagsMapper;
    private final CategoriesMapper categoriesMapper;
    private final MarkdownService markdownService;

    /**
     * 创建文章
     *
     * @param request 创建文章请求DTO
     * @param userId  当前用户ID
     * @return 创建文章响应DTO
     */
    // TODO 定时发布未完成
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<CreatePostResponse> createPost(CreatePostRequest request, Long userId) {
        try {
            // 1. 参数验证
            validateCreateOrUpdatePostRequest(request);

            // 2. 验证分类是否存在
            validateCategoryExists(request.getCategoryId());

            OffsetDateTime now = OffsetDateTime.now();

            Posts post = new Posts();
            post.setAuthorId(userId);
            post.setCategoryId(request.getCategoryId());
            post.setTitle(request.getTitle());

            // 3. 生成或使用用户提供的slug，确保唯一性
            post.setSlug(generateUniqueSlug(request.getSlug(), request.getTitle()));

            post.setExcerpt(request.getExcerpt());
            post.setContent(request.getContent());

            // 4. 生成HTML内容
            post.setHtmlContent(convertMarkdownToHtml(request.getContent()));

            // 5. 计算阅读时间和字数统计
            post.setReadingTime(markdownService.estimateReadingTime(request.getContent()));
            post.setWordCount(markdownService.countWords(request.getContent()));

            post.setCoverImageUrl(request.getCoverImageUrl());
            post.setFeaturedImageUrl(request.getFeaturedImageUrl());
            post.setStatus(Boolean.TRUE.equals(request.getPublishNow()) ? "published" : "draft");
            post.setVisibility(request.getVisibility() == null ? "public" : request.getVisibility());
            post.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
            post.setIsTop(request.getIsTop() != null && request.getIsTop());
            post.setAllowComments(request.getAllowComments() != null && request.getAllowComments());
            post.setCreatedAt(now);
            post.setUpdatedAt(now);

            // 6. 生成SEO相关字段
            generateSeoFields(post, request);

            // 7. 处理密码保护逻辑
            handlePasswordProtection(post, request);

            // 8. 设置发布时间
            if (request.getScheduledAt() == null && Boolean.TRUE.equals(request.getPublishNow())) {
                post.setPublishedAt(now);
            }

            // 9. 插入数据库
            try {
                postsMapper.insert(post);
            } catch (DataIntegrityViolationException e) {
                handleDataIntegrityViolation(e, post.getSlug());
            }

            // 获取插入后的ID
            Long postId = post.getId();

            // 10. 处理标签关联
            handlePostTags(postId, request.getTagIds());

            // 11. 更新分类文章数量
            incrementCategoryPostCount(request.getCategoryId());

            return ApiResponse.success(CreatePostResponse.fromEntity(post));

        } catch (BusinessException e) {
            log.warn("创建文章业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("创建文章系统异常", e);
            return ApiResponse.error("创建文章失败，请稍后重试");
        }
    }

    /**
     * 更新文章
     * 
     * @param request 更新文章请求DTO
     * @param userId  当前用户ID
     * @return 更新文章响应DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<UpdatePostResponse> updatePost(Long postId, UpdatePostRequest request, Long userId) {
        try {
            // 首先获取文章，确保存在且属于当前用户
            LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Posts::getId, postId);
            queryWrapper.eq(Posts::getAuthorId, userId);
            Posts existingPost = postsMapper.selectOne(queryWrapper);
            if (existingPost == null) {
                return ApiResponse.error("文章不存在或无权限修改");
            }

            // 1. 参数验证
            validateCreateOrUpdatePostRequest(request);

            // 2. 验证分类是否存在
            validateCategoryExists(request.getCategoryId());

            OffsetDateTime now = OffsetDateTime.now();

            // 3. 更新文章字段
            existingPost.setCategoryId(request.getCategoryId());
            existingPost.setTitle(request.getTitle());

            // 4. 处理slug更新，确保唯一性
            String newSlug = generateUniqueSlug(request.getSlug(), request.getTitle());
            if (!newSlug.equals(existingPost.getSlug())) {
                existingPost.setSlug(newSlug);
            }

            existingPost.setExcerpt(request.getExcerpt());
            existingPost.setContent(request.getContent());

            // 5. 生成HTML内容
            existingPost.setHtmlContent(convertMarkdownToHtml(request.getContent()));

            // 6. 计算阅读时间和字数统计
            existingPost.setReadingTime(markdownService.estimateReadingTime(request.getContent()));
            existingPost.setWordCount(markdownService.countWords(request.getContent()));

            existingPost.setStatus(request.getStatus() != null ? request.getStatus() : existingPost.getStatus());
            existingPost.setVisibility(request.getVisibility() == null ? "public" : request.getVisibility());
            existingPost.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
            existingPost.setIsTop(request.getIsTop() != null && request.getIsTop());
            existingPost.setAllowComments(request.getAllowComments() != null && request.getAllowComments());
            existingPost.setUpdatedAt(now);

            // 7. 生成SEO相关字段
            generateSeoFields(existingPost, request);

            // 8. 处理密码保护逻辑
            handlePasswordProtection(existingPost, request);

            // 9. 设置发布时间
            if (request.getScheduledAt() == null && "published".equals(request.getStatus())) {
                existingPost.setPublishedAt(now);
            }

            // 10. 更新数据库
            try {
                postsMapper.updateById(existingPost);
            } catch (DataIntegrityViolationException e) {
                handleDataIntegrityViolation(e, existingPost.getSlug());
            }

            // 11. 处理标签关联更新
            updatePostTags(existingPost.getId(), request.getTagIds());

            // 12. 更新分类文章数量
            updateCategoryPostCount(existingPost.getId(), existingPost.getCategoryId(), request.getCategoryId());

            // 13. 构建响应
            UpdatePostResponse response = new UpdatePostResponse();
            response.setId(existingPost.getId());
            response.setTitle(existingPost.getTitle());
            response.setSlug(existingPost.getSlug());
            response.setStatus(existingPost.getStatus());
            response.setVisibility(existingPost.getVisibility());
            response.setRevisionCreated(Boolean.FALSE); // 暂时不支持版本控制
            response.setCurrentVersion(1); // 暂时不支持版本控制
            response.setLastModifiedAt(existingPost.getUpdatedAt());
            response.setUpdatedAt(existingPost.getUpdatedAt());

            return ApiResponse.success(response);

        } catch (BusinessException e) {
            log.warn("更新文章业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("更新文章系统异常", e);
            return ApiResponse.error("更新文章失败，请稍后重试");
        }
    }

    public ApiResponse<Void> deletePost(Long postId, Long userId) {
        try {
            LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Posts::getId, postId);
            queryWrapper.eq(Posts::getAuthorId, userId);
            Posts existingPost = postsMapper.selectOne(queryWrapper);
            if (existingPost == null) {
                return ApiResponse.error("文章不存在或无权限删除");
            }
            existingPost.setStatus("deleted");
            existingPost.setUpdatedAt(OffsetDateTime.now());
            postsMapper.updateById(existingPost);

            return ApiResponse.success("删除文章成功");
        } catch (Exception e) {
            log.error("删除文章系统异常", e);
            return ApiResponse.error("删除文章失败，请稍后重试");
        }
    }

    /**
     * 验证创建或更新文章请求参数
     */
    private void validateCreateOrUpdatePostRequest(BasePostDto request) {
        if (!StringUtils.hasText(request.getTitle())) {
            throw BusinessException.paramError("文章标题不能为空");
        }

        if (!StringUtils.hasText(request.getContent())) {
            throw BusinessException.paramError("文章内容不能为空");
        }

        // 验证可见性设置
        if (request.getVisibility() != null &&
                !List.of("public", "private", "password_protected").contains(request.getVisibility())) {
            throw BusinessException.invalidVisibility(request.getVisibility());
        }
    }

    /**
     * 验证分类是否存在
     */
    private void validateCategoryExists(Long categoryId) {
        if (categoryId == null) {
            return; // 允许不指定分类
        }

        LambdaQueryWrapper<Categories> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Categories::getId, categoryId);

        if (categoriesMapper.selectCount(queryWrapper) == 0) {
            throw BusinessException.categoryNotFound(categoryId);
        }
    }

    /**
     * 生成唯一的slug
     */
    private String generateUniqueSlug(String providedSlug, String title) {
        String baseSlug;

        // 1. 处理用户提供的slug
        if (StringUtils.hasText(providedSlug)) {
            String sanitizedSlug = SlugGenerator.sanitizeUserSlug(providedSlug);
            if (sanitizedSlug != null) {
                baseSlug = sanitizedSlug;
            } else {
                log.warn("用户提供的slug格式无效: {}, 将使用标题生成", providedSlug);
                baseSlug = SlugGenerator.generateFromTitle(title);
            }
        } else {
            // 2. 根据标题生成slug
            baseSlug = SlugGenerator.generateFromTitle(title);
        }

        // 3. 确保slug的唯一性
        String finalSlug = baseSlug;
        int counter = 1;

        while (slugExists(finalSlug)) {
            counter++;
            finalSlug = SlugGenerator.generateUniqueSlug(baseSlug, counter);
        }

        return finalSlug;
    }

    /**
     * 检查slug是否已存在
     */
    private boolean slugExists(String slug) {
        LambdaQueryWrapper<Posts> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Posts::getSlug, slug);
        return postsMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 处理密码保护逻辑
     */
    private void handlePasswordProtection(Posts post, BasePostDto request) {
        if ("password_protected".equals(post.getVisibility())) {
            if (!StringUtils.hasText(request.getPassword())) {
                throw BusinessException.passwordRequired();
            }
            post.setPassword(request.getPassword());
        } else {
            post.setPassword(null);
        }
    }

    /**
     * 处理数据库约束异常
     */
    private void handleDataIntegrityViolation(DataIntegrityViolationException e, String slug) {
        String message = e.getMessage();
        if (message != null && message.toLowerCase().contains("ux_posts_slug")) {
            throw BusinessException.slugAlreadyExists(slug);
        }

        // 其他约束异常
        log.error("数据库约束异常: {}", message, e);
        throw BusinessException.of(com.kisesaki.blog.common.enums.ErrorCode.DATABASE_ERROR, "数据保存失败，请检查数据完整性");
    }

    /**
     * 处理文章标签关联
     */
    private void handlePostTags(Long postId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }

        tagIds.forEach(tagId -> {
            PostTags postTag = new PostTags();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTagsMapper.insert(postTag);
        });
    }

    /**
     * 更新文章标签关联
     */
    private void updatePostTags(Long postId, List<Long> newTagIds) {
        // 删除旧的标签关联
        LambdaQueryWrapper<PostTags> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(PostTags::getPostId, postId);
        postTagsMapper.delete(deleteWrapper);

        // 添加新的标签关联
        if (newTagIds != null && !newTagIds.isEmpty()) {
            newTagIds.forEach(tagId -> {
                PostTags postTag = new PostTags();
                postTag.setPostId(postId);
                postTag.setTagId(tagId);
                postTagsMapper.insert(postTag);
            });
        }
    }

    /**
     * 更新分类文章数量
     */
    private void updateCategoryPostCount(Long postId, Long oldCategoryId, Long newCategoryId) {
        // 如果分类没有改变，不需要更新
        if ((oldCategoryId == null && newCategoryId == null) ||
                (oldCategoryId != null && oldCategoryId.equals(newCategoryId))) {
            return;
        }

        // 减少旧分类的文章数量
        if (oldCategoryId != null) {
            new LambdaUpdateChainWrapper<>(categoriesMapper)
                    .eq(Categories::getId, oldCategoryId)
                    .setSql("post_count = post_count - 1")
                    .update();
        }

        // 增加新分类的文章数量
        if (newCategoryId != null) {
            new LambdaUpdateChainWrapper<>(categoriesMapper)
                    .eq(Categories::getId, newCategoryId)
                    .setSql("post_count = post_count + 1")
                    .update();
        }
    }

    /**
     * 增加分类的文章数量
     */
    private void incrementCategoryPostCount(Long categoryId) {
        if (categoryId == null) {
            return;
        }

        new LambdaUpdateChainWrapper<>(categoriesMapper)
                .eq(Categories::getId, categoryId)
                .setSql("post_count = post_count + 1")
                .update();
    }

    /**
     * 生成SEO相关字段
     *
     * @param post    文章实体
     * @param request 请求DTO
     */
    private void generateSeoFields(Posts post, BasePostDto request) {
        // SEO标题：优先使用用户提供的，否则使用文章标题
        if (StringUtils.hasText(request.getSeoTitle())) {
            post.setSeoTitle(request.getSeoTitle());
        } else {
            post.setSeoTitle(post.getTitle());
        }

        // SEO描述：优先使用用户提供的，否则使用摘要，再否则从内容截取
        if (StringUtils.hasText(request.getSeoDescription())) {
            post.setSeoDescription(request.getSeoDescription());
        } else if (StringUtils.hasText(request.getExcerpt())) {
            post.setSeoDescription(request.getExcerpt());
        } else {
            post.setSeoDescription(generateDescriptionFromContent(request.getContent()));
        }

        // SEO关键词：优先使用用户提供的，否则从标题和内容提取
        if (StringUtils.hasText(request.getSeoKeywords())) {
            post.setSeoKeywords(request.getSeoKeywords());
        } else {
            post.setSeoKeywords(generateKeywordsFromContent(post.getTitle()));
        }
    }

    /**
     * 从内容中生成描述
     *
     * @param content Markdown内容
     * @return 生成的描述
     */
    private String generateDescriptionFromContent(String content) {
        return markdownService.generateExcerpt(content, 150);
    }

    /**
     * 从标题和内容中生成关键词
     *
     * @param title 文章标题
     * @return 生成的关键词
     */
    private String generateKeywordsFromContent(String title) {
        // 这里简化处理，实际可以使用更复杂的NLP算法
        StringBuilder keywords = new StringBuilder();

        if (StringUtils.hasText(title)) {
            // 从标题中提取关键词
            String[] titleWords = title.split("[\\s,，。！？；：\"']+");
            for (String word : titleWords) {
                if (word.length() > 1) {
                    if (!keywords.isEmpty()) {
                        keywords.append(",");
                    }
                    keywords.append(word.trim());
                }
            }
        }

        return keywords.toString();
    }

    /**
     * 将Markdown转换为HTML
     *
     * @param markdownContent Markdown内容
     * @return HTML内容
     */
    private String convertMarkdownToHtml(String markdownContent) {
        return markdownService.convertToHtml(markdownContent);
    }
}
