package com.kisesaki.blog.content.post.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.content.post.dto.TagInfo;
import com.kisesaki.blog.content.post.dto.PostQuery.GetMyPostsListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.MyPostsListResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostDetailResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章查询服务
 * 负责处理文章的各种查询操作
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryService {

    private final PostsMapper postsMapper;
    private final PostRecommendationService recommendationService;

    /**
     * 按条件获取已发布文章列表
     *
     * @param params 查询参数
     * @return 文章列表
     */
    public PageResponse<PublishedPostListResponse> selectPublishedPosts(PublishedPostListParams params) {
        // 手动获取总数以避免MyBatis Plus自动count查询的DISTINCT问题
        long totalCount = postsMapper.countPublishedPosts(params);

        // 如果总数为0，直接返回空结果
        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        // 查询分页数据（禁用自动count查询）
        Page<PublishedPostListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize(),
                false // 禁用自动count查询
        );
        Page<PublishedPostListResponse> result = postsMapper.selectPublishedPostsPage(page, params);

        // 手动设置总数
        result.setTotal(totalCount);

        return PageResponse.of(result);
    }

    /**
     * 根据文章ID获取已发布文章详情
     *
     * @param postId 文章ID（可选）
     * @param slug   文章slug（可选）
     * @param userId 当前用户ID（可选，用于权限判断）
     * @return 文章详情
     */
    public PublishedPostDetailResponse getPublishedPostDetail(Long postId, String slug, Long userId) {
        PublishedPostDetailResponse result;
        if (slug != null && !slug.isBlank()) {
            result = postsMapper.getPublishedPostDetailBySlug(slug);
        } else if (postId != null) {
            result = postsMapper.getPublishedPostDetailById(postId);
        } else {
            throw BusinessException.paramError("文章ID或Slug不能为空");
        }

        if (result == null) {
            throw BusinessException.notFound("文章");
        }

        // 设置权限信息
        if (Objects.equals(result.getAuthor().getId(), userId)) {
            result.getPermissions().setCanEdit(true);
            result.getPermissions().setCanDelete(true);
        } else {
            result.getPermissions().setCanEdit(false);
            result.getPermissions().setCanDelete(false);
        }

        // 获取自定义元数据
        result.setMeta(recommendationService.getPostMeta(postId));

        // 获取上一篇和下一篇文章
        result.setPrevPost(recommendationService.getPrevPost(result.getPublishedAt(), postId));
        result.setNextPost(recommendationService.getNextPost(result.getPublishedAt(), postId));

        // 获取相关推荐文章
        List<Long> tagIds = result.getTags() != null ? result.getTags().stream().map(TagInfo::getId).toList()
                : List.of();

        result.setRelatedPosts(recommendationService.getRelatedPosts(
                result.getCategory() != null ? result.getCategory().getId() : null,
                tagIds,
                postId,
                5 // 默认推荐5篇相关文章
        ));

        return result;
    }

    /**
     * 获取我的文章列表（包括草稿）
     *
     * @param params 查询参数
     * @param userId 当前用户ID
     * @return 文章列表
     */
    public PageResponse<MyPostsListResponse> getMyPosts(GetMyPostsListParams params, Long userId) {
        // 手动获取总数以避免MyBatis Plus自动count查询的DISTINCT问题
        long totalCount = postsMapper.countMyPosts(params, userId);

        // 如果总数为0，直接返回空结果
        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        // 查询分页数据（禁用自动count查询）
        Page<MyPostsListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize(),
                false // 禁用自动count查询
        );
        Page<MyPostsListResponse> result = postsMapper.selectMyPostsPage(page, params, userId);

        // 手动设置总数
        result.setTotal(totalCount);

        return PageResponse.of(result);
    }
}
