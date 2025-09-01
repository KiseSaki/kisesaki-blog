package com.kisesaki.blog.user.Keys;

import com.kisesaki.blog.redis.BasePrefix;

/**
 * 用户相关的 Redis Key 前缀集合。
 *
 * 包含常用用户键：按ID缓存、会话、刷新令牌、登录尝试、验证码、设备列表、邮箱验证令牌等。
 */
public final class UserKey extends BasePrefix {

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 按用户ID缓存用户信息（永久）
    public static final UserKey BY_ID = new UserKey(0, "byId");

    // 用户会话（7天）
    public static final UserKey SESSION = new UserKey(60 * 60 * 24 * 7, "session");

    // 设备刷新令牌（30天）
    public static final UserKey DEVICE_REFRESH_TOKEN = new UserKey(60 * 60 * 24 * 30, "device:refreshToken");

    // 用户设备集合（30天，与刷新令牌同期）
    public static final UserKey USER_DEVICES = new UserKey(60 * 60 * 24 * 30, "devices");

    // 设备令牌元数据（30天）
    public static final UserKey DEVICE_TOKEN_METADATA = new UserKey(60 * 60 * 24 * 30, "device:metadata");

    // 登录失败尝试计数（15分钟）
    public static final UserKey LOGIN_ATTEMPT = new UserKey(60 * 15, "loginAttempt");

    // 验证码（5分钟）
    public static final UserKey VERIFICATION_CODE = new UserKey(60 * 5, "verifyCode");

    // 邮箱验证（注册确认）令牌（24小时）
    public static final UserKey EMAIL_VERIFICATION = new UserKey(60 * 60 * 24, "emailVerification");

    // 用户已登录设备集合（永久）
    public static final UserKey DEVICES = new UserKey(0, "devices");

    // 密码重置令牌（1小时）
    public static final UserKey PASSWORD_RESET = new UserKey(60 * 60, "passwordReset");

    /**
     * 拼接完整 Redis key，例如：
     * UserKey.EMAIL_VERIFICATION.buildKey(token) ->
     * "UserKey:emailVerification:abcdef..."
     *
     * @param parts 可变参数，按顺序追加到前缀后面，用 ':' 分隔
     * @return 最终的 Redis key 字符串
     */
    public String buildKey(Object... parts) {
        StringBuilder sb = new StringBuilder(getPrefix());
        if (parts != null) {
            for (Object p : parts) {
                sb.append(':').append(String.valueOf(p));
            }
        }
        return sb.toString();
    }

    /**
     * 构建设备刷新令牌的Redis键
     * 格式：UserKey:device:refreshToken:username:deviceId
     * 
     * @param username 用户名
     * @param deviceId 设备ID
     * @return Redis键字符串
     */
    public static String buildDeviceRefreshTokenKey(String username, String deviceId) {
        return DEVICE_REFRESH_TOKEN.buildKey(username, deviceId);
    }

    /**
     * 构建用户设备集合的Redis键
     * 格式：UserKey:devices:username
     * 
     * @param username 用户名
     * @return Redis键字符串
     */
    public static String buildUserDevicesKey(String username) {
        return USER_DEVICES.buildKey(username);
    }

    /**
     * 构建设备元数据的Redis键
     * 格式：UserKey:device:metadata:username:deviceId
     * 
     * @param username 用户名
     * @param deviceId 设备ID
     * @return Redis键字符串
     */
    public static String buildDeviceMetadataKey(String username, String deviceId) {
        return DEVICE_TOKEN_METADATA.buildKey(username, deviceId);
    }
}