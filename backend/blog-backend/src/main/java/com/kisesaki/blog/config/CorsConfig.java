package com.kisesaki.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;

/**
 * CORS 跨域配置类
 *
 * 配置跨域资源共享（Cross-Origin Resource Sharing），允许前端应用从不同域名访问后端 API。
 * 
 * 功能特性：
 * 1. 支持多个前端域名配置（开发环境和生产环境）
 * 2. 允许常用的 HTTP 方法和请求头
 * 3. 支持携带认证信息（cookies, authorization headers）
 * 4. 配置预检请求的缓存时间
 * 
 * 安全考虑：
 * - 生产环境应明确指定允许的域名，避免使用通配符
 * - 谨慎开启 allowCredentials，确保前端正确处理认证信息
 */
@Configuration
@Slf4j
public class CorsConfig {

    /**
     * 允许的前端域名列表，支持多环境配置
     * 开发环境通常包括 localhost 的不同端口
     * 生产环境应配置实际的域名
     */
    @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
    private String[] allowedOrigins;

    /**
     * 是否允许携带认证信息
     * 开发环境可以设置为 true，生产环境需要谨慎考虑
     */
    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    /**
     * 预检请求的缓存时间（秒）
     */
    @Value("${app.cors.max-age:3600}")
    private long maxAge;

    /**
     * 配置 CORS 规则
     *
     * @return CorsConfigurationSource CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 设置允许的源
        if (allowedOrigins != null && allowedOrigins.length > 0) {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
            log.info("CORS allowed origins: {}", Arrays.toString(allowedOrigins));
        } else {
            // 如果没有配置，默认允许所有源（仅开发环境）
            configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
            log.warn("CORS allowing all origins - this should only be used in development!");
        }

        // 允许的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));

        // 允许的请求头
        configuration.setAllowedHeaders(Arrays.asList(
                "Accept",
                "Accept-Language",
                "Content-Language",
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "X-CSRF-Token",
                "Cache-Control"));

        // 允许在响应中暴露的头部
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition"));

        // 是否允许携带认证信息（cookies, authorization headers 等）
        configuration.setAllowCredentials(allowCredentials);

        // 预检请求的缓存时间
        configuration.setMaxAge(maxAge);

        // 应用配置到所有路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS configuration initialized - allowCredentials: {}, maxAge: {}s",
                allowCredentials, maxAge);

        return source;
    }
}
