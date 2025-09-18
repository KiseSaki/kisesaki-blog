package com.kisesaki.blog.content.comment.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论反应实体类
 * 存储对评论的点赞/踩等反应
 */
@Data
@NoArgsConstructor
@TableName("comment_reactions")
public class CommentReactions {

    /**
     * 反应唯一ID (自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 评论ID
     */
    @TableField("comment_id")
    private Long commentId;

    /**
     * 反应类型 (like, dislike)
     */
    @TableField("reaction_type")
    private ReactionType reactionType;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 反应类型枚举
     */
    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}
