package com.kisesaki.blog.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * Redis 统一服务类。
 * 
 * 提供了对 Redis 的通用操作封装，支持 KeyPrefix 体系，
 * 包含基本的 CRUD 操作、过期时间管理、分布式锁等功能。
 * 
 * 主要功能：
 * - 基本的 get/set/delete 操作
 * - 带过期时间的数据操作
 * - 列表、集合、哈希等数据结构操作
 * - 分布式锁实现
 * - 批量操作支持
 * - 原子操作支持
 * 
 * @author KiseSaki
 * @since 2025-09-01
 */
@Service
@Slf4j
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ========== 基本操作 ==========

    /**
     * 设置键值对
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis set operation failed: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置键值对（使用 KeyPrefix）
     *
     * @param prefix KeyPrefix 实例
     * @param key    键后缀
     * @param value  值
     * @return 是否成功
     */
    public boolean set(KeyPrefix prefix, String key, Object value) {
        String realKey = prefix.getPrefix() + ":" + key;
        boolean result = set(realKey, value);
        if (result && prefix.getExpireSeconds() > 0) {
            expire(realKey, prefix.getExpireSeconds());
        }
        return result;
    }

    /**
     * 设置键值对并指定过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 是否成功
     */
    public boolean set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            return true;
        } catch (Exception e) {
            log.error("Redis set with expire operation failed: key={}, timeout={}, error={}",
                    key, timeout, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get operation failed: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取值（使用 KeyPrefix）
     *
     * @param prefix KeyPrefix 实例
     * @param key    键后缀
     * @return 值
     */
    public Object get(KeyPrefix prefix, String key) {
        return get(prefix.getPrefix() + ":" + key);
    }

    /**
     * 获取值并转换为指定类型
     *
     * @param key   键
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 转换后的值
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = get(key);
            if (value == null) {
                return null;
            }
            if (clazz.isInstance(value)) {
                return (T) value;
            }
            // 如果类型不匹配，可以在这里添加类型转换逻辑
            log.warn("Type mismatch for key {}: expected {}, got {}",
                    key, clazz.getSimpleName(), value.getClass().getSimpleName());
            return null;
        } catch (Exception e) {
            log.error("Redis get with type conversion failed: key={}, type={}, error={}",
                    key, clazz.getSimpleName(), e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取值并转换为指定类型（使用 KeyPrefix）
     *
     * @param prefix KeyPrefix 实例
     * @param key    键后缀
     * @param clazz  目标类型
     * @param <T>    泛型类型
     * @return 转换后的值
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        return get(prefix.getPrefix() + ":" + key, clazz);
    }

    /**
     * 删除键
     *
     * @param keys 键列表
     * @return 删除的键数量
     */
    public long delete(String... keys) {
        try {
            if (keys == null || keys.length == 0) {
                return 0;
            }
            Long count = redisTemplate.delete(Arrays.asList(keys));
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis delete operation failed: keys={}, error={}",
                    Arrays.toString(keys), e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 删除键（使用 KeyPrefix）
     *
     * @param prefix KeyPrefix 实例
     * @param keys   键后缀列表
     * @return 删除的键数量
     */
    public long delete(KeyPrefix prefix, String... keys) {
        if (keys == null || keys.length == 0) {
            return 0;
        }
        String[] realKeys = Arrays.stream(keys)
                .map(key -> prefix.getPrefix() + ":" + key)
                .toArray(String[]::new);
        return delete(realKeys);
    }

    /**
     * 检查键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis exists operation failed: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查键是否存在（使用 KeyPrefix）
     *
     * @param prefix KeyPrefix 实例
     * @param key    键后缀
     * @return 是否存在
     */
    public boolean exists(KeyPrefix prefix, String key) {
        return exists(prefix.getPrefix() + ":" + key);
    }

    // ========== 过期时间操作 ==========

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 是否成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            Boolean result = redisTemplate.expire(key, timeout, unit);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis expire operation failed: key={}, timeout={}, error={}",
                    key, timeout, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 设置过期时间（秒）
     *
     * @param key     键
     * @param seconds 过期时间（秒）
     * @return 是否成功
     */
    public boolean expire(String key, long seconds) {
        return expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取剩余过期时间
     *
     * @param key 键
     * @return 剩余过期时间（秒），-1表示永不过期，-2表示键不存在
     */
    public long getExpire(String key) {
        try {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire != null ? expire : -2;
        } catch (Exception e) {
            log.error("Redis getExpire operation failed: key={}, error={}", key, e.getMessage(), e);
            return -2;
        }
    }

    // ========== Hash 操作 ==========

    /**
     * Hash 设置
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     * @return 是否成功
     */
    public boolean hSet(String key, String field, Object value) {
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error("Redis hSet operation failed: key={}, field={}, error={}",
                    key, field, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Hash 获取
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    public Object hGet(String key, String field) {
        try {
            return redisTemplate.opsForHash().get(key, field);
        } catch (Exception e) {
            log.error("Redis hGet operation failed: key={}, field={}, error={}",
                    key, field, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Hash 批量设置
     *
     * @param key 键
     * @param map 字段值映射
     * @return 是否成功
     */
    public boolean hMSet(String key, Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return true;
            }
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("Redis hMSet operation failed: key={}, error={}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Hash 获取所有字段和值
     *
     * @param key 键
     * @return 字段值映射
     */
    public Map<Object, Object> hGetAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("Redis hGetAll operation failed: key={}, error={}", key, e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Hash 删除字段
     *
     * @param key    键
     * @param fields 字段列表
     * @return 删除的字段数量
     */
    public long hDelete(String key, Object... fields) {
        try {
            if (fields == null || fields.length == 0) {
                return 0;
            }
            Long count = redisTemplate.opsForHash().delete(key, fields);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis hDelete operation failed: key={}, fields={}, error={}",
                    key, Arrays.toString(fields), e.getMessage(), e);
            return 0;
        }
    }

    // ========== Set 操作 ==========

    /**
     * Set 添加元素
     *
     * @param key    键
     * @param values 值列表
     * @return 添加的元素数量
     */
    public long sAdd(String key, Object... values) {
        try {
            if (values == null || values.length == 0) {
                return 0;
            }
            Long count = redisTemplate.opsForSet().add(key, values);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis sAdd operation failed: key={}, values={}, error={}",
                    key, Arrays.toString(values), e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Set 获取所有元素
     *
     * @param key 键
     * @return 元素集合
     */
    public Set<Object> sMembers(String key) {
        try {
            Set<Object> members = redisTemplate.opsForSet().members(key);
            return members != null ? members : new HashSet<>();
        } catch (Exception e) {
            log.error("Redis sMembers operation failed: key={}, error={}", key, e.getMessage(), e);
            return new HashSet<>();
        }
    }

    /**
     * Set 移除元素
     *
     * @param key    键
     * @param values 值列表
     * @return 移除的元素数量
     */
    public long sRemove(String key, Object... values) {
        try {
            if (values == null || values.length == 0) {
                return 0;
            }
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis sRemove operation failed: key={}, values={}, error={}",
                    key, Arrays.toString(values), e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Set 检查元素是否存在
     *
     * @param key   键
     * @param value 值
     * @return 是否存在
     */
    public boolean sIsMember(String key, Object value) {
        try {
            Boolean result = redisTemplate.opsForSet().isMember(key, value);
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis sIsMember operation failed: key={}, value={}, error={}",
                    key, value, e.getMessage(), e);
            return false;
        }
    }

    // ========== List 操作 ==========

    /**
     * List 左侧推入元素
     *
     * @param key    键
     * @param values 值列表
     * @return 推入后的列表长度
     */
    public long lPush(String key, Object... values) {
        try {
            if (values == null || values.length == 0) {
                return 0;
            }
            Long count = redisTemplate.opsForList().leftPushAll(key, values);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis lPush operation failed: key={}, values={}, error={}",
                    key, Arrays.toString(values), e.getMessage(), e);
            return 0;
        }
    }

    /**
     * List 右侧推入元素
     *
     * @param key    键
     * @param values 值列表
     * @return 推入后的列表长度
     */
    public long rPush(String key, Object... values) {
        try {
            if (values == null || values.length == 0) {
                return 0;
            }
            Long count = redisTemplate.opsForList().rightPushAll(key, values);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis rPush operation failed: key={}, values={}, error={}",
                    key, Arrays.toString(values), e.getMessage(), e);
            return 0;
        }
    }

    /**
     * List 左侧弹出元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public Object lPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("Redis lPop operation failed: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * List 右侧弹出元素
     *
     * @param key 键
     * @return 弹出的元素
     */
    public Object rPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("Redis rPop operation failed: key={}, error={}", key, e.getMessage(), e);
            return null;
        }
    }

    /**
     * List 获取指定范围的元素
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     * @return 元素列表
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            List<Object> list = redisTemplate.opsForList().range(key, start, end);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            log.error("Redis lRange operation failed: key={}, start={}, end={}, error={}",
                    key, start, end, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * List 获取长度
     *
     * @param key 键
     * @return 列表长度
     */
    public long lLen(String key) {
        try {
            Long length = redisTemplate.opsForList().size(key);
            return length != null ? length : 0;
        } catch (Exception e) {
            log.error("Redis lLen operation failed: key={}, error={}", key, e.getMessage(), e);
            return 0;
        }
    }

    // ========== 分布式锁 ==========

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁的键
     * @param requestId  请求标识（用于释放锁时验证）
     * @param expireTime 锁的过期时间（秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime) {
        try {
            // setIfAbsent: 这个参数是实现互斥（mutual exclusion）的关键。
            // 它告诉 Redis：“只有当这个 lockKey 不存在的时候，才设置它的值。如果已经存在了，就什么都不做。”
            Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId,
                    Duration.ofSeconds(expireTime));
            return result != null && result;
        } catch (Exception e) {
            log.error("Redis tryLock operation failed: lockKey={}, requestId={}, error={}",
                    lockKey, requestId, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁的键
     * @param requestId 请求标识（必须与获取锁时相同）
     * @return 是否释放成功
     */
    public boolean releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else return 0 end";
        try {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            redisScript.setResultType(Long.class);

            Long result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
            return result != null && result == 1L;
        } catch (Exception e) {
            log.error("Redis releaseLock operation failed: lockKey={}, requestId={}, error={}",
                    lockKey, requestId, e.getMessage(), e);
            return false;
        }
    }

    // ========== 原子操作 ==========

    /**
     * 原子递增
     *
     * @param key 键
     * @return 递增后的值
     */
    public long increment(String key) {
        try {
            Long result = redisTemplate.opsForValue().increment(key);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("Redis increment operation failed: key={}, error={}", key, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 原子递增指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递增后的值
     */
    public long increment(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("Redis increment operation failed: key={}, delta={}, error={}",
                    key, delta, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 原子递减
     *
     * @param key 键
     * @return 递减后的值
     */
    public long decrement(String key) {
        try {
            Long result = redisTemplate.opsForValue().decrement(key);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("Redis decrement operation failed: key={}, error={}", key, e.getMessage(), e);
            return 0L;
        }
    }

    /**
     * 原子递减指定步长
     *
     * @param key   键
     * @param delta 步长
     * @return 递减后的值
     */
    public long decrement(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().decrement(key, delta);
            return result != null ? result : 0L;
        } catch (Exception e) {
            log.error("Redis decrement operation failed: key={}, delta={}, error={}",
                    key, delta, e.getMessage(), e);
            return 0L;
        }
    }

    // ========== 批量操作 ==========

    /**
     * 批量获取
     *
     * @param keys 键列表
     * @return 值列表
     */
    public List<Object> multiGet(Collection<String> keys) {
        try {
            if (CollectionUtils.isEmpty(keys)) {
                return new ArrayList<>();
            }
            List<Object> values = redisTemplate.opsForValue().multiGet(keys);
            return values != null ? values : new ArrayList<>();
        } catch (Exception e) {
            log.error("Redis multiGet operation failed: keys={}, error={}", keys, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 批量设置
     *
     * @param map 键值映射
     * @return 是否成功
     */
    public boolean multiSet(Map<String, Object> map) {
        try {
            if (map == null || map.isEmpty()) {
                return true;
            }
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            log.error("Redis multiSet operation failed: error={}", e.getMessage(), e);
            return false;
        }
    }

    // ========== 工具方法 ==========

    /**
     * 根据模式获取键列表
     *
     * @param pattern 模式（支持通配符）
     * @return 键列表
     */
    public Set<String> keys(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            return keys != null ? keys : new HashSet<>();
        } catch (Exception e) {
            log.error("Redis keys operation failed: pattern={}, error={}", pattern, e.getMessage(), e);
            return new HashSet<>();
        }
    }

    /**
     * 获取 RedisTemplate 实例（供高级操作使用）
     *
     * @return RedisTemplate 实例
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
