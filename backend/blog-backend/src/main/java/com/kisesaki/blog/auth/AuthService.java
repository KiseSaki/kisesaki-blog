package com.kisesaki.blog.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kisesaki.blog.auth.dto.request.LoginRequestDto;
import com.kisesaki.blog.auth.dto.request.RegisterRequestDto;
import com.kisesaki.blog.auth.dto.response.LoginResponseDto;
import com.kisesaki.blog.auth.security.jwt.JwtTokenProvider;
import com.kisesaki.blog.auth.security.jwt.RefreshTokenService;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证服务
 *
 * @author KiseSaki
 */
@Service // Spring框架注解，标识这是一个服务层组件，会被Spring容器自动扫描并注册为Bean
@RequiredArgsConstructor // 自动生成包含所有final字段的构造函数，用于依赖注入
@Slf4j // 自动生成一个名为log的静态Logger字段，用于日志记录
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * 用户登录
     *
     * @param loginRequestDto 登录请求信息
     * @return 登录结果，包含JWT令牌
     */
    public ApiResponse<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        // 执行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        // 认证信息添加到 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成 JWT Token
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = refreshTokenService.createAndStoreRefreshToken(authentication);

        long expiresIn = jwtExpiration / 1000;

        return ApiResponse.success("登录成功", new LoginResponseDto(accessToken, refreshToken, expiresIn));
    }

    /**
     * 用户注册
     * 
     * @param registerRequestDto 注册信息
     * @return 注册结果
     */
    @Transactional
    public ApiResponse<String> register(RegisterRequestDto registerRequestDto) {
        String username = registerRequestDto.getUsername();
        String password = registerRequestDto.getPassword();
        String email = registerRequestDto.getEmail();

        if (userMapper.existsByUsername(username)) {
            log.warn("注册失败，用户名已存在：{}", username);
            return ApiResponse.error("用户名已存在");
        }

        if (userMapper.existsByEmail(email)) {
            log.warn("注册失败，邮箱已被占用：{}", email);
            return ApiResponse.error("邮箱已被占用");
        }

        // 创建用户实体
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAccountType("local");
        user.setStatus("active");
        user.setEmailVerified(false);

        // 保存用户
        try {
            userMapper.insert(user);
            log.info("用户注册成功，用户名：{}, ID: {}", user.getUsername(), user.getId());
            return ApiResponse.success("注册成功", user.getId().toString());
        } catch (Exception e) {
            log.error("用户注册失败，用户名：{}，错误：{}", username, e.getMessage(), e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "注册失败，请稍后重试");
        }
    }
}
