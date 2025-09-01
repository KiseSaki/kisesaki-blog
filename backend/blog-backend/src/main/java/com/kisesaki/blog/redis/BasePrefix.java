package com.kisesaki.blog.redis;

/**
 * Redis Key 前缀基类。
 *
 * 该类实现了 KeyPrefix 接口，提供了前缀和过期时间的基本实现。
 * 通过继承该类，可以方便地创建具有特定前缀和过期时间的 Redis 键前缀类。
 *
 * 例如，可以创建一个 UserPrefix 类，继承自 BasePrefix，并设置特定的前缀和过期时间。
 * 这样在使用 Redis 时，可以通过 UserPrefix 来管理与用户相关的数据键。
 *
 * 注意：前缀通常用于组织和区分不同类型的数据，过期时间用于控制数据的生命周期。
 */
public abstract class BasePrefix implements KeyPrefix{
    private final int expireSeconds;
    private final String prefix;

    // 默认0代表永不过期
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int getExpireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        // 获取类名作为前缀的一部分，确保唯一性
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}