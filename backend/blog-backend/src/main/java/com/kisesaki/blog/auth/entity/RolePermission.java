package com.kisesaki.blog.auth.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 角色权限关联实体，对应表 role_permissions
 * 实现角色与权限的多对多关系
 * 
 * @author KiseSaki
 */
@Data
@TableName("role_permissions")
public class RolePermission {

    /** 角色ID */
    private Long roleId;

    /** 权限ID */
    private Long permissionId;

    /** 授予时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime grantedAt;
}
