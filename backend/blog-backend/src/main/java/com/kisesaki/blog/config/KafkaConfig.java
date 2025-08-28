package com.kisesaki.blog.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import lombok.extern.slf4j.Slf4j;

/**
 * Kafka 配置类
 *
 * 提供生产者与消费者的默认 Bean 配置。配置基于 Spring Boot 的 KafkaProperties，
 * 并明确指定了 key/value 的序列化/反序列化器以避免由于默认类型导致的序列化问题。
 * 
 * 增强功能：
 * - 添加了错误处理器和重试机制
 * - 配置了消费者并发度和提交模式
 * - 添加了详细的日志记录
 * 
 * 注意事项：
 * - 如果需要在消费者端反序列化为自定义对象，请将 VALUE_DESERIALIZER 改为 JsonDeserializer 并设置
 * TRUSTED_PACKAGES；
 * - 生产者默认使用 JsonSerializer 作为 value 序列化器，发送对象时会将其序列化为 JSON。
 */
@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {
    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * 生产者配置 Map
     *
     * 基于 application.yml 中的 spring.kafka 配置构建，并显式设置 key/value 的序列化器。
     * value 使用 JsonSerializer，方便发送 POJO 对象。
     *
     * @return 用于构造 ProducerFactory 的配置 Map
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
        // 明确 key/value 序列化器
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // 生产者性能优化配置
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // 等待所有副本确认
        props.put(ProducerConfig.RETRIES_CONFIG, 3); // 重试次数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 批处理大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1); // 批处理等待时间
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 缓冲区大小

        log.info("Kafka producer configured with enhanced settings");
        return props;
    }

    /**
     * 生产者工厂
     *
     * 基于 producerConfigs 创建 DefaultKafkaProducerFactory，用于注入 KafkaTemplate。
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * KafkaTemplate Bean
     *
     * 用于发送消息到 Kafka。建议在上层封装一个发布工具类以便统一处理主题、分区与错误重试策略。
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(pf);
        // 设置默认主题
        // template.setDefaultTopic("blog-events");
        log.info("KafkaTemplate configured successfully");
        return template;
    }

    /**
     * 消费者配置 Map
     *
     * 基于 application.yml 中的 spring.kafka 配置构建，并配置错误处理和重试机制。
     * 支持 ErrorHandlingDeserializer 来处理反序列化异常。
     *
     * @return 用于构造 ConsumerFactory 的配置 Map
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());

        // 配置反序列化器，使用错误处理包装器
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // 配置实际的反序列化器
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);

        // 消费者性能配置
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 手动提交
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 从最早的消息开始消费
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500); // 每次拉取的最大记录数
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000); // 会话超时时间

        log.info("Kafka consumer configured with error handling and performance optimizations");
        return props;
    }

    /**
     * 消费者工厂
     *
     * 返回 DefaultKafkaConsumerFactory，配置了错误处理机制。
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     * KafkaListener 容器工厂
     *
     * 为 @KafkaListener 提供 ConcurrentKafkaListenerContainerFactory 实例。
     * 配置了错误处理器、重试机制和并发设置。
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> cf) {
        ConcurrentKafkaListenerContainerFactory<String, String> f = new ConcurrentKafkaListenerContainerFactory<>();
        f.setConsumerFactory(cf);

        // 设置并发级别
        f.setConcurrency(3);

        // 配置错误处理器
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 3));
        f.setCommonErrorHandler(errorHandler);

        // 配置提交模式
        f.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        log.info("KafkaListenerContainerFactory configured with error handling and retry mechanism");
        return f;
    }
}