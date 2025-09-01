package com.kisesaki.blog.notification.dto.request;

import java.util.Map;

import com.kisesaki.blog.notification.enums.EmailType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件发送请求DTO
 *
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendRequestDto {
    // 接收者邮箱
    private String toEmail;

    // 邮件类型
    private EmailType emailType;
    // 邮件标题
    private String subject;
    // 邮件内容（纯文本）
    private String text;
    // 邮件内容（HTML）
    private String html;
    // 模板变量
    private Map<String, Object> templateVariables;

    // 优先级 (1-5, 1最高)
    @Builder.Default
    private Integer priority = 3;
    // 是否异步发送
    @Builder.Default
    private Boolean async = true;
    // 是否启用重试机制
    @Builder.Default
    private Boolean enableRetry = true;
    // 最大重试次数
    @Builder.Default
    private Integer maxRetries = 3;
    // 重试间隔 (秒)
    @Builder.Default
    private Integer delaySeconds = 60;

    // 用户ID
    private Long userId;
    // 业务标识符
    private String businessId;
    // 业务类型
    private String businessType;

    /**
     * 检查是否有HTML内容
     * 
     * @return 是否有HTML内容
     */
    public boolean hasHtmlContent() {
        return html != null && !html.trim().isEmpty();
    }

    /**
     * 检查是否有纯文本内容
     * 
     * @return 是否有纯文本内容
     */
    public boolean hasTextContent() {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * 检查是否存在模板变量
     *
     * @return 如果模板变量不为空且包含至少一个键值对，则返回 true；否则返回 false。
     */
    public boolean hasTemplateVariables() {
        return templateVariables != null && !templateVariables.isEmpty();
    }
}
