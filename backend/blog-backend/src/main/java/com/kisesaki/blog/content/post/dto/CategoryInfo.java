package com.kisesaki.blog.content.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "分类信息")
public class CategoryInfo {
    @Schema(description = "分类ID", example = "1")
    private Long id;

    @Schema(description = "分类名称", example = "技术分享")
    private String name;

    @Schema(description = "分类别名", example = "tech")
    private String slug;

    @Schema(description = "分类描述", example = "技术相关的文章分享")
    private String description;
}
