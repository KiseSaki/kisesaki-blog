package com.kisesaki.blog.user.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * 用户扩展信息实体，对应表 user_profiles
 * 
 * @author KiseSaki
 */
@Data
@TableName("user_profiles")
public class UserProfile {

    /** 主键ID (自增) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 显示名称 */
    private String displayName;

    /** 名字 */
    private String firstName;

    /** 姓氏 */
    private String lastName;

    /** 个人简介 */
    private String bio;

    /** 用户头像URL */
    private String avatarUrl;

    /** 用户封面图URL */
    private String coverImageUrl;

    /** 个人网站URL */
    private String websiteUrl;

    /** 所在地 */
    private String location;

    /** 公司/组织 */
    private String company;

    /** 职位标题 */
    private String title;

    /** 社交媒体链接 (JSON格式) */
    private JsonNode socialLinks;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 性别 ('male', 'female', 'other') */
    private String gender;

    /** 时区设置 */
    private String timezone;

    /** 首选语言 */
    private String language;

    /** 主题偏好 ('light', 'dark', 'system') */
    private String themePreference;

    /** 隐私级别 ('public', 'friends', 'private') */
    private String privacyLevel;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;
}
