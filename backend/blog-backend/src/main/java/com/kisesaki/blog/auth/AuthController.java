package com.kisesaki.blog.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.request.LoginRequestDto;
import com.kisesaki.blog.auth.dto.request.RefreshTokenRequestDto;
import com.kisesaki.blog.auth.dto.request.RegisterRequestDto;
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

    @PostMapping("/refreshToken")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public ResponseEntity<ApiResponse<LoginResponseDto>> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        ApiResponse<LoginResponseDto> response = authService.refreshToken(refreshTokenRequestDto);
        return ResponseEntity.ok(response);
    }
}
