package com.kisesaki.blog.auth.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 角色实体，对应表 roles
 * 
 * @author KiseSaki
 */
@Data
@TableName("roles")
public class Role {

    /** 角色唯一ID (自增) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 角色名称 */
    private String name;

    /** 角色描述 */
    private String description;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
}
