package com.kisesaki.blog.notification.event;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.context.ApplicationEvent;

import com.kisesaki.blog.notification.enums.EmailType;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
         * 你只需要拿到用户的ID（比如 123），就可以在日志系统中精确搜索 businessType="PASSWORD_RESET",
         * businessId="123"。
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
         * 静态工厂方法：创建欢迎邮件事件。
         *
         * @param source          事件源
         * @param toEmail         接收者邮箱
         * @param userId          用户ID
         * @param userDisplayName 用户名
         * @return 配置好的EmailEvent实例
         */
        public static EmailEvent createWelcomeEvent(Object source, String toEmail, Long userId,
                        String userDisplayName) {
                Map<String, Object> variables = Map.of(
                                "userDisplayName", userDisplayName,
                                "blogName", "KiseSaki博客",
                                "blogUrl", "http://localhost:3000",
                                "supportEmail", "support@kisesaki.com");

                return new EmailEvent(source, EmailType.WELCOME, toEmail, userId, userDisplayName,
                                variables, "欢迎加入KiseSaki博客！",
                                "USER_WELCOME", userId.toString(), 3); // 欢迎邮件，普通优先级
        }

        /**
         * 静态工厂方法：创建密码修改通知邮件事件。
         *
         * @param source          事件源
         * @param toEmail         接收者邮箱
         * @param userId          用户ID
         * @param userDisplayName 用户名
         * @param changeTime      修改时间
         * @param ipAddress       操作IP地址
         * @return 配置好的EmailEvent实例
         */
        public static EmailEvent createPasswordChangedEvent(Object source, String toEmail, Long userId,
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
         * 静态工厂方法：创建文章发布通知邮件事件。
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
        public static EmailEvent createPostPublishedEvent(Object source, String toEmail, Long userId,
                        String userDisplayName, String postTitle,
                        String postSummary, String authorName, Long postId) {
                Map<String, Object> variables = Map.of(
                                "userDisplayName", userDisplayName,
                                "postTitle", postTitle,
                                "postSummary", postSummary,
                                "authorName", authorName,
                                "postUrl", "http://localhost:3000/posts/" + postId,
                                "unsubscribeUrl", "http://localhost:3000/unsubscribe?userId=" + userId);

                return new EmailEvent(source, EmailType.POST_PUBLISHED, toEmail, userId, userDisplayName,
                                variables, "新文章发布：" + postTitle,
                                "POST_PUBLISHED", postId.toString(), 3); // 文章发布通知，普通优先级
        }

        /**
         * 静态工厂方法：创建关注通知邮件事件。
         *
         * @param source          事件源
         * @param toEmail         接收者邮箱
         * @param userId          被关注用户ID
         * @param userDisplayName 被关注用户名
         * @param followerName    关注者名称
         * @param followerId      关注者ID
         * @return 配置好的EmailEvent实例
         */
        public static EmailEvent createFollowNotificationEvent(Object source, String toEmail, Long userId,
                        String userDisplayName, String followerName, Long followerId) {
                Map<String, Object> variables = Map.of(
                                "userDisplayName", userDisplayName,
                                "followerName", followerName,
                                "followerProfileUrl", "http://localhost:3000/users/" + followerId,
                                "blogUrl", "http://localhost:3000",
                                "unsubscribeUrl", "http://localhost:3000/unsubscribe?userId=" + userId);

                return new EmailEvent(source, EmailType.FOLLOW_NOTIFICATION, toEmail, userId, userDisplayName,
                                variables, followerName + " 关注了您",
                                "FOLLOW_NOTIFICATION", followerId.toString(), 4); // 关注通知，优先级较低
        }

        /**
         * 静态工厂方法：创建周报摘要邮件事件。
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
        public static EmailEvent createWeeklyDigestEvent(Object source, String toEmail, Long userId,
                        String userDisplayName, String weekStartDate,
                        String weekEndDate, Map<String, Object> weeklyContent) {
                Map<String, Object> variables = Map.of(
                                "userDisplayName", userDisplayName,
                                "weekStartDate", weekStartDate,
                                "weekEndDate", weekEndDate,
                                "blogUrl", "http://localhost:3000",
                                "unsubscribeUrl", "http://localhost:3000/unsubscribe?userId=" + userId);
                variables.putAll(weeklyContent); // 合并周报内容

                return new EmailEvent(source, EmailType.WEEKLY_DIGEST, toEmail, userId, userDisplayName,
                                variables, "KiseSaki博客周报 - " + weekStartDate + " 至 " + weekEndDate,
                                "WEEKLY_DIGEST", weekStartDate, 4); // 周报，优先级较低
        }

        /**
         * 静态工厂方法：创建用户注册管理员通知邮件事件。
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
        public static EmailEvent createUserRegisteredEvent(Object source, String adminEmail, Long adminId,
                        String adminDisplayName, String newUserName,
                        String newUserEmail, Long newUserId, String registrationTime) {
                Map<String, Object> variables = Map.of(
                                "adminDisplayName", adminDisplayName,
                                "newUserName", newUserName,
                                "newUserEmail", newUserEmail,
                                "newUserId", newUserId,
                                "registrationTime", registrationTime,
                                "userProfileUrl", "http://localhost:3000/admin/users/" + newUserId,
                                "adminPanelUrl", "http://localhost:3000/admin");

                return new EmailEvent(source, EmailType.USER_REGISTERED, adminEmail, adminId, adminDisplayName,
                                variables, "新用户注册通知 - " + newUserName,
                                "USER_REGISTERED", newUserId.toString(), 4); // 用户注册通知，优先级较低
        }

        /**
         * 静态工厂方法：创建内容审核邮件事件。
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
        public static EmailEvent createContentModerationEvent(Object source, String moderatorEmail, Long moderatorId,
                        String moderatorName, String contentType, String contentTitle,
                        Long contentId, String authorName, String reportReason) {
                Map<String, Object> variables = Map.of(
                                "moderatorName", moderatorName,
                                "contentType", contentType,
                                "contentTitle", contentTitle,
                                "contentId", contentId,
                                "authorName", authorName,
                                "reportReason", reportReason,
                                "moderationUrl", "http://localhost:3000/admin/moderation/" + contentId,
                                "adminPanelUrl", "http://localhost:3000/admin");

                return new EmailEvent(source, EmailType.CONTENT_MODERATION, moderatorEmail, moderatorId, moderatorName,
                                variables, "内容审核通知 - " + contentTitle,
                                "CONTENT_MODERATION", contentId.toString(), 2); // 内容审核，优先级较高
        }

        /**
         * 静态工厂方法：创建功能发布公告邮件事件。
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
        public static EmailEvent createFeatureAnnouncementEvent(Object source, String toEmail, Long userId,
                        String userDisplayName, String featureName,
                        String featureDescription, String releaseVersion,
                        Map<String, Object> featureDetails) {
                Map<String, Object> variables = Map.of(
                                "userDisplayName", userDisplayName,
                                "featureName", featureName,
                                "featureDescription", featureDescription,
                                "releaseVersion", releaseVersion,
                                "blogUrl", "http://localhost:3000",
                                "changelogUrl", "http://localhost:3000/changelog",
                                "unsubscribeUrl", "http://localhost:3000/unsubscribe?userId=" + userId);
                variables.putAll(featureDetails); // 合并功能详情

                return new EmailEvent(source, EmailType.FEATURE_ANNOUNCEMENT, toEmail, userId, userDisplayName,
                                variables, "新功能发布：" + featureName + " - " + releaseVersion,
                                "FEATURE_ANNOUNCEMENT", releaseVersion, 4); // 功能发布，优先级较低
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
