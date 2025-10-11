package com.kisesaki.blog.content.comment.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.dto.admin.AdminBatchModerateBody;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentListParams;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentReportResponse;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentStatsResponse;
import com.kisesaki.blog.content.comment.dto.admin.AdminPinCommentBody;
import com.kisesaki.blog.content.comment.dto.admin.AdminUpdateCommentStatusBody;
import com.kisesaki.blog.content.comment.service.AdminCommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理员评论控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员评论", description = "管理员评论管理相关接口")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    /**
     * 获取所有评论列表（含待审核、已删除等）
     */
    @GetMapping("/comments")
    @Operation(summary = "获取所有评论列表", description = "获取所有评论列表,支持按状态、文章、用户等筛选")
    @PreAuthorize("hasAuthority('COMMENT_MODERATE')")
    public ApiResponse<PageResponse<CommentListResponse>> getAllComments(@Valid @ModelAttribute AdminCommentListParams params) {
        PageResponse<CommentListResponse> pageResponse = adminCommentService.getAllComments(params);
        return ResultUtils.success(pageResponse);
    }

    /**
     * 更新评论状态
     */
    @PutMapping("/comments/{id}/status")
    @Operation(summary = "更新评论状态", description = "更新指定评论的状态（审核通过、拒绝、标记为垃圾等）")
    @PreAuthorize("hasAuthority('COMMENT_MODERATE')")
    public ApiResponse<Void> updateCommentStatus(
            @PathVariable @Parameter(description = "评论ID", example = "123") Long id,
            @RequestBody @Valid AdminUpdateCommentStatusBody body,
            Authentication authentication) {
        Long adminId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminCommentService.updateCommentStatus(id, body.getStatus(), adminId);
        return ResultUtils.success();
    }

    /**
     * 彻底删除评论
     */
    @DeleteMapping("/comments/{id}")
    @Operation(summary = "彻底删除评论", description = "物理删除指定评论，不可恢复")
    @PreAuthorize("hasAuthority('COMMENT_DELETE')")
    public ApiResponse<Void> deleteComment(
            @PathVariable @Parameter(description = "评论ID", example = "123") Long id,
            Authentication authentication) {
        Long adminId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminCommentService.deleteComment(id, adminId);
        return ResultUtils.success();
    }

    /**
     * 置顶评论
     */
    @PutMapping("/comments/{id}/pin")
    @Operation(summary = "置顶评论", description = "设置或取消评论置顶")
    @PreAuthorize("hasAuthority('COMMENT_MODERATE')")
    public ApiResponse<Void> pinComment(
            @PathVariable @Parameter(description = "评论ID", example = "123") Long id,
            @RequestBody @Valid AdminPinCommentBody body,
            Authentication authentication) {
        Long adminId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminCommentService.pinComment(id, body.getIsPinned(), adminId);
        return ResultUtils.success();
    }

    /**
     * 获取被举报的评论列表
     */
    @GetMapping("/comments/reports")
    @Operation(summary = "获取被举报的评论列表", description = "获取所有被举报的评论及举报详情")
    @PreAuthorize("hasAuthority('COMMENT_MODERATE')")
    public ApiResponse<PageResponse<AdminCommentReportResponse>> getReportedComments(
            @RequestParam(defaultValue = "1") @Parameter(description = "页码", example = "1") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "每页大小", example = "10") int size) {
        PageResponse<AdminCommentReportResponse> pageResponse = adminCommentService.getReportedComments(page, size);
        return ResultUtils.success(pageResponse);
    }

    /**
     * 批量审核评论
     */
    @PostMapping("/comments/batch-moderate")
    @Operation(summary = "批量审核评论", description = "批量修改多个评论的状态")
    @PreAuthorize("hasAuthority('COMMENT_MODERATE')")
    public ApiResponse<Void> batchModerateComments(
            @RequestBody @Valid AdminBatchModerateBody body,
            Authentication authentication) {
        Long adminId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminCommentService.batchModerateComments(body, adminId);
        return ResultUtils.success();
    }

    /**
     * 评论统计数据
     */
    @GetMapping("/comments/stats")
    @Operation(summary = "评论统计数据", description = "获取评论的各种统计数据")
    @PreAuthorize("hasAuthority('COMMENT_MODERATE')")
    public ApiResponse<AdminCommentStatsResponse> getCommentStats() {
        AdminCommentStatsResponse stats = adminCommentService.getCommentStats();
        return ResultUtils.success(stats);
    }
}
