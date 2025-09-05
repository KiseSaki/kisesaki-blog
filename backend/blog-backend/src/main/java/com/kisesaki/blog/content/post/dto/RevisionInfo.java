package com.kisesaki.blog.content.post.dto;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "版本信息摘要")
public class RevisionInfo {
    @Schema(description = "版本号", example = "1")
    private Integer version;

    @Schema(description = "标题快照")
    private String title;

    @Schema(description = "摘要/说明")
    private String summary;

    @Schema(description = "创建者ID")
    private Long createdBy;

    @Schema(description = "创建时间")
    private OffsetDateTime createdAt;
}