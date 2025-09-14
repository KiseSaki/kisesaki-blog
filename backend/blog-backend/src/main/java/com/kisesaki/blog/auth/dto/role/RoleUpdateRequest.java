package com.kisesaki.blog.auth.dto.role;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "更新角色请求数据")
public class RoleUpdateRequest {

    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    @Schema(description = "角色名称", example = "editor")
    private String name;

    @Size(max = 200, message = "角色描述长度不能超过200个字符")
    @Schema(description = "角色描述", example = "编辑者角色，可以创建和编辑文章")
    private String description;

    @Schema(description = "权限ID列表", example = "[1, 2, 3]")
    private List<Long> permissionIds;
}