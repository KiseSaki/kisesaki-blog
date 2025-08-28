package com.kisesaki.blog.auth.dto.response;

/**
 * JWT认证响应DTO 用于封装JWT令牌的响应对象
 *
 * @author KiseSaki
 */
public class JwtAuthenticationResponse {

    /**
     * JWT访问令牌
     */
    private String token;

    /**
     * 构造函数
     *
     * @param token JWT访问令牌
     */
    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    /**
     * 获取JWT访问令牌
     *
     * @return JWT访问令牌
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置JWT访问令牌
     *
     * @param token JWT访问令牌
     */
    public void setToken(String token) {
        this.token = token;
    }
}
