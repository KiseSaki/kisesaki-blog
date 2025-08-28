package com.kisesaki.blog.auth.security.user;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kisesaki.blog.auth.entity.UserRole;
import com.kisesaki.blog.auth.mapper.UserRoleMapper;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * 实现了 Spring Security 的 UserDetailsService 接口。
 * 它的核心职责是根据用户名加载用户数据，用于本地用户名密码认证。
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    /**
     * Spring Security 进行用户名密码认证时，会调用此方法。
     *
     * @param username 用户在登录时输入的用户名
     * @return 一个 UserDetails 对象，包含了用户的完整信息
     * @throws UsernameNotFoundException 如果数据库中找不到该用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 获取用户信息
        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("该用户不存在"));

        List<UserRole> roles = userRoleMapper.findByUserId(user.getId());

        return new CustomUserPrincipal(user.getId(), user.getUsername(), user.getPassword(), roles);

    }

    /**
     * 这个方法是为 JWT 过滤器准备的。
     * 当 Token 验证通过后，我们可能需要通过用户ID来加载用户信息。
     *
     * @param id 用户ID
     * @return UserDetails 对象
     */
    public UserDetails loadUserById(Long id) throws UserPrincipalNotFoundException {
        // 获取用户信息
        User user = userMapper.findById(id)
                .orElseThrow(() -> new UserPrincipalNotFoundException("该用户不存在"));

        List<UserRole> roles = userRoleMapper.findByUserId(user.getId());

        return new CustomUserPrincipal(user.getId(), user.getUsername(), user.getPassword(), roles);
    }

}
