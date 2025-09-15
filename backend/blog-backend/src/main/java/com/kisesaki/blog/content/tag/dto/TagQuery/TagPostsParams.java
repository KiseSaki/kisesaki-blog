package com.kisesaki.blog.content.tag.dto.TagQuery;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签下文章列表请求参数")
public class TagPostsParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "文章状态过滤", example = "published", allowableValues = { "draft", "published", "archived" })
    private String status = "published";

    @Schema(description = "可见性过滤", example = "public", allowableValues = { "public", "private", "password_protected" })
    private String visibility = "public";

    @Schema(description = "是否只显示精选文章", example = "false")
    private Boolean featuredOnly = false;
}