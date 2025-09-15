package com.kisesaki.blog.auth.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色列表响应数据")
public class RoleListResponse {
    @Schema(description = "角色 ID", example = "1")
    private Long id;

    @Schema(description = "角色名称", example = "admin")
    private String name;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}
