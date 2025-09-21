package com.kisesaki.blog.content.comment.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.content.comment.dto.CommentListResponse;
import com.kisesaki.blog.content.comment.dto.admin.AdminBatchModerateBody;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentListParams;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentReportResponse;
import com.kisesaki.blog.content.comment.dto.admin.AdminCommentStatsResponse;
import com.kisesaki.blog.content.comment.entity.Comments;
import com.kisesaki.blog.content.comment.mapper.CommentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 管理员评论服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCommentService {

    private final CommentMapper commentMapper;

    /**
     * 获取所有评论列表（管理员）
     *
     * @param params 查询参数
     * @return 分页的评论列表
     */
    public PageResponse<CommentListResponse> getAllComments(AdminCommentListParams params) {
        log.info("管理员获取评论列表");

        Page<CommentListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize());

        Page<CommentListResponse> result = commentMapper.getAdminCommentList(page, params);

        return PageResponse.of(result);
    }

    /**
     * 更新评论状态
     *
     * @param commentId 评论ID
     * @param status    新状态
     * @param adminId   管理员ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCommentStatus(Long commentId, String status, Long adminId) {
        log.info("管理员 {} 更新评论 {} 状态为 {}", adminId, commentId, status);

        Comments comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw BusinessException.notFound("评论");
        }

        try {
            Comments.CommentStatus newStatus = Comments.CommentStatus.valueOf(status.toUpperCase());
            
            LambdaUpdateWrapper<Comments> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Comments::getId, commentId)
                    .set(Comments::getStatus, newStatus)
                    .set(Comments::getUpdatedAt, OffsetDateTime.now());

            int result = commentMapper.update(null, updateWrapper);
            if (result != 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新评论状态失败");
            }
        } catch (IllegalArgumentException e) {
            throw BusinessException.paramError("无效的评论状态");
        }
    }

    /**
     * 彻底删除评论
     *
     * @param commentId 评论ID
     * @param adminId   管理员ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long adminId) {
        log.info("管理员 {} 彻底删除评论 {}", adminId, commentId);

        Comments comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw BusinessException.notFound("评论");
        }

        // 物理删除评论
        int result = commentMapper.deleteById(commentId);
        if (result != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除评论失败");
        }
    }

    /**
     * 置顶评论
     *
     * @param commentId 评论ID
     * @param isPinned  是否置顶
     * @param adminId   管理员ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void pinComment(Long commentId, Boolean isPinned, Long adminId) {
        log.info("管理员 {} {} 评论 {}", adminId, isPinned ? "置顶" : "取消置顶", commentId);

        Comments comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw BusinessException.notFound("评论");
        }

        LambdaUpdateWrapper<Comments> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comments::getId, commentId)
                .set(Comments::getIsPinned, isPinned)
                .set(Comments::getUpdatedAt, OffsetDateTime.now());

        int result = commentMapper.update(null, updateWrapper);
        if (result != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新评论置顶状态失败");
        }
    }

    /**
     * 获取被举报的评论列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分页的被举报评论列表
     */
    public PageResponse<AdminCommentReportResponse> getReportedComments(int page, int size) {
        log.info("获取被举报的评论列表");

        Page<AdminCommentReportResponse> pageObj = new Page<>(page, size);
        Page<AdminCommentReportResponse> result = commentMapper.getReportedComments(pageObj);

        return PageResponse.of(result);
    }

    /**
     * 批量审核评论
     *
     * @param body    批量审核请求体
     * @param adminId 管理员ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchModerateComments(AdminBatchModerateBody body, Long adminId) {
        log.info("管理员 {} 批量审核评论，将 {} 条评论状态设为 {}", adminId, body.getIds().size(), body.getStatus());

        if (body.getIds().isEmpty()) {
            throw BusinessException.paramError("评论ID列表不能为空");
        }

        try {
            Comments.CommentStatus newStatus = Comments.CommentStatus.valueOf(body.getStatus().toUpperCase());
            
            LambdaUpdateWrapper<Comments> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Comments::getId, body.getIds())
                    .set(Comments::getStatus, newStatus)
                    .set(Comments::getUpdatedAt, OffsetDateTime.now());

            int result = commentMapper.update(null, updateWrapper);
            log.info("批量审核完成，实际更新了 {} 条评论", result);
        } catch (IllegalArgumentException e) {
            throw BusinessException.paramError("无效的评论状态");
        }
    }

    /**
     * 获取评论统计数据
     *
     * @return 评论统计数据
     */
    public AdminCommentStatsResponse getCommentStats() {
        log.info("获取评论统计数据");

        // 总评论数
        Long totalComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .isNull(Comments::getDeletedAt));

        // 各状态评论数
        Long pendingComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getStatus, Comments.CommentStatus.PENDING)
                .isNull(Comments::getDeletedAt));

        Long approvedComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getStatus, Comments.CommentStatus.APPROVED)
                .isNull(Comments::getDeletedAt));

        Long rejectedComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getStatus, Comments.CommentStatus.REJECTED)
                .isNull(Comments::getDeletedAt));

        Long spamComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .eq(Comments::getStatus, Comments.CommentStatus.SPAM)
                .isNull(Comments::getDeletedAt));

        // 被举报评论数
        Long reportedComments = commentMapper.getReportedCommentsCount();

        // 时间范围统计
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1); // 本周一
        LocalDate monthStart = today.withDayOfMonth(1); // 本月1号

        OffsetDateTime todayStart = today.atStartOfDay().atOffset(ZoneOffset.systemDefault().getRules().getOffset(today.atStartOfDay()));
        OffsetDateTime weekStartTime = weekStart.atStartOfDay().atOffset(ZoneOffset.systemDefault().getRules().getOffset(weekStart.atStartOfDay()));
        OffsetDateTime monthStartTime = monthStart.atStartOfDay().atOffset(ZoneOffset.systemDefault().getRules().getOffset(monthStart.atStartOfDay()));

        Long todayComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .ge(Comments::getCreatedAt, todayStart)
                .isNull(Comments::getDeletedAt));

        Long weekComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .ge(Comments::getCreatedAt, weekStartTime)
                .isNull(Comments::getDeletedAt));

        Long monthComments = commentMapper.selectCount(new LambdaQueryWrapper<Comments>()
                .ge(Comments::getCreatedAt, monthStartTime)
                .isNull(Comments::getDeletedAt));

        return AdminCommentStatsResponse.builder()
                .totalComments(totalComments)
                .pendingComments(pendingComments)
                .approvedComments(approvedComments)
                .rejectedComments(rejectedComments)
                .spamComments(spamComments)
                .reportedComments(reportedComments)
                .todayComments(todayComments)
                .weekComments(weekComments)
                .monthComments(monthComments)
                .build();
    }
}