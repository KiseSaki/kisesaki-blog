package com.kisesaki.blog.content.post.dto.PostCommand;

import com.kisesaki.blog.content.post.dto.BasePostDto;
import com.kisesaki.blog.content.post.dto.PostValidationGroups;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 更新文章请求DTO
 *
 * @author KiseSaki
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "更新文章请求")
public class UpdatePostRequest extends BasePostDto {

    @NotNull(groups = PostValidationGroups.Update.class, message = "文章ID不能为空")
    @Schema(description = "文章ID", example = "1")
    private Long id;

    @Schema(description = "是否创建新版本", example = "false")
    private Boolean createRevision = false;

    @Schema(description = "版本更新说明")
    private String revisionNote;

    /**
     * 验证分组标记接口
     */
    public interface ValidationGroups extends PostValidationGroups.Update {
    }
}
