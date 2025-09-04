package com.kisesaki.blog.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户简要信息 DTO
 * 用于在文章、评论等场景中显示用户基本信息
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户简要信息")
public class UserProfileDto {

    /** 用户ID */
    @Schema(description = "用户ID", example = "1234567890")
    private Long id;

    /** 用户名 */
    @Schema(description = "用户名", example = "kisesaki")
    private String username;

    /** 显示名称 */
    @Schema(description = "显示名称", example = "KiseSaki")
    private String displayName;

    /** 头像URL */
    @Schema(description = "头像URL", example = "https://avatar.example.com/kisesaki.jpg")
    private String avatarUrl;

    /** 个人简介 */
    @Schema(description = "个人简介", example = "热爱编程的全栈开发者")
    private String bio;

    /** 个人网站URL */
    @Schema(description = "个人网站URL", example = "https://kisesaki.com")
    private String websiteUrl;

    /** 所在地 */
    @Schema(description = "所在地", example = "北京, 中国")
    private String location;

    /** 公司/组织 */
    @Schema(description = "公司/组织", example = "KiseSaki Tech")
    private String company;

    /** 职位标题 */
    @Schema(description = "职位标题", example = "高级全栈工程师")
    private String title;

    /** 用户状态 */
    @Schema(description = "用户状态", example = "active")
    private String status;

    /** 是否已验证邮箱 */
    @Schema(description = "是否已验证邮箱", example = "true")
    private Boolean emailVerified;
}
