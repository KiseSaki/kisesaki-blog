package com.kisesaki.blog.user.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员更新用户状态请求")
public class AdminUserStatusUpdateRequest {

    @Schema(description = "用户状态", example = "active", allowableValues = { "active", "inactive", "banned" })
    @NotBlank(message = "用户状态不能为空")
    @Pattern(regexp = "^(active|inactive|banned)$", message = "用户状态必须是active、inactive或banned之一")
    private String status;

    @Schema(description = "状态变更原因", example = "违反社区规则")
    @Size(max = 500, message = "原因描述不能超过500个字符")
    private String reason;

}