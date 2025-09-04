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

}
