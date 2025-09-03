package com.kisesaki.blog.user;

import com.kisesaki.blog.auth.mapper.PermissionMapper;
import com.kisesaki.blog.auth.mapper.RoleMapper;
import com.kisesaki.blog.auth.mapper.RolePermissionMapper;
import com.kisesaki.blog.auth.mapper.UserRoleMapper;
import com.kisesaki.blog.user.dto.UserInfoDto;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.entity.UserProfile;
import com.kisesaki.blog.user.mapper.UserMapper;
import com.kisesaki.blog.user.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    /**
     * 根据用户ID获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    public UserInfoDto getUserInfoById(Long id) {
        User user = userMapper.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        UserProfile profile = userProfileMapper.findByUserId(id).orElse(null);
        return UserInfoDto.from(user, profile, java.util.Collections.emptyList());
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    public UserInfoDto getUserInfoByUsername(String username) {
        User user = userMapper.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        UserProfile profile = userProfileMapper.findByUserId(user.getId()).orElse(null);
        return UserInfoDto.from(user, profile, java.util.Collections.emptyList());
    }

}
