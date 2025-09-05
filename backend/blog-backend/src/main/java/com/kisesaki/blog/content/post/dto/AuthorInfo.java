package com.kisesaki.blog.content.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "作者信息")
public class AuthorInfo {
    @Schema(description = "作者ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "kisesaki")
    private String username;

    @Schema(description = "显示名称", example = "KiseSaki")
    private String displayName;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "个人简介", example = "全栈开发工程师")
    private String bio;
}
