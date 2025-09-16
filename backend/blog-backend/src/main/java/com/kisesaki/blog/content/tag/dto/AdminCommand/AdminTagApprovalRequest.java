package com.kisesaki.blog.content.tag.dto.AdminCommand;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签审核请求")
public class AdminTagApprovalRequest {
    @NotNull(message = "审核状态不能为空")
    @Pattern(regexp = "^(approved|rejected)$", message = "审核状态只能是 approved 或 rejected")
    @Schema(description = "审核状态", example = "approved", allowableValues = { "approved", "rejected" }, required = true)
    private String status;

    @Size(max = 200, message = "审核备注长度不能超过200字符")
    @Schema(description = "审核备注", example = "标签符合规范，审核通过")
    private String note;
}