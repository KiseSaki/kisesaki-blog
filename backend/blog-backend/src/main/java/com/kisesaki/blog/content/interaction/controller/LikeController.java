package com.kisesaki.blog.content.interaction.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.content.interaction.dto.like.ReactionStatusResponse;
import com.kisesaki.blog.content.interaction.entity.Likes;
import com.kisesaki.blog.content.interaction.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 互动控制器
 * 处理文章和评论的点赞、踩等反应功能
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "互动管理", description = "处理文章和评论的点赞、踩等反应功能")
public class LikeController {

    private final LikeService likeService;

    // =================== 文章反应相关接口 ===================

    @PostMapping("/posts/{postId}/like")
    @Operation(summary = "点赞文章")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> likePost(
            @Parameter(description = "文章ID") @PathVariable Long postId,
            Authentication authentication) {
        likeService.likePost(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/posts/{postId}/like")
    @Operation(summary = "取消点赞文章")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> unlikePost(
            @Parameter(description = "文章ID") @PathVariable Long postId,
            Authentication authentication) {
        likeService.unlikePost(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/posts/{postId}/dislike")
    @Operation(summary = "点踩文章")
    public ResponseEntity<ApiResponse<Void>> dislikePost(
            @Parameter(description = "文章ID") @PathVariable Long postId,
            Authentication authentication) {
        likeService.dislikePost(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/posts/{postId}/dislike")
    @Operation(summary = "取消点踩文章")
    public ResponseEntity<ApiResponse<Void>> unDislikePost(
            @Parameter(description = "文章ID") @PathVariable Long postId,
            Authentication authentication) {
        likeService.unDislikePost(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/posts/{postId}/reaction-status")
    @Operation(summary = "获取文章反应状态")
    public ResponseEntity<ApiResponse<ReactionStatusResponse>> getPostReactionStatus(
            @Parameter(description = "文章ID") @PathVariable Long postId,
            Authentication authentication) {

        ReactionStatusResponse response = buildReactionStatusResponse(
                Likes.TargetType.POST, postId, authentication);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // =================== 评论反应相关接口 ===================

    @PostMapping("/comments/{commentId}/like")
    @Operation(summary = "点赞评论")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> likeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            Authentication authentication) {
        likeService.likeComment(commentId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/comments/{commentId}/like")
    @Operation(summary = "取消点赞评论")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> unlikeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            Authentication authentication) {
        likeService.unlikeComment(commentId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/comments/{commentId}/dislike")
    @Operation(summary = "点踩评论")
    public ResponseEntity<ApiResponse<Void>> dislikeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            Authentication authentication) {
        likeService.dislikeComment(commentId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/comments/{commentId}/dislike")
    @Operation(summary = "取消点踩评论")
    public ResponseEntity<ApiResponse<Void>> unDislikeComment(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            Authentication authentication) {
        likeService.unDislikeComment(commentId, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/comments/{commentId}/reaction-status")
    @Operation(summary = "获取评论反应状态")
    public ResponseEntity<ApiResponse<ReactionStatusResponse>> getCommentReactionStatus(
            @Parameter(description = "评论ID") @PathVariable Long commentId,
            Authentication authentication) {

        ReactionStatusResponse response = buildReactionStatusResponse(
                Likes.TargetType.COMMENT, commentId, authentication);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // =================== 批量查询接口 ===================

    @GetMapping("/posts/reaction-status")
    @Operation(summary = "批量获取文章反应状态")
    public ResponseEntity<ApiResponse<Map<Long, ReactionStatusResponse>>> getPostsReactionStatus(
            @Parameter(description = "文章ID列表，逗号分隔") @RequestParam("postIds") List<Long> postIds,
            Authentication authentication) {

        Map<Long, ReactionStatusResponse> responseMap = buildBatchReactionStatusResponse(
                Likes.TargetType.POST, postIds, authentication);
        return ResponseEntity.ok(ApiResponse.success(responseMap));
    }

    @GetMapping("/comments/reaction-status")
    @Operation(summary = "批量获取评论反应状态")
    public ResponseEntity<ApiResponse<Map<Long, ReactionStatusResponse>>> getCommentsReactionStatus(
            @Parameter(description = "评论ID列表，逗号分隔") @RequestParam("commentIds") List<Long> commentIds,
            Authentication authentication) {

        Map<Long, ReactionStatusResponse> responseMap = buildBatchReactionStatusResponse(
                Likes.TargetType.COMMENT, commentIds, authentication);
        return ResponseEntity.ok(ApiResponse.success(responseMap));
    }

    // =================== 统计接口 ===================

    @GetMapping("/users/{userId}/likes/count")
    @Operation(summary = "获取用户点赞总数")
    public ResponseEntity<ApiResponse<Integer>> getUserLikesCount(
            @Parameter(description = "用户ID") @PathVariable Long userId) {

        int count = likeService.countUserLikes(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    // =================== 私有辅助方法 ===================

    /**
     * 构建反应状态响应
     */
    private ReactionStatusResponse buildReactionStatusResponse(Likes.TargetType targetType, Long targetId,
            Authentication authentication) {
        ReactionStatusResponse response = new ReactionStatusResponse();
        response.setTargetId(targetId);
        response.setTargetType(targetType);

        // 获取反应计数
        int likeCount = likeService.countReactionsByTarget(targetType, targetId, Likes.ReactionType.LIKE);
        int dislikeCount = likeService.countReactionsByTarget(targetType, targetId, Likes.ReactionType.DISLIKE);
        response.setLikeCount(likeCount);
        response.setDislikeCount(dislikeCount);

        // 获取用户反应状态
        Likes userReaction = likeService.getUserReaction(targetType, targetId, authentication);
        if (userReaction != null) {
            response.setUserReactionType(userReaction.getReactionType());
            response.setIsLiked(userReaction.getReactionType() == Likes.ReactionType.LIKE);
            response.setIsDisliked(userReaction.getReactionType() == Likes.ReactionType.DISLIKE);
        } else {
            response.setUserReactionType(null);
            response.setIsLiked(false);
            response.setIsDisliked(false);
        }

        return response;
    }

    /**
     * 构建批量反应状态响应
     */
    private Map<Long, ReactionStatusResponse> buildBatchReactionStatusResponse(
            Likes.TargetType targetType, List<Long> targetIds, Authentication authentication) {

        // 获取用户反应状态
        Map<Long, Likes> userReactions = likeService.getUserReactionsByTargetIds(targetType, targetIds, authentication);

        // 为每个目标构建响应
        return targetIds.stream()
                .collect(java.util.stream.Collectors.toMap(
                        targetId -> targetId,
                        targetId -> {
                            ReactionStatusResponse response = new ReactionStatusResponse();
                            response.setTargetId(targetId);
                            response.setTargetType(targetType);

                            // 获取反应计数
                            int likeCount = likeService.countReactionsByTarget(targetType, targetId,
                                    Likes.ReactionType.LIKE);
                            int dislikeCount = likeService.countReactionsByTarget(targetType, targetId,
                                    Likes.ReactionType.DISLIKE);
                            response.setLikeCount(likeCount);
                            response.setDislikeCount(dislikeCount);

                            // 设置用户反应状态
                            Likes userReaction = userReactions.get(targetId);
                            if (userReaction != null) {
                                response.setUserReactionType(userReaction.getReactionType());
                                response.setIsLiked(userReaction.getReactionType() == Likes.ReactionType.LIKE);
                                response.setIsDisliked(userReaction.getReactionType() == Likes.ReactionType.DISLIKE);
                            } else {
                                response.setUserReactionType(null);
                                response.setIsLiked(false);
                                response.setIsDisliked(false);
                            }

                            return response;
                        }));
    }
}
