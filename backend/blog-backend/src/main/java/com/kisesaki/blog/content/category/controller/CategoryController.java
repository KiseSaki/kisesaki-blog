package com.kisesaki.blog.content.category.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.category.dto.query.CategoryDetailResponse;
import com.kisesaki.blog.content.category.dto.query.CategoryPostsParams;
import com.kisesaki.blog.content.category.dto.query.CategoryQueryParams;
import com.kisesaki.blog.content.category.dto.query.CategoryTreeResponse;
import com.kisesaki.blog.content.category.dto.query.PopularCategoryParams;
import com.kisesaki.blog.content.category.dto.query.PopularCategoryResponse;
import com.kisesaki.blog.content.category.service.CategoryQueryService;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 分类控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "分类", description = "分类相关接口")
public class CategoryController {

    private final CategoryQueryService categoryService;

    /**
     * 获取分类列表
     */
    @GetMapping("")
    public ApiResponse<PageResponse<CategoryTreeResponse>> getCategoryList(@Valid CategoryQueryParams params) {
        return ResultUtils.success(categoryService.getCategoryList(params));
    }

    /**
     * 根据ID获取分类详情
     */
    @GetMapping("/{id}")
    public ApiResponse<CategoryDetailResponse> getCategoryDetailById(@PathVariable Long id) {
        return ResultUtils.success(categoryService.getCategoryDetailById(id));
    }

    /**
     * 根据别名获取分类详情
     */
    @GetMapping("/slug/{slug}")
    public ApiResponse<CategoryDetailResponse> getCategoryDetailBySlug(@PathVariable String slug) {
        return ResultUtils.success(categoryService.getCategoryDetailBySlug(slug));
    }

    /**
     * 获取热门分类
     */
    @GetMapping("/popular")
    public ApiResponse<List<PopularCategoryResponse>> getPopularCategories(@Valid PopularCategoryParams params) {
        return ResultUtils.success(categoryService.getPopularCategories(params));
    }

    /**
     * 获取指定分类下的文章列表
     */
    @GetMapping("/{categoryId}/posts")
    @Operation(summary = "获取指定分类下的文章列表", description = "根据分类ID获取该分类下的已发布文章列表，支持分页和排序")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getCategoryPosts(
            @Parameter(description = "分类ID", required = true, example = "1") @PathVariable Long categoryId,
            @Valid CategoryPostsParams params) {
        return ResultUtils.success(categoryService.getCategoryPosts(categoryId, params));
    }
}
