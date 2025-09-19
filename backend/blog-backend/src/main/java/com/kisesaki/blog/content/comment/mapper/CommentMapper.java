package com.kisesaki.blog.content.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.comment.dto.CommentListParams;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.entity.Comments;

@Mapper
public interface CommentMapper extends BaseMapper<Comments> {
        /**
         * 获取顶级评论列表（支持分页，自动嵌套回复）
         * totalRecords 只统计顶级评论数量
         *
         * @param page   分页参数
         * @param params 筛选参数
         * @param postId 文章ID
         * @return 分页的顶级评论列表，每个评论包含前3条回复
         */
        Page<CommentListResponse> getCommentList(
                        Page<CommentListResponse> page,
                        @Param("params") CommentListParams params,
                        @Param("postId") Long postId);

        /**
         * 获取指定评论的回复列表（用于collection子查询）
         * 限制返回前3条回复
         *
         * @param parentId 父评论ID
         * @param postId   文章ID
         * @return 回复列表
         */
        List<CommentListResponse> selectRepliesByParentId(
                        @Param("parentId") Long parentId,
                        @Param("postId") Long postId);

        /**
         * 获取指定评论的回复总数
         * 用于判断 hasMoreReplies 字段
         *
         * @param parentId 父评论ID
         * @param postId   文章ID
         * @return 回复总数
         */
        Long getReplyCount(
                        @Param("parentId") Long parentId,
                        @Param("postId") Long postId);

        /**
         * 获取更多回复（用于"查看更多回复"功能）
         *
         * @param parentId 父评论ID
         * @param postId   文章ID
         * @param offset   偏移量
         * @param limit    限制数量
         * @return 回复列表
         */
        List<CommentListResponse> getMoreReplies(
                        @Param("parentId") Long parentId,
                        @Param("postId") Long postId,
                        @Param("offset") int offset,
                        @Param("limit") int limit);
}
