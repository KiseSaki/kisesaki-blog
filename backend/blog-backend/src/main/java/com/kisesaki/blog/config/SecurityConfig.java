package com.kisesaki.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.kisesaki.blog.auth.security.jwt.JwtAuthenticationFilter;
import com.kisesaki.blog.auth.security.oauth2.CustomOAuth2UserService;
import com.kisesaki.blog.auth.security.oauth2.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 安全配置类
 * <p>
 * 功能特性：
 * 1. JWT 无状态认证机制
 * 2. OAuth2 社交登录支持（GitHub/Gitee）
 * 3. RBAC 权限控制（角色-权限模型）
 * 4. 公开接口与受保护接口的访问控制
 * 5. CORS 跨域支持
 * 6. 密码加密策略
 * <p>
 * 安全策略：
 * - 关闭 CSRF（因为使用 JWT）
 * - 无状态会话管理
 * - 自定义认证过滤器
 * - 统一异常处理
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 启用方法级权限控制
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

    // @Autowired
    // private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // @Autowired
    // private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 密码编码器配置
     * <p>
     * 使用 BCrypt 加密算法，强度为 12
     * BCrypt 是目前推荐的密码哈希算法，具有以下优势：
     * - 自适应成本：可以随着硬件性能提升调整强度
     * - 内置盐值：每次加密都会生成随机盐值
     * - 单向不可逆：无法从哈希值反推原密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Initializing BCryptPasswordEncoder with strength 12");
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 认证管理器配置
     * <p>
     * 用于处理用户认证逻辑
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * 安全过滤器链配置
     * <p>
     * 这是 Spring Security 的核心配置，定义了：
     * - 哪些接口需要认证
     * - 哪些接口可以公开访问
     * - 认证和授权的处理方式
     * - 跨域、会话管理等安全策略
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF，因为使用 JWT 进行无状态认证
                .csrf(AbstractHttpConfigurer::disable)

                // 配置跨域
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 配置会话管理：无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 配置请求授权规则
                .authorizeHttpRequests(authz -> authz
                        // 公开接口：不需要认证即可访问
                        .requestMatchers(
                                // Swagger 文档接口
                                "/swagger-ui/**",
                                "/v3-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",

                                // 静态资源
                                "/favicon.ico",
                                "/error",

                                // 认证相关公开接口
                                "/auth/login",
                                "/auth/register",
                                "/auth/refresh-token",
                                "/auth/forgot-password",
                                "/auth/reset-password",

                                // OAuth2 相关接口
                                "/oauth2/**",
                                "/login/oauth2/**",

                                // 博客公开接口
                                "/posts/public/**",
                                "/categories/public/**",
                                "/tags/public/**",
                                "/archive/**",
                                "/search/**")
                        .permitAll()

                        // 公开的 HTTP 方法
                        .requestMatchers(HttpMethod.GET,
                                "/posts",
                                "/posts/{id:\\d+}",
                                "/categories",
                                "/tags")
                        .permitAll()

                        // 管理员接口：需要 ADMIN 角色
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 用户相关接口：需要认证
                        .requestMatchers(
                                "/users/**",
                                "/posts/create",
                                "/posts/update/**",
                                "/posts/delete/**",
                                "/comments/**",
                                "/likes/**",
                                "/favorites/**")
                        .authenticated()

                        // 其他所有请求都需要认证
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oauth2AuthenticationSuccessHandler))
                // 异常处理配置
                .exceptionHandling(ex -> ex
                        // 未认证时的处理
                        // .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        // 权限不足时的处理
                        // .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Unauthorized access attempt: {}", authException.getMessage());
                            response.setStatus(401);
                            response.getWriter().write("{\"code\":401,\"message\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Access denied: {}", accessDeniedException.getMessage());
                            response.setStatus(403);
                            response.getWriter().write("{\"code\":403,\"message\":\"Access denied\"}");
                        }));

        // 添加 JWT 认证过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("Security filter chain configured successfully");
        return http.build();
    }
}
