package com.kisesaki.blog.user.dto.admin;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员获取用户信息响应")
public class AdminUserInfoResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "OAuth 提供商的用户ID")
    private String oauthId;

    @Schema(description = "OAuth 提供商")
    private String oauthProvider;

    @Schema(description = "账号类型")
    private String accountType;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "邮箱是否已验证")
    private Boolean emailVerified;

    @Schema(description = "邮箱验证时间")
    private OffsetDateTime emailVerifiedAt;

    @Schema(description = "最后登录时间")
    private OffsetDateTime lastLoginAt;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    @Schema(description = "更新时间")
    private OffsetDateTime updatedAt;

    // UserProfile相关信息
    @Schema(description = "显示名称")
    private String displayName;

    @Schema(description = "名字")
    private String firstName;

    @Schema(description = "姓氏")
    private String lastName;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "用户头像URL")
    private String avatarUrl;

    @Schema(description = "用户封面图URL")
    private String coverImageUrl;

    @Schema(description = "个人网站URL")
    private String websiteUrl;

    @Schema(description = "所在地")
    private String location;

    @Schema(description = "公司/组织")
    private String company;

    @Schema(description = "职位标题")
    private String title;

    @Schema(description = "社交媒体链接")
    private JsonNode socialLinks;

    @Schema(description = "出生日期")
    private LocalDate birthDate;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "时区设置")
    private String timezone;

    @Schema(description = "首选语言")
    private String language;

    @Schema(description = "主题偏好")
    private String themePreference;

    @Schema(description = "隐私级别")
    private String privacyLevel;

}
