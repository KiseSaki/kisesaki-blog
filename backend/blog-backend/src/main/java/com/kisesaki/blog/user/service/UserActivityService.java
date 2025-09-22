package com.kisesaki.blog.user.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserActivityParams;
import com.kisesaki.blog.user.dto.admin.AdminUserActivityResponse;
import com.kisesaki.blog.user.entity.UserActivity;
import com.kisesaki.blog.user.mapper.UserActivityMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户活动日志服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserActivityService {

    private final UserActivityMapper userActivityMapper;
    private final ObjectMapper objectMapper;

    /**
     * 获取用户活动日志列表
     *
     * @param userId 用户ID
     * @param params 查询参数
     * @return 活动日志列表
     */
    public PageResponse<AdminUserActivityResponse> getUserActivityList(Long userId, AdminUserActivityParams params) {
        log.debug("获取用户 {} 的活动日志列表，参数: {}", userId, params);

        // 先获取总数
        Long totalCount = userActivityMapper.adminGetUserActivityCount(userId, params);
        if (totalCount == 0) {
            return PageResponse.empty(params.getPageable().getCurrentPage());
        }

        Page<AdminUserActivityResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize(),
                false);
        Page<AdminUserActivityResponse> result = userActivityMapper.adminGetUserActivityList(page, userId, params);
        result.setTotal(totalCount);

        log.debug("获取到用户 {} 的活动日志 {} 条", userId, result.getRecords().size());
        return PageResponse.of(result);
    }

    /**
     * 记录用户活动日志
     *
     * @param userId      用户ID
     * @param action      操作类型
     * @param description 操作描述
     * @param ipAddress   IP地址
     * @param userAgent   用户代理
     * @param details     操作详情
     * @param result      操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public void logUserActivity(Long userId, String action, String description,
            String ipAddress, String userAgent, Object details, String result) {
        try {
            UserActivity activity = new UserActivity();
            activity.setUserId(userId);
            activity.setAction(action);
            activity.setDescription(description);
            activity.setIpAddress(ipAddress);
            activity.setUserAgent(userAgent);
            activity.setResult(result);
            activity.setCreatedAt(OffsetDateTime.now());

            // 将 details 对象转换为 JsonNode
            if (details != null) {
                JsonNode detailsJson = objectMapper.valueToTree(details);
                activity.setDetails(detailsJson);
            }

            userActivityMapper.insert(activity);
            log.debug("记录用户 {} 的活动日志: {}", userId, action);
        } catch (Exception e) {
            log.error("记录用户活动日志失败: userId={}, action={}", userId, action, e);
            // 不抛出异常，避免影响主要业务流程
        }
    }

    /**
     * 记录用户活动日志（简化版本）
     *
     * @param userId      用户ID
     * @param action      操作类型
     * @param description 操作描述
     */
    public void logUserActivity(Long userId, String action, String description) {
        logUserActivity(userId, action, description, null, null, null, "success");
    }

    /**
     * 记录用户活动日志（带结果）
     *
     * @param userId      用户ID
     * @param action      操作类型
     * @param description 操作描述
     * @param result      操作结果
     */
    public void logUserActivity(Long userId, String action, String description, String result) {
        logUserActivity(userId, action, description, null, null, null, result);
    }

    /**
     * 记录用户活动日志（带详情）
     *
     * @param userId      用户ID
     * @param action      操作类型
     * @param description 操作描述
     * @param details     操作详情
     */
    public void logUserActivity(Long userId, String action, String description, Object details) {
        logUserActivity(userId, action, description, null, null, details, "success");
    }
}