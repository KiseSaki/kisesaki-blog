package com.kisesaki.blog.auth.dto.permission;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "权限列表查询参数")
public class PermissionListParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "权限名称，支持模糊搜索", example = "read")
    private String name;

    @Schema(description = "资源类型", example = "article")
    private String resource;

    @Schema(description = "操作类型", example = "view")
    private String action;
}
