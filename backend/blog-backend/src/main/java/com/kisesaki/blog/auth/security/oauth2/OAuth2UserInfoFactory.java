package com.kisesaki.blog.auth.security.oauth2;

import java.util.Map;

/**
 * 工厂类，根据不同的 OAuth2 提供商（registrationId）创建对应的 OAuth2UserInfo 实例。
 */
public class OAuth2UserInfoFactory {

    /**
     * 根据不同的 OAuth2 提供商（registrationId）和用户信息（attributes）创建对应的 OAuth2UserInfo 实例。
     * 
     * @param registrationId
     * @param attributes
     * @return
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if ("github".equalsIgnoreCase(registrationId)) {
            return new GitHubOAuth2UserInfo(attributes);
        }
        // 可以在这里添加对其他提供商的支持，例如 Google, Facebook 等
        throw new IllegalArgumentException("不支持的 OAuth2 提供商: " + registrationId);
    }
}
