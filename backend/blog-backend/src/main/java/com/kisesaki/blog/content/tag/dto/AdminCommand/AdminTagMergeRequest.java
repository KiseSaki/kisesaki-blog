package com.kisesaki.blog.content.tag.dto.AdminCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签合并请求")
public class AdminTagMergeRequest {
    @NotNull(message = "源标签ID不能为空")
    @Schema(description = "源标签ID（将被合并的标签）", example = "5", required = true)
    private Long sourceTagId;

    @NotNull(message = "目标标签ID不能为空")
    @Schema(description = "目标标签ID（合并到的标签）", example = "3", required = true)
    private Long targetTagId;
}