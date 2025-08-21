package com.kisesaki.blog.user.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户实体，对应表 user
 */
@Data
@TableName("user")
public class User {
    /** 用户唯一ID (雪花算法) */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户名，唯一且非空 */
    private String username;

    /** 电子邮箱，唯一且非空 */
    private String email;

    /** 加密后的密码（OAuth登录用户可为空） */
    private String password;

    /** OAuth 提供商的用户ID */
    private String oauthId;

    /** OAuth 提供商（'github', 'gitee', 'google'） */
    private String oauthProvider;

    /** 账号类型（'local', 'oauth'），默认 'local'，非空 */
    private String accountType;

    /** 用户状态（'active', 'inactive', 'banned'），默认 'active'，非空 */
    private String status;

    /** 邮箱是否已验证，默认 false，非空 */
    private Boolean emailVerified;

    /** 邮箱验证时间 */
    private OffsetDateTime emailVerifiedAt;

    /** 最后登录时间 */
    private OffsetDateTime lastLoginAt;

    /** 最后登录IP（PostgreSQL INET 类型，Java用 String 表示） */
    private String lastLoginIp;

    /** 创建时间，默认 NOW()，非空 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    /** 更新时间，默认 NOW()，非空 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;
}
