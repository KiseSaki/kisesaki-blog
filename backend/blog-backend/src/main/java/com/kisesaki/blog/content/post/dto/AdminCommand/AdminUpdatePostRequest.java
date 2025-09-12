package com.kisesaki.blog.content.post.dto.AdminCommand;

import com.kisesaki.blog.content.post.dto.BasePostDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 管理员更新文章请求DTO
 *
 * @author KiseSaki
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理员更新文章请求")
public class AdminUpdatePostRequest extends BasePostDto {

    @Schema(description = "作者ID", example = "1")
    private Long authorId;
}
