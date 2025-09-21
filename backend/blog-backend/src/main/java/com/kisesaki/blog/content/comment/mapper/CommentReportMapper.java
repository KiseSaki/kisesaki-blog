package com.kisesaki.blog.content.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.comment.entity.CommentReports;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentReportMapper extends BaseMapper<CommentReports> {
}