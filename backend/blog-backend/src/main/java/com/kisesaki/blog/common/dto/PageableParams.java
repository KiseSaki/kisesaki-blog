package com.kisesaki.blog.common.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableParams {
    @Min(value = 1, message = "page 最小为 1")
    private Long page = 1L;

    @Min(value = 1, message = "size 最小为 1")
    @Max(value = 100, message = "size 最大为 100")
    private Long size = 10L;

    /**
     * sort 示例: createdAt,desc 或 title,asc
     */
    @Pattern(regexp = "^[a-zA-Z0-9_.]+,(asc|desc)$", message = "sort 格式: field,asc|desc")
    private String sort;

    /**
     * 是否返回总数
     */
    private Boolean includeTotal = true;

    /**
     * 计算偏移量
     */
    public long getComputedOffset() {
        long p = (page == null ? 1L : page);
        long s = (size == null ? 10L : size);
        return (p - 1) * s;
    }

    /**
     * 解析排序字段
     */
    public String getSortField() {
        if (sort == null || sort.isEmpty()) {
            return null;
        }
        return sort.split(",")[0];
    }

    /**
     * 解析排序方向
     */
    public String getSortDirection() {
        if (sort == null || sort.isEmpty()) {
            return "asc"; // 默认升序
        }
        String[] parts = sort.split(",");
        return parts.length > 1 ? parts[1] : "asc";
    }
}