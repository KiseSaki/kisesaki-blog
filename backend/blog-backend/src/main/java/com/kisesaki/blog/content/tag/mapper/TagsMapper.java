package com.kisesaki.blog.content.tag.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagDetailResponse;
import com.kisesaki.blog.content.tag.entity.Tags;

@Mapper
public interface TagsMapper extends BaseMapper<Tags> {

    /**
     * 根据ID查询标签详情
     * 
     * @param slug 标签Slug
     * @return 标签详情
     */
    TagDetailResponse getTagDetailById(Long id);

    /**
     * 根据Slug查询标签详情
     * 
     * @param slug 标签Slug
     * @return 标签详情
     */
    TagDetailResponse getTagDetailBySlug(String slug);
}
