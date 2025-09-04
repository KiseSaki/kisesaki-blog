package com.kisesaki.blog.content.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.category.entity.Categories;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoriesMapper extends BaseMapper<Categories> {
}
