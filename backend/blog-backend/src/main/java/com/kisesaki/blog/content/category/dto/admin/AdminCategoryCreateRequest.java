package com.kisesaki.blog.content.category.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员创建分类请求")
public class AdminCategoryCreateRequest {
    @NotBlank(message = "分类名称不能为空")
    @Size(min = 1, max = 50, message = "分类名称长度必须在1-50字符之间")
    @Schema(description = "分类名称", example = "技术分享")
    private String name;

    @Size(max = 50, message = "URL别名长度不能超过50字符")
    @Pattern(regexp = "^[a-z0-9\\-]*$", message = "URL别名只能包含小写字母、数字和短横线")
    @Schema(description = "URL友好别名，不传递则后端自动生成", example = "tech-share")
    private String slug;

    @Size(max = 500, message = "分类描述长度不能超过500字符")
    @Schema(description = "分类描述", example = "技术相关的文章分类")
    private String description;

    @Schema(description = "父分类ID", example = "null")
    private Long parentId;

    @Min(value = 0, message = "排序顺序不能为负数")
    @Schema(description = "排序顺序", example = "10", defaultValue = "0")
    private Integer sortOrder = 0;

    @Schema(description = "是否可见", example = "true", defaultValue = "true")
    private Boolean isVisible = true;
}