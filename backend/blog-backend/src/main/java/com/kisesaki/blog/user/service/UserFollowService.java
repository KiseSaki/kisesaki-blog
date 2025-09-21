package com.kisesaki.blog.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kisesaki.blog.user.dto.follow.UserFollowDto;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserFollow;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.mapper.UserFollowMapper;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户关注服务
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserFollowService {

    private final UserFollowMapper userFollowMapper;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    private static final String FOLLOW_STATUS_ACTIVE = "active";

    /**
     * 关注用户
     * 
     * @param followerId  关注者ID
     * @param followingId 被关注者ID
     * @return 是否关注成功
     */
    @Transactional
    public boolean followUser(Long followerId, Long followingId) {
        log.debug("用户 {} 关注用户 {}", followerId, followingId);

        // 检查是否为同一用户
        if (followerId.equals(followingId)) {
            log.warn("用户不能关注自己: {}", followerId);
            return false;
        }

        // 检查被关注者是否存在
        if (!userMapper.findById(followingId).isPresent()) {
            log.warn("被关注的用户不存在: {}", followingId);
            return false;
        }

        // 检查是否已经关注
        if (userFollowMapper.isFollowing(followerId, followingId, FOLLOW_STATUS_ACTIVE)) {
            log.debug("用户 {} 已经关注了用户 {}", followerId, followingId);
            return false;
        }

        // 创建关注关系
        UserFollow userFollow = new UserFollow();
        userFollow.setFollowerId(followerId);
        userFollow.setFollowingId(followingId);
        userFollow.setStatus(FOLLOW_STATUS_ACTIVE);

        int inserted = userFollowMapper.insert(userFollow);
        boolean success = inserted > 0;

        if (success) {
            log.info("用户 {} 成功关注用户 {}", followerId, followingId);
        } else {
            log.error("用户 {} 关注用户 {} 失败", followerId, followingId);
        }

        return success;
    }

    /**
     * 取消关注用户
     * 
     * @param followerId  关注者ID
     * @param followingId 被关注者ID
     * @return 是否取消关注成功
     */
    @Transactional
    public boolean unfollowUser(Long followerId, Long followingId) {
        log.debug("用户 {} 取消关注用户 {}", followerId, followingId);

        int deleted = userFollowMapper.deleteByFollowerAndFollowing(followerId, followingId);
        boolean success = deleted > 0;

        if (success) {
            log.info("用户 {} 成功取消关注用户 {}", followerId, followingId);
        } else {
            log.debug("用户 {} 取消关注用户 {} - 关注关系不存在", followerId, followingId);
        }

        return success;
    }

    /**
     * 检查是否已关注
     * 
     * @param followerId  关注者ID
     * @param followingId 被关注者ID
     * @return 是否已关注
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        return userFollowMapper.isFollowing(followerId, followingId, FOLLOW_STATUS_ACTIVE);
    }

    /**
     * 获取用户关注的人列表
     * 
     * @param userId 用户ID
     * @return 关注的用户列表
     */
    public List<UserFollowDto> getFollowingList(Long userId) {
        log.debug("获取用户 {} 关注的人列表", userId);

        List<UserFollow> follows = userFollowMapper.findFollowingsByFollower(userId, FOLLOW_STATUS_ACTIVE);

        return follows.stream()
                .map(follow -> buildUserFollowDto(follow, follow.getFollowingId(), userId))
                .collect(Collectors.toList());
    }

    /**
     * 获取关注某用户的人列表
     * 
     * @param userId 用户ID
     * @return 关注者列表
     */
    public List<UserFollowDto> getFollowersList(Long userId) {
        log.debug("获取关注用户 {} 的人列表", userId);

        List<UserFollow> follows = userFollowMapper.findFollowersByFollowing(userId, FOLLOW_STATUS_ACTIVE);

        return follows.stream()
                .map(follow -> buildUserFollowDto(follow, follow.getFollowerId(), userId))
                .collect(Collectors.toList());
    }

    /**
     * 获取用户关注数量
     * 
     * @param userId 用户ID
     * @return 关注数量
     */
    public long getFollowingCount(Long userId) {
        return userFollowMapper.countFollowingsByFollower(userId, FOLLOW_STATUS_ACTIVE);
    }

    /**
     * 获取用户粉丝数量
     * 
     * @param userId 用户ID
     * @return 粉丝数量
     */
    public long getFollowersCount(Long userId) {
        return userFollowMapper.countFollowersByFollowing(userId, FOLLOW_STATUS_ACTIVE);
    }

    /**
     * 构建用户关注信息DTO
     * 
     * @param follow        关注关系
     * @param targetUserId  目标用户ID
     * @param currentUserId 当前用户ID
     * @return UserFollowDto
     */
    private UserFollowDto buildUserFollowDto(UserFollow follow, Long targetUserId, Long currentUserId) {
        User user = userMapper.findById(targetUserId).orElse(null);
        if (user == null) {
            return null;
        }

        UserProfile profile = userProfileMapper.findByUserId(targetUserId).orElse(null);

        // 检查是否互相关注
        boolean isMultualFollow = false;
        if (!currentUserId.equals(targetUserId)) {
            isMultualFollow = userFollowMapper.isFollowing(targetUserId, currentUserId, FOLLOW_STATUS_ACTIVE);
        }

        return UserFollowDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .displayName(profile != null ? profile.getDisplayName() : null)
                .avatarUrl(profile != null ? profile.getAvatarUrl() : null)
                .bio(profile != null ? profile.getBio() : null)
                .status(follow.getStatus())
                .followedAt(follow.getCreatedAt())
                .isMultualFollow(isMultualFollow)
                .build();
    }
}
