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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.permission.PermissionCreateRequest;
import com.kisesaki.blog.auth.dto.permission.PermissionDetailResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionListParams;
import com.kisesaki.blog.auth.dto.permission.PermissionListResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionOptionResponse;
import com.kisesaki.blog.auth.dto.permission.PermissionUpdateRequest;
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
@PreAuthorize("hasRole('ADMIN')")
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

    /**
     * 创建权限
     */
    @PostMapping("")
    public ApiResponse<Void> createPermission(@Valid @RequestBody PermissionCreateRequest request) {
        permissionService.createPermission(request);
        return ApiResponse.success("权限创建成功");
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> updatePermission(@PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequest request) {
        permissionService.updatePermission(id, request);
        return ApiResponse.success("权限更新成功");
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.success("权限删除成功");
    }

    /**
     * 获取权限资源类型列表
     */
    @GetMapping("/resources")
    public ApiResponse<List<PermissionOptionResponse>> getPermissionResources() {
        List<PermissionOptionResponse> resources = permissionService.getPermissionResources();
        return ApiResponse.success(resources);
    }

    /**
     * 获取权限操作类型列表
     */
    @GetMapping("/actions")
    public ApiResponse<List<PermissionOptionResponse>> getPermissionActions() {
        List<PermissionOptionResponse> actions = permissionService.getPermissionActions();
        return ApiResponse.success(actions);
    }

}
