package com.kisesaki.blog.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JWT认证响应DTO 用于封装JWT令牌的响应对象
 *
 * @author KiseSaki
 */
@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
}
