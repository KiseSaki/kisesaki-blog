package com.kisesaki.blog.auth.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.kisesaki.blog.auth.dto.DeviceInfo;
import com.kisesaki.blog.auth.security.jwt.DeviceFingerprintService;
import com.kisesaki.blog.auth.security.jwt.JwtTokenProvider;
import com.kisesaki.blog.auth.security.jwt.RefreshTokenService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * OAuth2 登录成功后的处理器。
 * 当用户通过第三方平台成功登录后，这个处理器会被调用。
 * 它的核心任务是生成 JWT，并带着 JWT 重定向回前端。
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final DeviceFingerprintService deviceFingerprintService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // 从 Authentication 对象生成 JWT。
        // 此时的 Authentication 对象是由 CustomOAuth2UserService 返回的 CustomUserPrincipal 构成的。
        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        // 生成设备指纹信息
        DeviceInfo deviceInfo = deviceFingerprintService.generateDeviceFingerprint(request);
        String deviceId = deviceInfo.getDeviceId();
        String deviceInfoStr = deviceInfo.getDeviceInfo();

        // 创建并存储 Refresh Token
        String refreshToken = refreshTokenService.createAndStoreRefreshToken(authentication, deviceId, deviceInfoStr);

        // 构建目标 URL，用于重定向回前端。
        // 前端应该有一个专门的页面来接收这个重定向，并从 URL 中解析出 Token。
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth2/redirect") // 前端接收重定向的页面URL
                .queryParam("accessToken", accessToken) // 将 JWT 作为 URL 参数
                .queryParam("refreshToken", refreshToken) // 将 Refresh Token 作为 URL 参数
                .queryParam("deviceId", deviceId) // 将设备ID作为 URL 参数，前端需要存储用于后续token刷新
                .build().toUriString();

        // 清除可能存在的临时认证信息。
        // 例如，Spring Security 在 OAuth2 流程中可能会在 Session 中存储一些临时状态。
        clearAuthenticationAttributes(request);

        // 执行重定向。
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
