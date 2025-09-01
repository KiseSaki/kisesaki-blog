package com.kisesaki.blog.notification.dto.response;

import java.time.LocalDateTime;

import com.kisesaki.blog.notification.enums.EmailStatus;
import com.kisesaki.blog.notification.enums.EmailType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件发送响应DTO
 *
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendResponseDto {

    // 邮件ID
    private String emailId;
    // 发送状态
    private EmailStatus status;
    // 邮件类型
    private EmailType emailType;
    // 邮件大小（字节）
    private Long emailSize;

    // 收件人邮箱
    private String toEmail;
    // 邮件标题
    private String subject;

    // 用户ID
    private Long userId;
    // 业务标识符
    private String businessId;
    // 业务类型
    private String businessType;

    // 发送时间
    private LocalDateTime sentTime;
    // 创建时间
    private LocalDateTime createdTime;
    // 更新时间
    private LocalDateTime updatedTime;
    // 发送耗时（毫秒）
    private Long sendDuration;

    // SMTP响应信息
    private String smtpResponse;

    // 重试次数
    private Integer retryCount;
    // 最大重试次数
    private Integer maxRetries;
    // 错误信息
    private String errorMessage;
    // 错误代码
    private String errorCode;

    /**
     * 检查是否发送成功
     * 
     * @return 是否发送成功
     */
    public boolean isSuccess() {
        return status == EmailStatus.SUCCESS;
    }

    /**
     * 检查是否发送失败
     * 
     * @return 是否发送失败
     */
    public boolean isFailed() {
        return status == EmailStatus.FAILED;
    }

    /**
     * 检查是否还在处理中
     * 
     * @return 是否还在处理中
     */
    public boolean isInProgress() {
        return status != null && status.isInProgress();
    }

    /**
     * 检查是否可以重试
     * 
     * @return 是否可以重试
     */
    public boolean canRetry() {
        return status != null && status.canRetry() &&
                retryCount != null && maxRetries != null &&
                retryCount < maxRetries;
    }

    /**
     * 检查是否有错误信息
     * 
     * @return 是否有错误信息
     */
    public boolean hasError() {
        return errorMessage != null && !errorMessage.trim().isEmpty();
    }

}
