package com.kisesaki.services.email.email_service.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件类型枚举
 * 与blog-backend保持一致
 *
 * @author KiseSaki
 */
@Getter
@AllArgsConstructor
public enum EmailType {

    // 认证相关邮件
    WELCOME("welcome", "欢迎邮件", "auth/welcome.html"),
    EMAIL_VERIFICATION("email_verification", "邮箱验证", "auth/email-verification.html"),
    PASSWORD_RESET("password_reset", "密码重置", "auth/password-reset.html"),
    PASSWORD_CHANGED("password_changed", "密码修改通知", "auth/password-changed.html"),

    // 通知相关邮件
    COMMENT_REPLY("comment_reply", "评论回复通知", "notification/comment-reply.html"),
    POST_PUBLISHED("post_published", "文章发布通知", "notification/post-published.html"),
    FOLLOW_NOTIFICATION("follow_notification", "关注通知", "notification/follow-notification.html"),
    WEEKLY_DIGEST("weekly_digest", "周报摘要", "notification/weekly-digest.html"),

    // 管理员邮件
    USER_REGISTERED("user_registered", "用户注册通知", "admin/user-registered.html"),
    SYSTEM_ALERT("system_alert", "系统告警", "admin/system-alert.html"),
    CONTENT_MODERATION("content_moderation", "内容审核", "admin/content-moderation.html"),

    // 营销邮件
    NEWSLETTER("newsletter", "博客周报", "marketing/newsletter.html"),
    FEATURE_ANNOUNCEMENT("feature_announcement", "功能发布", "marketing/feature-announcement.html");

    // 邮件类型代码
    private final String code;
    
    // 邮件类型描述
    private final String description;
    
    // 邮件模板路径
    private final String templatePath;

    /**
     * 根据代码获取邮件类型枚举
     */
    public static EmailType fromCode(String code) {
        for (EmailType type : EmailType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的邮件类型代码 " + code);
    }

    /**
     * 检查是否为认证相关邮件
     */
    public boolean isAuthEmail() {
        return this == WELCOME || this == EMAIL_VERIFICATION ||
                this == PASSWORD_RESET || this == PASSWORD_CHANGED;
    }

    /**
     * 检查是否为通知相关邮件
     */
    public boolean isNotificationEmail() {
        return this == COMMENT_REPLY || this == POST_PUBLISHED ||
                this == FOLLOW_NOTIFICATION || this == WEEKLY_DIGEST;
    }

    /**
     * 检查是否为管理员邮件
     */
    public boolean isAdminEmail() {
        return this == USER_REGISTERED || this == SYSTEM_ALERT ||
                this == CONTENT_MODERATION;
    }

    /**
     * 检查是否为营销邮件
     */
    public boolean isMarketingEmail() {
        return this == NEWSLETTER || this == FEATURE_ANNOUNCEMENT;
    }
}
