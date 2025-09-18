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
@Schema(description = "分类查询参数")
public class CategoryQueryParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "搜索关键词", example = "技术")
    private String keyword;

    @Schema(description = "父分类ID", example = "1")
    private Long parentId;

    @Schema(description = "是否只查询顶级分类", example = "false", defaultValue = "false")
    private Boolean onlyRoot = false;
}