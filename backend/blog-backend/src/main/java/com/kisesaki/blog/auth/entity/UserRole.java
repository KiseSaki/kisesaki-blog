package com.kisesaki.blog.auth.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户角色关联实体，对应表 user_roles
 * 实现用户与角色的多对多关系
 * 
 * @author KiseSaki
 */
@Data
@TableName("user_roles")
public class UserRole {

    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;

    /** 分配时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime assignedAt;
}
