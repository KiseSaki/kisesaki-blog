package com.kisesaki.blog.content.category.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.category.dto.query.CategoryQueryParams;
import com.kisesaki.blog.content.category.dto.query.CategoryTreeResponse;
import com.kisesaki.blog.content.category.service.CategoryQueryService;

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
     * 
     * @param params 查询参数
     * @return 分类列表响应
     */
    @GetMapping("")
    public ApiResponse<PageResponse<CategoryTreeResponse>> getCategoryList(@Valid CategoryQueryParams params) {
        return ResultUtils.success(categoryService.getCategoryList(params));
    }
}
