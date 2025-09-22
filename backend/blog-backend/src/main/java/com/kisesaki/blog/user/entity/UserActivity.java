package com.kisesaki.blog.user.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * 用户活动日志实体，对应表 user_activity
 * 
 * @author KiseSaki
 */
@Data
@TableName("user_activity")
public class UserActivity {

    /** 活动日志唯一ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 操作类型 */
    private String action;

    /** 操作描述 */
    private String description;

    /** IP地址 */
    private String ipAddress;

    /** 用户代理 */
    private String userAgent;

    /** 操作详情（JSON格式） */
    private JsonNode details;

    /** 操作结果 */
    private String result;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;
}