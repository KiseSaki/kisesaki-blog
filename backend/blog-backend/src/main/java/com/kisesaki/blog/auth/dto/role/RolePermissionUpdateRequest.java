package com.kisesaki.blog.auth.dto.role;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "更新角色权限请求数据")
public class RolePermissionUpdateRequest {

    @NotNull(message = "权限ID列表不能为null")
    @Schema(description = "权限ID列表", example = "[1, 2, 3]", required = true)
    private List<Long> permissionIds;
}