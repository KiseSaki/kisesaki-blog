package com.kisesaki.blog.auth.security.oauth2;

import java.util.Map;

/**
 * 这是一个抽象基类，用于统一不同 OAuth2 提供商返回的用户信息结构。
 * 它的子类将负责解析特定提供商（如 GitHub, Google）的数据。
 */
public abstract class OAuth2UserInfo {
    // 使用 protected，这样子类可以直接访问
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 以下是所有提供商都必须能提取出的标准化用户信息字段
    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
