package com.kisesaki.blog.content.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.kisesaki.blog.content.category.dto.query.*;
import org.springframework.stereotype.Service;

import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
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
     * 根据ID获取分类详情
     * 
     * @param categoryId 分类ID
     * @return 分类详情响应数据，若分类不存在或不可见则返回 null
     */
    public CategoryDetailResponse getCategoryDetailById(Long categoryId) {
        log.debug("获取分类详情，分类ID: {}", categoryId);

        // 1. 获取当前分类基本信息
        CategoryDetailResponse categoryDetail = categoriesMapper.getCategoryDetailById(categoryId);
        if (categoryDetail == null) {
            log.warn("分类不存在或不可见，分类ID: {}", categoryId);
            throw BusinessException.notFound("分类不存在或不可见");
        }

        // 2. 递归查询并构建子分类树
        List<CategoryDetailResponse> children = getChildrenRecursively(categoryId);
        categoryDetail.setChildren(children);

        log.debug("分类详情查询完成，分类ID: {}, 子分类数量: {}", categoryId, children.size());
        return categoryDetail;
    }

    /**
     * 根据别名获取分类详情
     *
     * @param slug 分类别名
     * @return 分类详情响应数据，若分类不存在或不可见则返回 null
     */
    public CategoryDetailResponse getCategoryDetailBySlug(String slug) {
        log.debug("获取分类详情，分类别名: {}", slug);

        // 1. 获取当前分类基本信息
        CategoryDetailResponse categoryDetail = categoriesMapper.getCategoryDetailBySlug(slug);
        if (categoryDetail == null) {
            log.warn("分类不存在或不可见，分类别名: {}", slug);
            throw BusinessException.notFound("分类不存在或不可见");
        }

        // 2. 递归查询并构建子分类树
        List<CategoryDetailResponse> children = getChildrenRecursively(categoryDetail.getId());
        categoryDetail.setChildren(children);

        log.debug("分类详情查询完成，分类别名: {}, 子分类数量: {}", slug, children.size());
        return categoryDetail;
    }

    /**
     * 获取热门分类列表
     *
     * @param params 热门分类参数
     * @return 热门分类响应数据列表
     */
    public List<PopularCategoryResponse> getPopularCategories(PopularCategoryParams params) {
        log.debug("获取热门分类列表，参数: {}", params);
        List<PopularCategoryResponse> popularCategories = categoriesMapper.getPopularCategories(params);
        log.debug("热门分类列表查询完成，返回 {} 条记录", popularCategories.size());
        return popularCategories;
    }

    /**
     * 递归获取子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    private List<CategoryDetailResponse> getChildrenRecursively(Long parentId) {
        // 查询直接子分类
        List<CategoryDetailResponse> children = categoriesMapper.getDirectChildren(parentId);

        // 按排序顺序排序
        sortCategoryDetails(children);

        return children;
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
                sortCategories(children);
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
        sortCategories(rootCategories);

        return rootCategories;
    }

    /**
     * 按排序顺序对 CategoryTreeResponse 列表进行排序
     *
     * @param children 分类列表
     */
    private void sortCategories(List<CategoryTreeResponse> children) {
        sortGeneric(children, CategoryTreeResponse::getSortOrder, CategoryTreeResponse::getId);
    }

    /**
     * 按排序顺序对 CategoryDetailResponse 列表进行排序
     *
     * @param children 分类详情列表
     */
    private void sortCategoryDetails(List<CategoryDetailResponse> children) {
        sortGeneric(children, CategoryDetailResponse::getSortOrder, CategoryDetailResponse::getId);
    }

    /**
     * 通用排序实现（避免泛型擦除冲突）
     *
     * @param list            列表
     * @param sortOrderGetter 获取排序字段的函数（可能为 null）
     * @param idGetter        获取 id 的函数（用于回退比较）
     * @param <T>             列表元素类型
     */
    private <T> void sortGeneric(List<T> list, java.util.function.Function<T, Integer> sortOrderGetter,
            java.util.function.Function<T, Long> idGetter) {
        list.sort((o1, o2) -> {
            Integer s1 = sortOrderGetter.apply(o1);
            Integer s2 = sortOrderGetter.apply(o2);

            if (s1 == null && s2 == null) {
                return idGetter.apply(o1).compareTo(idGetter.apply(o2));
            }
            if (s1 == null) {
                return 1;
            }
            if (s2 == null) {
                return -1;
            }
            int sortComparison = s1.compareTo(s2);
            return sortComparison != 0 ? sortComparison : idGetter.apply(o1).compareTo(idGetter.apply(o2));
        });
    }
}
