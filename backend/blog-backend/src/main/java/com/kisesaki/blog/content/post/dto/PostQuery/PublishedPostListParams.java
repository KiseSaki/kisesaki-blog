package com.kisesaki.blog.content.post.dto.PostQuery;

import com.kisesaki.blog.common.dto.PageableParams;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishedPostListParams {
    @Valid
    private PageableParams pageable = new PageableParams();

    /* 关键字搜索（标题/摘要/内容片段） */
    private String q;

    /* 分类ID */
    private Long categoryId;

    /* 分类名称 */
    private String categoryName;

    /* 标签ID */
    private Long tagId;

    /* 标签名称 */
    private String tagName;

    /* 作者ID */
    private Long authorId;

    /* 作者用户名 */
    private String authorUsername;

    /* 用户昵称 */
    private String authorDisplayName;

    /* 是否置顶 */
    private Boolean isTop;

    /* 是否为精选文章 */
    private Boolean isFeatured;
}
