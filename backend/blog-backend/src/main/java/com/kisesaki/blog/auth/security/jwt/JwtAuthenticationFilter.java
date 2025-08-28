package com.kisesaki.blog.auth.security.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kisesaki.blog.auth.security.user.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 认证过滤器。
 * 这个过滤器在 Spring Security 的标准认证过滤器之前执行。
 * 它负责拦截所有请求，检查是否存在有效的 JWT Token，
 * 如果存在，则设置安全上下文（SecurityContext）。
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            // 请求中提取 JWT
            String jwt = extractJwtFromRequest(request);

            // 验证 JWT 是否合法
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 解析出用户名
                String username = jwtTokenProvider.getUsernameFromToken(jwt);

                // 加载用户信息
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // 创建Authentication对象，并设置到安全上下文中
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // 将请求的详细信息（如IP地址、SessionID）设置到 Authentication 对象中
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 将 Authentication 对象设置到 SecurityContextHolder 的上下文中
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            // 如果在处理过程中发生任何异常，记录错误日志。
            // 不会中断请求，而是简单地不设置 SecurityContext，后续的授权检查自然会失败。
            logger.error("无法在安全上下文中设置用户认证信息", e);
        }
        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从 HTTP 请求头中提取 JWT。
     * 
     * @param request HTTP 请求
     * @return 提取出的 JWT 字符串，或 null
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // 请求头 "Authorization" 的值通常是 "Bearer <token>"
        String bearerToken = request.getHeader("Authorization");

        // 检查请求头是否存在，并且是否以 "Bearer " 开头
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // 截取 "Bearer " 后面的部分，即真正的 Token
            return bearerToken.substring(7);
        }
        return null;
    }
}
