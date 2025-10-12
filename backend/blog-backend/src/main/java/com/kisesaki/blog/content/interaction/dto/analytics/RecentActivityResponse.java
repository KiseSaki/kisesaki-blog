package com.kisesaki.blog.content.interaction.dto.analytics;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 最近活动响应
 *
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "最近活动响应")
public class RecentActivityResponse {

    @Schema(description = "活动ID")
    private Long id;

    @Schema(description = "活动类型", example = "post")
    private String type;

    @Schema(description = "活动动作", example = "created")
    private String action;

    @Schema(description = "活动内容")
    private String content;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}
