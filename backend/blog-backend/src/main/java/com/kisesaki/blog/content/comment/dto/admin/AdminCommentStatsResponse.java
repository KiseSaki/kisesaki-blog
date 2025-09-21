package com.kisesaki.blog.content.comment.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "评论统计数据")
public class AdminCommentStatsResponse {
    @Schema(description = "总评论数", example = "1000")
    private Long totalComments;

    @Schema(description = "待审核评论数", example = "50")
    private Long pendingComments;

    @Schema(description = "已通过评论数", example = "900")
    private Long approvedComments;

    @Schema(description = "已拒绝评论数", example = "30")
    private Long rejectedComments;

    @Schema(description = "垃圾评论数", example = "20")
    private Long spamComments;

    @Schema(description = "被举报评论数", example = "15")
    private Long reportedComments;

    @Schema(description = "今日新增评论数", example = "25")
    private Long todayComments;

    @Schema(description = "本周新增评论数", example = "150")
    private Long weekComments;

    @Schema(description = "本月新增评论数", example = "600")
    private Long monthComments;
}