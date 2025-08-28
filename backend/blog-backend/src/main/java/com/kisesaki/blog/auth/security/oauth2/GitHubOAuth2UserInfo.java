package com.kisesaki.blog.auth.security.oauth2;

import java.util.Map;

/**
 * GitHubOAuth2UserInfo 类，专门负责解析从 GitHub 获取的用户信息。
 */
public class GitHubOAuth2UserInfo extends OAuth2UserInfo {
    public GitHubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        // 避免类型转换异常
        Object id = attributes.get("id");
        return id != null ? String.valueOf(id) : null;
    }

    @Override
    public String getName() {
        // GitHub 的用户名字段是 "login"
        return (String) attributes.get("login");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
