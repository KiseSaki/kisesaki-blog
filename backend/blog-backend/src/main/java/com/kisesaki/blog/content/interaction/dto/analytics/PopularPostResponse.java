package com.kisesaki.blog.content.interaction.dto.analytics;

import java.time.OffsetDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 热门文章响应
 *
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热门文章响应")
public class PopularPostResponse {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章别名")
    private String slug;

    @Schema(description = "浏览量")
    private Long viewCount;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "评论数")
    private Long commentCount;

    @Schema(description = "发布时间")
    private OffsetDateTime publishedAt;
}
