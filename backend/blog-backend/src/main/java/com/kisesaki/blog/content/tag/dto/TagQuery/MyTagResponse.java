package com.kisesaki.blog.content.tag.dto.TagQuery;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "我创建的标签响应数据")
public class MyTagResponse {
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

    @Schema(description = "是否已审核通过", example = "true")
    private Boolean isApproved;

    @Schema(description = "审核状态", example = "approved", allowableValues = { "pending", "approved", "rejected" })
    private String approvalStatus;

    @Schema(description = "审核备注", example = "标签名称重复，建议使用其他名称")
    private String approvalNote;
}