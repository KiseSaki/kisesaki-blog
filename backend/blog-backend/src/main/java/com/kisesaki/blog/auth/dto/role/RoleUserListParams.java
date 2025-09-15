package com.kisesaki.blog.auth.dto.role;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色用户列表查询参数")
public class RoleUserListParams extends PageableParams {

    @Schema(description = "用户名模糊搜索", example = "admin")
    private String username;

    @Schema(description = "邮箱模糊搜索", example = "admin@example.com")
    private String email;
}