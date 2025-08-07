package com.kisesaki.blog.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置类
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Configuration
public class CorsConfig {

    /**
     * 跨域配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许的源地址
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", // 前端开发服务器
                "http://localhost:5173", // Vite 默认端口
                "http://127.0.0.1:*", // 本地开发
                "https://*.kisesaki.com", // 生产环境域名
                "https://*.netlify.app", // Netlify 部署
                "https://*.vercel.app" // Vercel 部署
        ));

        // 允许的请求方法
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 允许的请求头
        configuration.setAllowedHeaders(List.of("*"));

        // 允许携带认证信息
        configuration.setAllowCredentials(true);

        // 预检请求的缓存时间（秒）
        configuration.setMaxAge(3600L);

        // 暴露的响应头
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition",
                "X-Total-Count", "X-Current-Page"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
