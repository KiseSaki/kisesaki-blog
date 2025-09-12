package com.kisesaki.blog.content.post.dto.PostCommand;

import com.kisesaki.blog.content.post.dto.BasePostDto;
import com.kisesaki.blog.content.post.dto.PostValidationGroups;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建文章请求DTO
 *
 * @author KiseSaki
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "创建文章请求")
public class CreatePostRequest extends BasePostDto {

    // 创建文章时，所有字段的校验都会应用 PostValidationGroups.Create 组
    // 基础字段的校验规则已在 BasePostDto 中定义

    @Schema(description = "是否立即发布", example = "false")
    private Boolean publishNow = false;

    /**
     * 验证分组标记接口
     */
    public interface ValidationGroups extends PostValidationGroups.Create {
    }
}
