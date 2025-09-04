package com.kisesaki.blog.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.kisesaki.blog.auth.security.user.CustomUserPrincipal;

/**
 * 认证工具类
 * 用于获取当前登录用户的信息
 * 
 * @author KiseSaki
 */
public class AuthUtils {

    /**
     * 获取当前登录用户的ID
     * 
     * @return 用户ID，如果未登录则返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
            return principal.getId();
        }
        return null;
    }

    /**
     * 获取当前登录用户的用户名
     * 
     * @return 用户名，如果未登录则返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
            return principal.getUsername();
        }
        return null;
    }

    /**
     * 获取当前登录用户的完整信息
     * 
     * @return CustomUserPrincipal对象，如果未登录则返回null
     */
    public static CustomUserPrincipal getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            return (CustomUserPrincipal) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 从Authentication对象中获取用户ID
     * 
     * @param authentication 认证对象
     * @return 用户ID，如果无法获取则返回null
     */
    public static Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
            return principal.getId();
        }
        return null;
    }

    /**
     * 从Authentication对象中获取用户名
     * 
     * @param authentication 认证对象
     * @return 用户名，如果无法获取则返回null
     */
    public static String getUsernameFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
            return principal.getUsername();
        }
        return null;
    }

    /**
     * 检查是否已登录
     * 
     * @return 是否已登录
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserPrincipal;
    }
}
