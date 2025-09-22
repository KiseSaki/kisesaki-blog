package com.kisesaki.blog.user.dto.admin;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 管理后台用户列表项响应
 */
@Data
@Schema(description = "管理员查看的用户列表项")
public class AdminUserListResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "显示名")
    private String displayName;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "账号类型，local 或 oauth")
    private String accountType;

    @Schema(description = "用户状态，active/inactive/banned")
    private String status;

    @Schema(description = "最后登录时间")
    private OffsetDateTime lastLoginAt;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

}
