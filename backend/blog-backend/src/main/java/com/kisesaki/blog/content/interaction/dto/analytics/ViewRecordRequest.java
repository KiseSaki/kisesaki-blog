package com.kisesaki.blog.content.interaction.dto.analytics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面浏览记录请求DTO
 */
@Data
@NoArgsConstructor
public class ViewRecordRequest {

    /**
     * 页面类型
     */
    @NotNull(message = "页面类型不能为空")
    private PageType pageType;

    /**
     * 页面URL
     */
    @NotBlank(message = "页面URL不能为空")
    @Size(max = 1000, message = "页面URL长度不能超过1000字符")
    private String pageUrl;

    /**
     * 页面标题
     */
    @Size(max = 200, message = "页面标题长度不能超过200字符")
    private String pageTitle;

    /**
     * 文章ID（仅文章页面）
     */
    private Long postId;

    /**
     * 来源页面
     */
    @Size(max = 1000, message = "来源页面长度不能超过1000字符")
    private String referrer;

    /**
     * UTM来源
     */
    @Size(max = 100, message = "UTM来源长度不能超过100字符")
    private String utmSource;

    /**
     * UTM媒介
     */
    @Size(max = 100, message = "UTM媒介长度不能超过100字符")
    private String utmMedium;

    /**
     * UTM活动
     */
    @Size(max = 100, message = "UTM活动长度不能超过100字符")
    private String utmCampaign;

    /**
     * 会话ID
     */
    @NotBlank(message = "会话ID不能为空")
    @Size(max = 128, message = "会话ID长度不能超过128字符")
    private String sessionId;

    /**
     * 页面停留时间（秒）
     */
    private Integer duration;

    /**
     * 页面类型枚举
     */
    public enum PageType {
        POST,
        CATEGORY,
        TAG,
        HOME,
        ARCHIVE
    }
}