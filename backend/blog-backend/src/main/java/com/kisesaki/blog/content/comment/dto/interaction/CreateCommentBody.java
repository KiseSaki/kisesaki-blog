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
    @Schema(description = "回复目标评论ID。" +
            "为空时创建顶级评论；" +
            "指向顶级评论时创建该评论的回复；" +
            "指向二级回复时创建同一父评论下的回复（@功能）",
            example = "1")
    private Long replyToId;

    @Schema(description = "评论内容（不能为空，最多500字符）", 
            example = "这是一条评论", 
            required = true)
    private String content;
}
