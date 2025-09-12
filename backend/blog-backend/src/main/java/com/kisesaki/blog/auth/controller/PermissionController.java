package com.kisesaki.blog.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.permission.PermissionListParams;
import com.kisesaki.blog.auth.dto.permission.PermissionListResponse;
import com.kisesaki.blog.auth.service.PermissionService;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ApiResponse<PageResponse<PermissionListResponse>> getPermissionsList(PermissionListParams params) {
        PageResponse<PermissionListResponse> pageResponse = permissionService.getPermissionsList(params);
        return ApiResponse.success(pageResponse);
    }

}
