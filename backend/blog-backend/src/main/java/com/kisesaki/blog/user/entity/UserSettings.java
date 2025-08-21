package com.kisesaki.blog.user.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户设置实体，对应表 user_settings
 * 
 * @author KiseSaki
 */
@Data
@TableName("user_settings")
public class UserSettings {

    /** 设置唯一ID (自增) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 设置键名 */
    private String settingKey;

    /** 设置值 */
    private String settingValue;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;
}
