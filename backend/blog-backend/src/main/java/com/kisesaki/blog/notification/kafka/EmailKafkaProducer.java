package com.kisesaki.blog.notification.kafka;

import com.kisesaki.blog.notification.config.kafka.KafkaEmailTopicConfig;
import com.kisesaki.blog.notification.enums.EmailType;
import com.kisesaki.blog.notification.event.EmailEvent;
import com.kisesaki.blog.notification.event.EmailEventFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailKafkaProducer {

    private final EmailEventFactory emailEventFactory;
    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;

    /**
     * 核心方法：发送邮件事件到 Kafka
     * <p>
     * 根据邮件优先级自动选择合适的 Topic：
     * - 高优先级（priority ≤ 2）: email-events-high-priority
     * - 普通优先级（priority ≥ 3）: email-events
     *
     * @param event 邮件事件对象
     */
    public void publishEmailEvent(EmailEvent event) {
        // 设置触发时间
        if (event.getTriggerTime() == null) {
            event.setTriggerTime(LocalDateTime.now());
        }

        // 根据事件优先级选择主题
        String topic = selectTopicByPriority(event);

        // 消息Key
        String messageKey = event.getUserId() != null ? event.getUserId().toString() :
                event.getToEmail();

        log.info("准备发送邮件事件到Kafka - 主题: {}, Key: {}, 事件: {}", topic, messageKey, event);

        // 创建一个异步任务
        CompletableFuture<SendResult<String, EmailEvent>> future = kafkaTemplate.send(topic, messageKey, event);

        // 执行异步回调
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("发送邮件事件到Kafka失败 - 主题: {}, Key: {}, 事件: {}, 错误: {}",
                        topic, messageKey, event, ex.getMessage(), ex);
            } else {
                log.info("成功发送邮件事件到Kafka - 主题: {}, Key: {}, 事件: {}, 分区: {}, 偏移量: {}, 描述: {}",
                        topic, messageKey, event,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        event.getEventDescription());
            }
        });
    }

    /**
     * 根据事件的优先级选择合适的主题进行发布
     */
    private String selectTopicByPriority(EmailEvent event) {
        // 示例逻辑：根据事件的优先级选择主题
        if (event.getPriority() < 3) { // 假设优先级1-2为高优先级
            return KafkaEmailTopicConfig.EMAIL_HIGH_PRIORITY_TOPIC;
        } else {
            return KafkaEmailTopicConfig.EMAIL_TOPIC;
        }
    }

    // ================ 认证相关邮件 ================

    /**
     * 发布"用户注册确认"邮件事件。
     *
     * @param toEmail      接收者邮箱
     * @param userId       用户ID
     * @param userName     用户名
     * @param confirmToken 确认令牌
     */
    public void publishUserRegistrationEvent(String toEmail, Long userId, String userName, String confirmToken) {
        EmailEvent event = emailEventFactory.createUserRegistrationEvent(toEmail, userId, userName,
                confirmToken);
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
        EmailEvent event = emailEventFactory.createPasswordResetEvent(toEmail, userId, userName, resetToken);
        publishEmailEvent(event);
    }

    /**
     * 发布"欢迎新用户"邮件事件。
     * 通常在用户完成邮箱验证后，作为真正的欢迎邮件发送。
     *
     * @param toEmail  接收者邮箱
     * @param userId   用户ID
     * @param userName 用户名
     */
    public void publishWelcomeEmailEvent(String toEmail, Long userId, String userName) {
        EmailEvent event = emailEventFactory.createWelcomeEvent(toEmail, userId, userName);
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
        EmailEvent event = emailEventFactory.createPasswordChangedEvent(toEmail, userId, userName, changeTime,
                ipAddress);
        publishEmailEvent(event);
    }

    // ================ 通知相关邮件 ================

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
        EmailEvent event = emailEventFactory.createCommentNotificationEvent(toEmail, userId, userName,
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
        EmailEvent event = emailEventFactory.createSystemNotificationEvent(toEmail, userId, userName,
                notificationTitle, notificationContent);
        publishEmailEvent(event);
    }

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
        EmailEvent event = emailEventFactory.createPostPublishedEvent(toEmail, userId, userName,
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
        EmailEvent event = emailEventFactory.createFollowNotificationEvent(toEmail, userId, userName,
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
        EmailEvent event = emailEventFactory.createWeeklyDigestEvent(toEmail, userId, userName,
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
        EmailEvent event = emailEventFactory.createUserRegisteredEvent(adminEmail, adminId, adminName,
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
        EmailEvent event = emailEventFactory.createContentModerationEvent(moderatorEmail, moderatorId,
                moderatorName, contentType, contentTitle, contentId, authorName, reportReason);
        publishEmailEvent(event);
    }

    // ================ 营销邮件 ================

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
        EmailEvent event = emailEventFactory.createMarketingEvent(toEmail, userId, userName,
                campaignName, campaignVariables);
        publishEmailEvent(event);
    }

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
        EmailEvent event = emailEventFactory.createMarketingEvent(toEmail, userId, userName,
                "newsletter", weeklyContent);
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
        EmailEvent event = EmailEvent.builder()
                .emailType(emailType)
                .toEmail(toEmail)
                .userId(userId)
                .userDisplayName(userName)
                .templateVariables(templateVariables)
                .subject(subject)
                .businessType(businessType)
                .businessId(businessId)
                .priority(priority)
                .triggerTime(LocalDateTime.now())
                .async(true)
                .retryCount(0)
                .build();

        publishEmailEvent(event);
    }
}
