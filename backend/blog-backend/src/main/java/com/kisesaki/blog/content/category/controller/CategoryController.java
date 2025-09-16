package com.kisesaki.blog.content.category.controller;

import com.kisesaki.blog.content.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final CategoryService categoryService;
}
