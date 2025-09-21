package com.kisesaki.blog.content.interaction.dto.analytics;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章浏览统计响应DTO
 */
@Data
@NoArgsConstructor
public class PostViewStatsResponse {

    /**
     * 文章ID
     */
    private Long postId;

    /**
     * 总浏览次数
     */
    private Long totalViews;

    /**
     * 独立访客数
     */
    private Long uniqueViews;

    /**
     * 今日浏览次数
     */
    private Long todayViews;

    public PostViewStatsResponse(Long postId, Long totalViews, Long uniqueViews, Long todayViews) {
        this.postId = postId;
        this.totalViews = totalViews;
        this.uniqueViews = uniqueViews;
        this.todayViews = todayViews;
    }
}