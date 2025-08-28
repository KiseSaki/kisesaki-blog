package com.kisesaki.blog.auth.security.oauth2;

import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.kisesaki.blog.auth.entity.UserRole;
import com.kisesaki.blog.auth.mapper.UserRoleMapper;
import com.kisesaki.blog.auth.security.user.CustomUserPrincipal;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    /**
     * 当 OAuth2 登录成功后，Spring Security 会调用此方法。
     * 
     * @param userRequest 包含了 access token 和客户端注册信息
     * @return 一个 OAuth2User 对象，我们将返回自定义的 CustomUserPrincipal
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 调用父类方法，从第三方平台获取用户信息
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 这里可以对 oAuth2User 进行自定义处理

        // 返回处理后的用户信息
        return processOAuth2User(userRequest, oAuth2User);
    }

    /**
     * 处理 OAuth2 用户信息
     * 
     * @param userRequest 请求
     * @param oauth2User 从第三方获取的用户信息
     * @return 自定义的用户详情对象
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        // 获取提供商的 registrationId
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 使用工厂类获取标准化的用户信息对象
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oauth2User.getAttributes());

        // 在数据库中查找或创建用户
        Optional<User> userOptional = userMapper.findByOAuth(registrationId, userInfo.getId());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            user = new User();
            user.setOauthProvider(registrationId);
            user.setOauthId(userInfo.getId());
            user.setUsername(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            userMapper.insert(user);
        }

        // 获取用户角色信息
        List<UserRole> userRoles = userRoleMapper.findByUserId(user.getId());

        return new CustomUserPrincipal(user.getId(), user.getUsername(), user.getPassword(), userRoles);
    }
}
