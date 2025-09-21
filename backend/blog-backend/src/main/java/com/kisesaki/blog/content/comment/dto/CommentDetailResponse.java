package com.kisesaki.blog.content.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论详情响应数据
 *
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "评论详情响应数据")
public class CommentDetailResponse {

    @Schema(description = "评论信息")
    private CommentListResponse comment;

    @Schema(description = "上下文信息")
    private CommentContextDto context;

    /**
     * 评论上下文信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "评论上下文信息")
    public static class CommentContextDto {

        @Schema(description = "所属文章ID", example = "123")
        private Long postId;

        @Schema(description = "文章标题", example = "Spring Boot 最佳实践")
        private String postTitle;

        @Schema(description = "文章slug", example = "spring-boot-best-practices")
        private String postSlug;

        @Schema(description = "父评论信息（如果是回复）")
        private CommentListResponse parentComment;

        @Schema(description = "被回复的评论信息（如果是@回复）")
        private CommentListResponse replyToComment;
    }
}