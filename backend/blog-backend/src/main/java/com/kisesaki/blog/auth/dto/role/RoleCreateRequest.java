package com.kisesaki.blog.auth.dto.role;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "创建角色请求数据")
public class RoleCreateRequest {

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    @Schema(description = "角色名称", example = "editor", required = true)
    private String name;

    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    @Schema(description = "角色描述", example = "编辑者角色，可以创建和编辑文章")
    private String description;

    @NotNull(message = "权限ID列表不能为null")
    @Schema(description = "权限ID列表", example = "[1, 2, 3]", required = true)
    private List<Long> permissionIds;
}