package com.kisesaki.blog.content.post.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * posts 表实体
 */
@Data
@NoArgsConstructor
@TableName("posts")
public class Posts {

    /* 文章唯一ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /* 作者ID，外键 references user.id， NOT NULL */
    private Long authorId;

    /* 分类ID，外键 references categories.id */
    private Long categoryId;

    /* 标题 */
    private String title;

    /* URL 友好别名（唯一） */
    private String slug;

    /* 摘要/简介 */
    private String excerpt;

    /* Markdown 原始内容，NOT NULL */
    private String content;

    /* 渲染后的 HTML 缓存 */
    private String htmlContent;

    /* 封面图片 URL */
    private String coverImageUrl;

    /* 特色图片 URL */
    private String featuredImageUrl;

    /* 状态：draft, published, archived, deleted；默认 draft */
    private String status;

    /* 可见性：public, private, password_protected；默认 public */
    private String visibility;

    /* 访问密码（仅当 visibility 为 password_protected 时有效） */
    private String password;

    /* 浏览次数，默认 0 */
    private Integer viewCount;

    /* 点赞数量，默认 0 */
    private Integer likeCount;

    /* 评论数量，默认 0 */
    private Integer commentCount;

    /* 分享数量，默认 0 */
    private Integer shareCount;

    /* 收藏数量，默认 0 */
    private Integer favoriteCount;

    /* 预估阅读时间（分钟） */
    private Integer readingTime;

    /* 字数统计 */
    private Integer wordCount;

    /* 是否为精选文章 */
    private Boolean isFeatured;

    /* 是否置顶 */
    private Boolean isTop;

    /* 是否允许评论，默认 true */
    private Boolean allowComments;

    /* SEO 标题 */
    private String seoTitle;

    /* SEO 描述 */
    private String seoDescription;

    /* SEO 关键词，逗号分隔 */
    private String seoKeywords;

    /* 发布时间（带时区） */
    private OffsetDateTime publishedAt;

    /* 计划发布时间（用于定时发布） */
    private OffsetDateTime scheduledAt;

    /* 最后修改时间 */
    private OffsetDateTime lastModifiedAt;

    /* 创建时间，默认 NOW() */
    private OffsetDateTime createdAt;

    /* 更新时间，默认 NOW() */
    private OffsetDateTime updatedAt;

}
