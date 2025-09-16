package com.kisesaki.blog.content.tag.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagApprovalRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCleanupResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCreateRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagMergeRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagPendingResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagUnusedResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagUpdateRequest;
import com.kisesaki.blog.content.tag.service.AdminTagService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
@Tag(name = "标签管理", description = "标签管理相关接口")
public class AdminTagController {

    private final AdminTagService adminTagService;

    /**
     * 获取管理员标签列表
     */
    @GetMapping("")
    public ApiResponse<PageResponse<AdminTagListResponse>> adminGetTagList(@Valid AdminTagListParams params) {
        return ApiResponse.success(adminTagService.adminGetTagList(params));
    }

    /**
     * 创建新标签
     */
    @PostMapping("")
    public ApiResponse<Long> adminTagCreate(@Valid @RequestBody AdminTagCreateRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        return ApiResponse.success(adminTagService.adminTagCreate(request, userId));
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> adminTagUpdate(@PathVariable("id") Long id,
            @Valid @RequestBody AdminTagUpdateRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminTagService.adminTagUpdate(id, request, userId);
        return ApiResponse.success("更新成功");
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> adminTagDelete(@PathVariable("id") Long id, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminTagService.adminTagDelete(id, userId);
        return ApiResponse.success("删除成功");
    }

    /**
     * 审核标签
     */
    @PutMapping("/{id}/approve")
    public ApiResponse<Void> adminTagApprove(@PathVariable("id") Long id,
            @Valid @RequestBody AdminTagApprovalRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminTagService.adminTagApprove(id, request, userId);
        return ApiResponse.success("审核成功");
    }

    /**
     * 获取所有待审核标签
     */
    @GetMapping("/pending")
    public ApiResponse<List<AdminTagPendingResponse>> adminGetPendingTags() {
        return ApiResponse.success(adminTagService.adminGetPendingTags());
    }

    /**
     * 获取未使用的标签列表
     */
    @GetMapping("/unused")
    public ApiResponse<List<AdminTagUnusedResponse>> adminGetUnusedTags(
            @RequestParam(value = "unusedDays", required = false) Integer unusedDays) {
        return ApiResponse.success(adminTagService.adminGetUnusedTags(unusedDays));
    }

    /**
     * 清理未使用的标签
     */
    @DeleteMapping("/cleanup")
    public ApiResponse<AdminTagCleanupResponse> adminCleanupUnusedTags(
            @RequestParam(value = "unusedDays", required = false, defaultValue = "30") Integer unusedDays,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        return ApiResponse.success(adminTagService.adminCleanupUnusedTags(unusedDays, userId));
    }

    /**
     * 合并标签
     */
    @PostMapping("/merge")
    public ApiResponse<Void> adminMergeTags(@Valid @RequestBody AdminTagMergeRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminTagService.adminMergeTags(request, userId);
        return ApiResponse.success("标签合并成功");
    }

}
