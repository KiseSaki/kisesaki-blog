package com.kisesaki.blog.user.dto.admin;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员更新用户信息请求")
public class AdminUserUpdateRequest {

    @Schema(description = "用户名", example = "john_doe")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和连字符")
    private String username;

    @Schema(description = "电子邮箱", example = "john@example.com")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @Schema(description = "用户状态", example = "active", allowableValues = { "active", "inactive", "banned" })
    @Pattern(regexp = "^(active|inactive|banned)$", message = "用户状态必须是active、inactive或banned之一")
    private String status;

    @Schema(description = "邮箱是否已验证", example = "true")
    private Boolean emailVerified;

    /** 显示名称 */
    @Schema(description = "显示名称", example = "John Doe")
    @Size(max = 100, message = "显示名称长度不能超过100个字符")
    private String displayName;

    /** 名字 */
    @Schema(description = "名字", example = "John")
    @Size(max = 50, message = "名字长度不能超过50个字符")
    private String firstName;

    /** 姓氏 */
    @Schema(description = "姓氏", example = "Doe")
    @Size(max = 50, message = "姓氏长度不能超过50个字符")
    private String lastName;

    /** 个人简介 */
    @Schema(description = "个人简介", example = "A passionate developer")
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    private String bio;

    /** 用户头像URL */
    @Schema(description = "用户头像URL", example = "https://example.com/avatar.jpg")
    @Size(max = 500, message = "头像URL长度不能超过500个字符")
    private String avatarUrl;

    /** 用户封面图URL */
    @Schema(description = "用户封面图URL", example = "https://example.com/cover.jpg")
    @Size(max = 500, message = "封面图URL长度不能超过500个字符")
    private String coverImageUrl;

    /** 个人网站URL */
    @Schema(description = "个人网站URL", example = "https://johndoe.com")
    @Size(max = 500, message = "网站URL长度不能超过500个字符")
    private String websiteUrl;

    /** 所在地 */
    @Schema(description = "所在地", example = "New York, USA")
    @Size(max = 100, message = "所在地长度不能超过100个字符")
    private String location;

    /** 公司/组织 */
    @Schema(description = "公司/组织", example = "Tech Corp")
    @Size(max = 100, message = "公司名称长度不能超过100个字符")
    private String company;

    /** 职位标题 */
    @Schema(description = "职位标题", example = "Software Engineer")
    @Size(max = 100, message = "职位标题长度不能超过100个字符")
    private String title;

    /** 社交媒体链接 (JSON格式) */
    @Schema(description = "社交媒体链接 (JSON格式)", example = "{\"twitter\":\"@johndoe\",\"github\":\"johndoe\"}")
    private JsonNode socialLinks;

    /** 出生日期 */
    @Schema(description = "出生日期", example = "1990-01-01")
    private LocalDate birthDate;

    /** 性别 ('male', 'female', 'other') */
    @Schema(description = "性别", example = "male", allowableValues = { "male", "female", "other" })
    @Pattern(regexp = "^(male|female|other)$", message = "性别必须是male、female或other之一")
    private String gender;

    /** 时区设置 */
    @Schema(description = "时区设置", example = "America/New_York")
    @Size(max = 50, message = "时区长度不能超过50个字符")
    private String timezone;

    /** 首选语言 */
    @Schema(description = "首选语言", example = "en")
    @Size(max = 10, message = "语言代码长度不能超过10个字符")
    private String language;

    /** 主题偏好 ('light', 'dark', 'system') */
    @Schema(description = "主题偏好", example = "dark", allowableValues = { "light", "dark", "system" })
    @Pattern(regexp = "^(light|dark|system)$", message = "主题偏好必须是light、dark或system之一")
    private String themePreference;

    /** 隐私级别 ('public', 'friends', 'private') */
    @Schema(description = "隐私级别", example = "public", allowableValues = { "public", "friends", "private" })
    @Pattern(regexp = "^(public|friends|private)$", message = "隐私级别必须是public、friends或private之一")
    private String privacyLevel;
}