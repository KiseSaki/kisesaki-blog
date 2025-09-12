package com.kisesaki.blog.content.post.dto;

import java.time.OffsetDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 增改用文章基础DTO
 *
 * @author KiseSaki
 */
@Data
@Schema(description = "文章基础DTO")
public class BasePostDto {

    @NotNull(groups = PostValidationGroups.Create.class, message = "分类ID不能为空")
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @NotBlank(groups = PostValidationGroups.Create.class, message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    @Schema(description = "文章标题", example = "Spring Boot 入门指南")
    private String title;

    @Size(max = 200, message = "URL别名长度不能超过200个字符")
    @Pattern(regexp = "^[a-z0-9\\-]*$", message = "URL别名只能包含小写字母、数字和短横线")
    @Schema(description = "URL友好别名，不传递就后端生成", example = "spring-boot-guide")
    private String slug;

    @Size(max = 500, message = "摘要长度不能超过500个字符")
    @Schema(description = "文章摘要", example = "本文介绍了Spring Boot的基础知识...")
    private String excerpt;

    @NotBlank(groups = PostValidationGroups.Create.class, message = "内容不能为空")
    @Schema(description = "Markdown原始内容")
    private String content;

    @Schema(description = "封面图片URL", example = "https://example.com/images/cover.jpg")
    private String coverImageUrl;

    @Schema(description = "特色图片URL", example = "https://example.com/images/featured.jpg")
    private String featuredImageUrl;

    @Pattern(regexp = "^(draft|published|archived)$", message = "状态只能是 draft、published 或 archived")
    @Schema(description = "文章状态", example = "draft", allowableValues = { "draft", "published", "archived" })
    private String status = "draft";

    @Pattern(regexp = "^(public|private|password_protected)$", message = "可见性只能是 public、private 或 password_protected")
    @Schema(description = "可见性", example = "public", allowableValues = { "public", "private", "password_protected" })
    private String visibility = "public";

    @Size(max = 255, message = "访问密码长度不能超过255个字符")
    @Schema(description = "访问密码（仅当visibility为password_protected时有效）")
    private String password;

    @Schema(description = "是否为精选文章", example = "false")
    private Boolean isFeatured = false;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isTop = false;

    @Schema(description = "是否允许评论", example = "true")
    private Boolean allowComments = true;

    @Size(max = 200, message = "SEO标题长度不能超过200个字符")
    @Schema(description = "SEO标题，不传递就后端生成")
    private String seoTitle;

    @Size(max = 500, message = "SEO描述长度不能超过500个字符")
    @Schema(description = "SEO描述，不传递就后端生成")
    private String seoDescription;

    @Size(max = 1000, message = "SEO关键词长度不能超过1000个字符")
    @Schema(description = "SEO关键词（逗号分隔），不传递就后端生成")
    private String seoKeywords;

    @Schema(description = "计划发布时间（用于定时发布）")
    private OffsetDateTime scheduledAt;

    @Schema(description = "标签ID列表")
    private List<Long> tagIds;
}
