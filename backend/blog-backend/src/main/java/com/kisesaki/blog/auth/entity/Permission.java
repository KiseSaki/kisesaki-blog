package com.kisesaki.blog.auth.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 权限实体，对应表 permissions
 * 
 * @author KiseSaki
 */
@Data
@TableName("permissions")
public class Permission {

    /** 权限唯一ID (自增) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 权限名称 */
    private String name;

    /** 权限描述 */
    private String description;

    /** 资源类型 */
    private String resource;

    /** 操作类型 */
    private String action;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
}
