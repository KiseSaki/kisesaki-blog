package com.kisesaki.blog.content.interaction.dto.analytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义事件记录请求DTO
 */
@Data
@NoArgsConstructor
public class EventRecordRequest {

    /**
     * 事件类型
     */
    @NotBlank(message = "事件类型不能为空")
    @Size(max = 50, message = "事件类型长度不能超过50字符")
    private String eventType;

    /**
     * 事件数据（JSON格式）
     */
    @Size(max = 2000, message = "事件数据长度不能超过2000字符")
    private String eventData;

    /**
     * 页面URL
     */
    @NotBlank(message = "页面URL不能为空")
    @Size(max = 1000, message = "页面URL长度不能超过1000字符")
    private String pageUrl;

    /**
     * 会话ID
     */
    @NotBlank(message = "会话ID不能为空")
    @Size(max = 128, message = "会话ID长度不能超过128字符")
    private String sessionId;

    /**
     * 事件触发时间戳（毫秒）
     */
    private Long timestamp;
}