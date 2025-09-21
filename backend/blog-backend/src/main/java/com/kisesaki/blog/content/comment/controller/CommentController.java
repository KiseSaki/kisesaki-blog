package com.kisesaki.blog.content.comment.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.comment.dto.CommentDetailResponse;
import com.kisesaki.blog.content.comment.dto.CommentListParams;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.dto.MyCommentParams;
import com.kisesaki.blog.content.comment.dto.interaction.CreateCommentBody;
import com.kisesaki.blog.content.comment.dto.interaction.ReportCommentBody;
import com.kisesaki.blog.content.comment.dto.interaction.UpdateCommentBody;
import com.kisesaki.blog.content.comment.service.CommentInteractionService;
import com.kisesaki.blog.content.comment.service.CommentQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 评论控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("")
@Tag(name = "评论", description = "评论相关接口")
@RequiredArgsConstructor
public class CommentController {

    private final CommentQueryService commentQueryService;
    private final CommentInteractionService commentInteractionService;

    /**
     * 获取文章评论列表
     */
    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "获取文章评论列表", description = "获取指定文章的评论列表，支持分页、排序和筛选")
    public ApiResponse<PageResponse<CommentListResponse>> getPostComments(
            @PathVariable @Parameter(description = "文章ID", example = "123") Long postId,
            @Valid CommentListParams params) {
        PageResponse<CommentListResponse> pageResponse = commentQueryService.getCommentList(postId, params);
        return ResultUtils.success(pageResponse);
    }

    /**
     * 获取单条评论详情
     */
    @GetMapping("/comments/{id}")
    @Operation(summary = "获取单条评论详情", description = "获取指定评论的详细信息，包含上下文信息")
    public ApiResponse<CommentDetailResponse> getCommentDetail(
            @PathVariable @Parameter(description = "评论ID", example = "456") Long id) {
        CommentDetailResponse commentDetail = commentQueryService.getCommentDetail(id);
        return ResultUtils.success(commentDetail);
    }

    /**
     * 获取评论的回复列表（用于懒加载更多回复）
     */
    @GetMapping("/comments/{id}/replies")
    @Operation(summary = "获取评论回复列表", description = "分页获取指定评论的回复列表，用于懒加载更多回复")
    public ApiResponse<PageResponse<CommentListResponse>> getCommentReplies(
            @PathVariable @Parameter(description = "父评论ID", example = "456") Long id,
            @RequestParam(defaultValue = "1") @Parameter(description = "页码", example = "1") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小", example = "10") int size) {
        PageResponse<CommentListResponse> replies = commentQueryService.getCommentReplies(id, page, size);
        return ResultUtils.success(replies);
    }

    /**
     * 创建评论
     */
    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "创建评论", description = "在指定文章下创建一条评论")
    public ApiResponse<Long> createComment(@PathVariable Long postId, @RequestBody CreateCommentBody body,
            Authentication authentication, HttpServletRequest request) {
        return ResultUtils.success(commentInteractionService.createComment(postId, body, authentication, request));
    }

    /**
     * 更新评论（15分钟内的）
     */
    @PostMapping("/comments/{id}")
    @Operation(summary = "更新评论", description = "更新指定ID的评论内容，仅限15分钟内的评论")
    public ApiResponse<Long> updateComment(@PathVariable Long id, @RequestBody UpdateCommentBody body,
            Authentication authentication, HttpServletRequest request) {
        return ResultUtils.success(commentInteractionService.updateComment(id, body, authentication, request));
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/comments/{id}")
    @Operation(summary = "删除评论", description = "删除指定ID的评论，管理员或评论作者可执行此操作")
    public ApiResponse<Void> deleteComment(@PathVariable Long id, Authentication authentication) {
        commentInteractionService.deleteComment(id, authentication);
        return ResultUtils.success();
    }

    /**
     * 获取我的评论列表
     */
    @GetMapping("/comments/my")
    @Operation(summary = "获取我的评论列表", description = "获取当前用户的评论列表，支持按文章和状态筛选")
    public ApiResponse<PageResponse<CommentListResponse>> getMyComments(
            @Valid MyCommentParams params,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new com.kisesaki.blog.common.exception.BusinessException(
                    com.kisesaki.blog.common.enums.ErrorCode.UNAUTHORIZED, "用户认证失败");
        }

        PageResponse<CommentListResponse> pageResponse = commentQueryService.getMyComments(userId, params);
        return ResultUtils.success(pageResponse);
    }

    /**
     * 举报评论
     */
    @PostMapping("/comments/{id}/report")
    @Operation(summary = "举报评论", description = "举报指定ID的评论")
    public ApiResponse<Void> reportComment(@PathVariable Long id, @RequestBody @Valid ReportCommentBody body,
            Authentication authentication, HttpServletRequest request) {
        commentInteractionService.reportComment(id, body, authentication, request);
        return ResultUtils.success();
    }

}
