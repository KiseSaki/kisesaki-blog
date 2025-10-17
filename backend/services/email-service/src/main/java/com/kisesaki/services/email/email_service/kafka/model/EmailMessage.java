package com.kisesaki.services.email.email_service.kafka.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件消息模型
 * 对应blog-backend的EmailEvent
 * 
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime triggerTime;

    // 是否异步发送
    @Builder.Default
    private boolean async = true;

    // 重试次数
    @Builder.Default
    private int retryCount = 0;

    // 最大重试次数
    @Builder.Default
    private int maxRetryCount = 3;

    // 优先级 (1-5, 1最高)
    @Builder.Default
    private int priority = 3;

    // 业务标识符
    private String businessId;
    
    // 业务类型
    private String businessType;

    /**
     * 增加重试次数
     */
    public void incrementRetryCount() {
        retryCount++;
    }

    /**
     * 判断是否可以继续重试
     */
    public boolean canRetry() {
        return retryCount < maxRetryCount;
    }

    /**
     * 获取事件的简要描述字符串
     */
    public String getEventDescription() {
        return String.format("EmailMessage[type=%s, to=%s, userId=%s, businessType=%s, priority=%d]",
            emailType != null ? emailType.getDescription() : "UNKNOWN",
            toEmail,
            userId,
            businessType,
            priority);
    }
}
