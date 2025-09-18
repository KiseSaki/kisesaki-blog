package com.kisesaki.blog.content.category.dto.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门分类查询参数")
public class PopularCategoryParams {
    @Min(value = 1, message = "限制数量必须大于0")
    @Max(value = 50, message = "限制数量不能超过50")
    @Schema(description = "返回数量限制", example = "10", defaultValue = "10")
    private Integer limit = 10;

    @Schema(description = "父分类ID，用于获取指定分类下的热门子分类", example = "1")
    private Long parentId;

    @Schema(description = "最小文章数量阈值", example = "1", defaultValue = "1")
    @Min(value = 0, message = "最小文章数量不能为负数")
    private Integer minPostCount = 1;
}