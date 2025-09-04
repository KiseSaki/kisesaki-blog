package com.kisesaki.blog.user.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.entity.UserSettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息 DTO
 * <p>
 * 该 DTO 将用户基础信息（来自 User 实体）、扩展资料（来自 UserProfile 实体）
 * 以及设置（来自 UserSettings）合并，用于对外返回用户详情。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDto {

    // ===== User 基本信息（来源：user 表） =====
    /** 用户唯一ID（Snowflake） */
    private Long id;

    /** 用户名，唯一 */
    private String username;

    /** 电子邮箱 */
    private String email;

    /** 账号类型，例如 'local' 或 'oauth' */
    private String accountType;

    /** 用户状态，例如 'active', 'inactive', 'banned' */
    private String status;

    /** 邮箱是否已验证 */
    private Boolean emailVerified;

    /** 邮箱验证时间 */
    private OffsetDateTime emailVerifiedAt;

    /** 最后登录时间 */
    private OffsetDateTime lastLoginAt;

    /** 最后登录 IP（字符串形式） */
    private String lastLoginIp;

    /** 账号创建时间 */
    private OffsetDateTime createdAt;

    /** 账号更新时间 */
    private OffsetDateTime updatedAt;

    // ===== UserProfile 扩展信息（来源：user_profiles 表） =====
    /** 显示名称 */
    private String displayName;

    /** 名字 */
    private String firstName;

    /** 姓氏 */
    private String lastName;

    /** 个人简介 */
    private String bio;

    /** 头像 URL */
    private String avatarUrl;

    /** 封面图 URL */
    private String coverImageUrl;

    /** 个人网站 URL */
    private String websiteUrl;

    /** 所在地 */
    private String location;

    /** 公司/组织 */
    private String company;

    /** 职位标题 */
    private String title;

    /** 社交媒体链接（JSON） */
    private JsonNode socialLinks;

    /** 出生日期 */
    private LocalDate birthDate;

    /** 性别，例如 'male','female','other' */
    private String gender;

    /** 时区 */
    private String timezone;

    /** 首选语言 */
    private String language;

    /** 主题偏好，例如 'light','dark','system' */
    private String themePreference;

    /** 隐私级别，例如 'public','friends','private' */
    private String privacyLevel;

    // ===== UserSettings（来源：user_settings 表） =====
    /** 用户设置键值对映射（settingKey -> settingValue） */
    private Map<String, String> settings;

    /**
     * 从实体构建 DTO 的静态工厂方法。
     *
     * @param user 用户实体，不能为空
     * @param profile 用户扩展资料，允许为空
     * @param settingsList 用户设置列表，允许为空
     * @return UserInfoDto
     */
    public static UserInfoDto from(User user, UserProfile profile, List<UserSettings> settingsList) {
        if (user == null) return null;

        Map<String, String> settingsMap = new HashMap<>();
        if (settingsList != null) {
            for (UserSettings s : settingsList) {
                if (s != null && s.getSettingKey() != null) {
                    settingsMap.put(s.getSettingKey(), s.getSettingValue());
                }
            }
        }

        return UserInfoDto.builder()
                // User
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .accountType(user.getAccountType())
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .emailVerifiedAt(user.getEmailVerifiedAt())
                .lastLoginAt(user.getLastLoginAt())
                .lastLoginIp(user.getLastLoginIp())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                // Profile
                .displayName(profile == null ? null : profile.getDisplayName())
                .firstName(profile == null ? null : profile.getFirstName())
                .lastName(profile == null ? null : profile.getLastName())
                .bio(profile == null ? null : profile.getBio())
                .avatarUrl(profile == null ? null : profile.getAvatarUrl())
                .coverImageUrl(profile == null ? null : profile.getCoverImageUrl())
                .websiteUrl(profile == null ? null : profile.getWebsiteUrl())
                .location(profile == null ? null : profile.getLocation())
                .company(profile == null ? null : profile.getCompany())
                .title(profile == null ? null : profile.getTitle())
                .socialLinks(profile == null ? null : profile.getSocialLinks())
                .birthDate(profile == null ? null : profile.getBirthDate())
                .gender(profile == null ? null : profile.getGender())
                .timezone(profile == null ? null : profile.getTimezone())
                .language(profile == null ? null : profile.getLanguage())
                .themePreference(profile == null ? null : profile.getThemePreference())
                .privacyLevel(profile == null ? null : profile.getPrivacyLevel())
                // Settings
                .settings(settingsMap)
                .build();
    }
}
