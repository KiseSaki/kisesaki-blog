// ...existing code...
package com.kisesaki.blog.content.post.dto.PostQuery;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import com.kisesaki.blog.content.post.dto.AuthorInfo;
import com.kisesaki.blog.content.post.dto.CategoryInfo;
import com.kisesaki.blog.content.post.dto.Permissions;
import com.kisesaki.blog.content.post.dto.RevisionInfo;
import com.kisesaki.blog.content.post.dto.TagInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "文章详情")
public class PublishedPostDetailResponse {
    /*
     * ----------------------------- 可以单表查询的 -----------------------------
     */
    // 基本标识信息：ID、标题与别名
    @Schema(description = "文章ID", example = "1")
    private Long id;

    @Schema(description = "文章标题", example = "Spring Boot 入门指南")
    private String title;

    @Schema(description = "URL友好别名", example = "spring-boot-guide")
    private String slug;

    // 内容摘要与正文：摘要、渲染后的 HTML 内容、阅读时间
    @Schema(description = "文章摘要", example = "本文介绍了Spring Boot的基础知识...")
    private String excerpt;

    @Schema(description = "渲染后的 HTML 内容")
    private String htmlContent;

    @Schema(description = "预估阅读时间（分钟）", example = "5")
    private Integer readingTime;

    // 媒体与展示：封面、特色图片、是否置顶、是否精选
    @Schema(description = "封面图片URL", example = "https://example.com/images/cover.jpg")
    private String coverImageUrl;

    @Schema(description = "特色图片URL", example = "https://example.com/images/featured.jpg")
    private String featuredImageUrl;

    @Schema(description = "是否为精选文章", example = "false")
    private Boolean isFeatured;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isTop;

    // 统计指标：浏览、点赞、评论、分享
    @Schema(description = "浏览次数", example = "156")
    private Integer viewCount;

    @Schema(description = "点赞数量", example = "24")
    private Integer likeCount;

    @Schema(description = "评论数量", example = "8")
    private Integer commentCount;

    @Schema(description = "分享数量", example = "3")
    private Integer shareCount;

    // SEO 与元数据
    @Schema(description = "SEO 标题")
    private String seoTitle;

    @Schema(description = "SEO 描述")
    private String seoDescription;

    @Schema(description = "SEO 关键词（逗号分隔）")
    private String seoKeywords;

    // 时间信息
    @Schema(description = "发布时间")
    private OffsetDateTime publishedAt;

    @Schema(description = "最后修改时间")
    private OffsetDateTime lastModifiedAt;

    /*
     * ----------------------------- 需要多表联查的 -----------------------------
     */
    // 版本与修订信息
    @Schema(description = "版本历史（仅元信息）")
    private List<RevisionInfo> revisions;

    /* 关联数据：作者、分类、标签 */
    @Schema(description = "作者信息")
    private AuthorInfo author;

    @Schema(description = "分类信息")
    private CategoryInfo category;

    @Schema(description = "标签列表")
    private List<TagInfo> tags;

    /*
     * ----------------------------- 需要逻辑处理的 -----------------------------
     */
    // 权限与元数据
    @Schema(description = "自定义元数据 (metaKey -> metaValue)")
    private Map<String, String> meta;

    // 文章导航：上一篇、下一篇与相关推荐
    @Schema(description = "上一篇文章（简要）")
    private AdjacentPost prevPost;

    @Schema(description = "下一篇文章（简要）")
    private AdjacentPost nextPost;

    @Schema(description = "相关推荐列表（简要）")
    private List<RelatedPost> relatedPosts;

    @Schema(description = "前端权限信息")
    private Permissions permissions = new Permissions();

    /*
     * -----------------------------
     * 
     * -----------------------------
     */
    @Data
    @NoArgsConstructor
    @Schema(description = "相邻文章简要")
    public static class AdjacentPost {
        @Schema(description = "文章ID")
        private Long id;

        @Schema(description = "文章标题")
        private String title;

        @Schema(description = "文章别名（slug）")
        private String slug;
    }

    @Data
    @NoArgsConstructor
    @Schema(description = "相关推荐条目")
    public static class RelatedPost {
        @Schema(description = "文章ID")
        private Long id;

        @Schema(description = "标题")
        private String title;

        @Schema(description = "别名")
        private String slug;

        @Schema(description = "封面图")
        private String coverImageUrl;
    }
}