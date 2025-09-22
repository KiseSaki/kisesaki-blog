package com.kisesaki.blog.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.user.dto.UserStatsDto;
import com.kisesaki.blog.user.dto.info.UpdateProfileDto;
import com.kisesaki.blog.user.dto.info.UserInfoDto;
import com.kisesaki.blog.user.dto.info.UserProfileDto;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.entity.UserSettings;
import com.kisesaki.blog.user.mapper.UserFollowMapper;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;
import com.kisesaki.blog.user.mapper.UserSettingsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserSettingsMapper userSettingsMapper;
    private final UserFollowMapper userFollowMapper;
    private final UserActivityService userActivityService;

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public UserInfoDto getUserInfoById(Long id) {
        log.debug("根据用户ID获取用户信息: {}", id);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserProfile profile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, id));

        List<UserSettings> settings = userSettingsMapper.selectList(new LambdaQueryWrapper<UserSettings>()
                .eq(UserSettings::getUserId, id));

        return UserInfoDto.from(user, profile, settings);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public UserInfoDto getUserInfoByUsername(String username) {
        log.debug("根据用户名获取用户信息: {}", username);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserProfile profile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, user.getId()));

        List<UserSettings> settings = userSettingsMapper.selectList(new LambdaQueryWrapper<UserSettings>()
                .eq(UserSettings::getUserId, user.getId()));

        return UserInfoDto.from(user, profile, settings);
    }

    /**
     * 获取用户简要信息（用于展示）
     *
     * @param userId 用户ID
     * @return 用户简要信息
     */
    public UserProfileDto getUserProfile(Long userId) {
        log.debug("获取用户简要信息: {}", userId);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserProfile profile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId));

        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(profile != null ? profile.getDisplayName() : null)
                .avatarUrl(profile != null ? profile.getAvatarUrl() : null)
                .bio(profile != null ? profile.getBio() : null)
                .websiteUrl(profile != null ? profile.getWebsiteUrl() : null)
                .location(profile != null ? profile.getLocation() : null)
                .company(profile != null ? profile.getCompany() : null)
                .title(profile != null ? profile.getTitle() : null)
                .status(user.getStatus())
                .emailVerified(user.getEmailVerified())
                .build();
    }

    /**
     * 更新用户资料
     *
     * @param userId    用户ID
     * @param updateDto 更新资料DTO
     * @return 更新后的用户信息
     */
    @Transactional
    public UserInfoDto updateUserProfile(Long userId, UpdateProfileDto updateDto) {
        log.debug("更新用户资料: {}", userId);

        // 检查用户是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 获取或创建用户扩展信息
        UserProfile profile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId));
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
        }

        // 更新扩展信息字段
        if (updateDto.getDisplayName() != null) {
            profile.setDisplayName(updateDto.getDisplayName());
        }
        if (updateDto.getFirstName() != null) {
            profile.setFirstName(updateDto.getFirstName());
        }
        if (updateDto.getLastName() != null) {
            profile.setLastName(updateDto.getLastName());
        }
        if (updateDto.getBio() != null) {
            profile.setBio(updateDto.getBio());
        }
        if (updateDto.getWebsiteUrl() != null) {
            profile.setWebsiteUrl(updateDto.getWebsiteUrl());
        }
        if (updateDto.getLocation() != null) {
            profile.setLocation(updateDto.getLocation());
        }
        if (updateDto.getCompany() != null) {
            profile.setCompany(updateDto.getCompany());
        }
        if (updateDto.getTitle() != null) {
            profile.setTitle(updateDto.getTitle());
        }
        if (updateDto.getSocialLinks() != null) {
            profile.setSocialLinks(updateDto.getSocialLinks());
        }
        if (updateDto.getBirthDate() != null) {
            profile.setBirthDate(updateDto.getBirthDate());
        }
        if (updateDto.getGender() != null) {
            profile.setGender(updateDto.getGender());
        }
        if (updateDto.getTimezone() != null) {
            profile.setTimezone(updateDto.getTimezone());
        }
        if (updateDto.getLanguage() != null) {
            profile.setLanguage(updateDto.getLanguage());
        }
        if (updateDto.getThemePreference() != null) {
            profile.setThemePreference(updateDto.getThemePreference());
        }
        if (updateDto.getPrivacyLevel() != null) {
            profile.setPrivacyLevel(updateDto.getPrivacyLevel());
        }

        // 保存或更新用户扩展信息
        if (profile.getId() == null) {
            userProfileMapper.insert(profile);
            log.debug("创建用户扩展信息: {}", userId);
        } else {
            userProfileMapper.updateById(profile);
            log.debug("更新用户扩展信息: {}", userId);
        }

        // 记录活动日志
        userActivityService.logUserActivity(
                userId,
                UserActivityType.PROFILE_UPDATE,
                "用户更新个人资料",
                updateDto);

        log.info("用户资料更新成功: {}", userId);
        return getUserInfoById(userId);
    }

    /**
     * 更新用户头像
     *
     * @param userId    用户ID
     * @param avatarUrl 头像URL
     * @return 更新后的用户信息
     */
    @Transactional
    public UserInfoDto updateUserAvatar(Long userId, String avatarUrl) {
        log.debug("更新用户头像: {}, URL: {}", userId, avatarUrl);

        // 检查用户是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 获取或创建用户扩展信息
        UserProfile profile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                .eq(UserProfile::getUserId, userId));

        String oldAvatarUrl = null;
        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            profile.setAvatarUrl(avatarUrl);
            userProfileMapper.insert(profile);
            log.debug("创建用户扩展信息并设置头像: {}", userId);
        } else {
            oldAvatarUrl = profile.getAvatarUrl();
            profile.setAvatarUrl(avatarUrl);
            userProfileMapper.updateById(profile);
            log.debug("更新用户头像: {}", userId);
        }

        // 记录活动日志
        Map<String, Object> logDetails = new HashMap<>();
        logDetails.put("oldAvatarUrl", oldAvatarUrl);
        logDetails.put("newAvatarUrl", avatarUrl);

        userActivityService.logUserActivity(
                userId,
                UserActivityType.AVATAR_UPDATE,
                "用户更新头像",
                logDetails);

        log.info("用户头像更新成功: {}", userId);
        return getUserInfoById(userId);
    }

    /**
     * 获取用户统计信息
     *
     * @param userId 用户ID
     * @return 用户统计信息
     */
    public UserStatsDto getUserStats(Long userId) {
        log.debug("获取用户统计信息: {}", userId);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // TODO: 这里需要根据实际的业务表来计算统计数据
        // 目前返回默认值，后续集成博客、评论、点赞等模块后再实现

        // 获取关注和粉丝数据
        long followingCount = userFollowMapper.countFollowingsByFollower(userId, "active");
        long followersCount = userFollowMapper.countFollowersByFollowing(userId, "active");

        long daysSinceJoined = java.time.temporal.ChronoUnit.DAYS.between(
                user.getCreatedAt().toLocalDate(),
                java.time.LocalDate.now());

        // 简单的活跃度评分计算
        double activityScore = Math.min(100.0, (followersCount + followingCount) * 0.5);

        return UserStatsDto.builder()
                .userId(userId)
                .username(user.getUsername())
                .followersCount(followersCount)
                .followingCount(followingCount)
                .daysSinceJoined(daysSinceJoined)
                .activityScore(activityScore)
                .build();
    }

    /**
     * 检查用户名是否可用
     *
     * @param username 用户名
     * @return 是否可用
     */
    public boolean isUsernameAvailable(String username) {
        log.debug("检查用户名是否可用: {}", username);
        long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .ne(User::getStatus, "deleted"));
        return count == 0;
    }

    /**
     * 检查邮箱是否可用
     *
     * @param email 邮箱
     * @return 是否可用
     */
    public boolean isEmailAvailable(String email) {
        log.debug("检查邮箱是否可用: {}", email);
        long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getStatus, "deleted"));
        return count == 0;
    }

    /**
     * 根据邮箱获取用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    public Optional<UserInfoDto> getUserInfoByEmail(String email) {
        log.debug("根据邮箱获取用户信息: {}", email);

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getStatus, "deleted"));

        if (user != null) {
            UserProfile profile = userProfileMapper.selectOne(new LambdaQueryWrapper<UserProfile>()
                    .eq(UserProfile::getUserId, user.getId()));
            List<UserSettings> settings = userSettingsMapper.selectList(new LambdaQueryWrapper<UserSettings>()
                    .eq(UserSettings::getUserId, user.getId()));
            return Optional.of(UserInfoDto.from(user, profile, settings));
        }

        return Optional.empty();
    }

}
