package com.kisesaki.blog.auth.security.jwt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kisesaki.blog.user.Keys.UserKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Refresh Token 服务
 * 
 * 功能特性：
 * 1. 基于设备管理的多设备登录支持
 * 2. 同一设备重复登录会替换旧token，避免token堆积
 * 3. 支持设备指纹验证，增强安全性
 * 4. 提供详细的token使用统计和监控
 * 5. 自动清理过期和无效token
 * 6. 支持批量操作和并发安全
 * 
 * Redis存储结构：
 * - UserKey:device:refreshToken:{username}:{deviceId} -> token内容
 * - UserKey:devices:{username} -> Set<deviceId>
 * - UserKey:device:metadata:{username}:{deviceId} -> 元数据JSON
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Refresh Token 过期时间（毫秒）
     * 从配置文件中读取，用于设置token在Redis中的TTL
     */
    @Value("${kisesaki.blog.jwt.refresh-expiration}")
    private Long refreshTokenExpirationMs;

    /**
     * 每个用户最大设备数量限制
     * 默认值为10，超出限制时会自动删除最旧的设备token
     */
    @Value("${kisesaki.blog.security.max-devices-per-user:10}")
    private Integer maxDevicesPerUser;

    /**
     * 创建并存储 Refresh Token
     * 
     * 核心功能：
     * 1. 为指定用户和设备创建新的refresh token
     * 2. 检查设备数量限制，超限时自动删除最旧设备
     * 3. 如果设备重复登录，会替换旧token，避免token堆积
     * 4. 使用原子操作确保数据一致性
     *
     * @param authentication 认证信息，包含用户名、权限等
     * @param deviceId       设备标识（必需），建议使用设备指纹或唯一标识
     * @param deviceInfo     设备信息（可选），如"Chrome 90.0 / Windows 10"，用于管理界面展示
     * @return 生成的 Refresh Token 字符串
     * @throws IllegalArgumentException 当设备ID为空时抛出
     */
    public String createAndStoreRefreshToken(Authentication authentication, String deviceId, String deviceInfo) {
        Assert.hasText(deviceId, "设备ID不能为空");

        String username = authentication.getName();

        // 检查设备数量限制，超限时自动清理
        checkDeviceLimit(username);

        return createDeviceToken(username, authentication, deviceId, deviceInfo);
    }

    /**
     * 创建设备绑定的token
     * 
     * @param username       用户名
     * @param authentication 认证信息
     * @param deviceId       设备ID
     * @param deviceInfo     设备信息
     * @return 生成的 Refresh Token 字符串
     */
    private String createDeviceToken(String username, Authentication authentication, String deviceId,
            String deviceInfo) {
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        String redisKey = UserKey.buildDeviceRefreshTokenKey(username, deviceId);
        // 设备token存储键
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        // 元数据存储键
        String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId);

        // 检查是否是设备重复登录
        String oldToken = stringRedisTemplate.opsForValue().get(redisKey);
        if (oldToken != null) {
            log.info("设备重复登录, 替换旧token - 用户: {}, 设备: {}", username, deviceId);
        }

        // 使用 Lua 脚本确保原子性操作
        String luaScript = """
                -- 存储新token
                redis.call('SET', KEYS[1], ARGV[1], 'PX', ARGV[2])
                -- 添加设备到用户设备集合
                redis.call('SADD', KEYS[2], ARGV[3])
                redis.call('EXPIRE', KEYS[2], ARGV[4])
                -- 存储元数据
                redis.call('SET', KEYS[3], ARGV[5], 'PX', ARGV[2])
                return 'OK'
                """;

        // 创建元数据
        String metadata = createTokenMetadata(deviceInfo);

        // 创建 Redis 脚本
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(luaScript, String.class);
        // 执行 Redis 脚本
        stringRedisTemplate.execute(redisScript,
                // KEYS列表，用于传递给脚本，按顺序依次是KEYS[1]，KEYS[2]，KEYS[3]
                List.of(redisKey, userDevicesKey, metadataKey),
                // ARGV[1]，新生成的 Refresh Token 字符串，用于存储到 Redis
                refreshToken,
                // ARGV[2]，token 过期时间（毫秒），用于设置键的 PX（毫秒过期）
                String.valueOf(UserKey.DEVICE_REFRESH_TOKEN.getExpireSeconds() * 1000L),
                // ARGV[3]，设备 ID，用于添加到用户设备集合。
                deviceId,
                // ARGV[4]，设备集合的过期时间（秒），与 token 过期时间一致
                String.valueOf(UserKey.DEVICE_REFRESH_TOKEN.getExpireSeconds()),
                // ARGV[5]，token 元数据 JSON 字符串（包含设备信息、创建时间等），存储到元数据键
                metadata);

        log.debug("为用户 {} 的设备 {} 创建了新的 refresh token", username, deviceId);
        return refreshToken;
    }

    /**
     * 验证 Refresh Token 的有效性
     * 
     * 验证流程：
     * 1. 首先使用JwtTokenProvider验证token格式和签名
     * 2. 如果提供了设备ID，优先验证该设备的token
     * 3. 如果设备ID验证失败或未提供，遍历用户所有设备查找匹配token
     * 4. 验证成功后自动更新token的最后使用时间
     *
     * @param username     用户名
     * @param refreshToken 提供的 Refresh Token
     * @param deviceId     设备ID（可选，提供则优先验证指定设备token）
     * @return 如果有效则返回 true，否则返回 false
     */
    public boolean validateRefreshToken(String username, String refreshToken, String deviceId) {
        // 首先验证token本身的有效性（格式、签名、过期时间等）
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return false;
        }

        // 如果指定了设备ID，优先验证该设备
        if (deviceId != null && validateDeviceToken(username, refreshToken, deviceId)) {
            updateTokenLastUsed(username, deviceId);
            return true;
        }

        // 验证所有设备token（遍历查找）
        return validateAllDeviceTokens(username, refreshToken);
    }

    /**
     * 验证指定设备的token
     * 
     * @param username     用户名
     * @param refreshToken 要验证的token
     * @param deviceId     设备ID
     * @return 验证结果，true表示token有效且匹配该设备
     */
    private boolean validateDeviceToken(String username, String refreshToken, String deviceId) {
        // 构建Redis键名
        String redisKey = UserKey.buildDeviceRefreshTokenKey(username, deviceId);
        // 从Redis获取存储的token
        String storedToken = stringRedisTemplate.opsForValue().get(redisKey);
        // 比较提供的token和存储的token是否一致
        return refreshToken.equals(storedToken);
    }

    /**
     * 遍历用户所有设备验证token
     * 当不知道具体设备ID时，通过遍历用户所有设备来查找匹配的token
     * 
     * @param username     用户名
     * @param refreshToken 要验证的token
     * @return 验证结果，true表示在某个设备上找到了匹配的token
     */
    private boolean validateAllDeviceTokens(String username, String refreshToken) {
        // 获取用户所有设备ID集合的Redis键
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        // 从Redis Set中获取所有设备ID
        Set<String> deviceIds = stringRedisTemplate.opsForSet().members(userDevicesKey);

        if (deviceIds != null && !deviceIds.isEmpty()) {
            // 遍历每个设备，验证token是否匹配
            for (String deviceId : deviceIds) {
                if (validateDeviceToken(username, refreshToken, deviceId)) {
                    // 找到匹配的token，更新最后使用时间
                    updateTokenLastUsed(username, deviceId);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 删除指定的 Refresh Token
     * 
     * 删除策略：
     * 1. 如果提供了设备ID，优先删除该设备的token
     * 2. 如果指定设备删除失败或未提供设备ID，遍历所有设备查找并删除匹配的token
     * 3. 使用原子操作确保删除的一致性
     * 4. 同时清理相关的元数据和设备引用
     *
     * @param username     用户名
     * @param refreshToken 要删除的 Refresh Token
     * @param deviceId     设备ID（可选，提供则优先删除指定设备token）
     */
    public void deleteRefreshToken(String username, String refreshToken, String deviceId) {
        boolean deleted = false;

        // 如果指定了设备ID，优先删除该设备的token
        if (deviceId != null) {
            deleted = deleteSpecificDeviceToken(username, refreshToken, deviceId);
        }

        // 如果未删除成功，则遍历所有设备查找并删除匹配的token
        if (!deleted) {
            String userDevicesKey = UserKey.buildUserDevicesKey(username);
            Set<String> deviceIds = stringRedisTemplate.opsForSet().members(userDevicesKey);

            if (deviceIds != null) {
                for (String device : deviceIds) {
                    if (deleteSpecificDeviceToken(username, refreshToken, device)) {
                        break; // 找到并删除后立即退出
                    }
                }
            }
        }
    }

    /**
     * 删除指定设备的token
     * 验证token是否属于该设备，如果匹配则删除相关的所有数据
     * 
     * @param username     用户名
     * @param refreshToken 要删除的token
     * @param deviceId     设备ID
     * @return 删除结果，true表示成功找到并删除了token
     */
    private boolean deleteSpecificDeviceToken(String username, String refreshToken, String deviceId) {
        // 构建设备token的Redis键
        String redisKey = UserKey.buildDeviceRefreshTokenKey(username, deviceId);
        // 获取存储的token进行验证
        String storedToken = stringRedisTemplate.opsForValue().get(redisKey);

        // 验证token是否匹配
        if (refreshToken.equals(storedToken)) {
            // 构建相关的Redis键
            String userDevicesKey = UserKey.buildUserDevicesKey(username); // 用户设备集合键
            String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId); // 元数据键

            // 使用 Lua 脚本确保原子性删除操作
            // 删除token、从设备集合中移除设备ID、删除元数据
            String luaScript = """
                    redis.call('DEL', KEYS[1])          -- 删除token
                    redis.call('SREM', KEYS[2], ARGV[1]) -- 从设备集合中移除设备ID
                    redis.call('DEL', KEYS[3])          -- 删除元数据
                    return 'OK'
                    """;

            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(luaScript, String.class);
            stringRedisTemplate.execute(redisScript,
                    List.of(redisKey, userDevicesKey, metadataKey), // 传递三个Redis键
                    deviceId); // 传递设备ID作为参数

            log.info("删除了用户 {} 设备 {} 的 refresh token", username, deviceId);
            return true;
        }
        return false;
    }

    /**
     * 删除用户所有 Refresh Token（用于登出所有设备）
     * 
     * 应用场景：
     * 1. 用户主动选择"登出所有设备"
     * 2. 安全策略要求（如密码重置后）
     * 3. 账户被锁定或冻结时
     * 
     * 操作步骤：
     * 1. 获取用户所有登录设备列表
     * 2. 逐个删除每个设备的token和元数据
     * 3. 最后清理设备集合引用
     *
     * @param username 用户名
     */
    public void deleteAllRefreshTokens(String username) {
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        Set<String> deviceIds = stringRedisTemplate.opsForSet().members(userDevicesKey);

        if (deviceIds != null && !deviceIds.isEmpty()) {
            // 批量删除所有设备的token和元数据
            for (String deviceId : deviceIds) {
                String redisKey = UserKey.buildDeviceRefreshTokenKey(username, deviceId);
                String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId);
                stringRedisTemplate.delete(redisKey);
                stringRedisTemplate.delete(metadataKey);
            }
            // 删除设备集合
            stringRedisTemplate.delete(userDevicesKey);

            log.info("删除了用户 {} 的所有 refresh token，共 {} 个设备", username, deviceIds.size());
        } else {
            log.debug("用户 {} 没有活跃的设备token需要删除", username);
        }
    }

    /**
     * 删除指定设备的token（用于踢出特定设备）
     * 
     * 应用场景：
     * 1. 用户在设备管理界面主动踢出某个设备
     * 2. 发现异常登录时踢出可疑设备
     * 3. 管理员强制下线特定设备
     * 
     * 注意：此方法不验证token内容，直接根据设备ID删除
     *
     * @param username 用户名
     * @param deviceId 设备ID
     */
    public void deleteDeviceToken(String username, String deviceId) {
        String redisKey = UserKey.buildDeviceRefreshTokenKey(username, deviceId);
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId);

        // 使用 Lua 脚本确保原子性删除
        String luaScript = """
                redis.call('DEL', KEYS[1])          -- 删除token
                redis.call('SREM', KEYS[2], ARGV[1]) -- 从设备集合中移除设备ID
                redis.call('DEL', KEYS[3])          -- 删除元数据
                return 'OK'
                """;

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(luaScript, String.class);
        stringRedisTemplate.execute(redisScript,
                List.of(redisKey, userDevicesKey, metadataKey),
                deviceId);

        log.info("删除了用户 {} 设备 {} 的登录状态", username, deviceId);
    }

    /**
     * 获取用户所有登录设备信息
     * 
     * 返回用户当前所有活跃登录设备的ID集合
     * 可用于：
     * 1. 设备管理界面展示
     * 2. 安全监控和审计
     * 3. 设备数量统计
     *
     * @param username 用户名
     * @return 设备ID集合，如果用户没有登录设备则返回空集合
     */
    public Set<String> getUserDevices(String username) {
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        return stringRedisTemplate.opsForSet().members(userDevicesKey);
    }

    /**
     * 获取用户活跃设备数量
     * 
     * 用于：
     * 1. 快速检查用户登录设备数量
     * 2. 设备数量限制验证
     * 3. 统计分析
     *
     * @param username 用户名
     * @return 活跃设备数量，如果用户没有登录设备则返回0
     */
    public Long getActiveDeviceCount(String username) {
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        return stringRedisTemplate.opsForSet().size(userDevicesKey);
    }

    /**
     * 清理过期的token引用（定时任务调用）
     * 
     * 功能说明：
     * 1. 检查用户设备集合中的每个设备ID
     * 2. 如果设备对应的token已过期被Redis自动删除，则清理对应的引用
     * 3. 保持数据一致性，避免"僵尸"设备引用堆积
     * 
     * 建议：配置定时任务定期调用此方法进行清理
     *
     * @param username 用户名
     */
    public void cleanExpiredTokens(String username) {
        String userDevicesKey = UserKey.buildUserDevicesKey(username);
        Set<String> deviceIds = stringRedisTemplate.opsForSet().members(userDevicesKey);

        if (deviceIds != null && !deviceIds.isEmpty()) {
            int cleanedCount = 0;
            for (String deviceId : deviceIds) {
                String redisKey = UserKey.buildDeviceRefreshTokenKey(username, deviceId);
                // 检查token是否还存在
                if (!stringRedisTemplate.hasKey(redisKey)) {
                    // token已过期，清理相关引用
                    stringRedisTemplate.opsForSet().remove(userDevicesKey, deviceId);
                    String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId);
                    stringRedisTemplate.delete(metadataKey);
                    cleanedCount++;
                }
            }

            if (cleanedCount > 0) {
                log.debug("清理了用户 {} 的 {} 个过期设备引用", username, cleanedCount);
            }
        }
    }

    /**
     * 获取设备token的元数据
     * 
     * 元数据包含：
     * 1. 设备信息（浏览器、操作系统等）
     * 2. token创建时间
     * 3. 最后使用时间
     * 
     * 用途：
     * 1. 设备管理界面展示详细信息
     * 2. 安全审计和分析
     * 3. 异常行为检测
     *
     * @param username 用户名
     * @param deviceId 设备ID
     * @return 元数据JSON字符串，如果不存在则返回null
     */
    public String getTokenMetadata(String username, String deviceId) {
        String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId);
        return stringRedisTemplate.opsForValue().get(metadataKey);
    }

    /**
     * 检查用户设备数量是否超过限制
     * 如果超过最大设备数量，会自动删除最旧的设备token
     * 
     * @param username 用户名
     */
    private void checkDeviceLimit(String username) {
        // 获取当前活跃设备数量
        Long deviceCount = getActiveDeviceCount(username);

        // 检查是否超过限制
        if (deviceCount != null && deviceCount >= maxDevicesPerUser) {
            log.warn("用户 {} 设备数量超限，当前: {}, 最大: {}", username, deviceCount, maxDevicesPerUser);

            // 构建用户设备集合的Redis键
            String userDevicesKey = UserKey.buildUserDevicesKey(username);

            // 删除最旧的设备（这里使用集合的任意元素，实际场景可以根据时间戳排序）
            Set<String> deviceIds = stringRedisTemplate.opsForSet().members(userDevicesKey);
            if (deviceIds != null && !deviceIds.isEmpty()) {
                String oldestDeviceId = deviceIds.iterator().next(); // 获取集合中任意一个设备ID
                deleteDeviceToken(username, oldestDeviceId);
            }
        }
    }

    /**
     * 创建token元数据JSON字符串
     * 包含设备信息、创建时间、最后使用时间等信息
     * 
     * @param deviceInfo 设备信息描述（如浏览器类型、操作系统等）
     * @return JSON格式的元数据字符串
     */
    private String createTokenMetadata(String deviceInfo) {
        // 当前时间戳，用于记录创建和最后使用时间
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 构建简单的JSON格式元数据
        return String.format("""
                {
                    "deviceInfo": "%s",
                    "createdAt": "%s",
                    "lastUsedAt": "%s"
                }
                """,
                deviceInfo != null ? deviceInfo : "unknown", // 设备信息，默认为unknown
                currentTime, // 创建时间
                currentTime); // 最后使用时间（初始时与创建时间相同）
    }

    /**
     * 更新token的最后使用时间
     * 在token验证成功时调用，用于记录token的活跃状态
     * 
     * @param username 用户名
     * @param deviceId 设备ID
     */
    private void updateTokenLastUsed(String username, String deviceId) {
        // 构建元数据的Redis键
        String metadataKey = UserKey.buildDeviceMetadataKey(username, deviceId);
        // 获取当前元数据
        String metadata = stringRedisTemplate.opsForValue().get(metadataKey);

        if (metadata != null) {
            // 生成新的时间戳
            String newTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // 简单替换lastUsedAt字段的值（生产环境建议使用JSON解析库如Jackson）
            String updatedMetadata = metadata.replaceAll(
                    "\"lastUsedAt\": \"[^\"]*\"", // 匹配原有的lastUsedAt字段
                    "\"lastUsedAt\": \"" + newTimestamp + "\""); // 替换为新的时间戳

            // 更新Redis中的元数据，保持原有的TTL
            stringRedisTemplate.opsForValue().set(metadataKey, updatedMetadata,
                    UserKey.DEVICE_TOKEN_METADATA.getExpireSeconds(), TimeUnit.SECONDS);
        }
    }
}