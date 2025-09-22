package com.kisesaki.blog.user.dto.admin;

import java.time.OffsetDateTime;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员获取用户活动日志参数")
public class AdminUserActivityParams {

    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "操作类型", example = "login")
    private String action;

    @Schema(description = "开始时间")
    private OffsetDateTime fromDate;

    @Schema(description = "结束时间")
    private OffsetDateTime toDate;

}