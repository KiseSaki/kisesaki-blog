package com.kisesaki.blog.user.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.user.dto.admin.AdminUserActivityParams;
import com.kisesaki.blog.user.dto.admin.AdminUserActivityResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserInfoResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserListParams;
import com.kisesaki.blog.user.dto.admin.AdminUserListResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserStatsResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserStatusUpdateRequest;
import com.kisesaki.blog.user.dto.admin.AdminUserUpdateRequest;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.entity.UserStatusChange;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;
import com.kisesaki.blog.user.mapper.UserStatusChangeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserStatusChangeMapper userStatusChangeMapper;
    private final UserActivityService userActivityService;

    /**
     * 获取用户列表
     *
     * @param params 查询参数
     * @return 用户列表
     */
    public PageResponse<AdminUserListResponse> getUserList(AdminUserListParams params) {
        // 先获取总数
        Long totalCount = userMapper.adminGetUserListCount(params);
        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        Page<AdminUserListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(), params.getPageable().getPageSize(),
                false);
        Page<AdminUserListResponse> result = userMapper.adminGetUserList(page, params);
        result.setTotal(totalCount);

        return PageResponse.of(result);
    }

    /**
     * 获取用户详细信息
     *
     * @param userId 用户ID
     * @return 用户详细信息
     */
    public AdminUserInfoResponse getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.of(ErrorCode.NOT_FOUND, "用户不存在");
        }
        UserProfile userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId)
                .last("LIMIT 1"));
        AdminUserInfoResponse response = new AdminUserInfoResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setOauthId(user.getOauthId());
        response.setOauthProvider(user.getOauthProvider());
        response.setAccountType(user.getAccountType());
        response.setStatus(user.getStatus());
        response.setEmailVerified(user.getEmailVerified());
        response.setEmailVerifiedAt(user.getEmailVerifiedAt());
        response.setLastLoginAt(user.getLastLoginAt());
        response.setLastLoginIp(user.getLastLoginIp());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        if (userProfile != null) {
            response.setDisplayName(userProfile.getDisplayName());
            response.setFirstName(userProfile.getFirstName());
            response.setLastName(userProfile.getLastName());
            response.setBio(userProfile.getBio());
            response.setAvatarUrl(userProfile.getAvatarUrl());
            response.setCoverImageUrl(userProfile.getCoverImageUrl());
            response.setLocation(userProfile.getLocation());
            response.setWebsiteUrl(userProfile.getWebsiteUrl());
            response.setCompany(userProfile.getCompany());
            response.setTitle(userProfile.getTitle());
            response.setSocialLinks(userProfile.getSocialLinks());
            response.setBirthDate(userProfile.getBirthDate());
            response.setGender(userProfile.getGender());
            response.setTimezone(userProfile.getTimezone());
            response.setLanguage(userProfile.getLanguage());
            response.setThemePreference(userProfile.getThemePreference());
            response.setPrivacyLevel(userProfile.getPrivacyLevel());
        }
        return response;
    }

    /**
     * 更新用户信息
     *
     * @param userId  用户ID
     * @param request 更新请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(Long userId, AdminUserUpdateRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.of(ErrorCode.NOT_FOUND, "用户不存在");
        }

        // 记录更新前的信息用于活动日志
        Map<String, Object> oldValues = new HashMap<>();
        oldValues.put("username", user.getUsername());
        oldValues.put("email", user.getEmail());
        oldValues.put("status", user.getStatus());
        oldValues.put("emailVerified", user.getEmailVerified());

        // 更新User字段
        updateUserFields(user, request);

        // 更新UserProfile
        UserProfile userProfile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId)
                .last("LIMIT 1"));
        if (userProfile == null) {
            userProfile = new UserProfile();
            userProfile.setUserId(userId);
        }

        // 记录 UserProfile 更新前的信息
        if (userProfile.getId() != null) {
            oldValues.put("displayName", userProfile.getDisplayName());
            oldValues.put("bio", userProfile.getBio());
            oldValues.put("avatarUrl", userProfile.getAvatarUrl());
        }

        updateUserProfileFields(userProfile, request);

        if (userProfile.getId() == null) {
            userProfileMapper.insert(userProfile);
        } else {
            userProfileMapper.updateById(userProfile);
        }

        // 记录活动日志
        Map<String, Object> newValues = new HashMap<>();
        newValues.put("username", user.getUsername());
        newValues.put("email", user.getEmail());
        newValues.put("status", user.getStatus());
        newValues.put("emailVerified", user.getEmailVerified());
        newValues.put("displayName", userProfile.getDisplayName());
        newValues.put("bio", userProfile.getBio());
        newValues.put("avatarUrl", userProfile.getAvatarUrl());

        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("oldValues", oldValues);
        logDetails.put("newValues", newValues);
        logDetails.put("requestData", request);

        userActivityService.logUserActivity(
                userId,
                UserActivityType.ADMIN_USER_UPDATE,
                "管理员更新用户信息",
                logDetails);
    }

    /**
     * 更新User实体的字段
     *
     * @param user    用户实体
     * @param request 更新请求
     */
    private void updateUserFields(User user, AdminUserUpdateRequest request) {
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            validateUsernameUnique(request.getUsername(), user.getId());
            user.setUsername(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            validateEmailUnique(request.getEmail(), user.getId());
            user.setEmail(request.getEmail());
        }
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (request.getEmailVerified() != null) {
            user.setEmailVerified(request.getEmailVerified());
            if (request.getEmailVerified()) {
                user.setEmailVerifiedAt(java.time.OffsetDateTime.now());
            } else {
                user.setEmailVerifiedAt(null);
            }
        }
        userMapper.updateById(user);
    }

    /**
     * 更新用户状态
     * 
     * @param userId         用户ID
     * @param request        状态更新请求
     * @param authentication 当前认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long userId, AdminUserStatusUpdateRequest request, Authentication authentication) {
        OffsetDateTime now = OffsetDateTime.now();

        Long adminId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (adminId == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "无法获取操作用户信息");
        }

        // 获取用户状态
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw BusinessException.of(ErrorCode.NOT_FOUND, "用户不存在");
        }

        if (request.getStatus() == null) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "用户状态未传递");
        }
        if (request.getStatus().equals(user.getStatus())) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "用户状态未改变");
        }

        // 记录状态变更
        String oldStatus = user.getStatus();
        user.setStatus(request.getStatus());
        user.setUpdatedAt(now);
        userMapper.updateById(user);

        // 记录状态变更日志
        UserStatusChange statusChange = new UserStatusChange();
        statusChange.setUserId(userId);
        statusChange.setChangedBy(adminId);
        statusChange.setOldStatus(oldStatus);
        statusChange.setNewStatus(request.getStatus());
        statusChange.setReason(request.getReason());
        statusChange.setCreatedAt(now);

        userStatusChangeMapper.insert(statusChange);

        // 记录活动日志
        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("oldStatus", oldStatus);
        logDetails.put("newStatus", request.getStatus());
        logDetails.put("reason", request.getReason());
        logDetails.put("adminId", adminId);

        userActivityService.logUserActivity(
                userId,
                UserActivityType.ADMIN_USER_STATUS_CHANGE,
                String.format("管理员将用户状态从 %s 更改为 %s", oldStatus, request.getStatus()),
                logDetails);
    }

    /**
     * 更新UserProfile实体的字段
     *
     * @param userProfile 用户资料实体
     * @param request     更新请求
     */
    private void updateUserProfileFields(UserProfile userProfile, AdminUserUpdateRequest request) {
        if (request.getDisplayName() != null) {
            userProfile.setDisplayName(request.getDisplayName());
        }
        if (request.getFirstName() != null) {
            userProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            userProfile.setLastName(request.getLastName());
        }
        if (request.getBio() != null) {
            userProfile.setBio(request.getBio());
        }
        if (request.getAvatarUrl() != null) {
            userProfile.setAvatarUrl(request.getAvatarUrl());
        }
        if (request.getCoverImageUrl() != null) {
            userProfile.setCoverImageUrl(request.getCoverImageUrl());
        }
        if (request.getWebsiteUrl() != null) {
            userProfile.setWebsiteUrl(request.getWebsiteUrl());
        }
        if (request.getLocation() != null) {
            userProfile.setLocation(request.getLocation());
        }
        if (request.getCompany() != null) {
            userProfile.setCompany(request.getCompany());
        }
        if (request.getTitle() != null) {
            userProfile.setTitle(request.getTitle());
        }
        if (request.getSocialLinks() != null) {
            userProfile.setSocialLinks(request.getSocialLinks());
        }
        if (request.getBirthDate() != null) {
            userProfile.setBirthDate(request.getBirthDate());
        }
        if (request.getGender() != null) {
            userProfile.setGender(request.getGender());
        }
        if (request.getTimezone() != null) {
            userProfile.setTimezone(request.getTimezone());
        }
        if (request.getLanguage() != null) {
            userProfile.setLanguage(request.getLanguage());
        }
        if (request.getThemePreference() != null) {
            userProfile.setThemePreference(request.getThemePreference());
        }
        if (request.getPrivacyLevel() != null) {
            userProfile.setPrivacyLevel(request.getPrivacyLevel());
        }
    }

    /**
     * 验证用户名唯一性
     *
     * @param username      用户名
     * @param excludeUserId 排除的用户ID
     */
    private void validateUsernameUnique(String username, Long excludeUserId) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .ne(User::getId, excludeUserId));
        if (count != null && count > 0) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "用户名已被占用");
        }
    }

    /**
     * 验证邮箱唯一性
     *
     * @param email         邮箱
     * @param excludeUserId 排除的用户ID
     */
    private void validateEmailUnique(String email, Long excludeUserId) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getId, excludeUserId));
        if (count != null && count > 0) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "邮箱已被占用");
        }
    }

    /**
     * 获取用户活动日志
     *
     * @param userId 用户ID
     * @param params 查询参数
     * @return 活动日志列表
     */
    public PageResponse<AdminUserActivityResponse> getUserActivity(Long userId, AdminUserActivityParams params) {
        log.debug("获取用户 {} 的活动日志", userId);

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return userActivityService.getUserActivityList(userId, params);
    }

    /**
     * 获取用户统计数据
     *
     * @return 用户统计数据
     */
    public AdminUserStatsResponse getUserStats() {
        log.debug("获取用户统计数据");

        AdminUserStatsResponse response = new AdminUserStatsResponse();

        // 使用 Lambda Wrapper 进行基础统计
        response.setTotalUsers(userMapper.selectCount(null));
        response.setActiveUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getStatus, "active")));
        response.setInactiveUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getStatus, "inactive")));
        response.setBannedUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getStatus, "banned")));

        // 账号类型统计
        response.setOauthUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getAccountType, "oauth")));
        response.setLocalUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getAccountType, "local")));

        // 邮箱验证统计
        response.setEmailVerifiedUsers(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getEmailVerified, true)));

        // 时间范围统计
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime todayStart = now.toLocalDate().atStartOfDay().atOffset(now.getOffset());
        OffsetDateTime todayEnd = todayStart.plusDays(1).minusNanos(1);
        OffsetDateTime weekStart = todayStart.minusDays(7);
        OffsetDateTime monthStart = todayStart.minusDays(30);

        // 今日注册用户数
        response.setTodayRegistrations(userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .ge(User::getCreatedAt, todayStart)
                        .le(User::getCreatedAt, todayEnd)));

        // 一周内注册用户数
        response.setWeekRegistrations(userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .ge(User::getCreatedAt, weekStart)
                        .le(User::getCreatedAt, now)));

        // 一月内注册用户数
        response.setMonthRegistrations(userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .ge(User::getCreatedAt, monthStart)
                        .le(User::getCreatedAt, now)));

        // 复杂的趋势数据和分组统计仍使用 XML 查询
        response.setRegistrationTrend(userMapper.getRegistrationTrend(7));
        response.setLoginTrend(userMapper.getLoginTrend(7));

        // 分组统计
        Map<String, Long> accountTypeStats = new HashMap<>();
        userMapper.getAccountTypeStats()
                .forEach(map -> accountTypeStats.put((String) map.get("accountType"), (Long) map.get("count")));
        response.setAccountTypeStats(accountTypeStats);

        Map<String, Long> statusStats = new HashMap<>();
        userMapper.getStatusStats()
                .forEach(map -> statusStats.put((String) map.get("status"), (Long) map.get("count")));
        response.setStatusStats(statusStats);

        response.setGeneratedAt(now);

        log.debug("用户统计数据获取完成，总用户数: {}", response.getTotalUsers());
        return response;
    }
}
