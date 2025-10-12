package com.kisesaki.blog.content.category.controller;

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

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryCreateRequest;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryListResponse;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryParentRequest;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryQueryParams;
import com.kisesaki.blog.content.category.dto.admin.AdminCategorySortRequest;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryTreeResponse;
import com.kisesaki.blog.content.category.dto.admin.AdminCategoryUpdateRequest;
import com.kisesaki.blog.content.category.service.AdminCategoryService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 管理员分类控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Tag(name = "管理员分类管理", description = "管理员分类管理相关接口")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    /**
     * 获取所有分类（含统计信息）
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    public ApiResponse<PageResponse<AdminCategoryListResponse>> getAdminCategoryList(
            @Valid AdminCategoryQueryParams params) {
        return ResultUtils.success(adminCategoryService.getAdminCategoryList(params));
    }

    /**
     * 创建新分类
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ApiResponse<AdminCategoryListResponse> createCategory(
            @Valid @RequestBody AdminCategoryCreateRequest request) {
        return ResultUtils.success(adminCategoryService.createCategory(request));
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_EDIT')")
    public ApiResponse<AdminCategoryListResponse> updateCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AdminCategoryUpdateRequest request) {
        return ResultUtils.success(adminCategoryService.updateCategory(id, request));
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public ApiResponse<Void> deleteCategory(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
        return ResultUtils.success("分类删除成功");
    }

    /**
     * 更新分类排序
     */
    @PutMapping("/{id}/sort")
    @PreAuthorize("hasAuthority('CATEGORY_EDIT')")
    public ApiResponse<Void> updateCategorySort(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AdminCategorySortRequest request) {
        adminCategoryService.updateCategorySort(id, request);
        return ResultUtils.success("分类排序更新成功");
    }

    /**
     * 修改分类父级关系
     */
    @PutMapping("/{id}/parent")
    @PreAuthorize("hasAuthority('CATEGORY_EDIT')")
    public ApiResponse<Void> updateCategoryParent(
            @Parameter(description = "分类ID", required = true) @PathVariable Long id,
            @Valid @RequestBody AdminCategoryParentRequest request) {
        adminCategoryService.updateCategoryParent(id, request);
        return ResultUtils.success("分类父级关系更新成功");
    }

    /**
     * 获取树形结构的分类列表
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('CATEGORY_VIEW')")
    public ApiResponse<List<AdminCategoryTreeResponse>> getAdminCategoryTree() {
        return ResultUtils.success(adminCategoryService.getAdminCategoryTree());
    }
}
