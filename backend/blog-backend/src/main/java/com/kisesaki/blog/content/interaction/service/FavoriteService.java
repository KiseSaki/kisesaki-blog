package com.kisesaki.blog.content.interaction.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.interaction.dto.favorite.FavoritePostResponse;
import com.kisesaki.blog.content.interaction.dto.favorite.FavoriteStatusResponse;
import com.kisesaki.blog.content.interaction.dto.favorite.FavoriteUserResponse;
import com.kisesaki.blog.content.interaction.entity.Favorites;
import com.kisesaki.blog.content.interaction.mapper.FavoriteMapper;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostsMapper;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 收藏服务
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final PostsMapper postsMapper;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    /**
     * 收藏文章
     *
     * @param postId         文章ID
     * @param authentication 认证信息
     * @return 收藏状态响应
     */
    @Transactional(rollbackFor = Exception.class)
    public FavoriteStatusResponse favoritePost(Long postId, Authentication authentication) {
        Long userId = requireUserId(authentication);

        // 验证文章是否存在
        validatePostExists(postId);

        // 检查是否已经收藏
        Favorites existingFavorite = favoriteMapper.selectOne(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId)
                        .eq(Favorites::getPostId, postId)
                        .last("LIMIT 1"));
        if (existingFavorite != null) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "您已经收藏过这篇文章");
        }

        // 创建收藏记录
        Favorites favorite = new Favorites();
        favorite.setUserId(userId);
        favorite.setPostId(postId);
        favorite.setCreatedAt(LocalDateTime.now());

        favoriteMapper.insert(favorite);
        log.info("用户 {} 收藏了文章 {}", userId, postId);

        // 更新文章收藏计数
        updatePostFavoriteCount(postId);

        // 返回收藏状态
        Long favoriteCount = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getPostId, postId));
        return new FavoriteStatusResponse(postId, true, favoriteCount.intValue());
    }

    /**
     * 取消收藏文章
     *
     * @param postId         文章ID
     * @param authentication 认证信息
     * @return 收藏状态响应
     */
    @Transactional(rollbackFor = Exception.class)
    public FavoriteStatusResponse unfavoritePost(Long postId, Authentication authentication) {
        Long userId = requireUserId(authentication);

        // 验证文章是否存在
        validatePostExists(postId);

        // 删除收藏记录
        int deletedCount = favoriteMapper.delete(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId)
                        .eq(Favorites::getPostId, postId));
        if (deletedCount == 0) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "您还未收藏这篇文章");
        }

        log.info("用户 {} 取消收藏了文章 {}", userId, postId);

        // 更新文章收藏计数
        updatePostFavoriteCount(postId);

        // 返回收藏状态
        Long favoriteCount = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getPostId, postId));
        return new FavoriteStatusResponse(postId, false, favoriteCount.intValue());
    }

    /**
     * 获取用户对指定文章的收藏状态
     *
     * @param postId         文章ID
     * @param authentication 认证信息（可为null，未登录用户）
     * @return 收藏状态响应
     */
    public FavoriteStatusResponse getFavoriteStatus(Long postId, Authentication authentication) {
        boolean favorited = false;

        if (authentication != null) {
            Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
            if (userId != null) {
                Favorites favorite = favoriteMapper.selectOne(
                        new LambdaQueryWrapper<Favorites>()
                                .eq(Favorites::getUserId, userId)
                                .eq(Favorites::getPostId, postId)
                                .last("LIMIT 1"));
                favorited = (favorite != null);
            }
        }

        Long favoriteCount = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getPostId, postId));
        return new FavoriteStatusResponse(postId, favorited, favoriteCount.intValue());
    }

    /**
     * 批量获取用户对多篇文章的收藏状态
     *
     * @param postIds        文章ID列表
     * @param authentication 认证信息（可为null）
     * @return 文章ID到收藏状态的映射
     */
    public Map<Long, Boolean> getFavoriteStatusByPostIds(List<Long> postIds, Authentication authentication) {
        if (authentication == null || postIds.isEmpty()) {
            return postIds.stream().collect(Collectors.toMap(id -> id, id -> false));
        }

        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return postIds.stream().collect(Collectors.toMap(id -> id, id -> false));
        }

        List<Favorites> favorites = favoriteMapper.findUserFavoritesByPostIds(userId, postIds);
        Map<Long, Boolean> favoriteMap = favorites.stream()
                .collect(Collectors.toMap(Favorites::getPostId, f -> true));

        // 确保所有文章ID都有对应的状态
        return postIds.stream().collect(Collectors.toMap(id -> id, favoriteMap::containsKey));
    }

    /**
     * 获取文章的收藏用户列表
     *
     * @param postId 文章ID
     * @param page   页码
     * @param size   每页大小
     * @return 收藏用户分页列表
     */
    public Page<FavoriteUserResponse> getPostFavoriteUsers(Long postId, int page, int size) {
        validatePostExists(postId);

        Page<Favorites> favoritePage = new Page<>(page, size);
        Page<Favorites> favoriteResult = favoriteMapper.selectPage(favoritePage,
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getPostId, postId)
                        .orderByDesc(Favorites::getCreatedAt));

        Page<FavoriteUserResponse> resultPage = new Page<>(page, size);
        resultPage.setTotal(favoriteResult.getTotal());

        if (!favoriteResult.getRecords().isEmpty()) {
            List<Long> userIds = favoriteResult.getRecords().stream()
                    .map(Favorites::getUserId)
                    .collect(Collectors.toList());

            List<User> users = userMapper.selectBatchIds(userIds);
            Map<Long, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getId, u -> u));

            // 获取用户档案信息
            List<UserProfile> userProfiles = userProfileMapper.selectList(
                    new LambdaQueryWrapper<UserProfile>()
                            .in(UserProfile::getUserId, userIds));
            Map<Long, UserProfile> profileMap = userProfiles.stream()
                    .collect(Collectors.toMap(UserProfile::getUserId, p -> p));

            List<FavoriteUserResponse> userResponses = favoriteResult.getRecords().stream()
                    .map(favorite -> {
                        User user = userMap.get(favorite.getUserId());
                        UserProfile profile = profileMap.get(favorite.getUserId());
                        if (user != null) {
                            return new FavoriteUserResponse(
                                    user.getId(),
                                    user.getUsername(),
                                    profile != null ? profile.getDisplayName() : user.getUsername(),
                                    profile != null ? profile.getAvatarUrl() : null,
                                    favorite.getCreatedAt());
                        }
                        return null;
                    })
                    .filter(response -> response != null)
                    .collect(Collectors.toList());

            resultPage.setRecords(userResponses);
        }

        return resultPage;
    }

    /**
     * 获取用户的收藏列表
     *
     * @param userId 用户ID
     * @param page   页码
     * @param size   每页大小
     * @return 收藏文章分页列表
     */
    public Page<FavoritePostResponse> getUserFavorites(Long userId, int page, int size) {
        validateUserExists(userId);

        Page<Favorites> favoritePage = new Page<>(page, size);
        Page<Favorites> favoriteResult = favoriteMapper.selectPage(favoritePage,
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId)
                        .orderByDesc(Favorites::getCreatedAt));

        Page<FavoritePostResponse> resultPage = new Page<>(page, size);
        resultPage.setTotal(favoriteResult.getTotal());

        if (!favoriteResult.getRecords().isEmpty()) {
            List<Long> postIds = favoriteResult.getRecords().stream()
                    .map(Favorites::getPostId)
                    .collect(Collectors.toList());

            List<Posts> posts = postsMapper.selectBatchIds(postIds);
            Map<Long, Posts> postMap = posts.stream()
                    .collect(Collectors.toMap(Posts::getId, p -> p));

            // 获取作者信息
            List<Long> authorIds = posts.stream()
                    .map(Posts::getAuthorId)
                    .distinct()
                    .collect(Collectors.toList());
            List<User> authors = userMapper.selectBatchIds(authorIds);
            Map<Long, User> authorMap = authors.stream()
                    .collect(Collectors.toMap(User::getId, a -> a));

            // 获取作者档案信息
            List<UserProfile> authorProfiles = userProfileMapper.selectList(
                    new LambdaQueryWrapper<UserProfile>()
                            .in(UserProfile::getUserId, authorIds));
            Map<Long, UserProfile> authorProfileMap = authorProfiles.stream()
                    .collect(Collectors.toMap(UserProfile::getUserId, p -> p));

            List<FavoritePostResponse> postResponses = favoriteResult.getRecords().stream()
                    .map(favorite -> {
                        Posts post = postMap.get(favorite.getPostId());
                        if (post != null) {
                            User author = authorMap.get(post.getAuthorId());
                            UserProfile authorProfile = authorProfileMap.get(post.getAuthorId());
                            String authorDisplayName = "未知作者";
                            if (author != null) {
                                authorDisplayName = authorProfile != null && authorProfile.getDisplayName() != null
                                        ? authorProfile.getDisplayName()
                                        : author.getUsername();
                            }
                            return new FavoritePostResponse(
                                    post.getId(),
                                    post.getTitle(),
                                    post.getExcerpt(),
                                    post.getCoverImageUrl(),
                                    authorDisplayName,
                                    post.getPublishedAt(),
                                    favorite.getCreatedAt(),
                                    post.getReadingTime(),
                                    post.getViewCount(),
                                    post.getLikeCount());
                        }
                        return null;
                    })
                    .filter(response -> response != null)
                    .collect(Collectors.toList());

            resultPage.setRecords(postResponses);
        }

        return resultPage;
    }

    /**
     * 获取当前用户的收藏列表
     *
     * @param authentication 认证信息
     * @param page           页码
     * @param size           每页大小
     * @return 收藏文章分页列表
     */
    public Page<FavoritePostResponse> getCurrentUserFavorites(Authentication authentication, int page, int size) {
        Long userId = requireUserId(authentication);
        return getUserFavorites(userId, page, size);
    }

    /**
     * 统计用户的收藏总数
     *
     * @param userId 用户ID
     * @return 收藏总数
     */
    public int countUserFavorites(Long userId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId));
        return count.intValue();
    }

    /**
     * 验证文章是否存在
     */
    private void validatePostExists(Long postId) {
        Posts post = postsMapper.selectById(postId);
        if (post == null) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "文章不存在");
        }
    }

    /**
     * 验证用户是否存在
     */
    private void validateUserExists(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "用户不存在");
        }
    }

    /**
     * 更新文章收藏计数
     */
    private void updatePostFavoriteCount(Long postId) {
        Long favoriteCount = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getPostId, postId));

        LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Posts::getId, postId)
                .set(Posts::getFavoriteCount, favoriteCount.intValue());

        postsMapper.update(null, updateWrapper);
    }

    /**
     * 从 Authentication 中获取用户ID，若为空则抛出未认证异常
     */
    private Long requireUserId(Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        return userId;
    }

    // ============= Lambda Wrapper 便利方法 =============

    /**
     * 查找用户对指定文章的收藏记录
     */
    private Favorites findUserFavorite(Long userId, Long postId) {
        return favoriteMapper.selectOne(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId)
                        .eq(Favorites::getPostId, postId)
                        .last("LIMIT 1"));
    }

    /**
     * 统计文章的收藏数量
     */
    private Long countFavoritesByPost(Long postId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getPostId, postId));
    }

    /**
     * 删除用户对指定文章的收藏
     */
    private int deleteFavorite(Long userId, Long postId) {
        return favoriteMapper.delete(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId)
                        .eq(Favorites::getPostId, postId));
    }

    /**
     * 检查用户是否收藏了指定文章
     */
    private boolean isFavorited(Long userId, Long postId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorites>()
                        .eq(Favorites::getUserId, userId)
                        .eq(Favorites::getPostId, postId));
        return count > 0;
    }
}
