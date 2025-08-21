package com.kisesaki.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security 安全配置类
 * 
 * @author KiseSaki
 * @since 2025-08-20
 */
@Configuration @EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF 保护（API 开发时）
                .csrf(csrf -> csrf.disable())

                // 配置 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 禁用 OAuth2 登录（暂时）
                .oauth2Login(oauth2 -> oauth2.disable())

                // 配置会话管理（JWT 无状态）
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 配置请求授权
                .authorizeHttpRequests(authz -> authz
                        // 公开接口（不需要认证）
                        .requestMatchers("/test/**", // 测试接口
                                "/actuator/**", // 监控端点
                                "/swagger-ui/**", // Swagger UI
                                "/v3/api-docs/**", // OpenAPI 文档
                                "/auth/**", // 认证相关接口
                                "/public/**", // 公开资源
                                "/h2-console/**" // H2 数据库控制台（开发环境）
                        ).permitAll()

                        // 管理员接口（需要管理员权限）
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 其他所有请求都需要认证
                        .anyRequest().authenticated())

                // 配置异常处理
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write(
                                    "{\"code\":401,\"message\":\"未认证或认证已过期\",\"data\":null}");
                        }).accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter()
                                    .write("{\"code\":403,\"message\":\"权限不足\",\"data\":null}");
                        }));

        // 如果使用 H2 数据库，允许 H2 控制台的 frame
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
