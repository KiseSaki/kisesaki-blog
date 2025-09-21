package com.kisesaki.blog.content.comment.dto.admin;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "批量审核评论请求")
public class AdminBatchModerateBody {
    @NotEmpty(message = "评论ID列表不能为空")
    @Schema(description = "评论ID列表", example = "[1, 2, 3]", required = true)
    private List<Long> ids;

    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|SPAM)$", message = "状态只能是：PENDING、APPROVED、REJECTED、SPAM")
    @Schema(description = "评论状态", example = "APPROVED", allowableValues = {"PENDING", "APPROVED", "REJECTED", "SPAM"}, required = true)
    private String status;
}