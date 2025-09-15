package com.kisesaki.blog.content.tag.dto.TagQuery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签搜索结果项")
public class TagSearchItem {
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

    @Schema(description = "匹配得分（用于排序）", example = "0.95")
    private Double matchScore;
}