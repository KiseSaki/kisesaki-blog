package com.kisesaki.blog.auth.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.role.RoleCreateRequest;
import com.kisesaki.blog.auth.dto.role.RoleDetailResponse;
import com.kisesaki.blog.auth.dto.role.RoleListParams;
import com.kisesaki.blog.auth.dto.role.RoleListResponse;
import com.kisesaki.blog.auth.dto.role.RolePermissionUpdateRequest;
import com.kisesaki.blog.auth.dto.role.RoleUpdateRequest;
import com.kisesaki.blog.auth.dto.role.RoleUserListParams;
import com.kisesaki.blog.auth.dto.role.RoleUserListResponse;
import com.kisesaki.blog.auth.service.RoleService;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@Tag(name = "角色管理", description = "角色的创建、删除、分配等操作")
@PreAuthorize("hasAuthority('USER_MANAGE')")
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取角色列表
     */
    @GetMapping("")
    @Operation(summary = "获取角色列表", description = "支持分页和按名称模糊搜索")
    public ApiResponse<PageResponse<RoleListResponse>> getRoleList(
            @Valid RoleListParams params) {
        return ApiResponse.success(roleService.getRoleList(params));
    }

    /**
     * 获取角色详情（包含权限列表）
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取角色详情", description = "获取角色详细信息，包含权限列表")
    public ApiResponse<RoleDetailResponse> getRoleDetail(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRoleDetail(id));
    }

    /**
     * 创建新角色
     */
    @PostMapping("")
    @Operation(summary = "创建角色", description = "创建新角色并分配权限")
    public ApiResponse<RoleDetailResponse> createRole(@Valid @RequestBody RoleCreateRequest request) {
        return ApiResponse.success(roleService.createRole(request));
    }

    /**
     * 更新角色信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新角色", description = "更新角色基本信息和权限")
    public ApiResponse<RoleDetailResponse> updateRole(@PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request) {
        return ApiResponse.success(roleService.updateRole(id, request));
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色", description = "删除指定角色")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success();
    }

    /**
     * 获取角色权限列表
     */
    @GetMapping("/{id}/permissions")
    @Operation(summary = "获取角色权限", description = "获取指定角色的权限列表")
    public ApiResponse<List<RoleDetailResponse.PermissionDto>> getRolePermissions(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRolePermissions(id));
    }

    /**
     * 更新角色权限
     */
    @PutMapping("/{id}/permissions")
    @Operation(summary = "更新角色权限", description = "更新指定角色的权限配置")
    public ApiResponse<Void> updateRolePermissions(@PathVariable Long id,
            @Valid @RequestBody RolePermissionUpdateRequest request) {
        roleService.updateRolePermissions(id, request.getPermissionIds());
        return ApiResponse.success();
    }

    /**
     * 获取拥有该角色的用户列表
     */
    @GetMapping("/{id}/users")
    @Operation(summary = "获取角色用户", description = "获取拥有指定角色的用户列表")
    public ApiResponse<PageResponse<RoleUserListResponse>> getRoleUsers(@PathVariable Long id,
            @Valid RoleUserListParams params) {
        return ApiResponse.success(roleService.getRoleUsers(id, params));
    }
}
