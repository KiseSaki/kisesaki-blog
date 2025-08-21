package com.kisesaki.blog.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页响应
 *
 * @param <T> 数据类型
 * @author KiseSaki
 */
@Schema(description = "分页响应") @JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {

    @Schema(description = "当前页码", example = "1")
    private int currentPage;

    @Schema(description = "每页大小", example = "10")
    private int pageSize;

    @Schema(description = "总记录数", example = "100")
    private long totalRecords;

    @Schema(description = "总页数", example = "10")
    private long totalPages;

    @Schema(description = "数据列表")
    private List<T> data;

    @Schema(description = "是否为第一页", example = "true")
    private boolean first;

    @Schema(description = "是否为最后一页", example = "false")
    private boolean last;

    @Schema(description = "是否为空页", example = "false")
    private boolean empty;

    @SuppressWarnings("unused")
    private PageResponse() {
    } // 私有构造函数，防止直接实例化

    /**
     * 构造函数
     *
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @param totalRecords 总记录数
     * @param totalPages 总页数
     * @param data 数据列表
     * @param first 是否为第一页
     * @param last 是否为最后一页
     * @param empty 是否为空页
     */
    public PageResponse(int currentPage, int pageSize, long totalRecords, long totalPages,
            List<T> data, boolean first, boolean last, boolean empty) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
        this.data = data;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }

    /**
     * 从Spring Data Page对象构建PageResponse 直接从 Spring Data 的分页对象提取数据，适用于用 JPA 做分页时
     *
     * @param <T> 数据类型
     * @param page 分页对象
     * @return PageResponse
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return new PageResponse<>(page.getNumber() + 1, // Spring Data的页码从0开始，转换为从1开始
                page.getSize(), page.getTotalElements(), page.getTotalPages(), page.getContent(),
                page.isFirst(), page.isLast(), page.isEmpty());
    }

    /**
     * 自定义PageResponse 只要有数据列表和总数等信息即可构建，适合非 JPA 场景
     *
     * @param <T> 数据类型
     * @param data 数据列表
     * @param totalRecords 总记录数
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @return PageResponse
     */
    public static <T> PageResponse<T> of(List<T> data, long totalRecords, int currentPage,
            int pageSize) {
        long totalPages = (long) Math.ceil((double) totalRecords / pageSize);
        boolean first = currentPage == 1;
        boolean last = currentPage >= totalPages;
        boolean empty = data == null || data.isEmpty();

        return new PageResponse<>(currentPage, pageSize, totalRecords, totalPages, data, first,
                last, empty);
    }

    /**
     * 创建空的分页响应
     *
     * @param <T> 数据类型
     * @param currentPage 当前页码
     * @param pageSize 每页大小
     * @return PageResponse
     */
    public static <T> PageResponse<T> empty(int currentPage, int pageSize) {
        return new PageResponse<>(currentPage, pageSize, 0, 0, List.of(), true, true, true);
    }

    // Getter and Setter methods

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public String toString() {
        return "PageResponse{" + "currentPage=" + currentPage + ", pageSize=" + pageSize
                + ", totalRecords=" + totalRecords + ", totalPages=" + totalPages + ", data=" + data
                + ", first=" + first + ", last=" + last + ", empty=" + empty + '}';
    }
}