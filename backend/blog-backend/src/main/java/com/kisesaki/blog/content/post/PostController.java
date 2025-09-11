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
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostResponse;
import com.kisesaki.blog.content.post.dto.PostCommand.MetaDataDto;
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
        PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
        return ResultUtils.success("获取文章列表成功", pageResponse);
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
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        PublishedPostDetailResponse result = postQueryService.getPublishedPostDetail(id, null, userId);
        return ResultUtils.success("获取文章详情成功", result);
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
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        PublishedPostDetailResponse result = postQueryService.getPublishedPostDetail(null, slug, userId);
        return ResultUtils.success("获取文章详情成功", result);
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
        params.setIsFeatured(true);
        PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
        return ResultUtils.success("获取精选文章列表成功", pageResponse);
    }

    @GetMapping("/recent")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getRecentPosts(@Valid PublishedPostListParams params) {
        // 强制按发布时间降序排序
        params.getPageable().setSort("publishedAt:desc");
        PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
        return ResultUtils.success("获取最新文章列表成功", pageResponse);
    }

    @GetMapping("/popular")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getPopularPosts(@Valid PublishedPostListParams params) {
        // 强制按浏览量降序排序
        params.getPageable().setSort("viewCount:desc");
        PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
        return ResultUtils.success("获取热门文章列表成功", pageResponse);
    }

    /*
     * ----------------------------- 用户创作相关接口 PostCommand
     * -----------------------------
     */
    @PostMapping("")
    public ApiResponse<CreatePostResponse> createPost(@Valid @RequestBody CreatePostRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 创建文章: {}", userId, request);
        CreatePostResponse result = postCommandService.createPost(request, userId);
        return ResultUtils.success("创建文章成功", result);
    }

    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    public ApiResponse<UpdatePostResponse> updatePost(@PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 更新文章 {}: {}", userId, id, request);
        UpdatePostResponse result = postCommandService.updatePost(id, request, userId);
        return ResultUtils.success("更新文章成功", result);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 删除文章 {}", userId, id);
        postCommandService.deletePost(id, userId);
        return ResultUtils.success("删除文章成功");
    }

    /**
     * 发布文章
     */
    @PutMapping("/{id}/publish")
    public ApiResponse<Void> publishPost(@PathVariable Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 发布文章 {}", userId, id);
        postCommandService.publishPost(id, userId);
        return ResultUtils.success("发布文章成功");
    }

    /**
     * 取消发布（变为草稿）
     */
    @PutMapping("/{id}/unpublish")
    public ApiResponse<Void> unpublishPost(@PathVariable Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 取消发布文章 {}", userId, id);
        postCommandService.unpublishPost(id, userId);
        return ResultUtils.success("取消发布文章成功");
    }

    /**
     * 复制文章
     */
    @PostMapping("/{id}/duplicate")
    public ApiResponse<Long> duplicatePost(@PathVariable Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 复制文章 {}", userId, id);
        Long newPostId = postCommandService.duplicatePost(id, userId);
        return ResultUtils.success("复制文章成功", newPostId);
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
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(com.kisesaki.blog.common.enums.ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        PageResponse<MyPostsListResponse> pageResponse = postQueryService.getMyPosts(params, userId);
        return ResultUtils.success("获取我的文章列表成功", pageResponse);
    }

    /*
     * ----------------------------- 文章元数据相关接口
     * -----------------------------
     */

    /**
     * 获取文章元数据
     *
     * @param id             文章ID
     * @param authentication 认证信息
     * @return 文章元数据
     */
    @GetMapping("/{id}/meta")
    public ApiResponse<MetaDataDto.PostMetaResponse> getPostMeta(@PathVariable Long id,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        MetaDataDto.PostMetaResponse result = postCommandService.getPostMeta(id, userId);
        return ResultUtils.success("获取文章元数据成功", result);
    }

    /**
     * 更新文章元数据
     *
     * @param id             文章ID
     * @param request        更新请求
     * @param authentication 认证信息
     * @return 更新后的元数据
     */
    @PutMapping("/{id}/meta")
    public ApiResponse<MetaDataDto.PostMetaResponse> updatePostMeta(@PathVariable Long id,
            @Valid @RequestBody MetaDataDto.UpdatePostMetaRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 更新文章 {} 元数据: {}", userId, id, request);
        MetaDataDto.PostMetaResponse result = postCommandService.updatePostMeta(id, request, userId);
        return ResultUtils.success("更新文章元数据成功", result);
    }

    /**
     * 删除指定元数据
     *
     * @param id             文章ID
     * @param key            元数据键
     * @param authentication 认证信息
     * @return 删除结果
     */
    @DeleteMapping("/{id}/meta/{key}")
    public ApiResponse<Void> deletePostMeta(@PathVariable Long id, @PathVariable String key,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 删除文章 {} 的元数据: {}", userId, id, key);
        postCommandService.deletePostMeta(id, key, userId);
        return ResultUtils.success("删除元数据成功");
    }

}
