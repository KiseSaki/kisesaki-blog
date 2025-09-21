package com.kisesaki.blog.content.comment.dto.interaction;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "举报评论请求")
public class ReportCommentBody {
    @NotBlank(message = "举报原因不能为空")
    @Size(max = 50, message = "举报原因不能超过50字符")
    @Schema(description = "举报原因", example = "垃圾信息", required = true)
    private String reason;

    @Size(max = 500, message = "举报描述不能超过500字符")
    @Schema(description = "举报详细描述", example = "该评论包含恶意链接")
    private String description;
}