package com.kisesaki.blog.content.post;

import org.springframework.security.access.prepost.PreAuthorize;
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
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminCreatePostRequest;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostBatchDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostQueryDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostStatsDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminPostStatusDto;
import com.kisesaki.blog.content.post.dto.AdminCommand.AdminUpdatePostRequest;
import com.kisesaki.blog.content.post.service.AdminPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理员文章管理控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "管理员文章管理", description = "管理员文章管理相关接口")
public class AdminPostController {

    private final AdminPostService postAdminService;

    /**
     * 获取所有文章列表（包含草稿、已删除等）
     *
     * @param params 查询参数
     * @return 文章列表
     */
    @GetMapping("")
    @Operation(summary = "获取所有文章列表", description = "管理员获取所有文章列表，包含草稿、已删除等状态的文章")
    @PreAuthorize("hasAuthority('POST_VIEW_ALL')")
    public ApiResponse<PageResponse<AdminPostQueryDto.AdminPostListResponse>> getAdminPostsList(
            @Valid AdminPostQueryDto.AdminPostListParams params) {
        log.info("管理员获取文章列表：{}", params);
        PageResponse<AdminPostQueryDto.AdminPostListResponse> result = postAdminService.getAdminPostsList(params);
        return ResultUtils.success("获取文章列表成功", result);
    }

    /**
     * 管理员创建文章（可指定作者）
     *
     * @param request 创建请求
     * @return 创建的文章ID
     */
    @PostMapping("")
    @Operation(summary = "管理员创建文章", description = "管理员创建文章，可以指定作者")
    @PreAuthorize("hasAuthority('POST_CREATE')")
    public ApiResponse<Long> createPostAsAdmin(@Valid @RequestBody AdminCreatePostRequest request) {
        log.info("管理员创建文章：{}", request);
        Long postId = postAdminService.createPostAsAdmin(request);
        return ResultUtils.success("创建文章成功", postId);
    }

    /**
     * 更新任意文章
     *
     * @param id      文章ID
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新任意文章", description = "管理员可以更新任何文章")
    @PreAuthorize("hasAuthority('POST_EDIT_ALL')")
    public ApiResponse<Void> updatePostAsAdmin(@PathVariable Long id,
            @Valid @RequestBody AdminUpdatePostRequest request) {
        log.info("管理员更新文章 {}：{}", id, request);
        postAdminService.updatePostAsAdmin(id, request);
        return ResultUtils.success("更新文章成功");
    }

    /**
     * 删除任意文章（软删除）
     *
     * @param id 文章ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除任意文章", description = "管理员可以删除任何文章（软删除）")
    @PreAuthorize("hasAuthority('POST_DELETE_ALL')")
    public ApiResponse<Void> deletePostAsAdmin(@PathVariable Long id) {
        log.info("管理员删除文章：{}", id);
        postAdminService.deletePostAsAdmin(id);
        return ResultUtils.success("删除文章成功");
    }

    /**
     * 更新文章状态
     *
     * @param id      文章ID
     * @param request 状态更新请求
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更新文章状态", description = "管理员更新文章状态（发布/草稿/归档）")
    @PreAuthorize("hasAuthority('POST_PUBLISH')")
    public ApiResponse<Void> updatePostStatus(@PathVariable Long id,
            @Valid @RequestBody AdminPostStatusDto.UpdateStatusRequest request) {
        log.info("管理员更新文章 {} 状态：{}", id, request);
        postAdminService.updatePostStatus(id, request);
        return ResultUtils.success("更新文章状态成功");
    }

    /**
     * 设置/取消精选
     *
     * @param id      文章ID
     * @param request 精选设置请求
     * @return 设置结果
     */
    @PutMapping("/{id}/featured")
    @Operation(summary = "设置/取消精选", description = "管理员设置或取消文章精选状态")
    @PreAuthorize("hasAuthority('POST_MANAGE')")
    public ApiResponse<Void> setPostFeatured(@PathVariable Long id,
            @Valid @RequestBody AdminPostStatusDto.SetFeaturedRequest request) {
        log.info("管理员设置文章 {} 精选状态：{}", id, request);
        postAdminService.setPostFeatured(id, request);
        return ResultUtils.success("设置精选状态成功");
    }

    /**
     * 设置/取消置顶
     *
     * @param id      文章ID
     * @param request 置顶设置请求
     * @return 设置结果
     */
    @PutMapping("/{id}/top")
    @Operation(summary = "设置/取消置顶", description = "管理员设置或取消文章置顶状态")
    @PreAuthorize("hasAuthority('POST_MANAGE')")
    public ApiResponse<Void> setPostTop(@PathVariable Long id,
            @Valid @RequestBody AdminPostStatusDto.SetTopRequest request) {
        log.info("管理员设置文章 {} 置顶状态：{}", id, request);
        postAdminService.setPostTop(id, request);
        return ResultUtils.success("设置置顶状态成功");
    }

    /**
     * 转移文章作者
     *
     * @param id      文章ID
     * @param request 作者转移请求
     * @return 转移结果
     */
    @PutMapping("/{id}/author")
    @Operation(summary = "转移文章作者", description = "管理员转移文章作者")
    @PreAuthorize("hasAuthority('POST_MANAGE')")
    public ApiResponse<Void> transferPostAuthor(@PathVariable Long id,
            @Valid @RequestBody AdminPostStatusDto.TransferAuthorRequest request) {
        log.info("管理员转移文章 {} 作者：{}", id, request);
        postAdminService.transferPostAuthor(id, request);
        return ResultUtils.success("转移文章作者成功");
    }

    /**
     * 批量删除文章
     *
     * @param request 批量删除请求
     * @return 删除结果
     */
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除文章", description = "管理员批量删除文章")
    @PreAuthorize("hasAuthority('POST_DELETE_ALL')")
    public ApiResponse<Void> batchDeletePosts(@Valid @RequestBody AdminPostBatchDto.BatchDeleteRequest request) {
        log.info("管理员批量删除文章：{}", request);
        postAdminService.batchDeletePosts(request);
        return ResultUtils.success("批量删除文章成功");
    }

    /**
     * 批量更新状态
     *
     * @param request 批量状态更新请求
     * @return 更新结果
     */
    @PutMapping("/batch-status")
    @Operation(summary = "批量更新状态", description = "管理员批量更新文章状态")
    @PreAuthorize("hasAuthority('POST_PUBLISH')")
    public ApiResponse<Void> batchUpdateStatus(@Valid @RequestBody AdminPostBatchDto.BatchUpdateStatusRequest request) {
        log.info("管理员批量更新文章状态：{}", request);
        postAdminService.batchUpdateStatus(request);
        return ResultUtils.success("批量更新状态成功");
    }

    /**
     * 批量转移作者
     *
     * @param request 批量作者转移请求
     * @return 转移结果
     */
    @PutMapping("/batch-author")
    @Operation(summary = "批量转移作者", description = "管理员批量转移文章作者")
    @PreAuthorize("hasAuthority('POST_MANAGE')")
    public ApiResponse<Void> batchTransferAuthor(
            @Valid @RequestBody AdminPostBatchDto.BatchTransferAuthorRequest request) {
        log.info("管理员批量转移文章作者：{}", request);
        postAdminService.batchTransferAuthor(request);
        return ResultUtils.success("批量转移作者成功");
    }

    /**
     * 批量设置精选
     *
     * @param request 批量精选设置请求
     * @return 设置结果
     */
    @PutMapping("/batch-featured")
    @Operation(summary = "批量设置精选", description = "管理员批量设置或取消文章精选")
    @PreAuthorize("hasAuthority('POST_MANAGE')")
    public ApiResponse<Void> batchSetFeatured(@Valid @RequestBody AdminPostBatchDto.BatchSetFeaturedRequest request) {
        log.info("管理员批量设置文章精选：{}", request);
        postAdminService.batchSetFeatured(request);
        return ResultUtils.success("批量设置精选成功");
    }

    /**
     * 批量设置置顶
     *
     * @param request 批量置顶设置请求
     * @return 设置结果
     */
    @PutMapping("/batch-top")
    @Operation(summary = "批量设置置顶", description = "管理员批量设置或取消文章置顶")
    @PreAuthorize("hasAuthority('POST_MANAGE')")
    public ApiResponse<Void> batchSetTop(@Valid @RequestBody AdminPostBatchDto.BatchSetTopRequest request) {
        log.info("管理员批量设置文章置顶：{}", request);
        postAdminService.batchSetTop(request);
        return ResultUtils.success("批量设置置顶成功");
    }

    /**
     * 获取文章统计数据
     *
     * @return 统计数据
     */
    @GetMapping("/stats")
    @Operation(summary = "获取文章统计数据", description = "管理员获取文章统计数据")
    @PreAuthorize("hasAuthority('AUDIT_VIEW')")
    public ApiResponse<AdminPostStatsDto.PostStatsResponse> getPostStats() {
        log.info("管理员获取文章统计数据");
        AdminPostStatsDto.PostStatsResponse stats = postAdminService.getPostStats();
        return ResultUtils.success("获取统计数据成功", stats);
    }
}
