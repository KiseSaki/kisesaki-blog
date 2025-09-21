package com.kisesaki.blog.content.comment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.comment.dto.CommentListParams;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentListParams;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentReportResponse;
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

        /**
         * 获取单条评论详情
         *
         * @param commentId 评论ID
         * @return 评论详情
         */
        CommentListResponse getCommentById(@Param("commentId") Long commentId);

        /**
         * 获取文章基本信息
         *
         * @param postId 文章ID
         * @return 包含文章标题和slug的Map
         */
        java.util.Map<String, Object> getPostBasicInfo(@Param("postId") Long postId);

        /**
         * 获取评论回复列表（分页版本，用于懒加载）
         *
         * @param page     分页参数
         * @param parentId 父评论ID
         * @return 分页的回复列表
         */
        Page<CommentListResponse> getCommentRepliesPaged(
                        Page<CommentListResponse> page,
                        @Param("parentId") Long parentId);

        /**
         * 获取用户的评论列表（我的评论）
         *
         * @param page   分页参数
         * @param userId 用户ID
         * @param postId 文章ID（可选）
         * @param status 评论状态（可选）
         * @return 分页的用户评论列表
         */
        Page<CommentListResponse> getMyComments(
                        Page<CommentListResponse> page,
                        @Param("userId") Long userId,
                        @Param("postId") Long postId,
                        @Param("status") String status);

        /**
         * 获取所有评论列表（管理员）
         *
         * @param page   分页参数
         * @param params 查询参数
         * @return 分页的评论列表
         */
        Page<CommentListResponse> getAdminCommentList(
                        Page<CommentListResponse> page,
                        @Param("params") AdminCommentListParams params);

        /**
         * 获取被举报的评论列表
         *
         * @param page 分页参数
         * @return 分页的被举报评论列表
         */
        Page<AdminCommentReportResponse> getReportedComments(Page<AdminCommentReportResponse> page);

        /**
         * 获取被举报评论的总数
         *
         * @return 被举报评论总数
         */
        Long getReportedCommentsCount();
}
