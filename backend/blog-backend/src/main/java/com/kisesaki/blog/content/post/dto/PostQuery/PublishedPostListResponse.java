package com.kisesaki.blog.content.post.dto.PostQuery;

import java.time.OffsetDateTime;
import java.util.List;

import com.kisesaki.blog.content.post.dto.AuthorInfo;
import com.kisesaki.blog.content.post.dto.CategoryInfo;
import com.kisesaki.blog.content.post.dto.TagInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "文章列表响应数据")
public class PublishedPostListResponse {

    @Schema(description = "文章ID", example = "1")
    private Long id;

    @Schema(description = "文章标题", example = "Spring Boot 入门指南")
    private String title;

    @Schema(description = "URL友好别名", example = "spring-boot-guide")
    private String slug;

    @Schema(description = "文章摘要", example = "本文介绍了Spring Boot的基础知识...")
    private String excerpt;

    @Schema(description = "封面图片URL", example = "https://example.com/images/cover.jpg")
    private String coverImageUrl;

    @Schema(description = "浏览次数", example = "156")
    private Integer viewCount;

    @Schema(description = "点赞数量", example = "24")
    private Integer likeCount;

    @Schema(description = "评论数量", example = "8")
    private Integer commentCount;

    @Schema(description = "分享数量", example = "3")
    private Integer shareCount;

    @Schema(description = "预估阅读时间（分钟）", example = "5")
    private Integer readingTime;

    @Schema(description = "是否为精选文章", example = "false")
    private Boolean isFeatured;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isTop;

    @Schema(description = "发布时间")
    private OffsetDateTime publishedAt;

    // 关联数据
    @Schema(description = "作者信息")
    private AuthorInfo author;

    @Schema(description = "分类信息")
    private CategoryInfo category;

    @Schema(description = "标签列表")
    private List<TagInfo> tags;
}
