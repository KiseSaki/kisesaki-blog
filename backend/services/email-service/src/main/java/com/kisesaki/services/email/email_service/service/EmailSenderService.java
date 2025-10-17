package com.kisesaki.services.email.email_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.kisesaki.services.email.email_service.config.EmailProperties;
import com.kisesaki.services.email.email_service.kafka.model.EmailMessage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件发送服务
 *
 * @author KiseSaki
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final EmailTemplateService templateService;
    private final EmailFailureService emailFailureService;

    /**
     * 发送邮件（带重试机制）
     *
     * @param emailMessage 邮件消息
     * @return 邮件ID
     * @throws MessagingException 邮件发送异常
     */
    @Retryable(
        retryFor = {MessagingException.class},
        maxAttemptsExpression = "#{@emailProperties.retry.maxAttempts}",
        backoff = @Backoff(
            delayExpression = "#{@emailProperties.retry.initialInterval}",
            multiplierExpression = "#{@emailProperties.retry.multiplier}",
            maxDelayExpression = "#{@emailProperties.retry.maxInterval}"
        )
    )
    public String sendEmail(EmailMessage emailMessage) throws MessagingException {
        String emailId = generateEmailId();
        LocalDateTime startTime = LocalDateTime.now();
        
        log.info("开始发送邮件: id={}, {}", emailId, emailMessage.getEventDescription());

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 设置发件人
            helper.setFrom(
                emailProperties.getFrom().getAddress(),
                emailProperties.getFrom().getName()
            );

            // 设置收件人
            helper.setTo(emailMessage.getToEmail());

            // 设置主题
            helper.setSubject(emailMessage.getSubject());

            // 渲染邮件内容
            String htmlContent = templateService.renderTemplate(
                emailMessage.getEmailType(),
                emailMessage.getTemplateVariables()
            );

            // 设置邮件内容（HTML格式）
            helper.setText(htmlContent, true);

            // 设置优先级
            if (emailMessage.getPriority() <= 2) {
                mimeMessage.setHeader("X-Priority", "1");
                mimeMessage.setHeader("X-MSMail-Priority", "High");
                mimeMessage.setHeader("Importance", "High");
            }

            // 设置自定义邮件头（用于追踪）
            if (emailMessage.getBusinessId() != null) {
                mimeMessage.setHeader("X-Business-ID", emailMessage.getBusinessId());
            }
            if (emailMessage.getBusinessType() != null) {
                mimeMessage.setHeader("X-Business-Type", emailMessage.getBusinessType());
            }
            mimeMessage.setHeader("X-Email-ID", emailId);

            // 发送邮件
            mailSender.send(mimeMessage);

            // 计算发送耗时
            LocalDateTime endTime = LocalDateTime.now();
            long duration = java.time.Duration.between(startTime, endTime).toMillis();

            log.info("邮件发送成功: id={}, to={}, subject={}, type={}, 耗时={}ms", 
                emailId,
                emailMessage.getToEmail(), 
                emailMessage.getSubject(),
                emailMessage.getEmailType().getDescription(),
                duration);

            return emailId;

        } catch (Exception e) {
            long duration = java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
            
            log.error("邮件发送失败: id={}, {}, 耗时={}ms, 错误={}", 
                emailId,
                emailMessage.getEventDescription(), 
                duration,
                e.getMessage());
            
            throw new MessagingException("邮件发送失败: " + e.getMessage(), e);
        }
    }

    /**
     * 重试失败后的恢复方法
     */
    @Recover
    public String recover(MessagingException e, EmailMessage emailMessage) {
        log.error("邮件发送最终失败，已达到最大重试次数: {}, 错误: {}", 
            emailMessage.getEventDescription(), 
            e.getMessage());

        logFailedEmail(emailMessage, e);
        
        return null;
    }

    /**
     * 验证邮件地址格式
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * 生成唯一的邮件ID
     * 用于追踪和日志记录
     */
    private String generateEmailId() {
        return "EMAIL-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 记录邮件失败处理日志
     */
    private void logFailedEmail(EmailMessage emailMessage, Exception e) {
        emailFailureService.handleFailure("MAIL_SENDER_RECOVER", emailMessage, e);
    }
}

