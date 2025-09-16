package com.kisesaki.blog.content.tag.dto.AdminCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签清理结果响应")
public class AdminTagCleanupResponse {
    @Schema(description = "清理的标签数量", example = "5")
    private Integer cleanedCount;

    @Schema(description = "清理前未使用标签总数", example = "10")
    private Integer totalUnusedCount;

    @Schema(description = "清理操作描述", example = "已清理5个超过30天未使用的标签")
    private String message;
}