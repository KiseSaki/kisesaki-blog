package com.kisesaki.blog.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.user.dto.admin.AdminUserInfoResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserListParams;
import com.kisesaki.blog.user.dto.admin.AdminUserListResponse;
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
}
