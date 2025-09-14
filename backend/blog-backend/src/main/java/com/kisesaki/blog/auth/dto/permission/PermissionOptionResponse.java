package com.kisesaki.blog.auth.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "权限选项响应数据")
public class PermissionOptionResponse {
    @Schema(description = "代码", example = "post")
    private String code;

    @Schema(description = "描述", example = "文章")
    private String description;
}