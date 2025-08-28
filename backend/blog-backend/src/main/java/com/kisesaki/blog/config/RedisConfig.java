package com.kisesaki.blog.config;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis 配置类。
 *
 * 提供一个通用的 RedisTemplate Bean，默认使用 String 作为 key/hashKey 的序列化器，
 * 使用 GenericJackson2JsonRedisSerializer 作为 value/hashValue 的序列化器。
 * 这样确保开发中存取对象时具有良好的可读性与向后兼容性，同时避免 Java 序列化带来的跨语言问题。
 * 
 * 注意：
 * 如果需要存储非常敏感的数据，请在业务层面做好加密处理或使用专门的序列化策略。
 * 如需自定义 ObjectMapper（例如处理多态类型），可替换 GenericJackson2JsonRedisSerializer
 * 的构造器。
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig {

    /**
     * 自定义ObjectMapper，处理Java8时间序列化
     */
    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 注册Java8时间模块
        mapper.registerModule(new JavaTimeModule());
        // 设置可见性
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 启用默认类型信息，用于反序列化
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        return mapper;
    }

    /**
     * 通用 RedisTemplate Bean 配置。
     *
     * - key/hashKey 使用 StringRedisSerializer 保证在 Redis 控制台可读性；
     * - value/hashValue 使用 GenericJackson2JsonRedisSerializer，支持对象的 JSON 序列化与反序列化；
     *
     * @param cf RedisConnectionFactory，由 Spring 管理注入
     * @return 配置好的 RedisTemplate 实例
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> tpl = new RedisTemplate<>();
        // 关联连接工厂
        tpl.setConnectionFactory(cf);

        // 使用自定义的 ObjectMapper 创建序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(
                createObjectMapper());

        // key 使用字符串序列化，便于在 redis-cli 中查看
        tpl.setKeySerializer(new StringRedisSerializer());

        // value 使用 JSON 序列化，避免 Java 原生序列化带来的兼容性问题
        tpl.setValueSerializer(jsonSerializer);

        // hash 的 key 同样使用字符串序列化（常用于 map 结构中的字段）
        tpl.setHashKeySerializer(new StringRedisSerializer());

        // hash 的 value 使用 JSON 序列化
        tpl.setHashValueSerializer(jsonSerializer);

        // 初始化属性，确保配置生效
        tpl.afterPropertiesSet();

        log.info("RedisTemplate configured with JSON serializer and Java 8 time support");
        return tpl;
    }

    /**
     * 缓存管理器配置
     * 
     * 配置Redis作为Spring Cache的实现，支持多种缓存配置
     * 只有在没有其他CacheManager Bean时才创建此Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedisConnectionFactory cf) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存有效期为30分钟
                .entryTtl(Duration.ofMinutes(30))
                // 设置key序列化器
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value序列化器
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(createObjectMapper())))
                // 不缓存null值
                .disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager.builder(cf)
                .cacheDefaults(config)
                .build();

        log.info("RedisCacheManager configured with 30 minutes TTL and JSON serialization");
        return cacheManager;
    }

}
