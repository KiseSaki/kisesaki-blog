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
     * 发布“用户注册确认”邮件事件。
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
     * 发布“密码重置”邮件事件。
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
     * 发布“新评论通知”邮件事件。
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
     * 发布“系统通知”邮件事件。
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
     * 发布“营销活动”邮件事件。
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

    // ... 其他具体业务的发布方法 ...

    /**
     * 发布“欢迎新用户”邮件事件。
     * 通常在用户完成邮箱验证后，作为真正的欢迎邮件发送。
     */
    public void publishWelcomeEmailEvent(String toEmail, Long userId, String userName) {
        Map<String, Object> variables = Map.of(
                "userName", userName,
                "blogName", "KiseSaki博客",
                "blogUrl", "http://localhost:3000",
                "supportEmail", "support@kisesaki.com");

        publishCustomEmailEvent(EmailType.WELCOME, toEmail, userId, userName, variables,
                "欢迎加入KiseSaki博客！", "USER_WELCOME", userId.toString(), 3);
    }

    /**
     * 发布“密码已修改”安全通知邮件事件。
     */
    public void publishPasswordChangedEvent(String toEmail, Long userId, String userName,
                                            String changeTime, String ipAddress) {
        Map<String, Object> variables = Map.of(
                "userName", userName,
                "changeTime", changeTime,
                "ipAddress", ipAddress,
                "supportEmail", "support@kisesaki.com");

        publishCustomEmailEvent(EmailType.PASSWORD_CHANGED, toEmail, userId, userName, variables,
                "密码修改通知 - KiseSaki博客", "PASSWORD_CHANGED", userId.toString(), 1);
    }
}