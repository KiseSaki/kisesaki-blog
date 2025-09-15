package com.kisesaki.blog.content.tag.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagDetailResponse;
import com.kisesaki.blog.content.tag.entity.Tags;

@Mapper
public interface TagsMapper extends BaseMapper<Tags> {
    TagDetailResponse getTagDetailById(Long id);
}
