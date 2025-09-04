package com.kisesaki.blog.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户统计信息 DTO
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户统计信息")
public class UserStatsDto {

    /** 用户ID */
    @Schema(description = "用户ID", example = "1234567890")
    private Long userId;

    /** 用户名 */
    @Schema(description = "用户名", example = "kisesaki")
    private String username;

    /** 关注者数量 */
    @Schema(description = "关注者数量", example = "89")
    private Long followersCount;

    /** 关注的人数量 */
    @Schema(description = "关注的人数量", example = "45")
    private Long followingCount;

    /** 加入天数 */
    @Schema(description = "加入天数", example = "365")
    private Long daysSinceJoined;

    /** 活跃度评分 (0-100) */
    @Schema(description = "活跃度评分", example = "85.6")
    private Double activityScore;
}
