package com.kisesaki.blog.content.post.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("post_meta")
public class PostMeta {

    /* 元数据唯一ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /* 文章ID，外键 references posts.id */
    private Long postId;

    /* 元数据键名 */
    private String metaKey;

    /* 元数据值 */
    private String metaValue;

    /* 创建时间 */
    private OffsetDateTime createdAt;

    /* 更新时间 */
    private OffsetDateTime updatedAt;

}
