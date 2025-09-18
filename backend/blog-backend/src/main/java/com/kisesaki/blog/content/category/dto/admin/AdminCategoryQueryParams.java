package com.kisesaki.blog.content.category.dto.admin;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员分类查询参数")
public class AdminCategoryQueryParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "搜索关键词", example = "技术")
    private String keyword;

    @Schema(description = "父分类ID", example = "1")
    private Long parentId;

    @Schema(description = "是否只查询顶级分类", example = "false", defaultValue = "false")
    private Boolean onlyRoot = false;

    @Schema(description = "可见性过滤", example = "all", defaultValue = "all", allowableValues = { "all", "visible",
            "hidden" })
    private String visibility = "all";
}