package com.kisesaki.blog.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.user.dto.admin.AdminUserInfoResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserListParams;
import com.kisesaki.blog.user.dto.admin.AdminUserListResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserUpdateRequest;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

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
        updateUserProfileFields(userProfile, request);

        if (userProfile.getId() == null) {
            userProfileMapper.insert(userProfile);
        } else {
            userProfileMapper.updateById(userProfile);
        }
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
}
