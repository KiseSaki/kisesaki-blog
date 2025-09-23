package com.kisesaki.blog.auth.security.user;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.auth.entity.Permission;
import com.kisesaki.blog.auth.entity.Role;
import com.kisesaki.blog.auth.mapper.RoleMapper;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现了 Spring Security 的 UserDetailsService 接口。
 * 它的核心职责是根据用户名加载用户数据，用于本地用户名密码认证。
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

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
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        return loadUserDetails(user);
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
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, id)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw new UserPrincipalNotFoundException("该用户不存在");
        }

        return loadUserDetails(user);
    }

    /**
     * 加载用户详细信息，包括角色和权限
     *
     * @param user 用户实体
     * @return UserDetails 对象
     */
    private UserDetails loadUserDetails(User user) {
        // 获取用户角色
        List<Role> roles = roleMapper.findRolesByUserId(user.getId());

        // 获取用户权限
        List<Permission> permissions = roleMapper.findPermissionsByUserId(user.getId());

        log.info("User {} loaded with roles: {} and permissions: {}",
                user.getUsername(),
                roles.stream().map(Role::getName).collect(Collectors.toList()),
                permissions.stream().map(Permission::getName).collect(Collectors.toList()));

        return new CustomUserPrincipal(user.getId(), user.getUsername(), user.getPassword(), roles, permissions);
    }
}
