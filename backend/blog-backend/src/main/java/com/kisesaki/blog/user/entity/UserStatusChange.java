package com.kisesaki.blog.user.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user_status_changes")
public class UserStatusChange {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    /** 操作人（管理员）ID */
    private Long changedBy;

    private String oldStatus;

    private String newStatus;

    /** 变更原因 */
    private String reason;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
}