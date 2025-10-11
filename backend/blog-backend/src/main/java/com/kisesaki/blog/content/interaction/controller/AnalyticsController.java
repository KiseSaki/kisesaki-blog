package com.kisesaki.blog.content.interaction.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.interaction.dto.analytics.DashboardStatsResponse;
import com.kisesaki.blog.content.interaction.dto.analytics.EventRecordRequest;
import com.kisesaki.blog.content.interaction.dto.analytics.PopularPostResponse;
import com.kisesaki.blog.content.interaction.dto.analytics.PostViewStatsResponse;
import com.kisesaki.blog.content.interaction.dto.analytics.RecentActivityResponse;
import com.kisesaki.blog.content.interaction.dto.analytics.ViewRecordRequest;
import com.kisesaki.blog.content.interaction.service.AnalyticsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 分析统计控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Tag(name = "分析统计", description = "页面浏览记录和事件统计相关接口")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * 记录页面浏览
     */
    @PostMapping("/view")
    @Operation(summary = "记录页面浏览", description = "记录用户的页面浏览行为，支持匿名访问")
    public ApiResponse<Void> recordPageView(
            @Valid @RequestBody ViewRecordRequest request,
            HttpServletRequest httpRequest,
            Authentication authentication) {

        analyticsService.recordPageView(request, httpRequest, authentication);
        return ResultUtils.success();
    }

    /**
     * 记录自定义事件
     */
    @PostMapping("/event")
    @Operation(summary = "记录自定义事件", description = "记录用户的自定义行为事件（如搜索、下载等），支持匿名访问")
    public ApiResponse<Void> recordCustomEvent(
            @Valid @RequestBody EventRecordRequest request,
            HttpServletRequest httpRequest,
            Authentication authentication) {

        analyticsService.recordCustomEvent(request, httpRequest, authentication);
        return ResultUtils.success();
    }

    /**
     * 获取文章浏览统计
     */
    @GetMapping("/posts/{id}/views")
    @Operation(summary = "获取文章浏览统计", description = "获取指定文章的浏览统计数据")
    public ApiResponse<PostViewStatsResponse> getPostViewStats(
            @Parameter(description = "文章ID") @PathVariable("id") Long postId) {

        PostViewStatsResponse stats = analyticsService.getPostViewStats(postId);
        return ResultUtils.success(stats);
    }

    /**
     * 获取仪表盘统计概览
     */
    @GetMapping("/dashboard/stats")
    @Operation(summary = "获取仪表盘统计概览", description = "获取仪表盘统计数据，包括文章、用户、评论和浏览统计")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ApiResponse<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse stats = analyticsService.getDashboardStats();
        return ResultUtils.success(stats);
    }

    /**
     * 获取热门文章列表
     */
    @GetMapping("/dashboard/popular-posts")
    @Operation(summary = "获取热门文章列表", description = "获取按浏览量排序的热门文章列表")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ApiResponse<List<PopularPostResponse>> getPopularPosts(
            @Parameter(description = "返回数量限制", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        List<PopularPostResponse> popularPosts = analyticsService.getPopularPosts(limit);
        return ResultUtils.success(popularPosts);
    }

    /**
     * 获取最近活动列表
     */
    @GetMapping("/dashboard/recent-activities")
    @Operation(summary = "获取最近活动列表", description = "获取最近的文章发布和评论活动")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ApiResponse<List<RecentActivityResponse>> getRecentActivities(
            @Parameter(description = "返回数量限制", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        List<RecentActivityResponse> activities = analyticsService.getRecentActivities(limit);
        return ResultUtils.success(activities);
    }
}
