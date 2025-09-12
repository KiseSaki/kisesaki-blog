package com.kisesaki.blog.auth.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登出请求DTO
 * 
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDto {

    @NotBlank(message = "Refresh Token 不能为空")
    private String refreshToken;

    /**
     * 设备ID（可选）
     * 提供时会删除指定设备的token，不提供则尝试匹配所有设备
     */
    private String deviceId;
}
