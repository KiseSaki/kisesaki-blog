package com.kisesaki.blog.content.category.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员更新分类排序请求")
public class AdminCategorySortRequest {
    @NotNull(message = "排序顺序不能为空")
    @Min(value = 0, message = "排序顺序不能为负数")
    @Schema(description = "排序顺序", example = "10", required = true)
    private Integer sortOrder;
}