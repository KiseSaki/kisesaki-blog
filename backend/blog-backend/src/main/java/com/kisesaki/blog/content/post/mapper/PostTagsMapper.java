package com.kisesaki.blog.content.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.post.entity.PostTags;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostTagsMapper extends BaseMapper<PostTags> {
}
