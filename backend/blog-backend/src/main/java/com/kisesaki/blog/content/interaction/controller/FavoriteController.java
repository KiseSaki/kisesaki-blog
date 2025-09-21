package com.kisesaki.blog.content.interaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.content.interaction.dto.favorite.FavoritePostResponse;
import com.kisesaki.blog.content.interaction.dto.favorite.FavoriteStatusResponse;
import com.kisesaki.blog.content.interaction.dto.favorite.FavoriteUserResponse;
import com.kisesaki.blog.content.interaction.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 收藏功能控制器
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "收藏管理", description = "文章收藏相关接口")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 收藏文章
     */
    @PostMapping("/posts/{id}/favorite")
    @Operation(summary = "收藏文章", description = "用户收藏指定文章")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> favoritePost(
            @Parameter(description = "文章ID") @PathVariable("id") Long postId,
            Authentication authentication) {

        FavoriteStatusResponse response = favoriteService.favoritePost(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 取消收藏文章
     */
    @DeleteMapping("/posts/{id}/favorite")
    @Operation(summary = "取消收藏文章", description = "用户取消收藏指定文章")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> unfavoritePost(
            @Parameter(description = "文章ID") @PathVariable("id") Long postId,
            Authentication authentication) {

        FavoriteStatusResponse response = favoriteService.unfavoritePost(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取文章收藏状态
     */
    @GetMapping("/posts/{id}/favorite-status")
    @Operation(summary = "获取文章收藏状态", description = "获取当前用户对指定文章的收藏状态")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getFavoriteStatus(
            @Parameter(description = "文章ID") @PathVariable("id") Long postId,
            Authentication authentication) {

        FavoriteStatusResponse response = favoriteService.getFavoriteStatus(postId, authentication);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取文章收藏用户列表
     */
    @GetMapping("/posts/{id}/favorites")
    @Operation(summary = "获取文章收藏用户列表", description = "分页获取收藏指定文章的用户列表")
    public ResponseEntity<ApiResponse<Page<FavoriteUserResponse>>> getPostFavoriteUsers(
            @Parameter(description = "文章ID") @PathVariable("id") Long postId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {

        Page<FavoriteUserResponse> response = favoriteService.getPostFavoriteUsers(postId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取我的收藏列表
     */
    @GetMapping("/users/favorites")
    @Operation(summary = "获取我的收藏列表", description = "分页获取当前用户的收藏文章列表")
    public ResponseEntity<ApiResponse<Page<FavoritePostResponse>>> getCurrentUserFavorites(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {

        Page<FavoritePostResponse> response = favoriteService.getCurrentUserFavorites(authentication, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取用户的收藏列表（公开）
     */
    @GetMapping("/users/{userId}/favorites")
    @Operation(summary = "获取用户的收藏列表", description = "分页获取指定用户的公开收藏文章列表")
    public ResponseEntity<ApiResponse<Page<FavoritePostResponse>>> getUserFavorites(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {

        Page<FavoritePostResponse> response = favoriteService.getUserFavorites(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 获取用户收藏总数
     */
    @GetMapping("/users/{userId}/favorites/count")
    @Operation(summary = "获取用户收藏总数", description = "获取指定用户的收藏文章总数")
    public ResponseEntity<ApiResponse<Integer>> getUserFavoriteCount(
            @Parameter(description = "用户ID") @PathVariable Long userId) {

        int count = favoriteService.countUserFavorites(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
}