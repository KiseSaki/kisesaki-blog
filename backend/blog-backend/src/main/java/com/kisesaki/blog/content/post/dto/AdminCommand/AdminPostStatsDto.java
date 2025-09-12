package com.kisesaki.blog.content.post.dto.AdminCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理员文章统计相关 DTO
 *
 * @author KiseSaki
 */
public class AdminPostStatsDto {

    /**
     * 文章统计数据响应
     */
    @Data
    @Schema(description = "文章统计数据响应")
    public static class PostStatsResponse {

        @Schema(description = "总文章数", example = "100")
        private Long totalPosts;

        @Schema(description = "已发布文章数", example = "80")
        private Long publishedPosts;

        @Schema(description = "草稿文章数", example = "15")
        private Long draftPosts;

        @Schema(description = "归档文章数", example = "5")
        private Long archivedPosts;

        @Schema(description = "精选文章数", example = "10")
        private Long featuredPosts;

        @Schema(description = "置顶文章数", example = "3")
        private Long topPosts;

        @Schema(description = "总浏览量", example = "50000")
        private Long totalViews;

        @Schema(description = "总点赞数", example = "1500")
        private Long totalLikes;

        @Schema(description = "总评论数", example = "800")
        private Long totalComments;

        @Schema(description = "本月新增文章数", example = "8")
        private Long monthlyNewPosts;

        @Schema(description = "本周新增文章数", example = "2")
        private Long weeklyNewPosts;

        @Schema(description = "今日新增文章数", example = "0")
        private Long dailyNewPosts;
    }
}
