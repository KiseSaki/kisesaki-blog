package com.kisesaki.blog.content.tag.dto.TagQuery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签搜索请求参数")
public class TagSearchParams {
    @Schema(description = "搜索关键词", example = "Java")
    @Size(min = 1, max = 50, message = "搜索关键词长度必须在1-50字符之间")
    private String q;

    @Schema(description = "返回结果数量限制", example = "10", minimum = "1", maximum = "50")
    @Min(value = 1, message = "返回数量最小为1")
    @Max(value = 50, message = "返回数量最大为50")
    private Integer limit = 10;

    @Schema(description = "是否只搜索已审核通过的标签", example = "true")
    private Boolean approvedOnly = true;

    @Schema(description = "排序方式", example = "popularity", allowableValues = { "name", "popularity", "created_at" })
    private String sort = "popularity";
}