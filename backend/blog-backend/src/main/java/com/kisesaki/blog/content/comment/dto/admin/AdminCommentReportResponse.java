package com.kisesaki.blog.content.comment.dto.admin;

import java.time.OffsetDateTime;

import com.kisesaki.blog.content.comment.dto.CommentUserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "被举报的评论信息")
public class AdminCommentReportResponse {
    @Schema(description = "举报ID", example = "1")
    private Long reportId;

    @Schema(description = "评论ID", example = "1")
    private Long commentId;

    @Schema(description = "评论内容")
    private String commentContent;

    @Schema(description = "评论作者信息")
    private CommentUserDto commentAuthor;

    @Schema(description = "举报人信息")
    private CommentUserDto reporter;

    @Schema(description = "举报原因", example = "垃圾信息")
    private String reason;

    @Schema(description = "举报详细描述", example = "该评论包含恶意链接")
    private String description;

    @Schema(description = "举报状态", example = "PENDING")
    private String status;

    @Schema(description = "处理人ID（管理员）", example = "1")
    private Long handledBy;

    @Schema(description = "处理时间")
    private OffsetDateTime handledAt;

    @Schema(description = "处理备注")
    private String handleNote;

    @Schema(description = "举报时间")
    private OffsetDateTime createdAt;
}