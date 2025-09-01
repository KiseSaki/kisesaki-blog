package com.kisesaki.blog.notification.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kisesaki.blog.config.ApplicationProperties;
import com.kisesaki.blog.notification.enums.EmailType;

import lombok.RequiredArgsConstructor;

/**
 * 邮件事件工厂类
 * 负责创建各种类型的邮件事件，并自动注入前端应用配置
 *
 * @author KiseSaki
 */
@Component
@RequiredArgsConstructor
public class EmailEventFactory {

    private final ApplicationProperties applicationProperties;

    /**
     * 创建用户注册确认邮件事件。
     * 封装了创建特定事件的复杂逻辑，对调用者暴露简洁的接口。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param confirmToken    确认令牌
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createUserRegistrationEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String confirmToken) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "confirmToken", confirmToken,
                "confirmUrl",
                applicationProperties.getFrontend().getBaseUrl() + "/auth/confirm?token=" + confirmToken);

        return new EmailEvent(source, EmailType.EMAIL_VERIFICATION, toEmail, userId, userDisplayName,
                variables, "欢迎注册KiseSaki博客 - 请确认您的邮箱",
                "USER_REGISTRATION", userId.toString(), 3); // 注册邮件
    }

    /**
     * 创建密码重置邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param resetToken      重置令牌
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createPasswordResetEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String resetToken) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "resetToken", resetToken,
                "resetUrl",
                applicationProperties.getFrontend().getBaseUrl() + "/auth/reset-password?token=" + resetToken,
                "expireMinutes",
                "30");

        return new EmailEvent(source, EmailType.PASSWORD_RESET, toEmail, userId, userDisplayName,
                variables, "密码重置请求 - KiseSaki博客",
                "PASSWORD_RESET", userId.toString(), 1); // 密码重置，优先级最高
    }

    /**
     * 创建新评论通知邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param postTitle       文章标题
     * @param commenterName   评论者名称
     * @param commentContent  评论内容
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createCommentNotificationEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String postTitle,
            String commenterName, String commentContent) {
        return createCommentNotificationEvent(source, toEmail, userId, userDisplayName,
                postTitle, commenterName, commentContent, null);
    }

    /**
     * 创建新评论通知邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param postTitle       文章标题
     * @param commenterName   评论者名称
     * @param commentContent  评论内容
     * @param postId          文章ID（可选，用于构建完整URL）
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createCommentNotificationEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String postTitle,
            String commenterName, String commentContent, Long postId) {
        String postUrl = applicationProperties.getFrontend().getBaseUrl() + "/posts/";
        if (postId != null) {
            postUrl += postId;
        }

        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "postTitle", postTitle,
                "commenterName", commenterName,
                "commentContent", commentContent,
                "postUrl", postUrl);

        return new EmailEvent(source, EmailType.COMMENT_REPLY, toEmail, userId, userDisplayName,
                variables, "您的文章有新评论 - " + postTitle,
                "COMMENT_NOTIFICATION", postTitle, 3); // 评论通知，普通优先级
    }

    /**
     * 创建系统通知邮件事件。
     *
     * @param source              事件源
     * @param toEmail             接收者邮箱
     * @param userId              用户ID
     * @param userDisplayName     用户名
     * @param notificationTitle   通知标题
     * @param notificationContent 通知内容
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createSystemNotificationEvent(Object source, String toEmail, Long userId,
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
     * 创建营销邮件事件。
     *
     * @param source            事件源
     * @param toEmail           接收者邮箱
     * @param userId            用户ID
     * @param userDisplayName   用户名
     * @param campaignName      活动名称
     * @param campaignVariables 活动变量
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createMarketingEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String campaignName,
            Map<String, Object> campaignVariables) {
        return new EmailEvent(source, EmailType.NEWSLETTER, toEmail, userId, userDisplayName,
                campaignVariables, "KiseSaki博客 - " + campaignName,
                "MARKETING", campaignName, 5); // 营销邮件，优先级最低
    }

    /**
     * 创建欢迎邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createWelcomeEvent(Object source, String toEmail, Long userId,
            String userDisplayName) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "blogName", "KiseSaki博客",
                "blogUrl", applicationProperties.getFrontend().getBaseUrl(),
                "supportEmail", "support@kisesaki.com");

        return new EmailEvent(source, EmailType.WELCOME, toEmail, userId, userDisplayName,
                variables, "欢迎加入KiseSaki博客！",
                "USER_WELCOME", userId.toString(), 3); // 欢迎邮件，普通优先级
    }

    /**
     * 创建密码修改通知邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param changeTime      修改时间
     * @param ipAddress       操作IP地址
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createPasswordChangedEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String changeTime, String ipAddress) {
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "changeTime", changeTime,
                "ipAddress", ipAddress,
                "supportEmail", "support@kisesaki.com");

        return new EmailEvent(source, EmailType.PASSWORD_CHANGED, toEmail, userId, userDisplayName,
                variables, "密码修改通知 - KiseSaki博客",
                "PASSWORD_CHANGED", userId.toString(), 1); // 密码修改，优先级最高
    }

    /**
     * 创建文章发布通知邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param postTitle       文章标题
     * @param postSummary     文章摘要
     * @param authorName      作者名称
     * @param postId          文章ID
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createPostPublishedEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String postTitle,
            String postSummary, String authorName, Long postId) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "postTitle", postTitle,
                "postSummary", postSummary,
                "authorName", authorName,
                "postUrl", baseUrl + "/posts/" + postId,
                "unsubscribeUrl", baseUrl + "/unsubscribe?userId=" + userId);

        return new EmailEvent(source, EmailType.POST_PUBLISHED, toEmail, userId, userDisplayName,
                variables, "新文章发布：" + postTitle,
                "POST_PUBLISHED", postId.toString(), 3); // 文章发布通知，普通优先级
    }

    /**
     * 创建关注通知邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          被关注用户ID
     * @param userDisplayName 被关注用户名
     * @param followerName    关注者名称
     * @param followerId      关注者ID
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createFollowNotificationEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String followerName, Long followerId) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = Map.of(
                "userDisplayName", userDisplayName,
                "followerName", followerName,
                "followerProfileUrl", baseUrl + "/users/" + followerId,
                "blogUrl", baseUrl,
                "unsubscribeUrl", baseUrl + "/unsubscribe?userId=" + userId);

        return new EmailEvent(source, EmailType.FOLLOW_NOTIFICATION, toEmail, userId, userDisplayName,
                variables, followerName + " 关注了您",
                "FOLLOW_NOTIFICATION", followerId.toString(), 4); // 关注通知，优先级较低
    }

    /**
     * 创建周报摘要邮件事件。
     *
     * @param source          事件源
     * @param toEmail         接收者邮箱
     * @param userId          用户ID
     * @param userDisplayName 用户名
     * @param weekStartDate   周开始日期
     * @param weekEndDate     周结束日期
     * @param weeklyContent   周报内容（包含热门文章、统计数据等）
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createWeeklyDigestEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String weekStartDate,
            String weekEndDate, Map<String, Object> weeklyContent) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = new HashMap<>();
        variables.put("userDisplayName", userDisplayName);
        variables.put("weekStartDate", weekStartDate);
        variables.put("weekEndDate", weekEndDate);
        variables.put("blogUrl", baseUrl);
        variables.put("unsubscribeUrl", baseUrl + "/unsubscribe?userId=" + userId);
        variables.putAll(weeklyContent); // 合并周报内容

        return new EmailEvent(source, EmailType.WEEKLY_DIGEST, toEmail, userId, userDisplayName,
                variables, "KiseSaki博客周报 - " + weekStartDate + " 至 " + weekEndDate,
                "WEEKLY_DIGEST", weekStartDate, 4); // 周报，优先级较低
    }

    /**
     * 创建用户注册管理员通知邮件事件。
     *
     * @param source           事件源
     * @param adminEmail       管理员邮箱
     * @param adminId          管理员ID
     * @param adminDisplayName 管理员名称
     * @param newUserName      新用户名称
     * @param newUserEmail     新用户邮箱
     * @param newUserId        新用户ID
     * @param registrationTime 注册时间
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createUserRegisteredEvent(Object source, String adminEmail, Long adminId,
            String adminDisplayName, String newUserName,
            String newUserEmail, Long newUserId, String registrationTime) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = Map.of(
                "adminDisplayName", adminDisplayName,
                "newUserName", newUserName,
                "newUserEmail", newUserEmail,
                "newUserId", newUserId,
                "registrationTime", registrationTime,
                "userProfileUrl", baseUrl + "/admin/users/" + newUserId,
                "adminPanelUrl", baseUrl + "/admin");

        return new EmailEvent(source, EmailType.USER_REGISTERED, adminEmail, adminId, adminDisplayName,
                variables, "新用户注册通知 - " + newUserName,
                "USER_REGISTERED", newUserId.toString(), 4); // 用户注册通知，优先级较低
    }

    /**
     * 创建内容审核邮件事件。
     *
     * @param source         事件源
     * @param moderatorEmail 审核员邮箱
     * @param moderatorId    审核员ID
     * @param moderatorName  审核员名称
     * @param contentType    内容类型（文章/评论等）
     * @param contentTitle   内容标题
     * @param contentId      内容ID
     * @param authorName     内容作者
     * @param reportReason   举报原因
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createContentModerationEvent(Object source, String moderatorEmail, Long moderatorId,
            String moderatorName, String contentType, String contentTitle,
            Long contentId, String authorName, String reportReason) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = Map.of(
                "moderatorName", moderatorName,
                "contentType", contentType,
                "contentTitle", contentTitle,
                "contentId", contentId,
                "authorName", authorName,
                "reportReason", reportReason,
                "moderationUrl", baseUrl + "/admin/moderation/" + contentId,
                "adminPanelUrl", baseUrl + "/admin");

        return new EmailEvent(source, EmailType.CONTENT_MODERATION, moderatorEmail, moderatorId, moderatorName,
                variables, "内容审核通知 - " + contentTitle,
                "CONTENT_MODERATION", contentId.toString(), 2); // 内容审核，优先级较高
    }

    /**
     * 创建功能发布公告邮件事件。
     *
     * @param source             事件源
     * @param toEmail            接收者邮箱
     * @param userId             用户ID
     * @param userDisplayName    用户名
     * @param featureName        功能名称
     * @param featureDescription 功能描述
     * @param releaseVersion     发布版本
     * @param featureDetails     功能详情
     * @return 配置好的EmailEvent实例
     */
    public EmailEvent createFeatureAnnouncementEvent(Object source, String toEmail, Long userId,
            String userDisplayName, String featureName,
            String featureDescription, String releaseVersion,
            Map<String, Object> featureDetails) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = new HashMap<>();
        variables.put("userDisplayName", userDisplayName);
        variables.put("featureName", featureName);
        variables.put("featureDescription", featureDescription);
        variables.put("releaseVersion", releaseVersion);
        variables.put("blogUrl", baseUrl);
        variables.put("changelogUrl", baseUrl + "/changelog");
        variables.put("unsubscribeUrl", baseUrl + "/unsubscribe?userId=" + userId);
        variables.putAll(featureDetails); // 合并功能详情

        return new EmailEvent(source, EmailType.FEATURE_ANNOUNCEMENT, toEmail, userId, userDisplayName,
                variables, "新功能发布：" + featureName + " - " + releaseVersion,
                "FEATURE_ANNOUNCEMENT", releaseVersion, 4); // 功能发布，优先级较低
    }
}
