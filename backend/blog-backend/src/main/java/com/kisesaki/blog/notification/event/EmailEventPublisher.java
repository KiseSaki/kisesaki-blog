package com.kisesaki.blog.notification.event;

import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.kisesaki.blog.notification.enums.EmailType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件事件发布器
 * <p>
 * 该类是系统中发布邮件事件的统一入口。它封装了 {@link EmailEvent} 的创建细节，
 * 并通过 Spring 的 {@link ApplicationEventPublisher} 将事件发布出去。
 * <p>
 * 业务逻辑层（如 UserService, CommentService）不应直接创建 EmailEvent，
 * 而应注入本类，并调用其提供的业务方法（如 publishUserRegistrationEvent）来触发邮件发送，
 * 以实现业务逻辑与通知模块的解耦。
 *
 * @author KiseSaki
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventPublisher {

    /**
     * Spring 框架提供的标准事件发布接口。
     * 所有的事件最终都通过调用这个接口的 publishEvent 方法来广播给应用中的所有监听器。
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 核心的私有方法，用于发布一个已构建好的邮件事件。
     *
     * @param event 待发布的邮件事件对象
     */
    public void publishEmailEvent(EmailEvent event) {
        try {
            log.debug("准备发布邮件事件: {}", event.getEventDescription());
            eventPublisher.publishEvent(event);
            log.debug("邮件事件发布成功: {}", event.getEventDescription());
        } catch (Exception e) {
            log.error("邮件事件发布失败: {} - {}", event.getEventDescription(), e.getMessage(), e);
        }
    }

    /**
     * 发布"用户注册确认"邮件事件。
     *
     * @param toEmail      接收者邮箱
     * @param userId       用户ID
     * @param userName     用户名
     * @param confirmToken 确认令牌
     */
    public void publishUserRegistrationEvent(String toEmail, Long userId, String userName, String confirmToken) {
        EmailEvent event = EmailEvent.createUserRegistrationEvent(this, toEmail, userId, userName, confirmToken);
        publishEmailEvent(event);
    }

    /**
     * 发布"密码重置"邮件事件。
     *
     * @param toEmail    接收者邮箱
     * @param userId     用户ID
     * @param userName   用户名
     * @param resetToken 重置令牌
     */
    public void publishPasswordResetEvent(String toEmail, Long userId, String userName, String resetToken) {
        EmailEvent event = EmailEvent.createPasswordResetEvent(this, toEmail, userId, userName, resetToken);
        publishEmailEvent(event);
    }

    /**
     * 发布"新评论通知"邮件事件。
     *
     * @param toEmail        接收者邮箱
     * @param userId         文章作者的用户ID
     * @param userName       文章作者的用户名
     * @param postTitle      文章标题
     * @param commenterName  评论者名称
     * @param commentContent 评论内容
     */
    public void publishCommentNotificationEvent(String toEmail, Long userId, String userName,
            String postTitle, String commenterName, String commentContent) {
        EmailEvent event = EmailEvent.createCommentNotificationEvent(this, toEmail, userId, userName,
                postTitle, commenterName, commentContent);
        publishEmailEvent(event);
    }

    /**
     * 发布"系统通知"邮件事件。
     *
     * @param toEmail             接收者邮箱
     * @param userId              用户ID
     * @param userName            用户名
     * @param notificationTitle   通知标题
     * @param notificationContent 通知内容
     */
    public void publishSystemNotificationEvent(String toEmail, Long userId, String userName,
            String notificationTitle, String notificationContent) {
        EmailEvent event = EmailEvent.createSystemNotificationEvent(this, toEmail, userId, userName,
                notificationTitle, notificationContent);
        publishEmailEvent(event);
    }

    /**
     * 发布"营销活动"邮件事件。
     *
     * @param toEmail           接收者邮箱
     * @param userId            用户ID
     * @param userName          用户名
     * @param campaignName      活动名称
     * @param campaignVariables 活动相关的模板变量
     */
    public void publishMarketingEvent(String toEmail, Long userId, String userName,
            String campaignName, Map<String, Object> campaignVariables) {
        EmailEvent event = EmailEvent.createMarketingEvent(this, toEmail, userId, userName,
                campaignName, campaignVariables);
        publishEmailEvent(event);
    }

    /**
     * 发布一个完全自定义的邮件事件。
     * 用于处理没有预定义模板的、临时的或特殊的邮件发送需求。
     *
     * @param emailType         邮件类型
     * @param toEmail           接收者邮箱
     * @param userId            用户ID
     * @param userName          用户名
     * @param templateVariables 模板变量
     * @param subject           邮件主题
     * @param businessType      业务类型 (用于日志追踪)
     * @param businessId        业务ID (用于日志追踪)
     * @param priority          优先级
     */
    public void publishCustomEmailEvent(EmailType emailType, String toEmail, Long userId, String userName,
            Map<String, Object> templateVariables, String subject,
            String businessType, String businessId, int priority) {
        EmailEvent event = new EmailEvent(this, emailType, toEmail, userId, userName,
                templateVariables, subject, businessType, businessId, priority);
        publishEmailEvent(event);
    }

    // ================ 认证相关邮件 ================

    /**
     * 发布"欢迎新用户"邮件事件。
     * 通常在用户完成邮箱验证后，作为真正的欢迎邮件发送。
     *
     * @param toEmail  接收者邮箱
     * @param userId   用户ID
     * @param userName 用户名
     */
    public void publishWelcomeEmailEvent(String toEmail, Long userId, String userName) {
        EmailEvent event = EmailEvent.createWelcomeEvent(this, toEmail, userId, userName);
        publishEmailEvent(event);
    }

    /**
     * 发布"密码已修改"安全通知邮件事件。
     *
     * @param toEmail    接收者邮箱
     * @param userId     用户ID
     * @param userName   用户名
     * @param changeTime 修改时间
     * @param ipAddress  操作IP地址
     */
    public void publishPasswordChangedEvent(String toEmail, Long userId, String userName,
            String changeTime, String ipAddress) {
        EmailEvent event = EmailEvent.createPasswordChangedEvent(this, toEmail, userId, userName, changeTime,
                ipAddress);
        publishEmailEvent(event);
    }

    // ================ 通知相关邮件 ================

    /**
     * 发布"文章发布通知"邮件事件。
     *
     * @param toEmail     接收者邮箱
     * @param userId      订阅者用户ID
     * @param userName    订阅者用户名
     * @param postTitle   文章标题
     * @param postSummary 文章摘要
     * @param authorName  作者名称
     * @param postId      文章ID
     */
    public void publishPostPublishedEvent(String toEmail, Long userId, String userName,
            String postTitle, String postSummary, String authorName, Long postId) {
        EmailEvent event = EmailEvent.createPostPublishedEvent(this, toEmail, userId, userName,
                postTitle, postSummary, authorName, postId);
        publishEmailEvent(event);
    }

    /**
     * 发布"关注通知"邮件事件。
     *
     * @param toEmail      被关注者邮箱
     * @param userId       被关注用户ID
     * @param userName     被关注用户名
     * @param followerName 关注者名称
     * @param followerId   关注者ID
     */
    public void publishFollowNotificationEvent(String toEmail, Long userId, String userName,
            String followerName, Long followerId) {
        EmailEvent event = EmailEvent.createFollowNotificationEvent(this, toEmail, userId, userName,
                followerName, followerId);
        publishEmailEvent(event);
    }

    /**
     * 发布"周报摘要"邮件事件。
     *
     * @param toEmail       接收者邮箱
     * @param userId        用户ID
     * @param userName      用户名
     * @param weekStartDate 周开始日期
     * @param weekEndDate   周结束日期
     * @param weeklyContent 周报内容
     */
    public void publishWeeklyDigestEvent(String toEmail, Long userId, String userName,
            String weekStartDate, String weekEndDate, Map<String, Object> weeklyContent) {
        EmailEvent event = EmailEvent.createWeeklyDigestEvent(this, toEmail, userId, userName,
                weekStartDate, weekEndDate, weeklyContent);
        publishEmailEvent(event);
    }

    // ================ 管理员邮件 ================

    /**
     * 发布"新用户注册管理员通知"邮件事件。
     *
     * @param adminEmail       管理员邮箱
     * @param adminId          管理员ID
     * @param adminName        管理员名称
     * @param newUserName      新用户名称
     * @param newUserEmail     新用户邮箱
     * @param newUserId        新用户ID
     * @param registrationTime 注册时间
     */
    public void publishUserRegisteredEvent(String adminEmail, Long adminId, String adminName,
            String newUserName, String newUserEmail, Long newUserId,
            String registrationTime) {
        EmailEvent event = EmailEvent.createUserRegisteredEvent(this, adminEmail, adminId, adminName,
                newUserName, newUserEmail, newUserId, registrationTime);
        publishEmailEvent(event);
    }

    /**
     * 发布"内容审核"邮件事件。
     *
     * @param moderatorEmail 审核员邮箱
     * @param moderatorId    审核员ID
     * @param moderatorName  审核员名称
     * @param contentType    内容类型
     * @param contentTitle   内容标题
     * @param contentId      内容ID
     * @param authorName     内容作者
     * @param reportReason   举报原因
     */
    public void publishContentModerationEvent(String moderatorEmail, Long moderatorId, String moderatorName,
            String contentType, String contentTitle, Long contentId,
            String authorName, String reportReason) {
        EmailEvent event = EmailEvent.createContentModerationEvent(this, moderatorEmail, moderatorId, moderatorName,
                contentType, contentTitle, contentId, authorName, reportReason);
        publishEmailEvent(event);
    }

    // ================ 营销邮件 ================

    /**
     * 发布"博客周报"邮件事件。
     * 这是一个特殊的营销邮件，用于发送定期的博客内容摘要。
     *
     * @param toEmail       接收者邮箱
     * @param userId        用户ID
     * @param userName      用户名
     * @param weeklyContent 周报内容（热门文章、统计数据等）
     */
    public void publishNewsletterEvent(String toEmail, Long userId, String userName,
            Map<String, Object> weeklyContent) {
        publishCustomEmailEvent(EmailType.NEWSLETTER, toEmail, userId, userName, weeklyContent,
                "KiseSaki博客周报", "NEWSLETTER", "weekly-" + System.currentTimeMillis(), 5);
    }

    /**
     * 发布"功能发布公告"邮件事件。
     *
     * @param toEmail            接收者邮箱
     * @param userId             用户ID
     * @param userName           用户名
     * @param featureName        功能名称
     * @param featureDescription 功能描述
     * @param releaseVersion     发布版本
     * @param featureDetails     功能详情
     */
    public void publishFeatureAnnouncementEvent(String toEmail, Long userId, String userName,
            String featureName, String featureDescription,
            String releaseVersion, Map<String, Object> featureDetails) {
        EmailEvent event = EmailEvent.createFeatureAnnouncementEvent(this, toEmail, userId, userName,
                featureName, featureDescription, releaseVersion, featureDetails);
        publishEmailEvent(event);
    }
}