package com.kisesaki.blog.auth.security.user;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.kisesaki.blog.auth.entity.Permission;
import com.kisesaki.blog.auth.entity.Role;

import lombok.Getter;

/**
 * 自定义的用户主体类，是 Spring Security 中用户身份的核心表示。
 * 它同时实现了 UserDetails 和 OAuth2User 接口，使其能够统一代表
 * 本地注册用户和通过 OAuth2 登录的第三方用户。
 */
public class CustomUserPrincipal implements UserDetails, OAuth2User {
    @Getter // --- 自定义方法 ---
    private final Long id;
    private final String username;
    // 加密后的密码
    private final String password;
    // 用户权限信息
    private final Collection<? extends GrantedAuthority> authorities;
    // OAuth2用户信息
    private final Map<String, Object> attributes;
    // 用户角色信息
    @Getter
    private final List<Role> roles;
    // 用户权限信息
    @Getter
    private final List<Permission> permissions;

    /**
     * 用于本地用户认证的构造函数。
     *
     * @param id          用户ID
     * @param username    用户名
     * @param password    加密后的密码
     * @param roles       角色列表
     * @param permissions 权限列表
     */
    public CustomUserPrincipal(Long id, String username, String password, List<Role> roles,
            List<Permission> permissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.permissions = permissions;
        this.authorities = buildAuthorities(roles, permissions);
        this.attributes = null;
    }

    /**
     * 用于 OAuth2 用户的构造函数。
     *
     * @param id          用户ID
     * @param username    用户名
     * @param roles       角色列表
     * @param permissions 权限列表
     * @param attributes  从 OAuth2 提供商获取的原始属性
     */
    public CustomUserPrincipal(Long id, String username, List<Role> roles, List<Permission> permissions,
            Map<String, Object> attributes) {
        this.id = id;
        this.username = username;
        this.password = null; // OAuth2用户没有密码
        this.roles = roles;
        this.permissions = permissions;
        this.authorities = buildAuthorities(roles, permissions);
        this.attributes = attributes;
    }

    /**
     * 构建用户的权限列表，包含角色和具体权限
     *
     * @param roles       角色列表
     * @param permissions 权限列表
     * @return 权限集合
     */
    private Collection<? extends GrantedAuthority> buildAuthorities(List<Role> roles, List<Permission> permissions) {
        // 角色权限（以ROLE_前缀）
        Stream<GrantedAuthority> roleAuthorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));

        // 具体权限
        Stream<GrantedAuthority> permissionAuthorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()));

        // 合并角色权限和具体权限
        return Stream.concat(roleAuthorities, permissionAuthorities)
                .collect(Collectors.toList());
    }

    // --- UserDetails 接口方法实现 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 以下方法返回 true 表示账户有效。可以根据业务需求实现具体逻辑 (例如账户冻结)。
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- OAuth2User 接口方法实现 ---

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // OAuth2 规范中，name 是用户的唯一标识符。
        // 对于我们系统，可以返回用户ID或用户名。返回ID更稳定。
        return username;
    }
}
