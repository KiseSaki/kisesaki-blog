package com.kisesaki.blog.auth.controller;

import com.kisesaki.blog.auth.dto.role.RoleDetailResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.role.RoleListParams;
import com.kisesaki.blog.auth.dto.role.RoleListResponse;
import com.kisesaki.blog.auth.service.RoleService;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色的创建、删除、分配等操作")
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取角色列表
     */
    @GetMapping("")
    public ApiResponse<PageResponse<RoleListResponse>> getRoleList(
            @Valid RoleListParams params) {
        return ApiResponse.success(roleService.getRoleList(params));
    }

    /**
     * 获取角色详情（包含权限列表）
     */
    @GetMapping("/{id}")
    public ApiResponse<RoleDetailResponse> getRoleDetail(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRoleDetail(id));
    }
}
