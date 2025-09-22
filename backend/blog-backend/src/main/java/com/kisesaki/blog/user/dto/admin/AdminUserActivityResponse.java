package com.kisesaki.blog.user.dto.admin;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员获取用户活动日志响应")
public class AdminUserActivityResponse {

    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "操作类型")
    private String action;

    @Schema(description = "操作描述")
    private String description;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "用户代理")
    private String userAgent;

    @Schema(description = "操作详情（JSON格式）")
    private JsonNode details;

    @Schema(description = "操作结果")
    private String result;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

}