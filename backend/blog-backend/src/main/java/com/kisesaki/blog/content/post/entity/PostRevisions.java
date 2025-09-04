package com.kisesaki.blog.content.post.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * post_revisions 表实体
 */
@Data
@NoArgsConstructor
@TableName("post_revisions")
public class PostRevisions {

    /* 版本唯一ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /* 文章ID，外键 references posts.id */
    private Long postId;

    /* 版本号 */
    private Integer version;

    /* 标题 */
    private String title;

    /* 内容 */
    private String content;

    /* 摘要 */
    private String summary;

    /* 创建者ID，外键 references users.id */
    private Long createdBy;

    /* 创建时间 */
    private OffsetDateTime createdAt;

}
