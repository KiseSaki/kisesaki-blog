package com.kisesaki.blog.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页响应 DTO
 * 
 * <p>
 * 提供统一的分页数据响应格式，支持从 Spring Data 的 Page 对象自动转换。
 * 包含完整的分页信息和数据列表，便于前端进行分页处理。
 * 
 * @param <T> 数据类型
 * @author KiseSaki
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "分页响应数据传输对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "currentPage", "pageSize", "totalRecords", "totalPages", "first", "last", "empty", "data" })
public class PageResponse<T> {

    @Schema(description = "当前页码（从1开始）", example = "1", minimum = "1")
    private int currentPage;

    @Schema(description = "每页大小", example = "10", minimum = "1", maximum = "100")
    private int pageSize;

    @Schema(description = "总记录数", example = "100", minimum = "0")
    private long totalRecords;

    @Schema(description = "总页数", example = "10", minimum = "0")
    private long totalPages;

    @Schema(description = "是否为第一页", example = "true")
    private boolean first;

    @Schema(description = "是否为最后一页", example = "false")
    private boolean last;

    @Schema(description = "是否为空页（无数据）", example = "false")
    private boolean empty;

    @Schema(description = "数据列表")
    private List<T> data;

    /**
     * 从 Spring Data 的 Page 对象创建分页响应
     * 
     * @param page Spring Data Page 对象
     * @param <T>  数据类型
     * @return 分页响应对象
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        if (page == null) {
            return PageResponse.<T>builder()
                    .currentPage(1)
                    .pageSize(0)
                    .totalRecords(0L)
                    .totalPages(0L)
                    .first(true)
                    .last(true)
                    .empty(true)
                    .data(List.of())
                    .build();
        }

        return PageResponse.<T>builder()
                .currentPage(page.getNumber() + 1) // Spring Data 页码从0开始，转换为从1开始
                .pageSize(page.getSize())
                .totalRecords(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .data(page.getContent())
                .build();
    }

    /**
     * 创建空的分页响应
     * 
     * @param pageSize 每页大小
     * @param <T>      数据类型
     * @return 空的分页响应对象
     */
    public static <T> PageResponse<T> empty(int pageSize) {
        return PageResponse.<T>builder()
                .currentPage(1)
                .pageSize(pageSize)
                .totalRecords(0L)
                .totalPages(0L)
                .first(true)
                .last(true)
                .empty(true)
                .data(List.of())
                .build();
    }

    /**
     * 创建单页数据响应
     * 
     * @param data 数据列表
     * @param <T>  数据类型
     * @return 单页响应对象
     */
    public static <T> PageResponse<T> single(List<T> data) {
        if (data == null || data.isEmpty()) {
            return PageResponse.<T>builder()
                    .currentPage(1)
                    .pageSize(0)
                    .totalRecords(0L)
                    .totalPages(0L)
                    .first(true)
                    .last(true)
                    .empty(true)
                    .data(List.of())
                    .build();
        }

        int size = data.size();
        return PageResponse.<T>builder()
                .currentPage(1)
                .pageSize(size)
                .totalRecords(size)
                .totalPages(1L)
                .first(true)
                .last(true)
                .empty(false)
                .data(data)
                .build();
    }

    /**
     * 检查是否有下一页
     * 
     * @return 是否有下一页
     */
    public boolean hasNext() {
        return !last && currentPage < totalPages;
    }

    /**
     * 检查是否有上一页
     * 
     * @return 是否有上一页
     */
    public boolean hasPrevious() {
        return !first && currentPage > 1;
    }

    /**
     * 获取下一页页码
     * 
     * @return 下一页页码，如果没有下一页则返回当前页码
     */
    public int getNextPage() {
        return hasNext() ? currentPage + 1 : currentPage;
    }

    /**
     * 获取上一页页码
     * 
     * @return 上一页页码，如果没有上一页则返回当前页码
     */
    public int getPreviousPage() {
        return hasPrevious() ? currentPage - 1 : currentPage;
    }
}