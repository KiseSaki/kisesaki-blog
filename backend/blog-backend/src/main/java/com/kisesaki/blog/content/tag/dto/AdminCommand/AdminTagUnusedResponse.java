package com.kisesaki.blog.content.tag.dto.AdminCommand;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "未使用标签响应数据")
public class AdminTagUnusedResponse {
    @Schema(description = "标签ID", example = "1")
    private Long id;

    @Schema(description = "标签名称", example = "Java")
    private String name;

    @Schema(description = "标签别名（URL 友好）", example = "java")
    private String slug;

    @Schema(description = "标签描述", example = "关于 Java 编程语言的文章")
    private String description;

    @Schema(description = "颜色（HEX）", example = "#f1e05a")
    private String color;

    @Schema(description = "创建时间", example = "2023-10-01T12:34:56Z")
    private OffsetDateTime createdAt;

    @Schema(description = "创建者ID", example = "1")
    private Long createdBy;

    @Schema(description = "创建者用户名", example = "user1")
    private String createdByUsername;

    @Schema(description = "未使用天数", example = "30")
    private Integer unusedDays;

    @Schema(description = "是否已审核通过", example = "true")
    private Boolean isApproved;
}