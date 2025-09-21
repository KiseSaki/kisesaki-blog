package com.kisesaki.blog.content.interaction.entity;

import java.net.InetAddress;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面浏览记录实体类
 * 用于记录页面浏览数据和统计
 */
@Data
@NoArgsConstructor
@TableName("page_views")
public class PageViews {

    /**
     * 浏览记录唯一ID (自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID (仅文章页面)
     */
    @TableField("post_id")
    private Long postId;

    /**
     * 用户ID (可为空，支持匿名访问)
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 页面类型 (post, category, tag, home, archive)
     */
    @TableField("page_type")
    private PageType pageType;

    /**
     * 页面URL
     */
    @TableField("page_url")
    private String pageUrl;

    /**
     * 页面标题
     */
    @TableField("page_title")
    private String pageTitle;

    /**
     * 来源页面
     */
    private String referrer;

    /**
     * UTM来源
     */
    @TableField("utm_source")
    private String utmSource;

    /**
     * UTM媒介
     */
    @TableField("utm_medium")
    private String utmMedium;

    /**
     * UTM活动
     */
    @TableField("utm_campaign")
    private String utmCampaign;

    /**
     * 访问者IP地址
     */
    @TableField("ip_address")
    private InetAddress ipAddress;

    /**
     * 用户代理字符串
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 设备类型 (desktop, mobile, tablet)
     */
    @TableField("device_type")
    private DeviceType deviceType;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 页面停留时间(秒)
     */
    private Integer duration;

    /**
     * 是否为跳出访问
     */
    @TableField("is_bounce")
    private Boolean isBounce = false;

    /**
     * 浏览时间
     */
    @TableField(value = "viewed_at", fill = FieldFill.INSERT)
    private LocalDateTime viewedAt;

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

    /**
     * 设备类型枚举
     */
    public enum DeviceType {
        DESKTOP,
        MOBILE,
        TABLET
    }
}
