package com.kisesaki.blog.content.tag.dto.AdminCommand;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员标签列表响应数据")
public class AdminTagListResponse {
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

    @Schema(description = "使用该标签的文章数量", example = "42")
    private Integer postCount;

    @Schema(description = "创建时间", example = "2023-10-01T12:34:56Z")
    private OffsetDateTime createdAt;

    @Schema(description = "更新时间", example = "2023-10-15T14:30:22Z")
    private OffsetDateTime updatedAt;

    @Schema(description = "创建者ID", example = "1")
    private Long createdBy;

    @Schema(description = "创建者用户名", example = "user1")
    private String createdByUsername;

    @Schema(description = "是否已审核通过", example = "true")
    private Boolean isApproved;

    @Schema(description = "审核状态", example = "approved", allowableValues = { "pending", "approved", "rejected" })
    private String approvalStatus;

    @Schema(description = "审核者ID", example = "2")
    private Long approvedBy;

    @Schema(description = "审核者用户名", example = "admin")
    private String approvedByUsername;

    @Schema(description = "审核时间", example = "2023-10-02T10:15:30Z")
    private OffsetDateTime approvedAt;

    @Schema(description = "审核备注", example = "标签符合规范，审核通过")
    private String approvalNote;

    @Schema(description = "最近使用时间", example = "2023-10-15T18:45:12Z")
    private OffsetDateTime lastUsedAt;

    @Schema(description = "热度权重", example = "85.5")
    private Double popularityScore;
}