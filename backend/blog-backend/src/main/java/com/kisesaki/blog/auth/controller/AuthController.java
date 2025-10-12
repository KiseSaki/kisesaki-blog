package com.kisesaki.blog.auth.controller;

import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.auth.request.ChangePasswordRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.ForgotPasswordRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.LoginRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.LogoutRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.RefreshTokenRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.RegisterRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.ResetPasswordRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.VerifyEmailRequestDto;
import com.kisesaki.blog.auth.dto.auth.response.LoginResponseDto;
import com.kisesaki.blog.auth.service.AuthService;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.ResultUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * 认证控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录、注册等认证相关接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户使用用户名和密码进行登录，支持设备管理")
    public ApiResponse<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request) {
        LoginResponseDto response = authService.login(loginRequestDto, request);
        return ResultUtils.success("登录成功", response);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public ApiResponse<String> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        String response = authService.register(registerRequest);
        return ResultUtils.success("注册成功", response);
    }

    @PostMapping("/verify-email")
    @Operation(summary = "验证邮箱", description = "使用邮箱验证令牌验证用户的邮箱")
    public ApiResponse<Void> verifyEmail(
            @Valid @RequestBody VerifyEmailRequestDto verifyEmailRequest) {
        authService.verifyEmail(verifyEmailRequest);
        return ResultUtils.success("邮箱验证成功");
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public ApiResponse<LoginResponseDto> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto,
            HttpServletRequest request) {
        LoginResponseDto response = authService.refreshToken(refreshTokenRequestDto, request);
        return ResultUtils.success("访问令牌刷新成功", response);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "用户登出", description = "用户登出当前设备")
    public ApiResponse<Void> logout(
            @Valid @RequestBody LogoutRequestDto logoutRequest,
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        authService.logout(username,
                logoutRequest.getRefreshToken(), logoutRequest.getDeviceId(), request);
        return ResultUtils.success("登出成功");
    }

    @PostMapping("/logout-all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "登出所有设备", description = "用户登出所有已登录的设备")
    public ApiResponse<Void> logoutAllDevices(Authentication authentication) {
        String username = authentication.getName();
        authService.logoutAllDevices(username);
        return ResultUtils.success("已登出所有设备");
    }

    @DeleteMapping("/devices/{deviceId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "踢出指定设备", description = "管理员或用户踢出指定设备")
    public ApiResponse<Void> kickDevice(
            @PathVariable String deviceId,
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        authService.kickDevice(username, deviceId, request);
        return ResultUtils.success("设备已被踢出");
    }

    @GetMapping("/devices")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取用户设备列表", description = "获取当前用户所有已登录的设备")
    public ApiResponse<Set<String>> getUserDevices(Authentication authentication) {
        String username = authentication.getName();
        Set<String> response = authService.getUserDevices(username);
        return ResultUtils.success("获取设备列表成功", response);
    }

    @PostMapping("change-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "修改密码", description = "用户修改自己的登录密码")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto changePasswordRequest,
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        authService.changePassword(username, changePasswordRequest, request);
        return ResultUtils.success("密码修改成功");
    }

    @PostMapping("forgot-password")
    @Operation(summary = "忘记密码", description = "用户通过邮箱重置登录密码")
    public ApiResponse<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        authService.forgotPassword(forgotPasswordRequestDto);
        return ResultUtils.success("密码重置邮件已发送，请检查您的邮箱");
    }

    @PostMapping("/reset-password")
    @Operation(summary = "确认重置密码", description = "用户通过邮箱收到的令牌确认重置密码")
    public ApiResponse<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ResultUtils.success("密码重置成功");
    }

    @PostMapping("/clean-expired")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "清理过期令牌", description = "清理用户的过期令牌")
    public ApiResponse<Void> cleanExpiredTokens(
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        authService.cleanExpiredTokens(username, request);
        return ResultUtils.success("清理过期令牌成功");
    }
}
