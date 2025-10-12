package com.kisesaki.blog.content.interaction.dto.analytics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 仪表盘统计概览响应
 *
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仪表盘统计概览响应")
public class DashboardStatsResponse {

    @Schema(description = "文章统计")
    private PostStats postStats;

    @Schema(description = "用户统计")
    private UserStats userStats;

    @Schema(description = "评论统计")
    private CommentStats commentStats;

    @Schema(description = "浏览统计")
    private ViewStats viewStats;

    /**
     * 文章统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "文章统计")
    public static class PostStats {
        @Schema(description = "文章总数")
        private Long totalPosts;

        @Schema(description = "已发布文章数")
        private Long publishedPosts;

        @Schema(description = "草稿文章数")
        private Long draftPosts;

        @Schema(description = "已归档文章数")
        private Long archivedPosts;
    }

    /**
     * 用户统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "用户统计")
    public static class UserStats {
        @Schema(description = "用户总数")
        private Long totalUsers;

        @Schema(description = "活跃用户数")
        private Long activeUsers;

        @Schema(description = "今日新增用户数")
        private Long newUsersToday;
    }

    /**
     * 评论统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "评论统计")
    public static class CommentStats {
        @Schema(description = "评论总数")
        private Long totalComments;

        @Schema(description = "待审核评论数")
        private Long pendingComments;

        @Schema(description = "今日评论数")
        private Long todayComments;
    }

    /**
     * 浏览统计
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "浏览统计")
    public static class ViewStats {
        @Schema(description = "总浏览量")
        private Long totalViews;

        @Schema(description = "今日浏览量")
        private Long todayViews;

        @Schema(description = "本周浏览量")
        private Long weekViews;

        @Schema(description = "本月浏览量")
        private Long monthViews;
    }
}
