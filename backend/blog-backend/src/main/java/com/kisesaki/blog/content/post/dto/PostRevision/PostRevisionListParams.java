package com.kisesaki.blog.content.post.dto.PostRevision;

import com.kisesaki.blog.common.dto.PageableParams;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "获取文章版本列表参数")
public class PostRevisionListParams {
    @Valid
    @Schema(description = "分页参数")
    private PageableParams pageable = new PageableParams();

}
