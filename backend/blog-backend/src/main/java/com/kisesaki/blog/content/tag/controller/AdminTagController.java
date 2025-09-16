package com.kisesaki.blog.content.tag.controller;

import com.kisesaki.blog.content.tag.dto.AdminCommand.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.util.AuthUtils;
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

    @PutMapping("/{id}/approve")
    public ApiResponse<Void> adminTagApprove(@PathVariable("id") Long id,
                                             @Valid @RequestBody AdminTagApprovalRequest request,
                                             Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminTagService.adminTagApprove(id, request, userId);
        return ApiResponse.success("审核成功");
    }
}
