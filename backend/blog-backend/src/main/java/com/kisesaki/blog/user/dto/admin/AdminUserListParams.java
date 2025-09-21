package com.kisesaki.blog.user.dto.admin;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员获取用户列表参数")
public class AdminUserListParams {
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "用户名，支持模糊搜索")
    private String username;

    @Schema(description = "用户显示名，支持模糊搜索")
    private String displayName;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "账号类型（'local', 'oauth'）")
    private String accountType;

    @Schema(description = "用户状态（'active', 'inactive', 'banned'）")
    private String status;
}
