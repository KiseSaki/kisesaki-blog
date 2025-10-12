package com.kisesaki.blog.content.post.dto.PostCommand;

import java.time.OffsetDateTime;
import java.util.List;

import com.kisesaki.blog.content.post.dto.TagInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章编辑详情响应DTO
 * 用于后台编辑表单回显，包含所有可编辑字段
 *
 * @author KiseSaki
 */
@Data
@Schema(description = "文章编辑详情响应")
public class PostEditDetailResponse {
    @Data
    static
    class Tags {
        private Long id;
        private String name;
    }

    @Schema(description = "文章ID", example = "1")
    private Long id;

    @Schema(description = "文章标题", example = "Spring Boot 入门指南")
    private String title;

    @Schema(description = "URL友好别名", example = "spring-boot-guide")
    private String slug;

    @Schema(description = "文章摘要", example = "本文介绍了Spring Boot的基础知识...")
    private String excerpt;

    @Schema(description = "Markdown原始内容")
    private String content;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "技术分享")
    private String categoryName;

    @Schema(description = "标签列表")
    private List<Tags> tags;

    @Schema(description = "封面图片URL", example = "https://example.com/images/cover.jpg")
    private String coverImageUrl;

    @Schema(description = "特色图片URL", example = "https://example.com/images/featured.jpg")
    private String featuredImageUrl;

    @Schema(description = "文章状态", example = "draft", allowableValues = { "draft", "published", "archived" })
    private String status;

    @Schema(description = "可见性", example = "public", allowableValues = { "public", "private", "password_protected" })
    private String visibility;

    @Schema(description = "访问密码（仅当visibility为password_protected时有效）")
    private String password;

    @Schema(description = "是否为精选文章", example = "false")
    private Boolean isFeatured;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isTop;

    @Schema(description = "是否允许评论", example = "true")
    private Boolean allowComments;

    @Schema(description = "SEO标题")
    private String seoTitle;

    @Schema(description = "SEO描述")
    private String seoDescription;

    @Schema(description = "SEO关键词（逗号分隔）")
    private String seoKeywords;

    @Schema(description = "计划发布时间（用于定时发布）")
    private OffsetDateTime scheduledAt;

    @Schema(description = "发布时间")
    private OffsetDateTime publishedAt;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    @Schema(description = "更新时间")
    private OffsetDateTime updatedAt;

    @Schema(description = "阅读时间（分钟）", example = "5")
    private Integer readingTime;

    @Schema(description = "作者ID", example = "1")
    private Long authorId;

    @Schema(description = "作者用户名", example = "johndoe")
    private String authorUsername;
}
