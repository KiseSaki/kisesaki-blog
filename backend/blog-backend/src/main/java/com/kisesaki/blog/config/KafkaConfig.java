package com.kisesaki.blog.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * Kafka配置类
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Configuration @EnableKafka
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    /**
     * Kafka管理员配置
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    /**
     * 文章事件主题
     */
    @Bean
    public NewTopic postEventTopic() {
        return TopicBuilder.name("blog-post-events").partitions(3).replicas(1).build();
    }

    /**
     * 用户事件主题
     */
    @Bean
    public NewTopic userEventTopic() {
        return TopicBuilder.name("blog-user-events").partitions(3).replicas(1).build();
    }

    /**
     * 日志事件主题
     */
    @Bean
    public NewTopic logEventTopic() {
        return TopicBuilder.name("blog-log-events").partitions(2).replicas(1).build();
    }

    /**
     * 通知事件主题
     */
    @Bean
    public NewTopic notificationEventTopic() {
        return TopicBuilder.name("blog-notification-events").partitions(2).replicas(1).build();
    }

    /**
     * 统计事件主题
     */
    @Bean
    public NewTopic statisticsEventTopic() {
        return TopicBuilder.name("blog-statistics-events").partitions(2).replicas(1).build();
    }
}
