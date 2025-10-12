package com.kisesaki.blog.content.post.dto.AdminCommand;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理员文章批量操作相关 DTO
 *
 * @author KiseSaki
 */
public class AdminPostBatchDto {

    /**
     * 批量删除请求
     */
    @Data
    @Schema(description = "批量删除请求")
    public static class BatchDeleteRequest {

        @NotNull(message = "文章ID列表不能为空")
        @Schema(description = "文章ID列表", example = "[1, 2, 3]")
        private List<Long> ids;
    }

    /**
     * 批量更新状态请求
     */
    @Data
    @Schema(description = "批量更新状态请求")
    public static class BatchUpdateStatusRequest {

        @NotNull(message = "文章ID列表不能为空")
        @Schema(description = "文章ID列表", example = "[1, 2, 3]")
        private List<Long> ids;

        @NotNull(message = "状态不能为空")
        @Schema(description = "文章状态", example = "published", allowableValues = { "draft", "published", "archived" })
        private String status;
    }

    /**
     * 批量转移作者请求
     */
    @Data
    @Schema(description = "批量转移作者请求")
    public static class BatchTransferAuthorRequest {

        @NotNull(message = "文章ID列表不能为空")
        @Schema(description = "文章ID列表", example = "[1, 2, 3]")
        private List<Long> ids;

        @NotNull(message = "作者ID不能为空")
        @Schema(description = "新作者ID", example = "2")
        private Long authorId;
    }

    /**
     * 批量设置精选请求
     */
    @Data
    @Schema(description = "批量设置精选请求")
    public static class BatchSetFeaturedRequest {

        @NotNull(message = "文章ID列表不能为空")
        @Schema(description = "文章ID列表", example = "[1, 2, 3]")
        private List<Long> ids;

        @NotNull(message = "精选状态不能为空")
        @Schema(description = "是否设为精选", example = "true")
        private Boolean isFeatured;
    }

    /**
     * 批量设置置顶请求
     */
    @Data
    @Schema(description = "批量设置置顶请求")
    public static class BatchSetTopRequest {

        @NotNull(message = "文章ID列表不能为空")
        @Schema(description = "文章ID列表", example = "[1, 2, 3]")
        private List<Long> ids;

        @NotNull(message = "置顶状态不能为空")
        @Schema(description = "是否设为置顶", example = "true")
        private Boolean isTop;
    }
}
