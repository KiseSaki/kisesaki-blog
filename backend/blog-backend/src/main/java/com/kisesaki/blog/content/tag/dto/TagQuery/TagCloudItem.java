package com.kisesaki.blog.content.tag.dto.TagQuery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签云项数据")
public class TagCloudItem {
    @Schema(description = "标签ID", example = "1")
    private Long id;

    @Schema(description = "标签名称", example = "Java")
    private String name;

    @Schema(description = "标签别名（URL 友好）", example = "java")
    private String slug;

    @Schema(description = "颜色（HEX）", example = "#f1e05a")
    private String color;

    @Schema(description = "使用该标签的文章数量", example = "42")
    private Integer postCount;

    @Schema(description = "字体大小权重（1-10）", example = "8")
    private Integer fontWeight;

    @Schema(description = "热度权重（基于文章数量、阅读量等计算）", example = "85.5")
    private Double popularityScore;
}