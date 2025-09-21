package com.kisesaki.blog.content.interaction.dto.favorite;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏状态响应DTO
 */
@Data
@NoArgsConstructor
public class FavoriteStatusResponse {

    /**
     * 文章ID
     */
    private Long postId;

    /**
     * 用户是否已收藏
     */
    private Boolean favorited;

    /**
     * 收藏数量
     */
    private Integer favoriteCount;

    public FavoriteStatusResponse(Long postId, Boolean favorited, Integer favoriteCount) {
        this.postId = postId;
        this.favorited = favorited;
        this.favoriteCount = favoriteCount;
    }
}