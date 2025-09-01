package com.kisesaki.blog.redis;

/** Redis Key 前缀接口。
 * 实现该接口的类用于定义 Redis 键的前缀，帮助组织和管理 Redis 中存储的数据。
 * 通过使用前缀，可以避免键冲突，并使得键更具可读性和结构化。
 * 例如，可以为不同的业务模块或数据类型定义不同的前缀，如 "user:", "order:" 等。
 * 这样在存储和检索数据时，可以更容易地识别和操作相关的键。
 * 注意：实现该接口的类通常会包含一个方法来返回具体的前缀字符串。
 */
public interface KeyPrefix {
    /**
     * 获取 Redis 键的前缀字符串。
     *
     * @return 前缀字符串
     */
    String getPrefix();

    /**
     * 获取前缀的过期时间（秒）。
     *
     * @return 过期时间，单位为秒
     */
    int getExpireSeconds();
}
