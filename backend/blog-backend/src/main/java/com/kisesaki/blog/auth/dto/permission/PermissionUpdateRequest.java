package com.kisesaki.blog.auth.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "权限更新请求参数")
public class PermissionUpdateRequest {
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    @Schema(description = "权限名称", example = "read_articles", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 200, message = "权限描述长度不能超过200个字符")
    @Schema(description = "权限描述", example = "允许读取文章")
    private String description;

    @NotBlank(message = "资源类型不能为空")
    @Size(max = 50, message = "资源类型长度不能超过50个字符")
    @Schema(description = "资源类型", example = "article", requiredMode = Schema.RequiredMode.REQUIRED)
    private String resource;

    @NotBlank(message = "操作类型不能为空")
    @Size(max = 50, message = "操作类型长度不能超过50个字符")
    @Schema(description = "操作类型", example = "view", requiredMode = Schema.RequiredMode.REQUIRED)
    private String action;
}