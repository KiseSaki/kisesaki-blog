package com.kisesaki.blog.content.interaction.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kisesaki.blog.content.interaction.handler.LikeReactionTypeHandler;
import com.kisesaki.blog.content.interaction.handler.TargetTypeHandler;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一点赞反应实体类
 * 用于处理文章和评论的点赞/踩等反应
 */
@Data
@NoArgsConstructor
@TableName("likes")
public class Likes {

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
     * 目标类型 (POST, COMMENT)
     */
    @TableField(value = "target_type", typeHandler = TargetTypeHandler.class)
    private TargetType targetType;

    /**
     * 目标ID (根据target_type指向posts.id或comments.id)
     */
    @TableField("target_id")
    private Long targetId;

    /**
     * 反应类型 (LIKE, DISLIKE)
     */
    @TableField(value = "reaction_type", typeHandler = LikeReactionTypeHandler.class)
    private ReactionType reactionType;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    /**
     * 目标类型枚举
     */
    public enum TargetType {
        POST,
        COMMENT
    }

    /**
     * 反应类型枚举
     */
    public enum ReactionType {
        LIKE,
        DISLIKE
    }
}