package com.kisesaki.blog.content.comment.dto;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论用户信息 DTO（简化版）
 * 专门用于评论列表显示，只包含必要的公开信息
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "评论用户信息（简化版）")
public class CommentUserDto {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "alice")
    private String username;

    @Schema(description = "显示名称", example = "Alice")
    private String displayName;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "个人简介", example = "热爱编程的开发者")
    private String bio;

    @Schema(description = "用户状态", example = "active")
    private String status;

    @Schema(description = "账号创建时间")
    private OffsetDateTime createdAt;

    @Schema(description = "是否为博客作者", example = "false")
    private Boolean isBlogAuthor;
}