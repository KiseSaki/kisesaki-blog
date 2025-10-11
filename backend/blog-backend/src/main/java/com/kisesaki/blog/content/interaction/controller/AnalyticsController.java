package com.kisesaki.blog.content.interaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.content.interaction.dto.analytics.EventRecordRequest;
import com.kisesaki.blog.content.interaction.dto.analytics.PostViewStatsResponse;
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
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "分析统计", description = "页面浏览记录和事件统计相关接口")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * 记录页面浏览
     */
    @PostMapping("/analytics/view")
    @Operation(summary = "记录页面浏览", description = "记录用户的页面浏览行为，支持匿名访问")
    public ResponseEntity<ApiResponse<Void>> recordPageView(
            @Valid @RequestBody ViewRecordRequest request,
            HttpServletRequest httpRequest,
            Authentication authentication) {

        analyticsService.recordPageView(request, httpRequest, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 记录自定义事件
     */
    @PostMapping("/analytics/event")
    @Operation(summary = "记录自定义事件", description = "记录用户的自定义行为事件（如搜索、下载等），支持匿名访问")
    public ResponseEntity<ApiResponse<Void>> recordCustomEvent(
            @Valid @RequestBody EventRecordRequest request,
            HttpServletRequest httpRequest,
            Authentication authentication) {

        analyticsService.recordCustomEvent(request, httpRequest, authentication);
        return ResponseEntity.ok(ApiResponse.success());
    }

    /**
     * 获取文章浏览统计
     */
    @GetMapping("/posts/{id}/views")
    @Operation(summary = "获取文章浏览统计", description = "获取指定文章的浏览统计数据")
    public ResponseEntity<ApiResponse<PostViewStatsResponse>> getPostViewStats(
            @Parameter(description = "文章ID") @PathVariable("id") Long postId) {

        PostViewStatsResponse stats = analyticsService.getPostViewStats(postId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
