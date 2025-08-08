package com.kisesaki.blog.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页响应格式
 * 
 * @param <T> 数据类型
 * @author KiseSaki
 * @since 2025-08-07
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class PageResponse<T> {

    /**
     * 当前页码（从1开始）
     */
    private int current;

    /**
     * 每页大小
     */
    private int size;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 总页数
     */
    private long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return current > 1;
    }

    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return current < pages;
    }

    /**
     * 是否为空页
     */
    public boolean isEmpty() {
        return records == null || records.isEmpty();
    }

    /**
     * 创建分页响应
     */
    public static <T> PageResponse<T> of(int current, int size, long total, List<T> records) {
        long pages = (total + size - 1) / size; // 向上取整
        return new PageResponse<>(current, size, total, pages, records);
    }

    /**
     * 创建空分页响应
     */
    public static <T> PageResponse<T> empty(int current, int size) {
        return new PageResponse<>(current, size, 0L, 0L, List.of());
    }
}
