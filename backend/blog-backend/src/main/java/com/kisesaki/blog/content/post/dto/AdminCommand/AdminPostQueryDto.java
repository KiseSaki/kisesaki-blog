package com.kisesaki.blog.content.post.dto.AdminCommand;

import java.time.OffsetDateTime;
import java.util.List;

import com.kisesaki.blog.content.post.dto.AuthorInfo;
import com.kisesaki.blog.content.post.dto.CategoryInfo;
import com.kisesaki.blog.content.post.dto.TagInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * 管理员文章查询相关 DTO
 *
 * @author KiseSaki
 */
public class AdminPostQueryDto {

    /**
     * 管理员获取文章列表请求参数
     */
    @Data
    @Schema(description = "管理员获取文章列表参数")
    public static class AdminPostListParams {

        @Valid
        @Schema(description = "分页参数")
        private com.kisesaki.blog.common.dto.PageableParams pageable = new com.kisesaki.blog.common.dto.PageableParams();

        @Schema(description = "关键字搜索（标题/摘要/内容片段）", example = "Spring Boot")
        private String q;

        @Schema(description = "文章状态（draft, published, archived, deleted）", example = "draft")
        private String status;

        @Schema(description = "作者ID", example = "1")
        private Long authorId;

        @Schema(description = "分类ID", example = "1")
        private Long categoryId;

        @Schema(description = "分类名称", example = "技术分享")
        private String categoryName;

        @Schema(description = "标签ID", example = "2")
        private Long tagId;

        @Schema(description = "标签名称", example = "Java")
        private String tagName;

        @Schema(description = "是否置顶", example = "false")
        private Boolean isTop;

        @Schema(description = "是否为精选文章", example = "false")
        private Boolean isFeatured;

        @Schema(description = "可见性（public, private, password_protected）", example = "public")
        private String visibility;

        @Schema(description = "开始时间")
        private OffsetDateTime startTime;

        @Schema(description = "结束时间")
        private OffsetDateTime endTime;
    }

    /**
     * 管理员文章列表响应
     */
    @Data
    @Schema(description = "管理员文章列表响应")
    public static class AdminPostListResponse {

        @Schema(description = "文章ID", example = "1")
        private Long id;

        @Schema(description = "标题", example = "Spring Boot 入门指南")
        private String title;

        @Schema(description = "URL友好别名", example = "spring-boot-guide")
        private String slug;

        @Schema(description = "摘要", example = "本文介绍了Spring Boot的基础知识...")
        private String excerpt;

        @Schema(description = "封面图片URL")
        private String coverImageUrl;

        @Schema(description = "文章状态", example = "draft")
        private String status;

        @Schema(description = "可见性", example = "public")
        private String visibility;

        @Schema(description = "浏览次数", example = "100")
        private Integer viewCount;

        @Schema(description = "点赞数量", example = "5")
        private Integer likeCount;

        @Schema(description = "评论数量", example = "3")
        private Integer commentCount;

        @Schema(description = "分享数量", example = "2")
        private Integer shareCount;

        @Schema(description = "阅读时长（分钟）", example = "5")
        private Integer readingTime;

        @Schema(description = "字数统计", example = "1200")
        private Integer wordCount;

        @Schema(description = "是否为精选文章", example = "false")
        private Boolean isFeatured;

        @Schema(description = "是否置顶", example = "false")
        private Boolean isTop;

        @Schema(description = "是否允许评论", example = "true")
        private Boolean allowComments;

        @Schema(description = "作者信息")
        private AuthorInfo author;

        @Schema(description = "分类信息")
        private CategoryInfo category;

        @Schema(description = "标签列表")
        private List<TagInfo> tags;

        @Schema(description = "发布时间")
        private OffsetDateTime publishedAt;

        @Schema(description = "定时发布时间")
        private OffsetDateTime scheduledAt;

        @Schema(description = "创建时间")
        private OffsetDateTime createdAt;

        @Schema(description = "更新时间")
        private OffsetDateTime updatedAt;

        @Schema(description = "最后修改时间")
        private OffsetDateTime lastModifiedAt;
    }
}
