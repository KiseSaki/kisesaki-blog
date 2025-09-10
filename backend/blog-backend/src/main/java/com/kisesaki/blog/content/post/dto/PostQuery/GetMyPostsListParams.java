package com.kisesaki.blog.content.post.dto.PostQuery;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 获取我的文章列表参数
 *
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "获取我的文章列表参数")
public class GetMyPostsListParams {

    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "关键字搜索（标题/摘要/内容片段）", example = "Spring Boot")
    private String q;

    @Schema(description = "文章状态（draft, published, archived）", example = "draft")
    private String status;

    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "技术分享")
    private String categoryName;

    @Schema(description = "标签ID", example = "2")
    private Long tagId;

    @Schema(description = "标签名称", example = "Java")
    private String tagName;

    @Schema(description = "是否置顶", example = "false")
    private Boolean isTop;

    @Schema(description = "是否为精选文章", example = "false")
    private Boolean isFeatured;

    @Schema(description = "可见性（public, private, password_protected）", example = "public")
    private String visibility;
}
