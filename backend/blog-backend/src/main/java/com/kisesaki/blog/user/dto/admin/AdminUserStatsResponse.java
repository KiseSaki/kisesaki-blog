package com.kisesaki.blog.user.dto.admin;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员获取用户统计数据响应")
public class AdminUserStatsResponse {

    @Schema(description = "总用户数")
    private Long totalUsers;

    @Schema(description = "活跃用户数")
    private Long activeUsers;

    @Schema(description = "非活跃用户数")
    private Long inactiveUsers;

    @Schema(description = "被封禁用户数")
    private Long bannedUsers;

    @Schema(description = "今日新注册用户数")
    private Long todayRegistrations;

    @Schema(description = "本周新注册用户数")
    private Long weekRegistrations;

    @Schema(description = "本月新注册用户数")
    private Long monthRegistrations;

    @Schema(description = "OAuth用户数")
    private Long oauthUsers;

    @Schema(description = "本地账号用户数")
    private Long localUsers;

    @Schema(description = "邮箱验证用户数")
    private Long emailVerifiedUsers;

    @Schema(description = "最近7天用户注册趋势")
    private List<DailyUserStats> registrationTrend;

    @Schema(description = "最近7天用户登录趋势")
    private List<DailyUserStats> loginTrend;

    @Schema(description = "按账号类型分组统计")
    private Map<String, Long> accountTypeStats;

    @Schema(description = "按状态分组统计")
    private Map<String, Long> statusStats;

    @Schema(description = "统计生成时间")
    private OffsetDateTime generatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "每日用户统计")
    public static class DailyUserStats {

        @Schema(description = "日期")
        private String date;

        @Schema(description = "数量")
        private Long count;
    }

}