package com.kisesaki.blog.content.category.dto.query;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类下文章列表查询参数")
public class CategoryPostsParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "是否包含子分类的文章", example = "true", defaultValue = "true")
    private Boolean includeChildren = true;

    @Schema(description = "文章状态过滤", example = "published", allowableValues = { "all", "published", "draft" })
    private String status = "published";
}