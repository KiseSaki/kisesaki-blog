package com.kisesaki.blog.content.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.category.dto.query.CategoryDetailResponse;
import com.kisesaki.blog.content.category.dto.query.CategoryQueryParams;
import com.kisesaki.blog.content.category.dto.query.CategoryTreeResponse;
import com.kisesaki.blog.content.category.entity.Categories;

@Mapper
public interface CategoriesMapper extends BaseMapper<Categories> {

    /**
     * 根据查询参数获取分类列表
     * 
     * @param params 查询参数
     * @return 分类列表
     */
    List<CategoryTreeResponse> selectCategoryList(@Param("params") CategoryQueryParams params);

    /**
     * 根据查询参数统计分类数量
     * 
     * @param params 查询参数
     * @return 分类总数
     */
    Long countCategoryList(@Param("params") CategoryQueryParams params);

    /**
     * 根据分类ID获取分类详情
     *
     * @param categoryId 分类ID
     * @return 分类详情响应数据
     */
    CategoryDetailResponse getCategoryDetailById(Long categoryId);

    /**
     * 获取指定父分类的直接子分类
     *
     * @param parentId 父分类ID
     * @return 直接子分类列表
     */
    List<CategoryDetailResponse> getDirectChildren(Long parentId);
}
