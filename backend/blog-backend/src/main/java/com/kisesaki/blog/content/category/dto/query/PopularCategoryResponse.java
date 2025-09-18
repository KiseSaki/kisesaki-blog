package com.kisesaki.blog.content.category.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门分类响应数据")
public class PopularCategoryResponse {
    @Schema(description = "分类ID", example = "1")
    private Long id;

    @Schema(description = "分类名称", example = "技术分享")
    private String name;

    @Schema(description = "分类别名（URL 友好）", example = "tech-share")
    private String slug;

    @Schema(description = "分类描述", example = "技术相关的文章分类")
    private String description;

    @Schema(description = "该分类下的文章数量", example = "25")
    private Integer postCount;

    @Schema(description = "父分类ID", example = "null")
    private Long parentId;

    @Schema(description = "父分类名称", example = "null")
    private String parentName;

    @Schema(description = "排序顺序", example = "10")
    private Integer sortOrder;
}