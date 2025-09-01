package com.kisesaki.blog.notification.service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kisesaki.blog.notification.config.EmailConfigurationProperties;
import com.kisesaki.blog.notification.dto.request.EmailSendRequestDto;
import com.kisesaki.blog.notification.dto.response.EmailSendResponseDto;
import com.kisesaki.blog.notification.enums.EmailStatus;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件服务
 * 
 * @author KiseSaki
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateService templateService;
    private final EmailConfigurationProperties emailProperties;

    /**
     * 发送邮件
     * 
     * @param request 邮件发送请求
     * @return 邮件发送响应
     */
    public EmailSendResponseDto sendEmail(EmailSendRequestDto request) {
        // 生成邮件ID
        String emailId = generateEmailId();

        // 创建响应对象
        EmailSendResponseDto response = EmailSendResponseDto.builder()
                .emailId(emailId)
                .status(EmailStatus.PENDING)
                .emailType(request.getEmailType())
                .subject(request.getSubject())
                .userId(request.getUserId())
                .businessId(request.getBusinessId())
                .businessType(request.getBusinessType())
                .createdTime(LocalDateTime.now())
                .retryCount(0)
                .maxRetries(request.getMaxRetries())
                .build();

        try {
            if (request.getAsync()) {
                // 异步发送
                sendEmailAsync(request, response);
            } else {
                // 同步发送
                sendEmailSync(request, response);
            }

        } catch (Exception e) {
            log.error("邮件发送失败: {} - {}", emailId, e.getMessage(), e);
            response.setStatus(EmailStatus.FAILED);
            response.setErrorMessage(e.getMessage());
            response.setUpdatedTime(LocalDateTime.now());
        }

        return response;
    }

    /**
     * 异步发送邮件
     * 
     * @param request  邮件发送请求
     * @param response 邮件发送响应
     * @return 异步结果
     */
    @Async("emailTaskExecutor")
    public CompletableFuture<EmailSendResponseDto> sendEmailAsync(EmailSendRequestDto request,
            EmailSendResponseDto response) {
        try {
            sendEmailSync(request, response);
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            response.setStatus(EmailStatus.FAILED);
            response.setErrorMessage(e.getMessage());
            response.setUpdatedTime(LocalDateTime.now());

            return CompletableFuture.completedFuture(response);
        }
    }

    /**
     * 同步发送邮件
     * 
     * @param request  邮件发送请求
     * @param response 邮件发送响应
     */
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendEmailSync(EmailSendRequestDto request, EmailSendResponseDto response) {
        LocalDateTime startTime = LocalDateTime.now();

        try {
            // 更新状态为发送中
            response.setStatus(EmailStatus.SENDING);
            // updateEmailLogStatus(response.getEmailId(), EmailStatus.SENDING, null);

            // 准备邮件内容
            String emailContent = prepareEmailContent(request);

            // 创建并发送邮件
            MimeMessage message = createMimeMessage(request, emailContent);
            mailSender.send(message);

            // 计算发送耗时
            LocalDateTime endTime = LocalDateTime.now();
            long duration = java.time.Duration.between(startTime, endTime).toMillis();

            // 更新成功状态
            response.setStatus(EmailStatus.SUCCESS);
            response.setSentTime(endTime);
            response.setUpdatedTime(endTime);
            response.setSendDuration(duration);

            log.info("邮件发送成功: {} -> {}, 耗时: {}ms", response.getEmailId(), request.getToEmail(), duration);

        } catch (Exception e) {
            // 增加重试次数
            Integer retryCount = response.getRetryCount() + 1;
            response.setRetryCount(retryCount);

            if (retryCount >= response.getMaxRetries()) {
                response.setStatus(EmailStatus.FAILED);
                log.error("邮件发送失败，已达最大重试次数: {} - {}", response.getEmailId(), e.getMessage());
            } else {
                response.setStatus(EmailStatus.RETRYING);
                log.warn("邮件发送失败，准备重试: {} ({}次/{}次) - {}",
                        response.getEmailId(), retryCount, response.getMaxRetries(), e.getMessage());
            }
        }
    }

    /**
     * 准备邮件内容
     * 
     * @param request 邮件发送请求
     * @return 邮件内容
     */
    private String prepareEmailContent(EmailSendRequestDto request) {
        // 如果请求中已有HTML内容，直接使用
        if (request.hasHtmlContent()) {
            return request.getHtml();
        }

        // 如果有模板变量，使用模板处理
        if (request.hasTemplateVariables()) {
            return templateService.processTemplate(
                    request.getEmailType(),
                    request.getTemplateVariables(),
                    Locale.SIMPLIFIED_CHINESE);
        }

        // 否则使用纯文本内容
        return request.hasTextContent() ? request.getText() : "";
    }

    /**
     * 创建MIME邮件消息
     * 
     * @param request 邮件发送请求
     * @param content 邮件内容
     * @return MIME消息
     * @throws Exception 创建异常
     */
    private MimeMessage createMimeMessage(EmailSendRequestDto request, String content) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // 设置发件人
        helper.setFrom(emailProperties.getSmtp().getUsername());

        // 设置收件人
        helper.setTo(request.getToEmail());

        // 设置主题
        helper.setSubject(request.getSubject());

        // 设置内容
        if (request.hasTemplateVariables()) {
            helper.setText(content, true); // HTML内容
        } else {
            helper.setText(content, false); // 纯文本内容
        }

        // 设置优先级
        if (request.getPriority() != null && request.getPriority() <= 2) {
            message.setHeader("X-Priority", "1");
            message.setHeader("X-MSMail-Priority", "High");
        }

        return message;
    }

    /**
     * 生成邮件ID
     * 
     * @return 邮件ID
     */
    private String generateEmailId() {
        return "EMAIL-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
