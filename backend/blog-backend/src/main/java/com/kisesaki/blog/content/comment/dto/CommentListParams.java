package com.kisesaki.blog.content.comment.dto;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论列表请求参数")
public class CommentListParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "评论用户ID", example = "1")
    private Long userId;

    @Schema(description = "回复目标评论ID（@某条评论）", example = "2")
    private Long replyToId;

    @Schema(description = "评论状态（PENDING/APPROVED/REJECTED/SPAM）")
    private String status;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isPinned;

    @Schema(description = "是否为作者回复", example = "false")
    private Boolean isAuthorReply;

    @Schema(description = "内容关键字（用于模糊搜索评论内容）", example = "测试")
    private String keyword;
}
