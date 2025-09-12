package com.kisesaki.blog.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 分页参数 DTO
 *
 * <p>
 * 提供统一的分页查询参数，字段命名与 PageResponse 保持一致。
 * 包含分页、排序、时间范围等常用查询参数。
 *
 * @author KiseSaki
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页查询参数")
public class PageableParams {

    @Schema(description = "当前页码（从1开始）", example = "1", minimum = "1")
    @Min(value = 1, message = "当前页码最小为 1")
    private Integer currentPage = 1;

    @Schema(description = "每页大小", example = "10", minimum = "1", maximum = "100")
    @Min(value = 1, message = "每页大小最小为 1")
    @Max(value = 100, message = "每页大小最大为 100")
    private Integer pageSize = 10;

    @Schema(description = "排序规则", example = "createdAt,desc", pattern = "^[a-zA-Z0-9_.]+,(asc|desc)$")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+,(asc|desc)$", message = "排序格式: field,asc|desc")
    private String sort = "createdAt,desc";

    @Schema(description = "是否返回总数", example = "true")
    private Boolean includeTotal = true;

    @Schema(description = "开始时间（用于时间范围查询）", example = "2024-01-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间（用于时间范围查询）", example = "2024-12-31 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "查询指定日期的数据", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 计算偏移量（用于数据库查询）
     */
    public long getOffset() {
        return (long) (currentPage - 1) * pageSize;
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

    /**
     * 是否为降序排序
     */
    public boolean isDescending() {
        return "desc".equalsIgnoreCase(getSortDirection());
    }

    /**
     * 验证时间范围是否合法
     */
    @AssertTrue(message = "开始时间不能晚于结束时间")
    private boolean isValidTimeRange() {
        if (startTime != null && endTime != null) {
            return !startTime.isAfter(endTime);
        }
        return true;
    }
}