package com.kisesaki.blog.notification.event;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.kisesaki.blog.config.ApplicationProperties;
import com.kisesaki.blog.notification.enums.EmailType;

import lombok.RequiredArgsConstructor;

/**
 * 邮件事件工厂类
 * 负责创建各种类型的邮件事件,并自动注入前端应用配置
 *
 * @author KiseSaki
 */
@Component
@RequiredArgsConstructor
public class EmailEventFactory {

    private final ApplicationProperties applicationProperties;

    public EmailEvent createUserRegistrationEvent( String toEmail, Long userId,
                                                  String userDisplayName, String confirmToken) {
        Map<String, Object> variables = Map.of(
            "userDisplayName", userDisplayName,
            "confirmToken", confirmToken,
            "confirmUrl",
            applicationProperties.getFrontend().getBaseUrl() + "/auth/verify-email?token=" + confirmToken);

        return EmailEvent.builder()
            .emailType(EmailType.EMAIL_VERIFICATION)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("欢迎注册KiseSaki博客 - 请确认您的邮箱")
            .businessType("USER_REGISTRATION")
            .businessId(userId.toString())
            .priority(3)
            .build();
    }

    public EmailEvent createPasswordResetEvent( String toEmail, Long userId,
                                               String userDisplayName, String resetToken) {
        Map<String, Object> variables = Map.of(
            "userDisplayName", userDisplayName,
            "resetToken", resetToken,
            "resetUrl",
            applicationProperties.getFrontend().getBaseUrl() + "/auth/reset-password?token=" + resetToken,
            "expireMinutes", "30");

        return EmailEvent.builder()
            .emailType(EmailType.PASSWORD_RESET)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("密码重置请求 - KiseSaki博客")
            .businessType("PASSWORD_RESET")
            .businessId(userId.toString())
            .priority(2)
            .build();
    }

    public EmailEvent createCommentNotificationEvent( String toEmail, Long userId,
                                                     String userDisplayName, String postTitle,
                                                     String commenterName, String commentContent) {
        return createCommentNotificationEvent(toEmail, userId, userDisplayName,
            postTitle, commenterName, commentContent, null);
    }

    public EmailEvent createCommentNotificationEvent( String toEmail, Long userId,
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

        return EmailEvent.builder()
            .emailType(EmailType.COMMENT_REPLY)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("您的文章有新评论 - " + postTitle)
            .businessType("COMMENT_NOTIFICATION")
            .businessId(postTitle)
            .priority(3)
            .build();
    }

    public EmailEvent createSystemNotificationEvent( String toEmail, Long userId,
                                                    String userDisplayName, String notificationTitle,
                                                    String notificationContent) {
        Map<String, Object> variables = Map.of(
            "userDisplayName", userDisplayName,
            "notificationTitle", notificationTitle,
            "notificationContent", notificationContent,
            "systemName", "KiseSaki博客");

        return EmailEvent.builder()
            .emailType(EmailType.SYSTEM_ALERT)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("系统通知 - " + notificationTitle)
            .businessType("SYSTEM_NOTIFICATION")
            .businessId(notificationTitle)
            .priority(4)
            .build();
    }

    public EmailEvent createMarketingEvent( String toEmail, Long userId,
                                           String userDisplayName, String campaignName,
                                           Map<String, Object> campaignVariables) {
        return EmailEvent.builder()
            .emailType(EmailType.NEWSLETTER)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(campaignVariables)
            .subject("KiseSaki博客 - " + campaignName)
            .businessType("MARKETING")
            .businessId(campaignName)
            .priority(5)
            .build();
    }

    public EmailEvent createWelcomeEvent( String toEmail, Long userId,
                                         String userDisplayName) {
        Map<String, Object> variables = Map.of(
            "userDisplayName", userDisplayName,
            "blogName", "KiseSaki博客",
            "blogUrl", applicationProperties.getFrontend().getBaseUrl(),
            "supportEmail", "support@kisesaki.com");

        return EmailEvent.builder()
            .emailType(EmailType.WELCOME)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("欢迎加入KiseSaki博客!")
            .businessType("USER_WELCOME")
            .businessId(userId.toString())
            .priority(3)
            .build();
    }

    public EmailEvent createPasswordChangedEvent( String toEmail, Long userId,
                                                 String userDisplayName, String changeTime, String ipAddress) {
        Map<String, Object> variables = Map.of(
            "userDisplayName", userDisplayName,
            "changeTime", changeTime,
            "ipAddress", ipAddress,
            "supportEmail", "support@kisesaki.com");

        return EmailEvent.builder()
            .emailType(EmailType.PASSWORD_CHANGED)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("密码修改通知 - KiseSaki博客")
            .businessType("PASSWORD_CHANGED")
            .businessId(userId.toString())
            .priority(2)
            .build();
    }

    public EmailEvent createPostPublishedEvent( String toEmail, Long userId,
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

        return EmailEvent.builder()
            .emailType(EmailType.POST_PUBLISHED)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("新文章发布:" + postTitle)
            .businessType("POST_PUBLISHED")
            .businessId(postId.toString())
            .priority(3)
            .build();
    }

    public EmailEvent createFollowNotificationEvent( String toEmail, Long userId,
                                                    String userDisplayName, String followerName, Long followerId) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = Map.of(
            "userDisplayName", userDisplayName,
            "followerName", followerName,
            "followerProfileUrl", baseUrl + "/users/" + followerId,
            "blogUrl", baseUrl,
            "unsubscribeUrl", baseUrl + "/unsubscribe?userId=" + userId);

        return EmailEvent.builder()
            .emailType(EmailType.FOLLOW_NOTIFICATION)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject(followerName + " 关注了您")
            .businessType("FOLLOW_NOTIFICATION")
            .businessId(followerId.toString())
            .priority(4)
            .build();
    }

    public EmailEvent createWeeklyDigestEvent( String toEmail, Long userId,
                                              String userDisplayName, String weekStartDate,
                                              String weekEndDate, Map<String, Object> weeklyContent) {
        String baseUrl = applicationProperties.getFrontend().getBaseUrl();
        Map<String, Object> variables = new HashMap<>();
        variables.put("userDisplayName", userDisplayName);
        variables.put("weekStartDate", weekStartDate);
        variables.put("weekEndDate", weekEndDate);
        variables.put("blogUrl", baseUrl);
        variables.put("unsubscribeUrl", baseUrl + "/unsubscribe?userId=" + userId);
        variables.putAll(weeklyContent);

        return EmailEvent.builder()
            .emailType(EmailType.WEEKLY_DIGEST)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("KiseSaki博客周报 - " + weekStartDate + " 至 " + weekEndDate)
            .businessType("WEEKLY_DIGEST")
            .businessId(weekStartDate)
            .priority(4)
            .build();
    }

    public EmailEvent createUserRegisteredEvent( String adminEmail, Long adminId,
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

        return EmailEvent.builder()
            .emailType(EmailType.USER_REGISTERED)
            .toEmail(adminEmail)
            .userId(adminId)
            .userDisplayName(adminDisplayName)
            .templateVariables(variables)
            .subject("新用户注册通知 - " + newUserName)
            .businessType("USER_REGISTERED")
            .businessId(newUserId.toString())
            .priority(4)
            .build();
    }

    public EmailEvent createContentModerationEvent( String moderatorEmail, Long moderatorId,
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

        return EmailEvent.builder()
            .emailType(EmailType.CONTENT_MODERATION)
            .toEmail(moderatorEmail)
            .userId(moderatorId)
            .userDisplayName(moderatorName)
            .templateVariables(variables)
            .subject("内容审核通知 - " + contentTitle)
            .businessType("CONTENT_MODERATION")
            .businessId(contentId.toString())
            .priority(3)
            .build();
    }

    public EmailEvent createFeatureAnnouncementEvent( String toEmail, Long userId,
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
        variables.putAll(featureDetails);

        return EmailEvent.builder()
            .emailType(EmailType.FEATURE_ANNOUNCEMENT)
            .toEmail(toEmail)
            .userId(userId)
            .userDisplayName(userDisplayName)
            .templateVariables(variables)
            .subject("新功能发布:" + featureName + " - " + releaseVersion)
            .businessType("FEATURE_ANNOUNCEMENT")
            .businessId(releaseVersion)
            .priority(4)
            .build();
    }
}
