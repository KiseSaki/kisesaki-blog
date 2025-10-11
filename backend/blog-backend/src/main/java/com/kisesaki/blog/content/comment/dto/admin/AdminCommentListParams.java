package com.kisesaki.blog.content.comment.dto.admin;

import com.kisesaki.blog.common.dto.PageableParams;
import com.kisesaki.blog.content.comment.entity.Comments.CommentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员评论列表请求参数")
public class AdminCommentListParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "评论状态（PENDING/APPROVED/REJECTED/SPAM）")
    private CommentStatus status;

    @Schema(description = "文章ID", example = "1")
    private Long postId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "是否只显示被举报的评论", example = "false")
    private Boolean reported;

    @Schema(description = "内容关键字（用于模糊搜索评论内容）", example = "测试")
    private String keyword;
}