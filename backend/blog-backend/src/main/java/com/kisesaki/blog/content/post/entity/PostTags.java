package com.kisesaki.blog.content.post.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("post_tags")
public class PostTags {

    /* 文章ID，外键 references posts.id */
    private Long postId;

    /* 标签ID，外键 references tags.id */
    private Long tagId;

    /* 创建时间 */
    private OffsetDateTime createdAt;

}
