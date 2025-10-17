package com.kisesaki.services.email.email_service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.kisesaki.services.email.email_service.kafka.model.EmailMessage;
import com.kisesaki.services.email.email_service.service.EmailFailureService;
import com.kisesaki.services.email.email_service.service.EmailSenderService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件事件Kafka消费者
 * 监听来自blog-backend的邮件事件并发送邮件
 *
 * @author KiseSaki
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventConsumer {

    private final EmailSenderService emailSenderService;
    private final EmailFailureService emailFailureService;

    /**
     * 消费普通优先级邮件事件
     */
    @KafkaListener(
        topics = "email_notifications",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeEmailEvent(
        @Payload EmailMessage emailMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        Acknowledgment acknowledgment
    ) {
        log.info("接收到邮件事件 - Topic: {}, Partition: {}, Offset: {}, Message: {}",
            topic, partition, offset, emailMessage.getEventDescription());

        processEmailMessage(emailMessage, acknowledgment);
    }

    /**
     * 消费高优先级邮件事件
     */
    @KafkaListener(
        topics = "email_high_priority_notifications",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeHighPriorityEmailEvent(
        @Payload EmailMessage emailMessage,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset,
        Acknowledgment acknowledgment
    ) {
        log.info("接收到高优先级邮件事件 - Topic: {}, Partition: {}, Offset: {}, Message: {}",
            topic, partition, offset, emailMessage.getEventDescription());

        processEmailMessage(emailMessage, acknowledgment);
    }

    /**
     * 处理邮件消息
     */
    private void processEmailMessage(EmailMessage emailMessage, Acknowledgment acknowledgment) {
        try {
            // 验证邮件地址
            if (!emailSenderService.isValidEmail(emailMessage.getToEmail())) {
                log.error("无效的邮件地址: {}", emailMessage.getToEmail());
                acknowledgment.acknowledge();
                return;
            }

            // 发送邮件
            emailSenderService.sendEmail(emailMessage);

            // 手动确认消息
            acknowledgment.acknowledge();
            
            log.info("邮件事件处理成功: {}", emailMessage.getEventDescription());

        } catch (MessagingException e) {
            log.error("邮件发送失败: {}, 错误: {}", 
                emailMessage.getEventDescription(), 
                e.getMessage());

            // 检查是否可以重试
            if (emailMessage.canRetry()) {
                emailMessage.incrementRetryCount();
                log.warn("邮件将进行第 {} 次重试", emailMessage.getRetryCount());
                // 这里可以重新发送到Kafka进行重试
                // 或者由Spring Retry机制自动处理
            } else {
                log.error("邮件重试次数已达上限，放弃发送: {}", 
                    emailMessage.getEventDescription());
                log.warn("发送失败邮件至死信队列: {}", emailMessage.getEventDescription());
                emailFailureService.handleFailure("CONSUMER_MAX_RETRY_EXCEEDED", emailMessage, e);
            }

            // 确认消息（避免无限重试）
            acknowledgment.acknowledge();

        } catch (Exception e) {
            log.error("处理邮件事件时发生未知错误: {}, 错误: {}", 
                emailMessage.getEventDescription(), 
                e.getMessage(), e);
            
            // 确认消息
            acknowledgment.acknowledge();
        }
    }
}

