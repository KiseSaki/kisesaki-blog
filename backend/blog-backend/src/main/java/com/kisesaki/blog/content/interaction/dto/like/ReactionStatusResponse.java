package com.kisesaki.blog.content.interaction.dto.like;

import com.kisesaki.blog.content.interaction.entity.Likes;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 反应状态响应DTO
 */
@Data
@NoArgsConstructor
public class ReactionStatusResponse {

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 目标类型
     */
    private Likes.TargetType targetType;

    /**
     * 用户是否已点赞
     */
    private Boolean isLiked;

    /**
     * 用户是否已点踩
     */
    private Boolean isDisliked;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    /**
     * 点踩数量
     */
    private Integer dislikeCount;

    /**
     * 用户的反应类型（如果有的话）
     */
    private Likes.ReactionType userReactionType;
}