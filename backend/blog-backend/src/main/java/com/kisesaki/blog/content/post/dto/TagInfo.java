package com.kisesaki.blog.content.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "标签信息")
public class TagInfo {
    @Schema(description = "标签ID", example = "1")
    private Long id;

    @Schema(description = "标签名称", example = "Spring Boot")
    private String name;

    @Schema(description = "标签别名", example = "spring-boot")
    private String slug;

    @Schema(description = "标签颜色", example = "#ff6b6b")
    private String color;
}