package com.kisesaki.blog.user.dto;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户关注信息 DTO
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户关注信息")
public class UserFollowDto {

    /** 用户ID */
    @Schema(description = "用户ID", example = "1234567890")
    private Long userId;

    /** 用户名 */
    @Schema(description = "用户名", example = "kisesaki")
    private String username;

    /** 显示名称 */
    @Schema(description = "显示名称", example = "KiseSaki")
    private String displayName;

    /** 头像URL */
    @Schema(description = "头像URL", example = "https://avatar.example.com/kisesaki.jpg")
    private String avatarUrl;

    /** 个人简介 */
    @Schema(description = "个人简介", example = "热爱编程的全栈开发者")
    private String bio;

    /** 关注状态 */
    @Schema(description = "关注状态", example = "active")
    private String status;

    /** 关注时间 */
    @Schema(description = "关注时间")
    private OffsetDateTime followedAt;

    /** 是否互相关注 */
    @Schema(description = "是否互相关注", example = "true")
    private Boolean isMultualFollow;
}
