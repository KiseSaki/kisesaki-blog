package com.kisesaki.blog.content.interaction.mapper;

import java.time.OffsetDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.interaction.entity.PageViews;

/**
 * 页面浏览记录数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface PageViewsMapper extends BaseMapper<PageViews> {

        /**
         * 统计文章的独立访客数（根据IP地址去重）
         * 需要使用 COUNT(DISTINCT) 的复杂查询
         *
         * @param postId 文章ID
         * @return 独立访客数
         */
        long countUniqueViewsByPost(@Param("postId") Long postId);

        /**
         * 检查同一会话是否已经访问过该页面（用于去重）
         * 需要使用时间计算的复杂查询
         *
         * @param sessionId 会话ID
         * @param pageUrl   页面URL
         * @param timeLimit 时间限制（分钟内不重复记录）
         * @return 记录数
         */
        int countSessionPageView(@Param("sessionId") String sessionId,
                        @Param("pageUrl") String pageUrl,
                        @Param("timeLimit") int timeLimit);

        /**
         * 批量插入浏览记录（用于批量处理）
         * 复杂的批量插入操作
         *
         * @param pageViewsList 浏览记录列表
         * @return 插入的记录数
         */
        int batchInsertViews(@Param("list") List<PageViews> pageViewsList);

        /**
         * 统计指定时间范围内的热门文章（按浏览量排序）
         * 需要 GROUP BY 和 ORDER BY 的复杂查询
         *
         * @param startTime 开始时间
         * @param endTime   结束时间
         * @param limit     返回数量限制
         * @return 文章ID和浏览量的列表
         */
        List<Object[]> selectHotPostsByTimeRange(@Param("startTime") OffsetDateTime startTime,
                        @Param("endTime") OffsetDateTime endTime,
                        @Param("limit") int limit);
}