package com.kisesaki.blog.content.comment.dto.interaction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建评论请求")
public class CreateCommentBody {
    @Schema(description = "父评论ID")
    private Long parentId;

    @Schema(description = "回复目标评论ID")
    private Long replyToId;

    @Schema(description = "评论内容", example = "这是一条评论")
    private String content;
}
