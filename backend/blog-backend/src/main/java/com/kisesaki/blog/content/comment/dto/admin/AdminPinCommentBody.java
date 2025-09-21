package com.kisesaki.blog.content.comment.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "置顶评论请求")
public class AdminPinCommentBody {
    @NotNull(message = "置顶状态不能为空")
    @Schema(description = "是否置顶", example = "true", required = true)
    private Boolean isPinned;
}