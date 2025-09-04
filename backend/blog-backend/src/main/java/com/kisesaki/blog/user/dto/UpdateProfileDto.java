package com.kisesaki.blog.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新用户资料 DTO
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "更新用户资料请求")
public class UpdateProfileDto {

    /** 显示名称 */
    @Size(max = 50, message = "显示名称长度不能超过50个字符")
    @Schema(description = "显示名称", example = "KiseSaki")
    private String displayName;

    /** 名字 */
    @Size(max = 30, message = "名字长度不能超过30个字符")
    @Schema(description = "名字", example = "张")
    private String firstName;

    /** 姓氏 */
    @Size(max = 30, message = "姓氏长度不能超过30个字符")
    @Schema(description = "姓氏", example = "三")
    private String lastName;

    /** 个人简介 */
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    @Schema(description = "个人简介", example = "热爱编程的全栈开发者")
    private String bio;

    /** 个人网站URL */
    @Size(max = 200, message = "个人网站URL长度不能超过200个字符")
    @Schema(description = "个人网站URL", example = "https://kisesaki.com")
    private String websiteUrl;

    /** 所在地 */
    @Size(max = 100, message = "所在地长度不能超过100个字符")
    @Schema(description = "所在地", example = "北京, 中国")
    private String location;

    /** 公司/组织 */
    @Size(max = 100, message = "公司/组织长度不能超过100个字符")
    @Schema(description = "公司/组织", example = "KiseSaki Tech")
    private String company;

    /** 职位标题 */
    @Size(max = 100, message = "职位标题长度不能超过100个字符")
    @Schema(description = "职位标题", example = "高级全栈工程师")
    private String title;

    /** 社交媒体链接 (JSON格式) */
    @Schema(description = "社交媒体链接", example = "{\"github\": \"https://github.com/kisesaki\", \"twitter\": \"https://twitter.com/kisesaki\"}")
    private JsonNode socialLinks;

    /** 出生日期 */
    @Schema(description = "出生日期", example = "1990-01-01")
    private LocalDate birthDate;

    /** 性别 */
    @Schema(description = "性别", example = "male", allowableValues = { "male", "female", "other" })
    private String gender;

    /** 时区设置 */
    @Size(max = 50, message = "时区设置长度不能超过50个字符")
    @Schema(description = "时区设置", example = "Asia/Shanghai")
    private String timezone;

    /** 首选语言 */
    @Size(max = 10, message = "首选语言长度不能超过10个字符")
    @Schema(description = "首选语言", example = "zh-CN")
    private String language;

    /** 主题偏好 */
    @Schema(description = "主题偏好", example = "dark", allowableValues = { "light", "dark", "system" })
    private String themePreference;

    /** 隐私级别 */
    @Schema(description = "隐私级别", example = "public", allowableValues = { "public", "friends", "private" })
    private String privacyLevel;
}
