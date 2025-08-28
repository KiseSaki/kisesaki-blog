package com.kisesaki.blog.auth.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 负责 Refresh Token 的创建、存储、验证和删除。
 * 所有 Refresh Token 的操作都应通过此服务进行。
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpirationMs;

    /**
     * 创建并存储 Refresh Token
     *
     * @param authentication 认证信息
     * @return 生成的 Refresh Token
     */
    public String createAndStoreRefreshToken(Authentication authentication) {
        String username = authentication.getName();
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        // 将 Refresh Token 存储在 Redis 中，设置过期时间
        stringRedisTemplate.opsForValue().set(
                username, refreshToken, refreshTokenExpirationMs,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    /**
     * 验证 Refresh Token 的有效性
     *
     * @param username     用户名
     * @param refreshToken 提供的 Refresh Token
     * @return 如果有效则返回 true，否则返回 false
     */
    public boolean validateRefreshToken(String username, String refreshToken) {
        // 从 Redis 中获取存储的 Refresh Token
        String storedToken = stringRedisTemplate.opsForValue().get(username);
        // 验证提供的 Token 是否与存储的 Token 匹配，并且检查其有效性
        return refreshToken.equals(storedToken) && jwtTokenProvider.validateToken(refreshToken);
    }

    /**
     * 删除存储的 Refresh Token
     *
     * @param username 用户名
     */
    public void deleteRefreshTokenByUsername(String username) {
        stringRedisTemplate.delete(username);
    }
}
