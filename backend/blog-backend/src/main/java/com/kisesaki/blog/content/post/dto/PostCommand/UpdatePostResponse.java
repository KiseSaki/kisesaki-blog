package com.kisesaki.blog.content.post.dto.PostCommand;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 更新文章响应DTO
 *
 * @author KiseSaki
 */
@Data
@Schema(description = "更新文章响应")
public class UpdatePostResponse {

    @Schema(description = "文章ID", example = "1")
    private Long id;

    @Schema(description = "文章标题", example = "Spring Boot 入门指南")
    private String title;

    @Schema(description = "URL友好别名", example = "spring-boot-guide")
    private String slug;

    @Schema(description = "文章状态", example = "published")
    private String status;

    @Schema(description = "可见性", example = "public")
    private String visibility;

    @Schema(description = "是否创建了新版本", example = "false")
    private Boolean revisionCreated;

    @Schema(description = "当前版本号", example = "1")
    private Integer currentVersion;

    @Schema(description = "最后修改时间")
    private OffsetDateTime lastModifiedAt;

    @Schema(description = "更新时间")
    private OffsetDateTime updatedAt;
}
