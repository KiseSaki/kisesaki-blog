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
 * 自定义事件记录实体类
 * 用于记录用户的自定义行为事件（如搜索、下载等）
 */
@Data
@NoArgsConstructor
@TableName("custom_events")
public class CustomEvents {

    /**
     * 事件记录唯一ID (自增)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID (可为空，支持匿名访问)
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 事件类型 (search, download, share, etc.)
     */
    @TableField("event_type")
    private String eventType;

    /**
     * 事件数据 (JSON格式，存储事件相关的详细信息)
     */
    @TableField("event_data")
    private String eventData;

    /**
     * 页面URL
     */
    @TableField("page_url")
    private String pageUrl;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

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
     * 事件发生时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 设备类型枚举
     */
    public enum DeviceType {
        DESKTOP,
        MOBILE,
        TABLET
    }
}