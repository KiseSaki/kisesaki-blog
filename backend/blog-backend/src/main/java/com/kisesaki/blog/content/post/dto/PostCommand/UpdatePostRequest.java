package com.kisesaki.blog.content.post.dto.PostCommand;

import com.kisesaki.blog.content.post.dto.BasePostDto;
import com.kisesaki.blog.content.post.dto.PostValidationGroups;

import io.swagger.v3.oas.annotations.media.Schema;
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
