package com.kisesaki.blog.content.post.dto.AdminCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理员文章状态管理相关 DTO
 *
 * @author KiseSaki
 */
public class AdminPostStatusDto {

    /**
     * 更新文章状态请求
     */
    @Data
    @Schema(description = "更新文章状态请求")
    public static class UpdateStatusRequest {

        @NotNull(message = "状态不能为空")
        @Schema(description = "文章状态", example = "published", allowableValues = { "draft", "published", "archived" })
        private String status;
    }

    /**
     * 设置精选文章请求
     */
    @Data
    @Schema(description = "设置精选文章请求")
    public static class SetFeaturedRequest {

        @NotNull(message = "精选状态不能为空")
        @Schema(description = "是否为精选文章", example = "true")
        private Boolean isFeatured;
    }

    /**
     * 设置置顶请求
     */
    @Data
    @Schema(description = "设置置顶请求")
    public static class SetTopRequest {

        @NotNull(message = "置顶状态不能为空")
        @Schema(description = "是否置顶", example = "true")
        private Boolean isTop;
    }

    /**
     * 转移文章作者请求
     */
    @Data
    @Schema(description = "转移文章作者请求")
    public static class TransferAuthorRequest {

        @NotNull(message = "作者ID不能为空")
        @Schema(description = "新作者ID", example = "2")
        private Long authorId;
    }
}
