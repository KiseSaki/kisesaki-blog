package com.kisesaki.blog.content.comment.service;

import java.time.OffsetDateTime;

import com.kisesaki.blog.content.comment.dto.interaction.UpdateCommentBody;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.comment.dto.interaction.CreateCommentBody;
import com.kisesaki.blog.content.comment.entity.Comments;
import com.kisesaki.blog.content.comment.mapper.CommentMapper;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentInteractionService {

    private final CommentMapper commentMapper;
    private final PostsMapper postsMapper;

    /**
     * 创建评论
     *
     * @param postId         文章ID
     * @param body           创建评论请求体
     * @param authentication 认证信息
     * @param request        HTTP请求对象
     * @return 创建的评论ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long createComment(Long postId, CreateCommentBody body, Authentication authentication,
                              HttpServletRequest request) {
        // 验证评论内容
        if (body.getContent() == null || body.getContent().trim().isEmpty()) {
            throw BusinessException.paramError("评论内容不能为空");
        }
        if (body.getContent().length() > 500) {
            throw BusinessException.paramError("评论内容不能超过500字符");
        }

        // 验证文章是否存在
        if (postsMapper.selectById(postId) == null) {
            throw BusinessException.notFound("文章不存在");
        }

        // 获取当前用户ID
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户认证失败");
        }

        Comments parentComment = null;
        Comments replyToComment = null;
        int level = 0;
        String path = "0";

        // 处理父评论
        if (body.getParentId() != null) {
            parentComment = commentMapper.selectById(body.getParentId());
            if (parentComment == null) {
                throw BusinessException.paramError("父评论不存在");
            }
            // 验证父评论是否属于同一篇文章
            if (!parentComment.getPostId().equals(postId)) {
                throw BusinessException.paramError("父评论与当前文章不匹配");
            }
            level = parentComment.getLevel() + 1;
            path = parentComment.getPath() + "." + parentComment.getId();
        }

        // 处理回复目标评论
        if (body.getReplyToId() != null) {
            replyToComment = commentMapper.selectById(body.getReplyToId());
            if (replyToComment == null) {
                throw BusinessException.paramError("回复目标评论不存在");
            }
            // 验证回复目标评论是否属于同一篇文章
            if (!replyToComment.getPostId().equals(postId)) {
                throw BusinessException.paramError("回复目标评论与当前文章不匹配");
            }
        }

        // 创建评论对象
        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(body.getParentId());
        comment.setReplyToId(body.getReplyToId());
        comment.setContent(body.getContent());
        comment.setLevel(level);
        comment.setPath(path);
        comment.setStatus(Comments.CommentStatus.APPROVED); // 默认通过审核，可根据需求调整

        // 设置IP地址和User-Agent
        String clientIp = getClientIpAddress(request);
        comment.setIpAddress(clientIp);
        comment.setUserAgent(request.getHeader("User-Agent"));

        log.debug("客户端IP: {}, User-Agent: {}", clientIp, request.getHeader("User-Agent"));

        // 设置创建时间（由MyBatis Plus自动填充，这里显式设置作为备用）
        comment.setCreatedAt(OffsetDateTime.now());
        comment.setUpdatedAt(OffsetDateTime.now());

        // 保存评论
        int result = commentMapper.insert(comment);
        if (result != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论保存失败");
        }

        log.info("用户 {} 在文章 {} 下创建了评论 {}", userId, postId, comment.getId());
        return comment.getId();
    }

    /**
     * 更新评论内容
     *
     * @param commentId      评论ID
     * @param body           更新评论请求体
     * @param authentication 认证信息
     * @param request        HTTP请求对象
     * @return 更新的评论ID
     */
    public Long updateComment(Long commentId, UpdateCommentBody body, Authentication authentication, HttpServletRequest request) {
        String newContent = body.getContent();
        // 验证评论内容
        if (newContent == null || newContent.trim().isEmpty()) {
            throw BusinessException.paramError("评论内容不能为空");
        }
        if (newContent.length() > 500) {
            throw BusinessException.paramError("评论内容不能超过500字符");
        }

        // 获取当前用户ID
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户认证失败");
        }

        // 查询评论
        Comments comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw BusinessException.notFound("评论不存在");
        }

        // 只允许修改十五分钟内的评论
        if (comment.getCreatedAt().isBefore(OffsetDateTime.now().minusMinutes(15))) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "评论创建超过15分钟，无法修改");
        }

        // 验证评论所有者
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "无权修改他人评论");
        }

        // 更新评论内容
        comment.setContent(newContent);
        comment.setUpdatedAt(OffsetDateTime.now());

        // 设置IP地址和User-Agent
        String clientIp = getClientIpAddress(request);
        comment.setIpAddress(clientIp);
        comment.setUserAgent(request.getHeader("User-Agent"));


        int result = commentMapper.updateById(comment);
        if (result != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论更新失败");
        }

        log.info("用户 {} 更新了评论 {}", userId, commentId);
        return comment.getId();
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] ipHeaders = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : ipHeaders) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // X-Forwarded-For 可能包含多个IP，取第一个
                if (ip.contains(",")) {
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        return request.getRemoteAddr();
    }
}
