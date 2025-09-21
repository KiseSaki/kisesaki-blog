package com.kisesaki.blog.content.comment.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kisesaki.blog.content.comment.handler.CommentReportStatusTypeHandler;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论举报实体类
 * 用于记录评论的举报信息
 */
@Data
@NoArgsConstructor
@TableName("comment_reports")
public class CommentReports {

    /**
     * 举报唯一ID (自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被举报的评论ID
     */
    @TableField("comment_id")
    private Long commentId;

    /**
     * 举报人用户ID
     */
    @TableField("reporter_id")
    private Long reporterId;

    /**
     * 举报原因
     */
    private String reason;

    /**
     * 举报详细描述（可选）
     */
    private String description;

    /**
     * 处理状态
     */
    @TableField(value = "status", typeHandler = CommentReportStatusTypeHandler.class)
    private ReportStatus status = ReportStatus.PENDING;

    /**
     * 处理人ID（管理员）
     */
    @TableField("handled_by")
    private Long handledBy;

    /**
     * 处理时间
     */
    @TableField("handled_at")
    private OffsetDateTime handledAt;

    /**
     * 处理备注
     */
    @TableField("handle_note")
    private String handleNote;

    /**
     * 举报者IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    /**
     * 举报状态枚举
     */
    public enum ReportStatus {
        PENDING,    // 待处理
        APPROVED,   // 举报成立
        REJECTED,   // 举报不成立
        IGNORED     // 忽略
    }
}