package com.kisesaki.blog.content.post.dto.PostCommand;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "批量操作文章请求")
public class BatchOperatePostsRequest {
    @Schema(description = "文章ID列表", example = "[1, 2, 3]")
    private List<Long> postIds;

    @Schema(description = "操作类型", example = "delete", allowableValues = { "delete", "publish", "unpublish", "archive", "setFeatured", "unsetFeatured", "setTop", "unsetTop" })
    private Operation operation;


    @Getter
    public enum Operation {
        PUBLISH("publish"),
        UNPUBLISH("unpublish"),
        DELETE("delete"),
        ARCHIVE("archive"),
        SET_FEATURED("setFeatured"),
        UNSET_FEATURED("unsetFeatured"),
        SET_TOP("setTop"),
        UNSET_TOP("unsetTop");

        private final String value;

        Operation(String value) {
            this.value = value;
        }
        
        /**
         * 用于序列化：返回自定义字符串值（如 "setTop"）
         */
        @JsonValue
        public String getValue() {
            return value;
        }

        /**
         * 用于反序列化：兼容自定义值（"setTop"）和枚举名（"SET_TOP"），大小写不敏感
         */
        @JsonCreator
        public static Operation fromValue(String value) {
            for (Operation operation : values()) {
                if (operation.value.equals(value)) {
                    return operation;
                }
            }
            throw new IllegalArgumentException("未知的操作类型: " + value);
        }
    }
}
