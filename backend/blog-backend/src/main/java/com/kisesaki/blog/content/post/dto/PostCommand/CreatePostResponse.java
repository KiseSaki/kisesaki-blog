package com.kisesaki.blog.content.post.dto.PostCommand;

import java.time.OffsetDateTime;

import com.kisesaki.blog.content.post.entity.Posts;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建文章响应DTO
 *
 * @author KiseSaki
 */
@Data
@Schema(description = "创建文章响应")
public class CreatePostResponse {

    @Schema(description = "文章ID", example = "1")
    private Long id;

    @Schema(description = "文章标题", example = "Spring Boot 入门指南")
    private String title;

    @Schema(description = "URL友好别名", example = "spring-boot-guide")
    private String slug;

    @Schema(description = "文章状态", example = "draft")
    private String status;

    @Schema(description = "可见性", example = "public")
    private String visibility;

    @Schema(description = "是否立即发布", example = "false")
    private Boolean isPublished;

    @Schema(description = "发布时间")
    private OffsetDateTime publishedAt;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;

    @Schema(description = "更新时间")
    private OffsetDateTime updatedAt;

    public static CreatePostResponse fromEntity(Posts post) {
        CreatePostResponse response = new CreatePostResponse();
        if(post != null){
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setSlug(post.getSlug());
            response.setStatus(post.getStatus());
            response.setVisibility(post.getVisibility());
            response.setIsPublished("published".equals(post.getStatus()));
            response.setPublishedAt(post.getCreatedAt());
            response.setCreatedAt(post.getCreatedAt());
            response.setUpdatedAt(post.getUpdatedAt());
        }
        return response;
    }
}
