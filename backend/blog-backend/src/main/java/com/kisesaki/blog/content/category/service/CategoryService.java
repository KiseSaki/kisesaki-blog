package com.kisesaki.blog.content.category.service;

import com.kisesaki.blog.content.category.mapper.CategoriesMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分类服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoriesMapper categoriesMapper;
}
