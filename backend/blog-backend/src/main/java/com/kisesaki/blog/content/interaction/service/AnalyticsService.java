package com.kisesaki.blog.content.interaction.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.interaction.dto.analytics.EventRecordRequest;
import com.kisesaki.blog.content.interaction.dto.analytics.PostViewStatsResponse;
import com.kisesaki.blog.content.interaction.dto.analytics.ViewRecordRequest;
import com.kisesaki.blog.content.interaction.entity.CustomEvents;
import com.kisesaki.blog.content.interaction.entity.PageViews;
import com.kisesaki.blog.content.interaction.mapper.CustomEventsMapper;
import com.kisesaki.blog.content.interaction.mapper.PageViewsMapper;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 分析统计服务
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private final PageViewsMapper pageViewsMapper;
    private final CustomEventsMapper customEventsMapper;
    private final PostsMapper postsMapper;

    // 同一会话重复访问的时间限制（分钟）
    private static final int SESSION_DUPLICATE_TIME_LIMIT = 30;

    /**
     * 记录页面浏览
     *
     * @param request        浏览记录请求
     * @param httpRequest    HTTP请求对象
     * @param authentication 认证信息（可为null）
     */
    @Transactional(rollbackFor = Exception.class)
    public void recordPageView(ViewRecordRequest request, HttpServletRequest httpRequest,
            Authentication authentication) {
        try {
            // 检查重复访问（同一会话在指定时间内不重复记录）
            if (isDuplicateView(request.getSessionId(), request.getPageUrl())) {
                log.debug("跳过重复的页面访问记录: sessionId={}, pageUrl={}",
                        request.getSessionId(), request.getPageUrl());
                return;
            }

            // 如果是文章页面，验证文章是否存在
            if (request.getPageType() == ViewRecordRequest.PageType.POST && request.getPostId() != null) {
                validatePostExists(request.getPostId());
            }

            PageViews pageView = new PageViews();

            // 基本信息
            pageView.setPostId(request.getPostId());
            pageView.setPageType(PageViews.PageType.valueOf(request.getPageType().name()));
            pageView.setPageUrl(request.getPageUrl());
            pageView.setPageTitle(request.getPageTitle());
            pageView.setReferrer(request.getReferrer());
            pageView.setUtmSource(request.getUtmSource());
            pageView.setUtmMedium(request.getUtmMedium());
            pageView.setUtmCampaign(request.getUtmCampaign());
            pageView.setSessionId(request.getSessionId());
            pageView.setDuration(request.getDuration());

            // 用户信息
            if (authentication != null) {
                Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
                pageView.setUserId(userId);
            }

            // 客户端信息
            extractClientInfo(httpRequest, pageView);

            pageView.setViewedAt(OffsetDateTime.now());

            pageViewsMapper.insert(pageView);
            log.info("记录页面浏览: postId={}, pageType={}, sessionId={}",
                    request.getPostId(), request.getPageType(), request.getSessionId());

        } catch (Exception e) {
            log.error("记录页面浏览失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "记录页面浏览失败");
        }
    }

    /**
     * 记录自定义事件
     *
     * @param request        事件记录请求
     * @param httpRequest    HTTP请求对象
     * @param authentication 认证信息（可为null）
     */
    @Transactional(rollbackFor = Exception.class)
    public void recordCustomEvent(EventRecordRequest request, HttpServletRequest httpRequest,
            Authentication authentication) {
        try {
            CustomEvents event = new CustomEvents();

            // 基本信息
            event.setEventType(request.getEventType());
            event.setEventData(request.getEventData());
            event.setPageUrl(request.getPageUrl());
            event.setSessionId(request.getSessionId());

            // 用户信息
            if (authentication != null) {
                Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
                event.setUserId(userId);
            }

            // 客户端信息
            extractClientInfo(httpRequest, event);

            event.setCreatedAt(OffsetDateTime.now());

            customEventsMapper.insert(event);
            log.info("记录自定义事件: eventType={}, sessionId={}",
                    request.getEventType(), request.getSessionId());

        } catch (Exception e) {
            log.error("记录自定义事件失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "记录自定义事件失败");
        }
    }

    /**
     * 获取文章浏览统计
     *
     * @param postId 文章ID
     * @return 浏览统计响应
     */
    public PostViewStatsResponse getPostViewStats(Long postId) {
        // 验证文章是否存在
        validatePostExists(postId);

        // 总浏览次数 - 使用 Lambda Wrapper
        long totalViews = pageViewsMapper.selectCount(
                new LambdaQueryWrapper<PageViews>()
                        .eq(PageViews::getPostId, postId));

        // 独立访客数 - 使用自定义方法（需要 COUNT(DISTINCT)）
        long uniqueViews = pageViewsMapper.countUniqueViewsByPost(postId);

        // 今日浏览次数 - 使用 Lambda Wrapper
        OffsetDateTime todayStart = OffsetDateTime.now().with(LocalTime.MIN);
        OffsetDateTime todayEnd = OffsetDateTime.now().with(LocalTime.MAX);
        long todayViews = pageViewsMapper.selectCount(
                new LambdaQueryWrapper<PageViews>()
                        .eq(PageViews::getPostId, postId)
                        .between(PageViews::getViewedAt, todayStart, todayEnd));

        return new PostViewStatsResponse(postId, totalViews, uniqueViews, todayViews);
    }

    /**
     * 检查是否为重复访问
     *
     * @param sessionId 会话ID
     * @param pageUrl   页面URL
     * @return 是否为重复访问
     */
    private boolean isDuplicateView(String sessionId, String pageUrl) {
        int count = pageViewsMapper.countSessionPageView(sessionId, pageUrl, SESSION_DUPLICATE_TIME_LIMIT);
        return count > 0;
    }

    /**
     * 验证文章是否存在
     */
    private void validatePostExists(Long postId) {
        if (postsMapper.selectById(postId) == null) {
            throw new BusinessException(ErrorCode.POST_NOT_FOUND, "文章不存在");
        }
    }

    /**
     * 提取客户端信息（IP地址、User-Agent、设备类型等）
     */
    private void extractClientInfo(HttpServletRequest request, PageViews pageView) {
        // IP地址
        String ipAddress = getClientIpAddress(request);
        try {
            pageView.setIpAddress(InetAddress.getByName(ipAddress));
        } catch (UnknownHostException e) {
            log.warn("无法解析IP地址: {}", ipAddress);
        }

        // User-Agent
        String userAgent = request.getHeader("User-Agent");
        pageView.setUserAgent(userAgent);

        // 设备类型、浏览器、操作系统检测
        if (userAgent != null) {
            parseUserAgent(userAgent, pageView);
        }
    }

    /**
     * 提取客户端信息（自定义事件版本）
     */
    private void extractClientInfo(HttpServletRequest request, CustomEvents event) {
        // IP地址
        String ipAddress = getClientIpAddress(request);
        try {
            event.setIpAddress(InetAddress.getByName(ipAddress));
        } catch (UnknownHostException e) {
            log.warn("无法解析IP地址: {}", ipAddress);
        }

        // User-Agent
        String userAgent = request.getHeader("User-Agent");
        event.setUserAgent(userAgent);

        // 设备类型、浏览器、操作系统检测
        if (userAgent != null) {
            parseUserAgent(userAgent, event);
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * 解析 User-Agent 字符串
     */
    private void parseUserAgent(String userAgent, PageViews pageView) {
        String lowerUserAgent = userAgent.toLowerCase();

        // 设备类型检测
        if (lowerUserAgent.contains("mobile")) {
            pageView.setDeviceType(PageViews.DeviceType.MOBILE);
        } else if (lowerUserAgent.contains("tablet") || lowerUserAgent.contains("ipad")) {
            pageView.setDeviceType(PageViews.DeviceType.TABLET);
        } else {
            pageView.setDeviceType(PageViews.DeviceType.DESKTOP);
        }

        // 浏览器检测
        if (lowerUserAgent.contains("chrome")) {
            pageView.setBrowser("Chrome");
        } else if (lowerUserAgent.contains("firefox")) {
            pageView.setBrowser("Firefox");
        } else if (lowerUserAgent.contains("safari")) {
            pageView.setBrowser("Safari");
        } else if (lowerUserAgent.contains("edge")) {
            pageView.setBrowser("Edge");
        } else {
            pageView.setBrowser("Other");
        }

        // 操作系统检测
        if (lowerUserAgent.contains("windows")) {
            pageView.setOs("Windows");
        } else if (lowerUserAgent.contains("mac")) {
            pageView.setOs("macOS");
        } else if (lowerUserAgent.contains("linux")) {
            pageView.setOs("Linux");
        } else if (lowerUserAgent.contains("android")) {
            pageView.setOs("Android");
        } else if (lowerUserAgent.contains("ios")) {
            pageView.setOs("iOS");
        } else {
            pageView.setOs("Other");
        }
    }

    /**
     * 解析 User-Agent 字符串（自定义事件版本）
     */
    private void parseUserAgent(String userAgent, CustomEvents event) {
        String lowerUserAgent = userAgent.toLowerCase();

        // 设备类型检测
        if (lowerUserAgent.contains("mobile")) {
            event.setDeviceType(CustomEvents.DeviceType.MOBILE);
        } else if (lowerUserAgent.contains("tablet") || lowerUserAgent.contains("ipad")) {
            event.setDeviceType(CustomEvents.DeviceType.TABLET);
        } else {
            event.setDeviceType(CustomEvents.DeviceType.DESKTOP);
        }

        // 浏览器检测
        if (lowerUserAgent.contains("chrome")) {
            event.setBrowser("Chrome");
        } else if (lowerUserAgent.contains("firefox")) {
            event.setBrowser("Firefox");
        } else if (lowerUserAgent.contains("safari")) {
            event.setBrowser("Safari");
        } else if (lowerUserAgent.contains("edge")) {
            event.setBrowser("Edge");
        } else {
            event.setBrowser("Other");
        }

        // 操作系统检测
        if (lowerUserAgent.contains("windows")) {
            event.setOs("Windows");
        } else if (lowerUserAgent.contains("mac")) {
            event.setOs("macOS");
        } else if (lowerUserAgent.contains("linux")) {
            event.setOs("Linux");
        } else if (lowerUserAgent.contains("android")) {
            event.setOs("Android");
        } else if (lowerUserAgent.contains("ios")) {
            event.setOs("iOS");
        } else {
            event.setOs("Other");
        }
    }

    // ==================== 便捷查询方法 (使用 Lambda Wrapper) ====================

    /**
     * 查询指定时间范围内的浏览记录
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 浏览记录列表
     */
    public List<PageViews> getViewsByTimeRange(OffsetDateTime startTime, OffsetDateTime endTime) {
        return pageViewsMapper.selectList(
                new LambdaQueryWrapper<PageViews>()
                        .between(PageViews::getViewedAt, startTime, endTime)
                        .orderByDesc(PageViews::getViewedAt));
    }

    /**
     * 查询指定文章的浏览记录
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param postId 文章ID
     * @param limit  记录数限制
     * @return 浏览记录列表
     */
    public List<PageViews> getViewsByPost(Long postId, int limit) {
        return pageViewsMapper.selectList(
                new LambdaQueryWrapper<PageViews>()
                        .eq(PageViews::getPostId, postId)
                        .orderByDesc(PageViews::getViewedAt)
                        .last(limit > 0 ? "LIMIT " + limit : ""));
    }

    /**
     * 删除过期的浏览记录
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param expireTime 过期时间
     * @return 删除的记录数
     */
    public int deleteExpiredViews(OffsetDateTime expireTime) {
        return pageViewsMapper.delete(
                new LambdaQueryWrapper<PageViews>()
                        .lt(PageViews::getViewedAt, expireTime));
    }

    /**
     * 更新页面停留时间
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param id       记录ID
     * @param duration 停留时间（秒）
     * @return 更新的记录数
     */
    public int updateViewDuration(Long id, Integer duration) {
        PageViews updateEntity = new PageViews();
        updateEntity.setDuration(duration);

        return pageViewsMapper.update(updateEntity,
                new LambdaQueryWrapper<PageViews>()
                        .eq(PageViews::getId, id));
    }

    /**
     * 查询指定时间范围内的事件记录
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 事件记录列表
     */
    public List<CustomEvents> getEventsByTimeRange(OffsetDateTime startTime, OffsetDateTime endTime) {
        return customEventsMapper.selectList(
                new LambdaQueryWrapper<CustomEvents>()
                        .between(CustomEvents::getCreatedAt, startTime, endTime)
                        .orderByDesc(CustomEvents::getCreatedAt));
    }

    /**
     * 按事件类型统计事件数量
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param eventType 事件类型
     * @param startTime 开始时间（可为null）
     * @param endTime   结束时间（可为null）
     * @return 事件数量
     */
    public long countEventsByType(String eventType, OffsetDateTime startTime, OffsetDateTime endTime) {
        LambdaQueryWrapper<CustomEvents> queryWrapper = new LambdaQueryWrapper<CustomEvents>()
                .eq(CustomEvents::getEventType, eventType);

        if (startTime != null && endTime != null) {
            queryWrapper.between(CustomEvents::getCreatedAt, startTime, endTime);
        }

        return customEventsMapper.selectCount(queryWrapper);
    }

    /**
     * 查询用户的事件记录
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param userId 用户ID
     * @param limit  记录数限制
     * @return 事件记录列表
     */
    public List<CustomEvents> getEventsByUser(Long userId, int limit) {
        return customEventsMapper.selectList(
                new LambdaQueryWrapper<CustomEvents>()
                        .eq(CustomEvents::getUserId, userId)
                        .orderByDesc(CustomEvents::getCreatedAt)
                        .last(limit > 0 ? "LIMIT " + limit : ""));
    }

    /**
     * 删除过期的事件记录
     * 使用 Lambda Wrapper 替代 XML 查询
     *
     * @param expireTime 过期时间
     * @return 删除的记录数
     */
    public int deleteExpiredEvents(OffsetDateTime expireTime) {
        return customEventsMapper.delete(
                new LambdaQueryWrapper<CustomEvents>()
                        .lt(CustomEvents::getCreatedAt, expireTime));
    }
}