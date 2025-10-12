package com.kisesaki.blog.content.post;

import org.springframework.security.access.prepost.PreAuthorize;
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
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.post.dto.RevisionInfo;
import com.kisesaki.blog.content.post.dto.PostCommand.BatchOperatePostsRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostResponse;
import com.kisesaki.blog.content.post.dto.PostCommand.MetaDataDto;
import com.kisesaki.blog.content.post.dto.PostCommand.PostEditDetailResponse;
import com.kisesaki.blog.content.post.dto.PostCommand.UpdatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.UpdatePostResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.GetMyPostsListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.MyPostsListResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostDetailResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.dto.PostRevision.PostRevisionContentResponse;
import com.kisesaki.blog.content.post.dto.PostRevision.PostRevisionListParams;
import com.kisesaki.blog.content.post.service.PostCommandService;
import com.kisesaki.blog.content.post.service.PostQueryService;
import com.kisesaki.blog.content.post.service.PostRevisionService;

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
    private final PostRevisionService postRevisionService;

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
    @PreAuthorize("hasAuthority('POST_CREATE')")
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
    @PreAuthorize("hasAuthority('POST_EDIT_OWN') or hasAuthority('POST_EDIT_ALL')")
    public ApiResponse<UpdatePostResponse> updatePost(@PathVariable Long id,
                                                      @Valid @RequestBody UpdatePostRequest request,
                                                      Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        UpdatePostResponse result = postCommandService.updatePost(id, request, userId);
        return ResultUtils.success("更新文章成功", result);
    }

    /**
     * 获取文章编辑详情
     * 用于编辑表单回显，返回所有可编辑字段（包括 Markdown 原始内容）
     *
     * @param id             文章ID
     * @param authentication 认证信息
     * @return 文章编辑详情
     */
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('POST_EDIT_OWN') or hasAuthority('POST_EDIT_ALL')")
    public ApiResponse<PostEditDetailResponse> getPostEditDetail(@PathVariable Long id,
                                                                 Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        log.info("用户 {} 获取文章 {} 编辑详情", userId, id);
        PostEditDetailResponse result = postCommandService.getPostEditDetail(id, userId);
        return ResultUtils.success("获取文章编辑详情成功", result);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('POST_DELETE_OWN') or hasAuthority('POST_DELETE_ALL')")
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
    @PreAuthorize("hasAuthority('POST_PUBLISH')")
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
    @PreAuthorize("hasAuthority('POST_PUBLISH')")
    public ApiResponse<Void> unpublishPost(@PathVariable Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 取消发布文章 {}", userId, id);
        postCommandService.unpublishPost(id, userId);
        return ResultUtils.success("取消发布文章成功");
    }

    /**
     * 归档文章
     */
    @PutMapping("/{id}/archive")
    @PreAuthorize("hasAuthority('POST_PUBLISH')")
    public ApiResponse<Void> archivePost(@PathVariable Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 归档文章 {}", userId, id);
        postCommandService.archivePost(id, userId);
        return ResultUtils.success("归档文章成功");
    }

    /**
     * 批量操作文章（仅操作用户自己的文章）
     */
    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('POST_EDIT_OWN')")
    public ApiResponse<Void> batchOperatePosts(@Valid @RequestBody BatchOperatePostsRequest request,
                                               Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 批量操作文章: {}", userId, request);
        postCommandService.batchOperatePosts(request, userId);
        return ResultUtils.success("批量操作文章成功");
    }

    /**
     * 复制文章
     */
    @PostMapping("/{id}/duplicate")
    @PreAuthorize("hasAuthority('POST_CREATE')")
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
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<PageResponse<MyPostsListResponse>> getMyPosts(
        @Valid GetMyPostsListParams params,
        Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
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
    @PreAuthorize("hasAuthority('POST_EDIT_OWN') or hasAuthority('POST_EDIT_ALL')")
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
    @PreAuthorize("hasAuthority('POST_EDIT_OWN') or hasAuthority('POST_EDIT_ALL')")
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
    @PreAuthorize("hasAuthority('POST_EDIT_OWN') or hasAuthority('POST_EDIT_ALL')")
    public ApiResponse<Void> deletePostMeta(@PathVariable Long id, @PathVariable String key,
                                            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        log.info("用户 {} 删除文章 {} 的元数据: {}", userId, id, key);
        postCommandService.deletePostMeta(id, key, userId);
        return ResultUtils.success("删除元数据成功");
    }

    /*
     * ----------------------------- 文章版本相关接口
     * -----------------------------
     */

    /**
     * 获取指定文章的版本列表
     *
     * @param postId         文章ID
     * @param params         分页参数
     * @param authentication 认证信息
     * @return 文章版本列表
     */
    @GetMapping("/{postId}/revisions")
    @PreAuthorize("hasAuthority('POST_REVISION_VIEW')")
    public ApiResponse<PageResponse<RevisionInfo>> getPostRevisions(@PathVariable Long postId,
                                                                    @Valid PostRevisionListParams params,
                                                                    Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        PageResponse<RevisionInfo> pageResponse = postRevisionService.getPostRevisionListByPostId(postId, userId,
            params);
        return ResultUtils.success("获取文章版本列表成功", pageResponse);
    }

    /**
     * 获取指定文章的指定版本内容
     *
     * @param postId         文章ID
     * @param revisionId     版本ID
     * @param authentication 认证信息
     * @return 文章版本内容
     */
    @GetMapping("/{postId}/revisions/{revisionId}")
    @PreAuthorize("hasAuthority('POST_REVISION_VIEW')")
    public ApiResponse<PostRevisionContentResponse> getPostRevisionContent(@PathVariable Long postId,
                                                                           @PathVariable Long revisionId,
                                                                           Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        PostRevisionContentResponse response = postRevisionService.getPostRevisionContent(postId, revisionId);
        return ResultUtils.success("获取文章版本内容成功", response);
    }

    /**
     * 恢复文章到指定版本
     *
     * @param postId         文章ID
     * @param revisionId     版本ID
     * @param authentication 认证信息
     * @return 操作结果
     */
    @PostMapping("/{postId}/revisions/{revisionId}/restore")
    @PreAuthorize("hasAuthority('POST_REVISION_RESTORE')")
    public ApiResponse<Void> restorePostRevision(@PathVariable Long postId, @PathVariable Long revisionId,
                                                 Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        postRevisionService.restorePostToRevision(postId, revisionId, userId);
        return ResultUtils.success("恢复文章版本成功");
    }

    @DeleteMapping("/{postId}/revisions/{revisionId}")
    @PreAuthorize("hasAuthority('POST_EDIT_OWN') or hasAuthority('POST_EDIT_ALL')")
    public ApiResponse<Void> deletePostRevision(@PathVariable Long postId, @PathVariable Long revisionId,
                                                Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
        }

        postRevisionService.deleteRevisionByPostId(postId, revisionId, userId);
        return ResultUtils.success("删除文章版本成功");
    }
}
