package com.kisesaki.blog.common.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.PageableParams;

import lombok.extern.slf4j.Slf4j;

/**
 * 分页查询工具类
 * 
 * <p>
 * 提供通用的分页查询功能，包括：
 * <ul>
 * <li>时间范围查询支持</li>
 * <li>排序规则应用</li>
 * <li>性能优化（可选的总数计算）</li>
 * <li>类型安全的查询构建</li>
 * </ul>
 * 
 * @author KiseSaki
 * @since 1.0.0
 */
@Slf4j
public class PageQueryUtils {

    /**
     * 执行分页查询
     * 
     * @param <T>          实体类型
     * @param <R>          响应类型
     * @param mapper       MyBatis-Plus Mapper
     * @param queryWrapper 查询条件构建器
     * @param pageable     分页参数
     * @param converter    实体到响应对象的转换器
     * @return 分页响应结果
     */
    public static <T, R> PageResponse<R> executePageQuery(
            BaseMapper<T> mapper,
            LambdaQueryWrapper<T> queryWrapper,
            PageableParams pageable,
            Function<T, R> converter) {

        // 根据 includeTotal 参数决定是否计算总数
        Long totalCount = null;
        if (pageable.getIncludeTotal()) {
            totalCount = mapper.selectCount(queryWrapper);
            if (totalCount == 0) {
                return PageResponse.of(List.of(), totalCount, pageable);
            }
        }

        // 构建分页对象
        Page<T> page = new Page<>(pageable.getCurrentPage(), pageable.getPageSize());

        // 执行分页查询
        Page<T> result = mapper.selectPage(page, queryWrapper);

        // 如果没有预先计算总数，则从分页结果中获取
        if (totalCount == null) {
            totalCount = result.getTotal();
        }

        // 转换成响应对象
        List<R> responseList = result.getRecords().stream()
                .map(converter)
                .toList();

        return PageResponse.of(responseList, totalCount, pageable);
    }

    /**
     * 应用时间范围查询条件
     * 
     * @param <T>               实体类型
     * @param queryWrapper      查询条件构建器
     * @param pageable          分页参数
     * @param createdAtFunction 创建时间字段的函数引用
     */
    public static <T> void applyTimeRangeConditions(
            LambdaQueryWrapper<T> queryWrapper,
            PageableParams pageable,
            SFunction<T, OffsetDateTime> createdAtFunction) {

        // 时间范围查询 - 转换为 OffsetDateTime
        if (pageable.getStartTime() != null) {
            OffsetDateTime startTime = convertToOffsetDateTime(pageable.getStartTime());
            queryWrapper.ge(createdAtFunction, startTime);
        }

        if (pageable.getEndTime() != null) {
            OffsetDateTime endTime = convertToOffsetDateTime(pageable.getEndTime());
            queryWrapper.le(createdAtFunction, endTime);
        }

        // 指定日期查询 - 转换为 OffsetDateTime 范围
        if (pageable.getDate() != null) {
            OffsetDateTime dayStart = pageable.getDate().atStartOfDay().atOffset(ZoneOffset.UTC);
            OffsetDateTime dayEnd = pageable.getDate().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
            queryWrapper.between(createdAtFunction, dayStart, dayEnd);
        }
    }

    /**
     * 应用时间范围查询条件（LocalDateTime 版本）
     * 
     * @param <T>               实体类型
     * @param queryWrapper      查询条件构建器
     * @param pageable          分页参数
     * @param createdAtFunction 创建时间字段的函数引用
     */
    public static <T> void applyTimeRangeConditionsForLocalDateTime(
            LambdaQueryWrapper<T> queryWrapper,
            PageableParams pageable,
            SFunction<T, LocalDateTime> createdAtFunction) {

        // 时间范围查询
        if (pageable.getStartTime() != null) {
            queryWrapper.ge(createdAtFunction, pageable.getStartTime());
        }

        if (pageable.getEndTime() != null) {
            queryWrapper.le(createdAtFunction, pageable.getEndTime());
        }

        // 指定日期查询
        if (pageable.getDate() != null) {
            LocalDateTime dayStart = pageable.getDate().atStartOfDay();
            LocalDateTime dayEnd = pageable.getDate().atTime(23, 59, 59);
            queryWrapper.between(createdAtFunction, dayStart, dayEnd);
        }
    }

    /**
     * 应用排序规则的构建器
     * 
     * @param <T> 实体类型
     */
    public static class SortBuilder<T> {
        private final LambdaQueryWrapper<T> queryWrapper;
        private final PageableParams pageable;
        private SFunction<T, ?> defaultSortField;
        private boolean defaultDescending = true;

        public SortBuilder(LambdaQueryWrapper<T> queryWrapper, PageableParams pageable) {
            this.queryWrapper = queryWrapper;
            this.pageable = pageable;
        }

        /**
         * 设置默认排序字段
         * 设置当没有指定排序字段时使用的默认排序
         */
        public SortBuilder<T> defaultSort(SFunction<T, ?> defaultSortField, boolean descending) {
            this.defaultSortField = defaultSortField;
            this.defaultDescending = descending;
            return this;
        }

        /**
         * 添加排序字段映射
         */
        public SortBuilder<T> addSortField(String fieldName, SFunction<T, ?> fieldFunction) {
            // 从分页参数中获取排序字段名
            String sortField = pageable.getSortField();
            // 获取排序方向
            boolean isDescending = pageable.isDescending();

            if (sortField != null && sortField.equals(fieldName)) {
                // 如果当前字段匹配，则应用排序
                queryWrapper.orderBy(true, !isDescending, fieldFunction);
                return this;
            }
            return this;
        }

        /**
         * 应用排序
         */
        public void apply() {
            String sortField = pageable.getSortField();

            // 如果没有找到匹配的排序字段，使用默认排序
            if (sortField == null && defaultSortField != null) {
                if (defaultDescending) {
                    queryWrapper.orderByDesc(defaultSortField);
                } else {
                    queryWrapper.orderByAsc(defaultSortField);
                }
            }
        }
    }

    /**
     * 创建排序构建器
     */
    public static <T> SortBuilder<T> createSortBuilder(LambdaQueryWrapper<T> queryWrapper, PageableParams pageable) {
        return new SortBuilder<>(queryWrapper, pageable);
    }

    /**
     * 转换 LocalDateTime 到 OffsetDateTime
     */
    private static OffsetDateTime convertToOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}