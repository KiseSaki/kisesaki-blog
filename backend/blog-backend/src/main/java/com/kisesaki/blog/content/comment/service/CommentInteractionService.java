package com.kisesaki.blog.content.comment.service;

import java.time.OffsetDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.content.comment.dto.interaction.CreateCommentBody;
import com.kisesaki.blog.content.comment.dto.interaction.ReportCommentBody;
import com.kisesaki.blog.content.comment.dto.interaction.UpdateCommentBody;
import com.kisesaki.blog.content.comment.entity.CommentReports;
import com.kisesaki.blog.content.comment.entity.Comments;
import com.kisesaki.blog.content.comment.mapper.CommentMapper;
import com.kisesaki.blog.content.comment.mapper.CommentReportMapper;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentInteractionService {

    private final CommentMapper commentMapper;
    private final CommentReportMapper commentReportMapper;
    private final PostsMapper postsMapper;

    /**
     * 创建评论（两层结构：顶级评论 + 二级回复）
     * <p>
     * 场景1：创建顶级评论 - replyToId=null
     * 场景2：回复顶级评论 - replyToId=顶级评论ID (level=0)
     * 场景3：回复二级评论（@功能） - replyToId=二级评论ID (level=1)
     * <p>
     * 后端会根据 replyToId 指向的评论层级自动判断并设置正确的 parentId 和 replyToId
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

        // 初始化评论属性
        Long parentId = null;
        Long actualReplyToId = null;
        int level = 0;
        String path = "0";

        // 场景1：创建顶级评论
        if (body.getReplyToId() == null) {
            // parentId = null, actualReplyToId = null, level = 0, path = "0"
            log.debug("创建顶级评论，postId={}", postId);
        }
        // 场景2和3：创建二级回复（需要根据目标评论的层级自动判断）
        else {
            Comments targetComment = commentMapper.selectById(body.getReplyToId());
            if (targetComment == null) {
                throw BusinessException.paramError("回复目标评论不存在");
            }
            
            // 验证目标评论是否属于同一篇文章
            if (!targetComment.getPostId().equals(postId)) {
                throw BusinessException.paramError("回复目标评论与当前文章不匹配");
            }
            
            // 场景2：目标是顶级评论 → 创建该顶级评论的二级回复
            if (targetComment.getLevel() == 0) {
                parentId = targetComment.getId();
                actualReplyToId = null; // 直接回复顶级评论，不需要@
                level = 1;
                path = "0." + targetComment.getId();
                log.debug("回复顶级评论 {}，创建二级回复", targetComment.getId());
            }
            // 场景3：目标是二级回复 → 创建同一父评论下的二级回复（@功能）
            else if (targetComment.getLevel() == 1) {
                if (targetComment.getParentId() == null) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "二级评论缺少父评论ID");
                }
                parentId = targetComment.getParentId();
                actualReplyToId = targetComment.getId(); // 设置@目标
                level = 1;
                path = "0." + targetComment.getParentId();
                log.debug("回复二级评论 {}，创建同级回复并@该用户", targetComment.getId());
            }
            // 不支持的层级
            else {
                throw BusinessException.paramError("不支持回复三层及以上的评论");
            }
        }

        // 创建评论对象
        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setReplyToId(actualReplyToId);
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
        // 如果评论已通过审核，则增加文章的评论计数（原子操作）
        if (comment.getStatus() == Comments.CommentStatus.APPROVED) {
            LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Posts::getId, postId)
                    .setSql("comment_count = comment_count + 1");
            int updateResult = postsMapper.update(null, updateWrapper);
            if (updateResult != 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新文章评论数失败");
            }
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
    public Long updateComment(Long commentId, UpdateCommentBody body, Authentication authentication,
            HttpServletRequest request) {
        String newContent = body.getContent();
        // 验证评论内容
        if (newContent == null || newContent.trim().isEmpty()) {
            throw BusinessException.paramError("评论内容不能为空");
        }
        if (newContent.length() > 500) {
            throw BusinessException.paramError("评论内容不能超过500字符");
        }

        // 获取当前用户ID
        Long userId = requireUserId(authentication);

        // 查询评论
        Comments comment = requireCommentExists(commentId);

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
     * 删除评论（逻辑删除）
     *
     * @param commentId      评论ID
     * @param authentication 认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Authentication authentication) {
        // 获取当前用户ID
        Long userId = requireUserId(authentication);

        // 查询评论
        Comments comment = requireCommentExists(commentId);

        // 验证评论所有者
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "无权删除他人评论");
        }

        // 逻辑删除评论
        int result = commentMapper.deleteById(commentId);
        if (result != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评论删除失败");
        }

        // 若被删除的评论是已审核状态，则需要将文章的评论数减1
        if (comment.getStatus() == Comments.CommentStatus.APPROVED) {
            LambdaUpdateWrapper<Posts> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Posts::getId, comment.getPostId())
                    .setSql("comment_count = comment_count - 1");
            int updateResult = postsMapper.update(null, updateWrapper);
            if (updateResult != 1) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新文章评论数失败");
            }
        }

        log.info("用户 {} 删除了评论 {}", userId, commentId);
    }

    /**
     * 举报评论
     *
     * @param commentId      评论ID
     * @param body           举报请求体
     * @param authentication 认证信息
     * @param request        HTTP请求对象
     */
    public void reportComment(Long commentId, ReportCommentBody body, Authentication authentication,
            HttpServletRequest request) {
        // 获取当前用户ID
        Long userId = requireUserId(authentication);

        // 判断评论是否存在
        requireCommentExists(commentId);

        // 检查是否已经举报过该评论
        LambdaQueryWrapper<CommentReports> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommentReports::getCommentId, commentId)
                .eq(CommentReports::getReporterId, userId);
        CommentReports existingReport = commentReportMapper.selectOne(queryWrapper);
        if (existingReport != null) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "您已举报过该评论");
        }

        // 创建举报记录
        CommentReports report = new CommentReports();
        report.setCommentId(commentId);
        report.setReporterId(userId);
        report.setReason(body.getReason());
        report.setDescription(body.getDescription());
        report.setStatus(CommentReports.ReportStatus.PENDING);
        report.setIpAddress(getClientIpAddress(request));
        report.setCreatedAt(OffsetDateTime.now());
        report.setUpdatedAt(OffsetDateTime.now());

        int result = commentReportMapper.insert(report);
        if (result != 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "举报提交失败");
        }

        log.info("用户 {} 举报了评论 {}，原因：{}", userId, commentId, body.getReason());
    }

    /**
     * 从 Authentication 中获取用户ID，若为空则抛出未认证异常
     */
    private Long requireUserId(Authentication authentication) {
        Long userId = AuthUtils.getUserIdFromAuthentication(authentication);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户认证失败");
        }
        return userId;
    }

    /**
     * 根据 ID 查询评论，若不存在则抛出 404
     */
    private Comments requireCommentExists(Long commentId) {
        Comments comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw BusinessException.notFound("评论");
        }
        return comment;
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
