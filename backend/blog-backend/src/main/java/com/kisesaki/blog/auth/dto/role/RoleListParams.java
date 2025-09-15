package com.kisesaki.blog.auth.dto.role;

import com.kisesaki.blog.common.dto.PageableParams;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "获取角色列表参数")
public class RoleListParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "角色名称，支持模糊搜索", example = "admin")
    private String name;
}
