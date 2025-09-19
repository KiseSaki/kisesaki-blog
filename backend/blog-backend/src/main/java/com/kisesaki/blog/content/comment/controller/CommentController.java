package com.kisesaki.blog.content.comment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.comment.dto.CommentDetailResponse;
import com.kisesaki.blog.content.comment.dto.CommentListParams;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.service.CommentQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 评论控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/api")
@Tag(name = "评论", description = "评论相关接口")
@RequiredArgsConstructor
public class CommentController {

    private final CommentQueryService commentQueryService;

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
}
