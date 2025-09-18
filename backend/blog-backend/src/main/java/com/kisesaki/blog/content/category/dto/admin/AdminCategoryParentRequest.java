package com.kisesaki.blog.content.category.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员修改分类父级关系请求")
public class AdminCategoryParentRequest {
    @Schema(description = "父分类ID，null表示设为顶级分类", example = "2")
    private Long parentId;
}