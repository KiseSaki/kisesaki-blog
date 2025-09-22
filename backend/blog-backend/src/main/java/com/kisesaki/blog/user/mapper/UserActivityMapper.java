package com.kisesaki.blog.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.user.dto.admin.AdminUserActivityParams;
import com.kisesaki.blog.user.dto.admin.AdminUserActivityResponse;
import com.kisesaki.blog.user.entity.UserActivity;

/**
 * 用户活动日志数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserActivityMapper extends BaseMapper<UserActivity> {

    /**
     * 管理员获取用户活动日志列表
     *
     * @param page   分页信息
     * @param userId 用户ID
     * @param params 查询参数
     * @return 活动日志列表
     */
    Page<AdminUserActivityResponse> adminGetUserActivityList(Page<AdminUserActivityResponse> page,
            @Param("userId") Long userId, @Param("params") AdminUserActivityParams params);

    /**
     * 管理员获取用户活动日志总数
     * 
     * @param userId 用户ID
     * @param params 查询参数
     * @return 活动日志总数
     */
    Long adminGetUserActivityCount(@Param("userId") Long userId, @Param("params") AdminUserActivityParams params);
}