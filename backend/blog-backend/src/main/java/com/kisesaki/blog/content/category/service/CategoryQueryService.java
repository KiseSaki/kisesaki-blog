package com.kisesaki.blog.content.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.content.category.dto.query.CategoryQueryParams;
import com.kisesaki.blog.content.category.dto.query.CategoryTreeResponse;
import com.kisesaki.blog.content.category.mapper.CategoriesMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 分类服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryQueryService {

    private final CategoriesMapper categoriesMapper;

    /**
     * 获取分类列表
     *
     * @param params 分类列表参数
     * @return 分类列表响应数据
     */
    public PageResponse<CategoryTreeResponse> getCategoryList(CategoryQueryParams params) {
        log.debug("获取分类列表，参数: {}", params);

        // 查询分类列表数据
        List<CategoryTreeResponse> categories = categoriesMapper.selectCategoryList(params);

        // 查询总数
        Long totalCount = categoriesMapper.countCategoryList(params);

        // 构建树形结构
        List<CategoryTreeResponse> treeData = buildCategoryTree(categories);

        // 构建分页响应
        PageResponse<CategoryTreeResponse> response = PageResponse.of(treeData, totalCount, params.getPageable());

        log.debug("分类列表查询完成，返回 {} 条记录，总数: {}", treeData.size(), totalCount);
        return response;
    }

    /**
     * 构建分类树形结构
     *
     * @param categories 分类列表
     * @return 树形结构的分类列表
     */
    private List<CategoryTreeResponse> buildCategoryTree(List<CategoryTreeResponse> categories) {
        if (categories == null || categories.isEmpty()) {
            return new ArrayList<>();
        }

        // 按父分类ID分组
        Map<Long, List<CategoryTreeResponse>> parentIdGroupMap = categories.stream()
                .filter(category -> category.getParentId() != null)
                .collect(Collectors.groupingBy(CategoryTreeResponse::getParentId));

        // 构建树形结构
        List<CategoryTreeResponse> rootCategories = new ArrayList<>();

        for (CategoryTreeResponse category : categories) {
            // 设置子分类
            List<CategoryTreeResponse> children = parentIdGroupMap.get(category.getId());
            if (children != null) {
                // 按排序顺序排序子分类
                children.sort((c1, c2) -> {
                    if (c1.getSortOrder() == null && c2.getSortOrder() == null) {
                        return c1.getId().compareTo(c2.getId());
                    }
                    if (c1.getSortOrder() == null) {
                        return 1;
                    }
                    if (c2.getSortOrder() == null) {
                        return -1;
                    }
                    int sortComparison = c1.getSortOrder().compareTo(c2.getSortOrder());
                    return sortComparison != 0 ? sortComparison : c1.getId().compareTo(c2.getId());
                });
                category.setChildren(children);
            } else {
                category.setChildren(new ArrayList<>());
            }

            // 顶级分类添加到根列表
            if (category.getParentId() == null) {
                rootCategories.add(category);
            }
        }

        // 按排序顺序排序根分类
        rootCategories.sort((c1, c2) -> {
            if (c1.getSortOrder() == null && c2.getSortOrder() == null) {
                return c1.getId().compareTo(c2.getId());
            }
            if (c1.getSortOrder() == null) {
                return 1;
            }
            if (c2.getSortOrder() == null) {
                return -1;
            }
            int sortComparison = c1.getSortOrder().compareTo(c2.getSortOrder());
            return sortComparison != 0 ? sortComparison : c1.getId().compareTo(c2.getId());
        });

        return rootCategories;
    }
}
