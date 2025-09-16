package com.kisesaki.blog.content.category.entity;

import java.time.OffsetDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("categories")
public class Categories {

    /* 分类唯一ID（自增） */
    @TableId(type = IdType.AUTO)
    private Long id;

    /* 分类名称 */
    private String name;

    /* 分类别名（URL 友好） */
    private String slug;

    /* 分类描述 */
    private String description;

    /* 父分类ID（自关联） */
    private Long parentId;

    /* 排序顺序 */
    private Integer sortOrder;

    /* 文章数量 */
    private Integer postCount;

    /* 是否可见 */
    private Boolean isVisible;

    /* 创建时间 */
    private OffsetDateTime createdAt;

    /* 更新时间 */
    private OffsetDateTime updatedAt;

}
