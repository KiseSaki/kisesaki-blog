package com.kisesaki.blog.notification.event;

import org.springframework.context.ApplicationEvent;

import com.kisesaki.blog.notification.enums.EmailType;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 邮件事件基类
 *
 * @author KiseSaki
 */
@Data
@EqualsAndHashCode(callSuper = false) // 仅比较当前类的属性，不考虑父类
public class EmailEvent extends ApplicationEvent {

    // 邮件类型
    private EmailType emailType;
    // 模板变量
    private Map<String, Object> templateVariables;

    // 接收人邮箱
    private String toEmail;
    // 接收人邮箱ID
    private Long userId;
    // 接收人昵称
    private String userDisplayName;
    // 邮件主题
    private String subject;

    // 触发时间
    private LocalDateTime triggerTime;
    // 是否异步发送
    private boolean async;
    // 重试次数
    private int retryCount;
    // 优先级 (1-5, 1最高)
    private int priority;

    /**
     * 日志用
     * 比如在密码重置场景下，代码里将 businessId 设置为 userId.toString()。
     * 你只需要拿到用户的ID（比如 123），就可以在日志系统中精确搜索 businessType="PASSWORD_RESET", businessId="123"。
     * 这样能立刻筛选出所有与该用户密码重置相关的邮件事件日志，快速定位问题是“事件没发布”、“发送失败”还是“被邮件服务商拦截”。
     */
    // 业务标识符
    private String businessId;
    // 业务类型
    private String businessType;

    /**
     * 基础构造函数
     *
     * @param source source 事件源对象，通常是发布事件的服务实例 (`this`)。
     */
    public EmailEvent(Object source) {
        super(source);
        this.triggerTime = LocalDateTime.now();
        this.async = true; // 默认异步发送
        this.retryCount = 0; // 默认不重试
        this.priority = 3; // 默认优先级为中等
    }

    /**
     * 全参数构造函数，用于创建高度自定义的邮件事件。
     */
    public EmailEvent(Object source, EmailType emailType, String toEmail, Long userId, String userDisplayName,
                      Map<String, Object> templateVariables, String subject, String businessType,
                      String businessId, int priority) {
        super(source);
        this.emailType = emailType;
        this.toEmail = toEmail;
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.templateVariables = templateVariables;
        this.subject = subject;
        this.businessType = businessType;
        this.businessId = businessId;
        this.priority = priority;
        this.triggerTime = LocalDateTime.now();
        this.async = true; // 默认异步
        this.retryCount = 0;
    }

    /**
     * 静态工厂方法：创建用户注册确认邮件事件。
     * 封装了创建特定事件的复杂逻辑，对调用者暴露简洁的接口。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param confirmToken    确认令牌
     * @return 配置好的EmailEvent实例
     */
    public static EmailEvent createUserRegistrationEvent(Object source, String toEmail, Long userId,
                                                         String userDisplayName, String confirmToken) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "confirmToken", confirmToken,
                "confirmUrl",
                "http://localhost:3000/auth/confirm?token=" + confirmToken);

        return new EmailEvent(source, EmailType.EMAIL_VERIFICATION, toEmail, userId, userDisplayName,
                variables, "欢迎注册KiseSaki博客 - 请确认您的邮箱",
                "USER_REGISTRATION", userId.toString(), 2); // 注册邮件，优先级较高
    }

    /**
     * 静态工厂方法：创建密码重置邮件事件。
     *
     * @return 配置好的EmailEvent实例
     */
    public static EmailEvent createPasswordResetEvent(Object source, String toEmail, Long userId,
                                                      String userDisplayName, String resetToken) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "resetToken", resetToken,
                "resetUrl",
                "http://localhost:3000/auth/reset-password?token=" + resetToken,
                "expireMinutes",
                "30");

        return new EmailEvent(source, EmailType.PASSWORD_RESET, toEmail, userId, userDisplayName,
                variables, "密码重置请求 - KiseSaki博客",
                "PASSWORD_RESET", userId.toString(), 1); // 密码重置，优先级最高
    }

    /**
     * 静态工厂方法：创建新评论通知邮件事件。
     *
     * @return 配置好的EmailEvent实例
     */
    public static EmailEvent createCommentNotificationEvent(Object source, String toEmail, Long userId,
                                                            String userDisplayName, String postTitle,
                                                            String commenterName, String commentContent) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "postTitle", postTitle,
                "commenterName", commenterName,
                "commentContent", commentContent,
                "postUrl", "http://localhost:3000/posts/" // 需要补充完整URL
        );

        return new EmailEvent(source, EmailType.COMMENT_REPLY, toEmail, userId, userDisplayName,
                variables, "您的文章有新评论 - " + postTitle,
                "COMMENT_NOTIFICATION", postTitle, 3); // 评论通知，普通优先级
    }

    /**
     * 静态工厂方法：创建系统通知邮件事件。
     *
     * @return 配置好的EmailEvent实例
     */
    public static EmailEvent createSystemNotificationEvent(Object source, String toEmail, Long userId,
                                                           String userDisplayName, String notificationTitle,
                                                           String notificationContent) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "notificationTitle", notificationTitle,
                "notificationContent", notificationContent,
                "systemName",
                "KiseSaki博客");

        return new EmailEvent(source, EmailType.SYSTEM_ALERT, toEmail, userId, userDisplayName,
                variables, "系统通知 - " + notificationTitle,
                "SYSTEM_NOTIFICATION", notificationTitle, 4); // 系统通知，优先级较低
    }

    /**
     * 静态工厂方法：创建营销邮件事件。
     *
     * @return 配置好的EmailEvent实例
     */
    public static EmailEvent createMarketingEvent(Object source, String toEmail, Long userId,
                                                  String userDisplayName, String campaignName,
                                                  Map<String, Object> campaignVariables) {
        return new EmailEvent(source, EmailType.NEWSLETTER, toEmail, userId, userDisplayName,
                campaignVariables, "KiseSaki博客 - " + campaignName,
                "MARKETING", campaignName, 5); // 营销邮件，优先级最低
    }

    /**
     * 增加重试次数。
     * 当一次发送尝试失败后，由重试逻辑调用此方法。
     */
    public void incrementRetryCount() {
        this.retryCount++;
    }

    /**
     * 判断是否可以继续重试。
     *
     * @return 如果当前重试次数小于最大限制（例如3次），则返回true。
     */
    public boolean canRetry() {
        return this.retryCount < 3; // 最多重试3次
    }

    /**
     * 获取事件的简要描述字符串。
     * 主要用于日志输出，方便快速了解事件的核心信息。
     *
     * @return 格式化的事件描述字符串。
     */
    public String getEventDescription() {
        return String.format("EmailEvent[type=%s, to=%s, userId=%s, businessType=%s, priority=%d]",
                emailType != null ? emailType.getDescription() : "UNKNOWN",
                toEmail,
                userId,
                businessType,
                priority);
    }
}
