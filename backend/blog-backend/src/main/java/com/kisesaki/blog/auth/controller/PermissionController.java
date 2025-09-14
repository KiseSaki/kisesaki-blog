package com.kisesaki.blog.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.permission.PermissionDetailResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionListParams;
import com.kisesaki.blog.auth.dto.permission.PermissionListResponse;
import com.kisesaki.blog.auth.service.PermissionService;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/permissions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "权限", description = "权限相关接口")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取权限列表
     */
    @GetMapping("")
    public ApiResponse<PageResponse<PermissionListResponse>> getPermissionsList(
            @Valid @RequestParam PermissionListParams params) {
        PageResponse<PermissionListResponse> pageResponse = permissionService.getPermissionsList(params);
        return ApiResponse.success(pageResponse);
    }

    /**
     * 根据ID获取权限详情
     */
    @GetMapping("/{id}")
    public ApiResponse<PermissionDetailResponse> getPermissionById(@PathVariable Long id) {
        // 这里调用service层的方法获取权限详情
        PermissionDetailResponse detailResponse = permissionService.getPermissionById(id);
        return ApiResponse.success(detailResponse);
    }

}
