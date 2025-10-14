package com.kisesaki.blog.notification.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import com.kisesaki.blog.notification.enums.EmailType;

/**
 * 邮件事件基类
 *
 * @author KiseSaki
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailEvent implements Serializable {

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
     * 增加重试次数
     * 当一次邮件发送失败后，可以调用此方法增加重试次数
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
