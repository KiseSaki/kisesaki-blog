package com.kisesaki.blog.content.post.dto.PostRevision;

import com.kisesaki.blog.content.post.dto.RevisionInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章版本内容")
public class PostRevisionContentResponse extends RevisionInfo {
    @Schema(description = "文章内容")
    private String content;
}
