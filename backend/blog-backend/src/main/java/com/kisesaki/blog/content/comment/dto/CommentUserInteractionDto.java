package com.kisesaki.blog.content.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前用户对评论的交互状态
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "当前用户对评论的交互状态")
public class CommentUserInteractionDto {

    @Schema(description = "是否已点赞", example = "true")
    private Boolean isLiked;

    @Schema(description = "是否已踩", example = "false")
    private Boolean isDisliked;

    @Schema(description = "是否可以编辑", example = "true")
    private Boolean canEdit;

    @Schema(description = "是否可以删除", example = "true")
    private Boolean canDelete;

    @Schema(description = "是否可以回复", example = "true")
    private Boolean canReply;

    @Schema(description = "是否可以置顶", example = "false")
    private Boolean canPin;
}