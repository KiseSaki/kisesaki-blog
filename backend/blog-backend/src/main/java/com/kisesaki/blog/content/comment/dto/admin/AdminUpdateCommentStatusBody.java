package com.kisesaki.blog.content.comment.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新评论状态请求")
public class AdminUpdateCommentStatusBody {
    @NotBlank(message = "状态不能为空")
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|SPAM)$", message = "状态只能是：PENDING、APPROVED、REJECTED、SPAM")
    @Schema(description = "评论状态", example = "APPROVED", allowableValues = {"PENDING", "APPROVED", "REJECTED", "SPAM"}, required = true)
    private String status;
}