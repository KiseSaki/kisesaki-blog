package com.kisesaki.blog.content.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.comment.entity.CommentReactions;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentReactionMapper extends BaseMapper<CommentReactions> {
}
