package com.kisesaki.blog.auth.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // 注入JWT密钥
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private SecretKey secretKey;

    /**
     * 初始化secretKey
     */
    @PostConstruct
    public void init() {
        // 将 Base64 编码的字符串密钥解码成字节数组
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        // 使用解码后的字节数组创建一个 HMAC-SHA 密钥对象，用于签名和验证
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 创建访问令牌
     */
    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // 获取用户的权限信息
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 构建Token
        return Jwts.builder()
                // 使用用户名设置标识符
                .subject(authentication.getName())
                // 设置角色信息
                .claim("roles", roles)
                // 设置过期时间
                .expiration(expiryDate)
                // 签名
                .signWith(secretKey)
                .compact();
    }

    /**
     * 验证访问令牌
     */
    public boolean validateToken(String token) {
        try {
            // JWT验证链式调用解析：
            // 1. Jwts.parser() - 创建JWT解析器构建器实例
            // 2. verifyWith(secretKey) - 设置用于验证JWT签名的密钥
            // 3. build() - 根据配置构建最终的JWT解析器
            // 4. parseSignedClaims(token) - 解析并验证JWT令牌（验证结构、签名、过期时间等）
            // 如果签名不正确或格式错误，会抛出相应异常
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            // SecurityException: 签名无效时抛出 (例如，密钥不匹配)
            // MalformedJwtException: Token 结构不正确
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            // Token 已过期
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            // Token 的格式或类型不被支持
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // 参数不合法，例如 Token 字符串为空或 null
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    /**
     * 从 JWT Token 中解析出用户名（Subject）。
     *
     * @param token JWT 字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        // 使用解析器解析 Token
        return Jwts.parser()
                // 指定用于验证签名的密钥
                .verifyWith(secretKey)
                // 构建解析器
                .build()
                // 解析签名的 Token (JWS)，这会验证签名
                .parseSignedClaims(token)
                // 获取 Token 的载荷 (payload) 部分
                .getPayload()
                // 获取 subject 声明
                .getSubject();
    }

    /**
     * 创建刷新令牌
     * @param authentication 认证信息
     * @return 刷新令牌
     */
    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
