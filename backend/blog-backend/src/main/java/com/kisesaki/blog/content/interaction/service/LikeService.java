package com.kisesaki.blog.content.interaction.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.comment.entity.Comments;
import com.kisesaki.blog.content.comment.mapper.CommentMapper;
import com.kisesaki.blog.content.interaction.entity.Likes;
import com.kisesaki.blog.content.interaction.mapper.LikeMapper;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 点赞反应业务服务类
 * 统一处理文章和评论的点赞/踩等反应功能
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final LikeMapper likeMapper;
    private final PostsMapper postsMapper;
    private final CommentMapper commentMapper;

    /**
     * 点赞文章
     *
     * @param postId         文章ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void likePost(Long postId, Authentication authentication) {
        handleReaction(Likes.TargetType.POST, postId, Likes.ReactionType.LIKE, authentication);
    }

    /**
     * 取消点赞文章
     *
     * @param postId         文章ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void unlikePost(Long postId, Authentication authentication) {
        removeReaction(Likes.TargetType.POST, postId, authentication);
    }

    /**
     * 点踩文章
     *
     * @param postId         文章ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void dislikePost(Long postId, Authentication authentication) {
        handleReaction(Likes.TargetType.POST, postId, Likes.ReactionType.DISLIKE, authentication);
    }

    /**
     * 取消点踩文章
     *
     * @param postId         文章ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void unDislikePost(Long postId, Authentication authentication) {
        removeReaction(Likes.TargetType.POST, postId, authentication);
    }

    /**
     * 点赞评论
     *
     * @param commentId      评论ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void likeComment(Long commentId, Authentication authentication) {
        handleReaction(Likes.TargetType.COMMENT, commentId, Likes.ReactionType.LIKE, authentication);
    }

    /**
     * 取消点赞评论
     *
     * @param commentId      评论ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void unlikeComment(Long commentId, Authentication authentication) {
        removeReaction(Likes.TargetType.COMMENT, commentId, authentication);
    }

    /**
     * 点踩评论
     *
     * @param commentId      评论ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void dislikeComment(Long commentId, Authentication authentication) {
        handleReaction(Likes.TargetType.COMMENT, commentId, Likes.ReactionType.DISLIKE, authentication);
    }

    /**
     * 取消点踩评论
     *
     * @param commentId      评论ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void unDislikeComment(Long commentId, Authentication authentication) {
        removeReaction(Likes.TargetType.COMMENT, commentId, authentication);
    }

    /**
     * 获取用户对指定目标的反应状态
     *
     * @param targetType     目标类型
     * @param targetId       目标ID
     * @param authentication 认证信息
     * @return 反应记录，如果不存在则返回null
     */
    public Likes getUserReaction(Likes.TargetType targetType, Long targetId, Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            return null;
        }

        return likeMapper.findUserReaction(userId, targetType, targetId);
    }

    /**
     * 批量获取用户对多个目标的反应状态
     *
     * @param targetType     目标类型
     * @param targetIds      目标ID列表
     * @param authentication 认证信息
     * @return 目标ID到反应记录的映射
     */
    public Map<Long, Likes> getUserReactionsByTargetIds(Likes.TargetType targetType, List<Long> targetIds,
            Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null || targetIds.isEmpty()) {
            return Map.of();
        }

        List<Likes> reactions = likeMapper.findUserReactionsByTargetIds(userId, targetType, targetIds);
        return reactions.stream()
                .collect(Collectors.toMap(Likes::getTargetId, reaction -> reaction));
    }

    /**
     * 统计指定目标的反应数量
     *
     * @param targetType   目标类型
     * @param targetId     目标ID
     * @param reactionType 反应类型
     * @return 反应数量
     */
    public int countReactionsByTarget(Likes.TargetType targetType, Long targetId, Likes.ReactionType reactionType) {
        return likeMapper.countReactionsByTarget(targetType, targetId, reactionType);
    }

    /**
     * 统计用户的点赞总数
     *
     * @param userId 用户ID
     * @return 点赞总数
     */
    public int countUserLikes(Long userId) {
        return likeMapper.countUserLikes(userId);
    }

    /**
     * 处理反应（点赞/踩）的核心逻辑
     */
    private void handleReaction(Likes.TargetType targetType, Long targetId, Likes.ReactionType reactionType,
            Authentication authentication) {
        // 验证目标是否存在
        validateTargetExists(targetType, targetId);

        // 获取用户ID
        Long userId = requireUserId(authentication);

        // 查询现有反应
        Likes existingReaction = likeMapper.findUserReaction(userId, targetType, targetId);

        if (existingReaction != null) {
            if (existingReaction.getReactionType() == reactionType) {
                // 如果已经是相同的反应类型，则抛出异常
                throw new BusinessException(ErrorCode.BUSINESS_ERROR,
                        reactionType == Likes.ReactionType.LIKE ? "您已经点赞过了" : "您已经点踩过了");
            } else {
                // 如果是不同的反应类型，则更新现有记录
                existingReaction.setReactionType(reactionType);
                existingReaction.setCreatedAt(OffsetDateTime.now()); // 更新时间
                likeMapper.updateById(existingReaction);
                log.info("用户 {} 更新了对 {} {} 的反应为 {}", userId, targetType, targetId, reactionType);
            }
        } else {
            // 创建新的反应记录
            Likes newReaction = new Likes();
            newReaction.setUserId(userId);
            newReaction.setTargetType(targetType);
            newReaction.setTargetId(targetId);
            newReaction.setReactionType(reactionType);
            newReaction.setCreatedAt(OffsetDateTime.now());

            likeMapper.insert(newReaction);
            log.info("用户 {} 对 {} {} 进行了 {} 反应", userId, targetType, targetId, reactionType);
        }

        // 更新目标对象的计数器
        updateTargetReactionCount(targetType, targetId);
    }

    /**
     * 移除反应
     */
    private void removeReaction(Likes.TargetType targetType, Long targetId, Authentication authentication) {
        // 验证目标是否存在
        validateTargetExists(targetType, targetId);

        // 获取用户ID
        Long userId = requireUserId(authentication);

        // 删除反应记录
        LambdaQueryWrapper<Likes> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Likes::getUserId, userId)
                .eq(Likes::getTargetType, targetType)
                .eq(Likes::getTargetId, targetId);

        int deletedCount = likeMapper.delete(wrapper);
        if (deletedCount == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "您还没有对此内容进行反应");
        }

        log.info("用户 {} 取消了对 {} {} 的反应", userId, targetType, targetId);

        // 更新目标对象的计数器
        updateTargetReactionCount(targetType, targetId);
    }

    /**
     * 验证目标是否存在
     */
    private void validateTargetExists(Likes.TargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case POST -> postsMapper.selectById(targetId) != null;
            case COMMENT -> commentMapper.selectById(targetId) != null;
        };

        if (!exists) {
            throw new BusinessException(ErrorCode.NOT_FOUND,
                    targetType == Likes.TargetType.POST ? "文章不存在" : "评论不存在");
        }
    }

    /**
     * 更新目标对象的反应计数器
     */
    private void updateTargetReactionCount(Likes.TargetType targetType, Long targetId) {
        int likeCount = likeMapper.countReactionsByTarget(targetType, targetId, Likes.ReactionType.LIKE);
        int dislikeCount = likeMapper.countReactionsByTarget(targetType, targetId, Likes.ReactionType.DISLIKE);

        switch (targetType) {
            case POST -> {
                LambdaUpdateWrapper<Posts> postWrapper = new LambdaUpdateWrapper<>();
                postWrapper.eq(Posts::getId, targetId)
                        .set(Posts::getLikeCount, likeCount);
                postsMapper.update(null, postWrapper);
            }
            case COMMENT -> {
                LambdaUpdateWrapper<Comments> commentWrapper = new LambdaUpdateWrapper<>();
                commentWrapper.eq(Comments::getId, targetId)
                        .set(Comments::getLikeCount, likeCount)
                        .set(Comments::getDislikeCount, dislikeCount);
                commentMapper.update(null, commentWrapper);
            }
        }

        log.debug("更新 {} {} 的反应计数: 点赞={}, 点踩={}", targetType, targetId, likeCount, dislikeCount);
    }

    /**
     * 从 Authentication 中获取用户ID，若为空则抛出未认证异常
     */
    private Long requireUserId(Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户未认证");
        }
        return userId;
    }
}