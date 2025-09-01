package com.kisesaki.blog.auth;

import java.util.Set;

import com.kisesaki.blog.auth.dto.request.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.response.LoginResponseDto;
import com.kisesaki.blog.common.dto.ApiResponse;

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
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request) {
        ApiResponse<LoginResponseDto> response = authService.login(loginRequestDto, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        ApiResponse<String> response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    @Operation(summary = "验证邮箱", description = "使用邮箱验证令牌验证用户的邮箱")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@Valid @RequestBody VerifyEmailRequestDto verifyEmailRequest) {
        ApiResponse<String> response = authService.verifyEmail(verifyEmailRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public ResponseEntity<ApiResponse<LoginResponseDto>> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto,
            HttpServletRequest request) {
        ApiResponse<LoginResponseDto> response = authService.refreshToken(refreshTokenRequestDto, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出当前设备")
    public ResponseEntity<ApiResponse<String>> logout(
            @Valid @RequestBody LogoutRequestDto logoutRequest,
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        ApiResponse<String> response = authService.logout(username,
                logoutRequest.getRefreshToken(), logoutRequest.getDeviceId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout-all")
    @Operation(summary = "登出所有设备", description = "用户登出所有已登录的设备")
    public ResponseEntity<ApiResponse<String>> logoutAllDevices(Authentication authentication) {
        String username = authentication.getName();
        ApiResponse<String> response = authService.logoutAllDevices(username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/devices/{deviceId}")
    @Operation(summary = "踢出指定设备", description = "管理员或用户踢出指定设备")
    public ResponseEntity<ApiResponse<String>> kickDevice(
            @PathVariable String deviceId,
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        ApiResponse<String> response = authService.kickDevice(username, deviceId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/devices")
    @Operation(summary = "获取用户设备列表", description = "获取当前用户所有已登录的设备")
    public ResponseEntity<ApiResponse<Set<String>>> getUserDevices(Authentication authentication) {
        String username = authentication.getName();
        ApiResponse<Set<String>> response = authService.getUserDevices(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/clean-expired")
    @Operation(summary = "清理过期令牌", description = "清理用户的过期令牌")
    public ResponseEntity<ApiResponse<String>> cleanExpiredTokens(
            Authentication authentication,
            HttpServletRequest request) {
        String username = authentication.getName();
        ApiResponse<String> response = authService.cleanExpiredTokens(username, request);
        return ResponseEntity.ok(response);
    }
}
