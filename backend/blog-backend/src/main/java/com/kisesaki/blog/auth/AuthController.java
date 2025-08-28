package com.kisesaki.blog.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kisesaki.blog.auth.dto.request.AuthRequest;
import com.kisesaki.blog.auth.dto.response.JwtAuthenticationResponse;
import com.kisesaki.blog.auth.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

/**
 * 认证控制器
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        // 执行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        // 认证信息添加到 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成 JWT Token
        String jwt = jwtTokenProvider.createAccessToken(authentication);

        // 返回 Token
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
