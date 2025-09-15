package com.kisesaki.blog.content.tag.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("tags")
public class Tags {

    /* 标签唯一ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /* 标签名称 */
    private String name;

    /* 标签别名（URL 友好） */
    private String slug;

    /* 标签描述 */
    private String description;

    /* 颜色（HEX） */
    private String color;

    /* 使用该标签的文章数量 */
    private Integer postCount;

    /* 创建时间 */
    private OffsetDateTime createdAt;

    /* 更新时间 */
    private OffsetDateTime updatedAt;

    /* 创建者ID */
    private Long createdBy;

    /* 是否已审核通过 */
    private Boolean isApproved;

    /* 审核状态：pending, approved, rejected */
    private String approvalStatus;

    /* 审核者ID */
    private Long approvedBy;

    /* 审核时间 */
    private OffsetDateTime approvedAt;

    /* 审核备注 */
    private String approvalNote;

    /* 最近使用时间 */
    private OffsetDateTime lastUsedAt;

    /* 热度权重（基于文章数量、阅读量等计算） */
    private Double popularityScore;

}
