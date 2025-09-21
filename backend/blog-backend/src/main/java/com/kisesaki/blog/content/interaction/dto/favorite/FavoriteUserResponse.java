package com.kisesaki.blog.content.interaction.dto.favorite;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏用户信息响应DTO
 */
@Data
@NoArgsConstructor
public class FavoriteUserResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 收藏时间
     */
    private LocalDateTime favoriteTime;

    public FavoriteUserResponse(Long userId, String username, String nickname, String avatarUrl,
            LocalDateTime favoriteTime) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.favoriteTime = favoriteTime;
    }
}