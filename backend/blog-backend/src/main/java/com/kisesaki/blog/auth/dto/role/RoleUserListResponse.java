package com.kisesaki.blog.auth.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "角色用户列表响应数据")
public class RoleUserListResponse {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    @Schema(description = "获得角色的时间")
    private OffsetDateTime assignedAt;
}