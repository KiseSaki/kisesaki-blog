package com.kisesaki.blog.user.dto.info;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 头像上传 DTO
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "头像上传请求")
public class UploadAvatarDto {

    /** 头像文件 */
    @NotNull(message = "头像文件不能为空")
    @Schema(description = "头像文件", type = "string", format = "binary")
    private MultipartFile file;
}
