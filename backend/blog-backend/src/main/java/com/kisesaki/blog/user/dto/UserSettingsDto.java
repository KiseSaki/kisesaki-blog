package com.kisesaki.blog.user.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户设置 DTO
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "用户设置")
public class UserSettingsDto {

    /** 用户设置键值对映射 */
    @NotEmpty(message = "设置不能为空")
    @Schema(description = "用户设置键值对映射", example = "{\"theme\": \"dark\", \"language\": \"zh-CN\", \"emailNotifications\": \"true\"}")
    private Map<String, String> settings;
}
