package com.kisesaki.blog.user.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户关注关系实体，对应表 user_follows
 * 
 * @author KiseSaki
 */
@Data
@TableName("user_follows")
public class UserFollow {

    /** 关注关系ID (自增) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关注者ID (谁关注) */
    private Long followerId;

    /** 被关注者ID (被谁关注) */
    private Long followingId;

    /** 关注状态 ('active', 'blocked') */
    private String status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;
}
