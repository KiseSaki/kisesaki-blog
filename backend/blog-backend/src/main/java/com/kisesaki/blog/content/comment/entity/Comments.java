package com.kisesaki.blog.content.comment.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kisesaki.blog.content.comment.handler.CommentStatusTypeHandler;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论实体类
 * 支持嵌套评论和丰富的交互功能
 */
@Data
@NoArgsConstructor
@TableName("comments")
public class Comments {

    /**
     * 评论唯一ID (自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属文章ID
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 评论用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 父评论ID (用于实现嵌套评论)
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 回复目标评论ID (用于@功能)
     */
    @TableField("reply_to_id")
    private Long replyToId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 渲染后的HTML内容
     */
    @TableField("html_content")
    private String htmlContent;

    /**
     * 点赞数量
     */
    @TableField("like_count")
    private Integer likeCount = 0;

    /**
     * 踩数量
     */
    @TableField("dislike_count")
    private Integer dislikeCount = 0;

    /**
     * 直接回复数量
     */
    @TableField("reply_count")
    private Integer replyCount = 0;

    /**
     * 嵌套层级 (0为顶级评论)
     */
    private Integer level = 0;

    /**
     * 评论路径 (如: "1.3.5" 表示层级关系)
     */
    private String path;

    /**
     * 评论状态 (pending, approved, rejected, spam)
     */
    @TableField(value = "status", typeHandler = CommentStatusTypeHandler.class)
    private CommentStatus status = CommentStatus.APPROVED;

    /**
     * 是否置顶
     */
    @TableField("is_pinned")
    private Boolean isPinned = false;

    /**
     * 是否为作者回复
     */
    @TableField("is_author_reply")
    private Boolean isAuthorReply = false;

    /**
     * 评论者IP地址
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 用户代理字符串
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 最后编辑时间
     */
    @TableField("edited_at")
    private LocalDateTime editedAt;

    /**
     * 删除时间 (软删除)
     */
    @TableField("deleted_at")
    @TableLogic
    private LocalDateTime deletedAt;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 评论状态枚举
     */
    public enum CommentStatus {
        PENDING,
        APPROVED,
        REJECTED,
        SPAM
    }
}
