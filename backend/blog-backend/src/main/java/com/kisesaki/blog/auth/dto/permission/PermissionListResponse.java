package com.kisesaki.blog.auth.dto.permission;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "权限列表响应数据")
public class PermissionListResponse {
    @Schema(description = "权限ID", example = "1")
    private Long id;

    @Schema(description = "权限名称", example = "read_articles")
    private String name;

    @Schema(description = "权限描述", example = "允许读取文章")
    private String description;

    @Schema(description = "资源类型", example = "article")
    private String resource;

    @Schema(description = "操作类型", example = "view")
    private String action;

    @Schema(description = "创建时间", example = "2023-10-01T12:34:56Z")
    private OffsetDateTime createdAt;
}
