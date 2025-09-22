package com.kisesaki.blog.user.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.user.dto.admin.AdminUserListParams;
import com.kisesaki.blog.user.dto.admin.AdminUserListResponse;
import com.kisesaki.blog.user.dto.admin.AdminUserStatsResponse.DailyUserStats;
import com.kisesaki.blog.user.entity.User;

/**
 * 用户数据访问层
 * 
 * @author KiseSaki
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据 OAuth 提供商和 ID 查找用户
     * 
     * @param oauthProvider OAuth 提供商
     * @param oauthId       OAuth ID
     * @return 用户信息
     */
    Optional<User> findByOAuth(@Param("oauthProvider") String oauthProvider, @Param("oauthId") String oauthId);

    /**
     * 管理员获取用户列表
     *
     * @param page   分页信息
     * @param params 查询参数
     * @return 用户列表
     */
    Page<AdminUserListResponse> adminGetUserList(Page<AdminUserListResponse> page,
            @Param("params") AdminUserListParams params);

    /**
     * 管理员获取用户列表对应的总数
     * 
     * @param params 查询参数
     * @return 用户总数
     */
    Long adminGetUserListCount(@Param("params") AdminUserListParams params);

    /**
     * 获取最近N天的用户注册趋势
     * 
     * @param days 天数
     * @return 每日注册统计
     */
    List<DailyUserStats> getRegistrationTrend(@Param("days") Integer days);

    /**
     * 获取最近N天的用户登录趋势
     * 
     * @param days 天数
     * @return 每日登录统计
     */
    List<DailyUserStats> getLoginTrend(@Param("days") Integer days);

    /**
     * 按账号类型分组统计
     * 
     * @return 账号类型统计
     */
    List<Map<String, Object>> getAccountTypeStats();

    /**
     * 按状态分组统计
     * 
     * @return 状态统计
     */
    List<Map<String, Object>> getStatusStats();
}
