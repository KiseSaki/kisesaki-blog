package com.kisesaki.blog.content.post.dto.AdminCommand;

import com.kisesaki.blog.content.post.dto.BasePostDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员创建文章请求DTO
 *
 * @author KiseSaki
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理员创建文章请求")
public class AdminCreatePostRequest extends BasePostDto {

    @NotNull(message = "作者ID不能为空")
    @Schema(description = "作者ID", example = "1")
    private Long authorId;

    @Schema(description = "是否立即发布", example = "false")
    private Boolean publishNow = false;
}
