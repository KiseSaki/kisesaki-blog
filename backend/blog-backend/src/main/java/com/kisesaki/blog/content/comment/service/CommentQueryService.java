package com.kisesaki.blog.content.comment.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.content.comment.dto.CommentDetailResponse;
import com.kisesaki.blog.content.comment.dto.CommentListParams;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 评论服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommentQueryService {

    private final CommentMapper commentMapper;

    /**
     * 获取文章的两级评论列表
     * 使用 MyBatis 嵌套查询，直接返回完整的树形结构
     * totalRecords 只统计顶级评论数量
     *
     * @param postId 文章ID
     * @param params 查询参数
     * @return 分页的顶级评论列表，每个评论自动包含前3条回复
     */
    public PageResponse<CommentListResponse> getCommentList(Long postId, CommentListParams params) {
        log.info("获取文章 {} 的评论列表", postId);

        Page<CommentListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize());

        // 直接查询，MyBatis 会自动组装嵌套结构
        Page<CommentListResponse> result = commentMapper.getCommentList(page, params, postId);

        // 设置 hasMoreReplies 标志
        for (CommentListResponse comment : result.getRecords()) {
            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                // 如果回复数量 > 显示的回复数量（3条），则还有更多
                boolean hasMoreReplies = comment.getReplyCount() > comment.getReplies().size();
                comment.setHasMoreReplies(hasMoreReplies);

                log.debug("顶级评论 {} 有 {} 条回复，显示了 {} 条，还有更多: {}",
                        comment.getId(), comment.getReplyCount(), comment.getReplies().size(), hasMoreReplies);
            } else {
                comment.setHasMoreReplies(false);
            }
        }

        return PageResponse.of(result);
    }

    /**
     * 获取更多回复（用于"查看更多回复"功能）
     *
     * @param parentId 父评论ID
     * @param postId   文章ID
     * @param offset   偏移量
     * @param limit    限制数量
     * @return 回复列表
     */
    public List<CommentListResponse> getMoreReplies(Long parentId, Long postId, int offset, int limit) {
        log.info("获取评论 {} 的更多回复，offset: {}, limit: {}", parentId, offset, limit);

        return commentMapper.getMoreReplies(parentId, postId, offset, limit);
    }

    /**
     * 获取单条评论详情
     *
     * @param commentId 评论ID
     * @return 评论详情
     */
    public CommentDetailResponse getCommentDetail(Long commentId) {
        log.info("获取评论详情：{}", commentId);

        CommentListResponse comment = commentMapper.getCommentById(commentId);
        if (comment == null) {
            throw BusinessException.notFound("评论");
        }

        // 构建上下文信息
        CommentDetailResponse.CommentContextDto.CommentContextDtoBuilder contextBuilder = CommentDetailResponse.CommentContextDto
                .builder()
                .postId(comment.getPostId());

        // 获取文章基本信息
        Map<String, Object> postInfo = commentMapper.getPostBasicInfo(comment.getPostId());
        if (postInfo != null) {
            contextBuilder
                    .postTitle((String) postInfo.get("title"))
                    .postSlug((String) postInfo.get("slug"));
        }

        // 如果是回复，获取父评论信息
        if (comment.getParentId() != null) {
            CommentListResponse parentComment = commentMapper.getCommentById(comment.getParentId());
            contextBuilder.parentComment(parentComment);
        }

        // 如果是@回复，获取被回复的评论信息
        if (comment.getReplyToId() != null && !comment.getReplyToId().equals(comment.getParentId())) {
            CommentListResponse replyToComment = commentMapper.getCommentById(comment.getReplyToId());
            contextBuilder.replyToComment(replyToComment);
        }

        CommentDetailResponse.CommentContextDto context = contextBuilder.build();

        return CommentDetailResponse.builder()
                .comment(comment)
                .context(context)
                .build();
    }

    /**
     * 获取评论回复列表（分页）
     *
     * @param parentId 父评论ID
     * @param page     页码
     * @param size     每页大小
     * @return 分页的回复列表
     */
    public PageResponse<CommentListResponse> getCommentReplies(Long parentId, int page, int size) {
        log.info("获取评论 {} 的回复列表，page: {}, size: {}", parentId, page, size);

        Page<CommentListResponse> pageObj = new Page<>(page, size);
        Page<CommentListResponse> result = commentMapper.getCommentRepliesPaged(pageObj, parentId);

        return PageResponse.of(result);
    }
}
