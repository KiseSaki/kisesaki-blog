package com.kisesaki.blog.auth.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色详情响应数据")
public class RoleDetailResponse {
    @Schema(description = "角色 ID", example = "1")
    private Long id;

    @Schema(description = "角色名称", example = "admin")
    private String name;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "创建时间")
    private String createdAt;

    @Schema(description = "角色拥有的权限列表")
    private List<PermissionDto> permissions;

    @Data
    public static class PermissionDto {
        @Schema(description = "权限 ID", example = "1")
        private Long id;
        @Schema(description = "权限名称", example = "read_articles")
        private String name;
        @Schema(description = "权限描述", example = "允许读取文章")
        private String description;
    }
}
