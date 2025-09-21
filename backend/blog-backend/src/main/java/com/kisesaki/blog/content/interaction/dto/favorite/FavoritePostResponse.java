package com.kisesaki.blog.content.interaction.dto.favorite;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏文章信息响应DTO
 */
@Data
@NoArgsConstructor
public class FavoritePostResponse {

    /**
     * 文章ID
     */
    private Long postId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String excerpt;

    /**
     * 封面图片URL
     */
    private String coverImageUrl;

    /**
     * 作者昵称
     */
    private String authorName;

    /**
     * 发布时间
     */
    private OffsetDateTime publishedAt;

    /**
     * 收藏时间
     */
    private LocalDateTime favoriteTime;

    /**
     * 阅读时间（分钟）
     */
    private Integer readingTime;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    public FavoritePostResponse(Long postId, String title, String excerpt, String coverImageUrl,
            String authorName, OffsetDateTime publishedAt, LocalDateTime favoriteTime,
            Integer readingTime, Integer viewCount, Integer likeCount) {
        this.postId = postId;
        this.title = title;
        this.excerpt = excerpt;
        this.coverImageUrl = coverImageUrl;
        this.authorName = authorName;
        this.publishedAt = publishedAt;
        this.favoriteTime = favoriteTime;
        this.readingTime = readingTime;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }
}