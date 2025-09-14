package com.kisesaki.blog.auth.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT认证响应DTO 用于封装JWT令牌的响应对象
 *
 * @author KiseSaki
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    /* 访问令牌 */
    private String accessToken;
    /* 刷新令牌 */
    private String refreshToken;
    /* 过期时间（秒） */
    private long expiresIn;
    /* 设备ID */
    private String deviceId;
}
