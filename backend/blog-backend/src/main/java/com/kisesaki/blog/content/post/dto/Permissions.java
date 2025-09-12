package com.kisesaki.blog.content.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "权限信息")
public class Permissions {
    @Schema(description = "是否可编辑")
    private Boolean canEdit;

    @Schema(description = "是否可删除")
    private Boolean canDelete;

    @Schema(description = "是否可评论")
    private Boolean canComment;
}