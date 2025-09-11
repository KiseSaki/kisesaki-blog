package com.kisesaki.blog.content.post;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostResponse;
import com.kisesaki.blog.content.post.dto.PostCommand.UpdatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.UpdatePostResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.GetMyPostsListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.MyPostsListResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostDetailResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.service.PostCommandService;
import com.kisesaki.blog.content.post.service.PostQueryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "文章", description = "文章相关接口")
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    /*
     * ----------------------------- 公共查询相关接口 PostQuery
     * -----------------------------
     */
    @GetMapping("")
    public ApiResponse<PageResponse<PublishedPostListResponse>> selectPublishedPosts(
            @Valid PublishedPostListParams params) {
        try {
            PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
            return ResultUtils.success("获取文章列表成功", pageResponse);
        } catch (Exception e) {
            log.error("获取已发布文章列表失败", e);
            return ResultUtils.error("获取已发布文章列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据文章ID获取已发布文章详情
     *
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public ApiResponse<PublishedPostDetailResponse> getPublishedPostDetailById(@PathVariable Long id,
            Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            return postQueryService.getPublishedPostDetail(id, null, userId);
        } catch (Exception e) {
            log.error("获取已发布文章详情失败", e);
            return ResultUtils.error("获取已发布文章详情失败: " + e.getMessage());
        }
    }

    /**
     * 根据文章slug获取已发布文章详情
     *
     * @param slug 文章slug
     * @return 文章详情
     */
    @GetMapping("/slug/{slug}")
    public ApiResponse<PublishedPostDetailResponse> getPublishedPostDetailBySlug(@PathVariable String slug,
            Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            return postQueryService.getPublishedPostDetail(null, slug, userId);
        } catch (Exception e) {
            log.error("获取已发布文章详情失败", e);
            return ResultUtils.error("获取已发布文章详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取精选文章列表
     *
     * @param params 查询参数
     * @return 精选文章列表
     */
    @GetMapping("/featured")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getFeaturedPosts(
            @Valid PublishedPostListParams params) {
        try {
            params.setIsFeatured(true);
            PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
            return ResultUtils.success("获取精选文章列表成功", pageResponse);
        } catch (Exception e) {
            log.error("获取精选文章列表失败", e);
            return ResultUtils.error("获取精选文章列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/recent")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getRecentPosts(@Valid PublishedPostListParams params) {
        try {
            // 强制按发布时间降序排序
            params.getPageable().setSort("publishedAt:desc");
            PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
            return ResultUtils.success("获取最新文章列表成功", pageResponse);
        } catch (Exception e) {
            log.error("获取最新文章列表失败", e);
            return ResultUtils.error("获取最新文章列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/popular")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getPopularPosts(@Valid PublishedPostListParams params) {
        try {
            // 强制按浏览量降序排序
            params.getPageable().setSort("viewCount:desc");
            PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
            return ResultUtils.success("获取热门文章列表成功", pageResponse);
        } catch (Exception e) {
            log.error("获取热门文章列表失败", e);
            return ResultUtils.error("获取热门文章列表失败: " + e.getMessage());
        }
    }

    /*
     * ----------------------------- 用户创作相关接口 PostCommand
     * -----------------------------
     */
    @PostMapping("")
    public ApiResponse<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest request,
            Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            log.info("用户 {} 创建文章: {}", userId, request);
            return postCommandService.createPost(request, userId);
        } catch (Exception e) {
            log.error("创建文章失败", e);
            return ResultUtils.error("创建文章失败: " + e.getMessage());
        }
    }

    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    public ApiResponse<UpdatePostResponse> updatePost(@PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            log.info("用户 {} 更新文章 {}: {}", userId, id, request);
            return postCommandService.updatePost(id, request, userId);
        } catch (Exception e) {
            log.error("更新文章失败", e);
            return ResultUtils.error("更新文章失败: " + e.getMessage());
        }
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            log.info("用户 {} 删除文章 {}", userId, id);
            return postCommandService.deletePost(id, userId);
        } catch (Exception e) {
            log.error("删除文章失败", e);
            return ResultUtils.error("删除文章失败: " + e.getMessage());
        }
    }

    /**
     * 发布文章
     */
    @PutMapping("/{id}/publish")
    public ApiResponse<Void> publishPost(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            log.info("用户 {} 发布文章 {}", userId, id);
            return postCommandService.publishPost(id, userId);
        } catch (Exception e) {
            log.error("发布文章失败", e);
            return ResultUtils.error("发布文章失败: " + e.getMessage());
        }
    }

    /**
     * 取消发布（变为草稿）
     */
    @PutMapping("/{id}/unpublish")
    public ApiResponse<Void> unpublishPost(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            log.info("用户 {} 取消发布文章 {}", userId, id);
            return postCommandService.unpublishPost(id, userId);
        } catch (Exception e) {
            log.error("取消发布文章失败", e);
            return ResultUtils.error("取消发布文章失败: " + e.getMessage());
        }
    }

    /**
     * 复制文章
     */
    @PostMapping("/{id}/duplicate")
    public ApiResponse<Long> duplicatePost(@PathVariable Long id, Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            log.info("用户 {} 复制文章 {}", userId, id);
            return postCommandService.duplicatePost(id, userId);
        } catch (Exception e) {
            log.error("复制文章失败", e);
            return ResultUtils.error("复制文章失败: " + e.getMessage());
        }
    }

    /**
     * 获取我的文章列表（包括草稿）
     *
     * @param params         查询参数
     * @param authentication 认证信息
     * @return 文章列表
     */
    @GetMapping("/my")
    public ApiResponse<PageResponse<MyPostsListResponse>> getMyPosts(
            @Valid GetMyPostsListParams params,
            Authentication authentication) {
        try {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            if (userId == null) {
                return ResultUtils.error("用户未登录");
            }

            PageResponse<MyPostsListResponse> pageResponse = postQueryService.getMyPosts(params, userId);
            return ResultUtils.success("获取我的文章列表成功", pageResponse);
        } catch (Exception e) {
            log.error("获取我的文章列表失败", e);
            return ResultUtils.error("获取我的文章列表失败: " + e.getMessage());
        }
    }

}
