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
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // 配置请求授权规则
                                .authorizeHttpRequests(authz -> authz
                                                // 静态资源和系统接口：完全公开
                                                .requestMatchers(
                                                                // Swagger 文档接口
                                                                "/swagger-ui/**",
                                                                "/v3-docs/**",
                                                                "/swagger-resources/**",
                                                                "/webjars/**",
                                                                // 静态资源
                                                                "/favicon.ico",
                                                                "/error",
                                                                "/uploads/**", // 文件访问路径
                                                                "/files/**")
                                                .permitAll()

                                                // ==================== 认证系统接口 ====================
                                                // 本地认证：完全公开
                                                .requestMatchers(
                                                                "/auth/register",
                                                                "/auth/login",
                                                                "/auth/refresh",
                                                                "/auth/forgot-password",
                                                                "/auth/reset-password",
                                                                "/auth/verify-email",
                                                                "/auth/resend-verification")
                                                .permitAll()

                                                // OAuth2 认证：完全公开
                                                .requestMatchers(
                                                                "/auth/oauth/*/authorize",
                                                                "/auth/oauth/*/callback")
                                                .permitAll()

                                                // 会话管理：需要登录
                                                .requestMatchers(
                                                                "/auth/logout",
                                                                "/auth/sessions/**",
                                                                "/auth/me")
                                                .authenticated()

                                                // OAuth2 绑定：需要登录
                                                .requestMatchers(
                                                                "/auth/oauth/*/bind",
                                                                "/auth/oauth/*/unbind",
                                                                "/auth/oauth/linked")
                                                .authenticated()

                                                // ==================== 文章系统接口 ====================
                                                // 公开访问的文章接口（PostQueryService）
                                                .requestMatchers(HttpMethod.GET,
                                                                "/posts",
                                                                "/posts/*",
                                                                "/posts/slug/*",
                                                                "/categories/*/posts",
                                                                "/tags/*/posts",
                                                                "/posts/featured",
                                                                "/posts/recent",
                                                                "/posts/popular",
                                                                "/posts/search")
                                                .permitAll()

                                                // 文章浏览统计：公开
                                                .requestMatchers(HttpMethod.POST, "/posts/*/view")
                                                .permitAll()

                                                // 用户创作接口：需要登录
                                                .requestMatchers(
                                                                "/posts/my/**",
                                                                "/posts/*/preview",
                                                                "/posts/*/meta",
                                                                "/posts/*/meta/*")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.POST, "/posts")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.PUT,
                                                                "/posts/*",
                                                                "/posts/*/publish",
                                                                "/posts/*/unpublish",
                                                                "/posts/*/meta")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/posts/*",
                                                                "/posts/*/meta/*")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.POST, "/posts/*/duplicate")
                                                .authenticated()

                                                // 文章版本管理：需要登录
                                                .requestMatchers("/posts/*/revisions/**")
                                                .authenticated()

                                                // 文章交互：点赞需要登录
                                                .requestMatchers(
                                                                "/posts/*/like")
                                                .authenticated()

                                                // ==================== 分类标签接口 ====================
                                                // 分类公开接口
                                                .requestMatchers(HttpMethod.GET,
                                                                "/categories",
                                                                "/categories/*",
                                                                "/categories/slug/*",
                                                                "/categories/popular")
                                                .permitAll()

                                                // 标签公开接口
                                                .requestMatchers(HttpMethod.GET,
                                                                "/tags",
                                                                "/tags/*",
                                                                "/tags/slug/*",
                                                                "/tags/popular",
                                                                "/tags/cloud")
                                                .permitAll()

                                                // 标签搜索：需要登录
                                                .requestMatchers(HttpMethod.GET, "/tags/search")
                                                .authenticated()

                                                // 用户创建标签：需要登录
                                                .requestMatchers(HttpMethod.POST, "/tags")
                                                .authenticated()

                                                .requestMatchers("/tags/my")
                                                .authenticated()

                                                // ==================== 用户系统接口 ====================
                                                // 用户公开信息
                                                .requestMatchers(HttpMethod.GET,
                                                                "/users/*",
                                                                "/users/*/profile",
                                                                "/users/*/posts",
                                                                "/users/*/stats",
                                                                "/users/search",
                                                                "/users/popular",
                                                                "/users/recent")
                                                .permitAll()

                                                // 用户资料管理：需要登录
                                                .requestMatchers(
                                                                "/users/profile",
                                                                "/users/avatar",
                                                                "/users/cover",
                                                                "/users/password",
                                                                "/users/dashboard",
                                                                "/users/settings/**")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.DELETE, "/users/account")
                                                .authenticated()

                                                // 用户关注功能：需要登录
                                                .requestMatchers(
                                                                "/users/*/follow",
                                                                "/users/following/posts",
                                                                "/users/recommendations")
                                                .authenticated()

                                                // ==================== 评论系统接口 ====================
                                                // 公开访问评论
                                                .requestMatchers(HttpMethod.GET,
                                                                "/posts/*/comments",
                                                                "/comments/*",
                                                                "/comments/*/replies")
                                                .permitAll()

                                                // 评论操作：需要登录
                                                .requestMatchers(
                                                                "/comments/my",
                                                                "/comments/*/report")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.POST,
                                                                "/posts/*/comments",
                                                                "/comments/*/like",
                                                                "/comments/*/dislike")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.PUT, "/comments/*")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.DELETE,
                                                                "/comments/*",
                                                                "/comments/*/like",
                                                                "/comments/*/dislike")
                                                .authenticated()

                                                // ==================== 点赞收藏关注接口 ====================
                                                // 公开查看点赞收藏
                                                .requestMatchers(HttpMethod.GET,
                                                                "/posts/*/likes",
                                                                "/posts/*/favorites",
                                                                "/users/*/likes",
                                                                "/users/*/favorites",
                                                                "/users/*/followers",
                                                                "/users/*/following")
                                                .permitAll()

                                                // 点赞收藏操作：需要登录
                                                .requestMatchers(
                                                                "/posts/*/favorite",
                                                                "/users/favorites")
                                                .authenticated()

                                                // ==================== 媒体资源接口 ====================
                                                // 文件上传：需要登录
                                                .requestMatchers("/media/**")
                                                .authenticated()

                                                // ==================== 搜索和统计接口 ====================
                                                // 搜索：公开
                                                .requestMatchers("/search/**")
                                                .permitAll()

                                                // 公开统计
                                                .requestMatchers(HttpMethod.GET, "/stats/overview")
                                                .permitAll()

                                                // 浏览统计：公开
                                                .requestMatchers(
                                                                "/analytics/view",
                                                                "/analytics/event",
                                                                "/analytics/popular")
                                                .permitAll()

                                                .requestMatchers(HttpMethod.GET, "/posts/*/views")
                                                .permitAll()

                                                // ==================== 通知订阅接口 ====================
                                                // 通知：需要登录
                                                .requestMatchers("/notifications/**")
                                                .authenticated()

                                                // 订阅：部分公开
                                                .requestMatchers(
                                                                "/subscriptions/newsletter",
                                                                "/subscriptions/unsubscribe/*",
                                                                "/subscriptions/verify")
                                                .permitAll()

                                                .requestMatchers("/subscriptions/**")
                                                .authenticated()

                                                // ==================== 系统配置接口 ====================
                                                // 公开系统设置
                                                .requestMatchers(HttpMethod.GET, "/settings/public")
                                                .permitAll()

                                                // SEO 元数据：公开
                                                .requestMatchers(HttpMethod.GET, "/seo/meta/*")
                                                .permitAll()

                                                // ==================== 举报接口 ====================
                                                // 举报：需要登录
                                                .requestMatchers("/reports/**")
                                                .authenticated()

                                                // ==================== 管理员接口 ====================
                                                // 所有管理员接口：需要管理员权限
                                                .requestMatchers("/admin/**")
                                                .hasRole("ADMIN")

                                                // ==================== 其他接口 ====================
                                                // 其他所有请求都需要认证
                                                .anyRequest().authenticated())

                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .successHandler(oauth2AuthenticationSuccessHandler))
                                // 异常处理配置
                                .exceptionHandling(ex -> ex
                                                // 未认证时的处理
                                                // .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                // 权限不足时的处理
                                                // .accessDeniedHandler(jwtAccessDeniedHandler)
                                                .authenticationEntryPoint((request, response, authException) -> {
                                                        log.warn("Unauthorized access attempt: {}",
                                                                        authException.getMessage());
                                                        response.setStatus(401);
                                                        response.getWriter().write(
                                                                        "{\"code\":401,\"message\":\"Unauthorized\"}");
                                                })
                                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                                        log.warn("Access denied: {}",
                                                                        accessDeniedException.getMessage());
                                                        response.setStatus(403);
                                                        response.getWriter().write(
                                                                        "{\"code\":403,\"message\":\"Access denied\"}");
                                                }));

                // 添加 JWT 认证过滤器
                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                log.info("Security filter chain configured successfully");
                return http.build();
        }
}
