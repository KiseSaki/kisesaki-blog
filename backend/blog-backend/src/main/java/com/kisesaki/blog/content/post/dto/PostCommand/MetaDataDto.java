package com.kisesaki.blog.content.post.dto.PostCommand;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文章元数据相关 DTO
 *
 * @author KiseSaki
 */
public class MetaDataDto {

    /**
     * 元数据项
     */
    @Data
    public static class MetaDataItem {
        /**
         * 元数据键
         */
        @NotNull(message = "元数据键不能为空")
        @NotEmpty(message = "元数据键不能为空")
        private String key;

        /**
         * 元数据值
         */
        @NotNull(message = "元数据值不能为空")
        @NotEmpty(message = "元数据值不能为空")
        private String value;
    }

    /**
     * 更新文章元数据请求
     */
    @Data
    public static class UpdatePostMetaRequest {
        /**
         * 元数据列表
         */
        @NotNull(message = "元数据列表不能为空")
        @Valid
        private List<MetaDataItem> metaData;
    }

    /**
     * 文章元数据响应
     */
    @Data
    public static class PostMetaResponse {
        /**
         * 文章ID
         */
        private Long postId;

        /**
         * 元数据列表
         */
        private List<MetaDataItem> metaData;
    }
}
