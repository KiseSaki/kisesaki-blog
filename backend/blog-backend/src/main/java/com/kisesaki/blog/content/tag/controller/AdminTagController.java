package com.kisesaki.blog.content.tag.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCreateRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
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
    public ApiResponse<PageResponse<AdminTagListResponse>> getAdminTagList(@Valid AdminTagListParams params) {
        return ApiResponse.success(adminTagService.getAdminTagList(params));
    }

    /**
     * 创建新标签
     */
    @PostMapping("")
    public ApiResponse<Long> createAdminTag(@Valid @RequestBody AdminTagCreateRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        return ApiResponse.success(adminTagService.createAdminTag(request, userId));
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> updateAdminTag(@PathVariable("id") Long id,
            @Valid @RequestBody AdminTagUpdateRequest request,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        adminTagService.updateAdminTag(id, request, userId);
        return ApiResponse.success();
    }
}
