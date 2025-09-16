package com.kisesaki.blog.content.tag.dto.AdminCommand;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "管理员标签列表请求参数")
public class AdminTagListParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

    @Schema(description = "标签名称，支持模糊搜索", example = "Git")
    private String name;

    @Schema(description = "审核状态过滤", example = "pending", allowableValues = { "pending", "approved", "rejected" })
    private String approvalStatus;

    @Schema(description = "创建者ID过滤", example = "1")
    private Long createdBy;

    @Schema(description = "是否只显示未使用的标签", example = "false")
    private Boolean unusedOnly = false;

    @Schema(description = "最小使用次数过滤", example = "0")
    private Integer minPostCount = 0;

    @Schema(description = "最大使用次数过滤", example = "100")
    private Integer maxPostCount;
}