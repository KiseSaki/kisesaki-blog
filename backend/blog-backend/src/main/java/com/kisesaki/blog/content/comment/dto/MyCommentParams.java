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
@Schema(description = "我的评论列表请求参数")
public class MyCommentParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "文章ID（可选，筛选特定文章下的评论）", example = "1")
    private Long postId;

    @Schema(description = "评论状态（可选，PENDING/APPROVED/REJECTED/SPAM）")
    private String status;
}