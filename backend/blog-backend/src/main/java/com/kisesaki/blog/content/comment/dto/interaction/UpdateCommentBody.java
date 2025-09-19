package com.kisesaki.blog.content.comment.dto.interaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新评论请求")
public class UpdateCommentBody {
    @Schema(description = "评论内容", example = "这是一条更新后的评论")
    private String content;
}
