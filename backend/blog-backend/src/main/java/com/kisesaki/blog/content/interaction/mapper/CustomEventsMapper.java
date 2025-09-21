package com.kisesaki.blog.content.interaction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kisesaki.blog.content.interaction.entity.CustomEvents;

/**
 * 自定义事件记录数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface CustomEventsMapper extends BaseMapper<CustomEvents> {

    /**
     * 批量插入事件记录
     * 复杂的批量插入操作
     *
     * @param eventsList 事件记录列表
     * @return 插入的记录数
     */
    int batchInsertEvents(@Param("list") List<CustomEvents> eventsList);
}