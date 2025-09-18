package com.kisesaki.blog.interaction.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用点赞实体类
 * 支持对文章、评论等的点赞
 */
@Data
@NoArgsConstructor
@TableName("likes")
public class Likes {

    /**
     * 点赞唯一ID (自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 目标对象ID
     */
    @TableField("target_id")
    private Long targetId;

    /**
     * 目标对象类型 (post, comment)
     */
    @TableField("target_type")
    private TargetType targetType;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 目标类型枚举
     */
    public enum TargetType {
        POST,
        COMMENT
    }
}
