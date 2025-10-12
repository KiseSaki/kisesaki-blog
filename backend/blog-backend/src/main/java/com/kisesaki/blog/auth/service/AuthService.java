package com.kisesaki.blog.auth.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.auth.dto.DeviceInfo;
import com.kisesaki.blog.auth.dto.auth.request.ChangePasswordRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.ForgotPasswordRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.LoginRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.RefreshTokenRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.RegisterRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.ResetPasswordRequestDto;
import com.kisesaki.blog.auth.dto.auth.request.VerifyEmailRequestDto;
import com.kisesaki.blog.auth.dto.auth.response.LoginResponseDto;
import com.kisesaki.blog.auth.entity.Role;
import com.kisesaki.blog.auth.event.UserRegistrationEvent;
import com.kisesaki.blog.auth.mapper.RoleMapper;
import com.kisesaki.blog.auth.security.jwt.DeviceFingerprintService;
import com.kisesaki.blog.auth.security.jwt.JwtTokenProvider;
import com.kisesaki.blog.auth.security.jwt.RefreshTokenService;
import com.kisesaki.blog.auth.security.user.CustomUserDetailsService;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.notification.event.EmailEventPublisher;
import com.kisesaki.blog.redis.RedisService;
import com.kisesaki.blog.user.Keys.UserKey;
import com.kisesaki.blog.user.entity.User;
import com.kisesaki.blog.user.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证服务
 *
 * @author KiseSaki
 */
@Service // Spring框架注解，标识这是一个服务层组件，会被Spring容器自动扫描并注册为Bean
@RequiredArgsConstructor // 自动生成包含所有final字段的构造函数，用于依赖注入
@Slf4j // 自动生成一个名为log的静态Logger字段，用于日志记录
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final DeviceFingerprintService deviceFingerprintService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisService RedisService;
    private final RedisService redisService;
    private final EmailEventPublisher emailEventPublisher;
    private final RoleMapper roleMapper;
    private final UserRoleService userRoleService;

    @Value("${kisesaki.blog.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * 格式化设备ID用于日志显示（安全地显示设备ID的前8位）
     *
     * @param deviceId 设备ID
     * @return 格式化后的设备ID字符串
     */
    private String formatDeviceIdForLog(String deviceId) {
        if (deviceId == null) {
            return "未指定";
        }
        return deviceId.substring(0, Math.min(8, deviceId.length())) + "...";
    }

    /**
     * 验证设备指纹的合法性
     *
     * @param request  HTTP请求对象
     * @param username 用户名
     * @param deviceId 设备ID
     * @return 是否验证通过
     */
    private boolean validateDeviceFingerprint(HttpServletRequest request, String username, String deviceId) {
        if (request == null || deviceId == null) {
            log.warn("设备指纹验证失败：缺少必要参数 - 用户: {}", username);
            return false;
        }

        try {
            boolean isValid = deviceFingerprintService.validateDeviceFingerprint(request, deviceId);
            if (!isValid) {
                log.warn("用户 {} 的设备指纹验证失败，设备: {}", username, formatDeviceIdForLog(deviceId));
            }
            return isValid;
        } catch (Exception e) {
            log.error("设备指纹验证过程中发生异常 - 用户: {}, 设备: {}", username, formatDeviceIdForLog(deviceId), e);
            return false;
        }
    }

    /**
     * 用户登录
     *
     * @param loginRequestDto 登录请求信息
     * @param request         HTTP请求对象，用于获取设备指纹
     * @return 登录结果，包含JWT令牌和设备信息
     */
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletRequest request) {
        // 执行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        // 认证信息添加到 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成设备信息
        DeviceInfo deviceInfo = deviceFingerprintService.generateDeviceFingerprint(request);
        String deviceId = deviceInfo.getDeviceId();
        String deviceInfoStr = deviceInfo.getDeviceInfo();

        // 生成 JWT Token
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = refreshTokenService.createAndStoreRefreshToken(authentication, deviceId, deviceInfoStr);

        long expiresIn = jwtExpiration / 1000;

        LoginResponseDto response = new LoginResponseDto(accessToken, refreshToken, expiresIn, deviceId);

        log.info("用户 {} 从设备 {} 登录成功", loginRequestDto.getUsername(), formatDeviceIdForLog(deviceId));

        return response;
    }

    /**
     * 用户注册
     *
     * @param registerRequestDto 注册信息
     * @return 注册结果
     */
    @Transactional
    public String register(RegisterRequestDto registerRequestDto) {
        String username = registerRequestDto.getUsername();
        String password = registerRequestDto.getPassword();
        String email = registerRequestDto.getEmail();

        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .ne(User::getStatus, "deleted")) > 0) {
            log.warn("注册失败，用户名已存在：{}", username);
            throw BusinessException.of(ErrorCode.USERNAME_ALREADY_EXISTS, "用户名已存在");
        }

        if (userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getStatus, "deleted")) > 0) {
            log.warn("注册失败，邮箱已被占用：{}", email);
            throw BusinessException.of(ErrorCode.EMAIL_ALREADY_EXISTS, "邮箱已被占用");
        }

        // 创建用户实体
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setAccountType("local");
        user.setStatus("active");
        user.setEmailVerified(false);

        // 保存用户
        try {
            userMapper.insert(user);
            log.info("用户注册成功，用户名：{}, ID: {}", user.getUsername(), user.getId());

            // 为新用户分配 GUEST 角色（未验证邮箱用户）
            assignGuestRoleToNewUser(user.getId());

            // 发送欢迎邮件
            UserRegistrationEvent event = new UserRegistrationEvent(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    LocalDateTime.now());
            eventPublisher.publishEvent(event);
            log.info("用户 {} 注册完成，ID: {}", user.getUsername(), user.getId());
            return user.getId().toString();
        } catch (Exception e) {
            log.error("用户注册失败，用户名：{}，错误：{}", username, e.getMessage(), e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "注册失败，请稍后重试");
        }
    }

    /**
     * 验证用户邮箱
     *
     * @param verifyEmailRequestDto 验证请求
     * @return 验证结果
     */
    public void verifyEmail(VerifyEmailRequestDto verifyEmailRequestDto) {
        String emailToken = verifyEmailRequestDto.getEmailToken();
        String redisKey = UserKey.buildEmailVerificationKey(emailToken);

        Map<Object, Object> verificationData = redisService.hGetAll(redisKey);
        if (verificationData == null || verificationData.isEmpty()) {
            log.warn("邮箱验证失败，令牌无效或已过期，令牌: {}", emailToken);
            throw BusinessException.of(ErrorCode.TOKEN_INVALID, "邮箱验证令牌无效或已过期");
        }

        // 获取用户ID和邮箱
        Long userId = (Long) verificationData.get("userId");

        // 验证用户存在性
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("邮箱验证失败，用户不存在，用户ID: {}", userId);
            throw BusinessException.of(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }

        // 检查邮箱是否已验证
        if (user.getEmailVerified() != null && user.getEmailVerified()) {
            log.info("用户 {} 的邮箱已验证，无需重复验证", user.getUsername());
            return;
        }

        // 更新用户的邮箱验证状态
        user.setEmailVerified(true);
        try {
            userMapper.updateById(user);
            // 删除整个key
            redisService.delete(redisKey);
            
            // 邮箱验证成功后，将用户从 GUEST 角色升级到 USER 角色
            upgradeUserFromGuestToUser(user.getId());
            
            log.info("用户 {} 的邮箱验证成功", user.getUsername());
        } catch (Exception e) {
            log.error("更新用户 {} 的邮箱验证状态失败", user.getUsername(), e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "邮箱验证失败，请稍后重试");
        }
    }

    /**
     * 刷新访问令牌
     *
     * @param refreshTokenRequestDto 刷新令牌请求
     * @return 新的访问令牌
     */
    public LoginResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto,
            HttpServletRequest request) {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        String deviceId = refreshTokenRequestDto.getDeviceId();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw BusinessException.of(ErrorCode.TOKEN_INVALID, "无效的刷新令牌");
        }

        // 获取用户名
        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);

        // 验证 Refresh Token（支持设备ID验证）
        if (!refreshTokenService.validateRefreshToken(username, refreshToken, deviceId)) {
            throw BusinessException.of(ErrorCode.TOKEN_INVALID, "刷新令牌无效或已过期");
        }

        // 验证设备指纹
        if (!deviceFingerprintService.validateDeviceFingerprint(request, deviceId)) {
            log.warn("用户 {} 的设备指纹验证失败，可能是设备变更或伪造请求，设备ID: {}", username, formatDeviceIdForLog(deviceId));
            throw BusinessException.of(ErrorCode.ACCESS_DENIED, "设备验证失败，无法刷新令牌");
        }

        // 加载用户详情
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        long expiresIn = jwtExpiration / 1000;

        // 返回响应时保持设备信息
        LoginResponseDto response = new LoginResponseDto(newAccessToken, refreshToken, expiresIn, deviceId);

        log.debug("用户 {} 的访问令牌刷新成功，设备: {}", username, formatDeviceIdForLog(deviceId));

        return response;
    }

    /**
     * 用户登出
     *
     * @param username     用户名
     * @param refreshToken 要删除的刷新令牌
     * @param deviceId     设备ID（可选）
     * @param request      HTTP请求对象，用于设备指纹验证
     * @return 登出结果
     */
    public void logout(String username, String refreshToken, String deviceId,
            HttpServletRequest request) {
        // 如果提供了设备ID，需要验证设备指纹
        if (deviceId != null && !validateDeviceFingerprint(request, username, deviceId)) {
            log.warn("用户 {} 登出时设备指纹验证失败，设备: {}", username, formatDeviceIdForLog(deviceId));
            throw BusinessException.of(ErrorCode.ACCESS_DENIED, "设备验证失败，无法完成登出操作");
        }

        try {
            refreshTokenService.deleteRefreshToken(username, refreshToken, deviceId);
            log.info("用户 {} 登出成功，设备: {}", username, formatDeviceIdForLog(deviceId));
        } catch (Exception e) {
            log.error("用户 {} 登出失败", username, e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "登出失败");
        }
    }

    /**
     * 登出所有设备
     *
     * @param username 用户名
     * @return 登出结果
     */
    public void logoutAllDevices(String username) {
        try {
            refreshTokenService.deleteAllRefreshTokens(username);
            log.info("用户 {} 已登出所有设备", username);
        } catch (Exception e) {
            log.error("用户 {} 登出所有设备失败", username, e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "登出所有设备失败");
        }
    }

    /**
     * 踢出指定设备
     *
     * @param username 用户名
     * @param deviceId 要踢出的设备ID
     * @param request  HTTP请求对象，用于设备指纹验证（当前设备）
     * @return 操作结果
     */
    public void kickDevice(String username, String deviceId, HttpServletRequest request) {
        // 验证当前操作设备的合法性（防止恶意踢出）
        DeviceInfo currentDevice = deviceFingerprintService.generateDeviceFingerprint(request);
        String currentDeviceId = currentDevice.getDeviceId();

        // 不允许踢出自己当前使用的设备
        if (currentDeviceId.equals(deviceId)) {
            log.warn("用户 {} 尝试踢出自己当前使用的设备: {}", username, formatDeviceIdForLog(deviceId));
            throw BusinessException.of(ErrorCode.ACCESS_DENIED, "不能踢出当前使用的设备");
        }

        try {
            refreshTokenService.deleteDeviceToken(username, deviceId);
            log.info("管理员踢出用户 {} 的设备: {}", username, formatDeviceIdForLog(deviceId));
        } catch (Exception e) {
            log.error("踢出用户 {} 设备 {} 失败", username, deviceId, e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "踢出设备失败");
        }
    }

    /**
     * 获取用户所有登录设备
     *
     * @param username 用户名
     * @return 设备ID集合
     */
    public Set<String> getUserDevices(String username) {
        try {
            Set<String> devices = refreshTokenService.getUserDevices(username);
            return devices;
        } catch (Exception e) {
            log.error("获取用户 {} 设备列表失败", username, e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "获取设备列表失败");
        }
    }

    /**
     * 修改密码
     *
     * @param changePasswordRequestDto 修改密码请求
     * @param request                  HTTP请求对象，用于设备指纹验证
     * @return 修改结果
     */
    public void changePassword(String username, ChangePasswordRequestDto changePasswordRequestDto,
            HttpServletRequest request) {
        String oldPassword = changePasswordRequestDto.getOldPassword();
        String newPassword = changePasswordRequestDto.getNewPassword();

        if (oldPassword.equals(newPassword)) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "新密码不能与旧密码相同");
        }

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            throw BusinessException.of(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }

        if (!user.getEmailVerified()) {
            throw BusinessException.of(ErrorCode.ACCESS_DENIED, "请先验证邮箱");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw BusinessException.of(ErrorCode.PASSWORD_ERROR, "旧密码不正确");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userMapper.updateById(user);

            // 修改密码后，删除所有刷新令牌，强制重新登录
            refreshTokenService.deleteAllRefreshTokens(username);
            // 发送密码修改通知邮件
            emailEventPublisher.publishPasswordChangedEvent(
                    user.getEmail(),
                    user.getId(),
                    user.getUsername(),
                    String.valueOf(LocalDateTime.now()),
                    request.getRemoteAddr());

            log.info("用户 {} 修改密码成功", username);
        } catch (Exception e) {
            log.error("用户 {} 修改密码失败", username, e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "密码修改失败");
        }
    }

    /**
     * 忘记密码，发送重置邮件
     *
     * @param forgotPasswordRequestDto 重置密码请求
     * @return 重置结果
     */
    public void forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        String email = forgotPasswordRequestDto.getEmail();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            log.warn("密码重置请求失败，邮箱未注册：{}", email);
            throw BusinessException.of(ErrorCode.USER_NOT_FOUND, "邮箱未注册");
        }

        if (!user.getEmailVerified()) {
            log.warn("密码重置请求失败，用户邮箱未验证：{}", email);
            throw BusinessException.of(ErrorCode.ACCESS_DENIED, "请先验证邮箱");
        }

        try {
            // 生成密码重置令牌并发送邮件
            String resetToken = UUID.randomUUID().toString();
            String redisKey = UserKey.buildPasswordResetKey(resetToken);
            RedisService.hSet(redisKey, "userId", user.getId());
            RedisService.expire(redisKey, 15 * 60); // 15分钟过期

            emailEventPublisher.publishPasswordResetEvent(
                    user.getEmail(),
                    user.getId(),
                    user.getUsername(),
                    resetToken);

            log.info("密码重置邮件已发送至：{}", email);
        } catch (Exception e) {
            log.error("发送密码重置邮件失败，邮箱：{}", email, e);
            throw BusinessException.of(ErrorCode.EMAIL_SEND_FAILED, "发送密码重置邮件失败，请稍后重试");
        }
    }

    /**
     * 验证密码重置令牌并设置新密码
     *
     * @param resetPasswordRequestDto 重置密码请求
     * @return 重置结果
     */
    public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        String resetToken = resetPasswordRequestDto.getResetToken();
        String newPassword = resetPasswordRequestDto.getNewPassword();
        String redisKey = UserKey.buildPasswordResetKey(resetToken);

        Map<Object, Object> resetData = redisService.hGetAll(redisKey);
        if (resetData == null || resetData.isEmpty()) {
            log.warn("密码重置失败，令牌无效或已过期，令牌: {}", resetToken);
            throw BusinessException.of(ErrorCode.TOKEN_INVALID, "密码重置令牌无效或已过期");
        }

        Long userId = (Long) resetData.get("userId");
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId)
                .ne(User::getStatus, "deleted"));
        if (user == null) {
            log.error("密码重置失败，用户不存在，用户ID: {}", userId);
            throw BusinessException.of(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "新密码不能与旧密码相同");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userMapper.updateById(user);
            // 删除整个key
            redisService.delete(redisKey);
            // 重置密码后，删除所有刷新令牌，强制重新登录
            refreshTokenService.deleteAllRefreshTokens(user.getUsername());

            log.info("用户 {} 的密码重置成功", user.getUsername());
        } catch (Exception e) {
            log.error("用户 {} 的密码重置失败", user.getUsername(), e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "密码重置失败，请稍后重试");
        }
    }

    /**
     * 清理用户过期的令牌
     *
     * @param username 用户名
     * @param request  HTTP请求对象，用于设备指纹验证
     * @return 清理结果
     */
    public void cleanExpiredTokens(String username, HttpServletRequest request) {
        // 验证当前设备的合法性
        DeviceInfo currentDevice = deviceFingerprintService.generateDeviceFingerprint(request);
        if (!validateDeviceFingerprint(request, username, currentDevice.getDeviceId())) {
            log.warn("用户 {} 清理过期令牌时设备指纹验证失败", username);
            throw BusinessException.of(ErrorCode.ACCESS_DENIED, "设备验证失败，无法执行清理操作");
        }

        try {
            refreshTokenService.cleanExpiredTokens(username);
            log.debug("清理用户 {} 的过期令牌完成", username);
        } catch (Exception e) {
            log.error("清理用户 {} 过期令牌失败", username, e);
            throw BusinessException.of(ErrorCode.SYSTEM_ERROR, "清理过期令牌失败");
        }
    }

    /**
     * 为新用户分配 GUEST 角色
     *
     * @param userId 用户ID
     */
    private void assignGuestRoleToNewUser(Long userId) {
        try {
            // 查找 GUEST 角色
            Role guestRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                    .eq(Role::getName, "GUEST"));

            if (guestRole != null) {
                userRoleService.assignRoleToUser(userId, guestRole.getId());
                log.info("为用户 {} 分配 GUEST 角色成功", userId);
            } else {
                log.warn("GUEST 角色不存在，无法为用户 {} 分配角色", userId);
            }
        } catch (Exception e) {
            log.error("为用户 {} 分配 GUEST 角色失败：{}", userId, e.getMessage(), e);
        }
    }

    /**
     * 邮箱验证完成后，将用户从 GUEST 升级到 USER 角色
     *
     * @param userId 用户ID
     */
    private void upgradeUserFromGuestToUser(Long userId) {
        try {
            // 查找 GUEST 和 USER 角色
            Role guestRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                    .eq(Role::getName, "GUEST"));
            Role userRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                    .eq(Role::getName, "USER"));

            if (guestRole != null && userRole != null) {
                // 移除 GUEST 角色
                userRoleService.removeRoleFromUser(userId, guestRole.getId());
                // 分配 USER 角色
                userRoleService.assignRoleToUser(userId, userRole.getId());
                log.info("用户 {} 从 GUEST 角色升级到 USER 角色成功", userId);
            } else {
                log.warn("角色不存在：GUEST={}, USER={}，无法为用户 {} 升级角色", 
                    guestRole != null, userRole != null, userId);
            }
        } catch (Exception e) {
            log.error("用户 {} 角色升级失败：{}", userId, e.getMessage(), e);
        }
    }
}
