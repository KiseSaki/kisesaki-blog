package com.kisesaki.services.email.email_service.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisesaki.services.email.email_service.kafka.model.EmailMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * �����ʼ�ʧ�����񣬿���������־��־û���ֹ���
 *
 * @author KiseSaki
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailFailureService {

    private static final Path FAILURE_LOG_PATH = Path.of("logs", "failed-emails.jsonl");
    private static final String DEAD_LETTER_TOPIC = "email_failed_notifications_dlq";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /**
     * ͳ�ƹ����ʼ��޸�ʧʱ�Ĵ���
     *
     * @param stage        ʧ�ܷ���
     * @param emailMessage �ʼ���Ϣ
     * @param exception    �쳣��
     */
    public void handleFailure(String stage, EmailMessage emailMessage, Exception exception) {
        FailedEmailRecord record = buildRecord(stage, emailMessage, exception);

        logFailureDetails(record);
        persistFailureRecord(record);
        publishDeadLetter(record, emailMessage);
        notifyOpsTeam(record);
    }

    private FailedEmailRecord buildRecord(String stage, EmailMessage emailMessage, Exception exception) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> variables = emailMessage != null && emailMessage.getTemplateVariables() != null
            ? new HashMap<>(emailMessage.getTemplateVariables())
            : Map.of();

        return new FailedEmailRecord(
            "FAILED-" + UUID.randomUUID().toString().replace("-", "").toUpperCase(),
            stage,
            emailMessage != null && emailMessage.getEmailType() != null
                ? emailMessage.getEmailType().getDescription()
                : "UNKNOWN",
            emailMessage != null ? emailMessage.getToEmail() : null,
            emailMessage != null ? emailMessage.getUserId() : null,
            emailMessage != null ? emailMessage.getUserDisplayName() : null,
            emailMessage != null ? emailMessage.getSubject() : null,
            emailMessage != null ? emailMessage.getBusinessType() : null,
            emailMessage != null ? emailMessage.getBusinessId() : null,
            emailMessage != null ? emailMessage.getRetryCount() : 0,
            emailMessage != null ? emailMessage.getMaxRetryCount() : 0,
            emailMessage != null ? emailMessage.getPriority() : 0,
            emailMessage != null ? emailMessage.getTriggerTime() : null,
            now,
            exception != null ? exception.getMessage() : "未知错误",
            variables
        );
    }

    private void logFailureDetails(FailedEmailRecord record) {
        log.error("===== �ʼ���������ʧ�� =====");
        log.error("ʧ�ܱ�ʶ: {}", record.failureId());
        log.error("ʧ��λ��: {}", record.stage());
        log.error("�ʼ�����: {}", record.emailType());
        log.error("�ռ���: {}", record.toEmail());
        log.error("�û�ID: {}", record.userId());
        log.error("�û��ǳ�: {}", record.userDisplayName());
        log.error("����: {}", record.subject());
        log.error("ҵ������: {}", record.businessType());
        log.error("ҵ��ID: {}", record.businessId());
        log.error("���Դ���: {}/{}", record.retryCount(), record.maxRetryCount());
        log.error("���ȼ�: {}", record.priority());
        log.error("����ʱ��: {}", record.triggerTime());
        log.error("ָ����ʱ��: {}", record.failedAt());
        log.error("ʧ��ԭ��: {}", record.errorMessage());
        log.error("========================");
    }

    private void persistFailureRecord(FailedEmailRecord record) {
        try {
            Path parent = FAILURE_LOG_PATH.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Map<String, Object> payload = new HashMap<>();
            payload.put("failureId", record.failureId());
            payload.put("stage", record.stage());
            payload.put("emailType", record.emailType());
            payload.put("toEmail", record.toEmail());
            payload.put("userId", record.userId());
            payload.put("userDisplayName", record.userDisplayName());
            payload.put("subject", record.subject());
            payload.put("businessType", record.businessType());
            payload.put("businessId", record.businessId());
            payload.put("retryCount", record.retryCount());
            payload.put("maxRetryCount", record.maxRetryCount());
            payload.put("priority", record.priority());
            payload.put("triggerTime", record.triggerTime());
            payload.put("failedAt", record.failedAt());
            payload.put("errorMessage", record.errorMessage());
            payload.put("templateVariables", record.templateVariables());

            try (BufferedWriter writer = Files.newBufferedWriter(
                FAILURE_LOG_PATH,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            )) {
                writer.write(objectMapper.writeValueAsString(payload));
                writer.newLine();
            }
        } catch (IOException ioException) {
            log.error("ʧ���ʼ���־û�����ʧ��: {}", ioException.getMessage(), ioException);
        }
    }

    private void publishDeadLetter(FailedEmailRecord record, EmailMessage emailMessage) {
        try {
            DeadLetterPayload payload = new DeadLetterPayload(
                record.failureId(),
                record.stage(),
                emailMessage,
                record.errorMessage(),
                record.failedAt()
            );
            kafkaTemplate.send(DEAD_LETTER_TOPIC, payload.failureId(), payload);
        } catch (Exception sendException) {
            log.error("����ʧ���ʼ� dead-letter ��ʧ��: {}", sendException.getMessage(), sendException);
        }
    }

    private void notifyOpsTeam(FailedEmailRecord record) {
        log.warn(
            "�ʼ�ʧ�ܱ���: id={}, stage={}, to={}, subject={}, reason={}",
            record.failureId(),
            record.stage(),
            record.toEmail(),
            record.subject(),
            record.errorMessage()
        );
    }

    private record FailedEmailRecord(
        String failureId,
        String stage,
        String emailType,
        String toEmail,
        Long userId,
        String userDisplayName,
        String subject,
        String businessType,
        String businessId,
        int retryCount,
        int maxRetryCount,
        int priority,
        LocalDateTime triggerTime,
        LocalDateTime failedAt,
        String errorMessage,
        Map<String, Object> templateVariables
    ) {}

    private record DeadLetterPayload(
        String failureId,
        String stage,
        EmailMessage emailMessage,
        String errorMessage,
        LocalDateTime failedAt
    ) {}
}

