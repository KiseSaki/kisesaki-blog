package com.kisesaki.blog.notification.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaEmailTopicConfig {

    // 邮件通知主题
    public static final String EMAIL_TOPIC = "email_notifications";

    // 高优先通知主题
    public static final String EMAIL_HIGH_PRIORITY_TOPIC = "email_high_priority_notifications";

    // 死信队列主题 (处理失败的邮件事件会被发送到这个主题)
    public static final String EMAIL_DEAD_LETTER_TOPIC = "email_dead_letter_notifications";

    /**
     * 创建普通邮件事件主题
     * <p>
     * 分区数: 3（可根据实际负载调整）
     * 副本数: 1（开发环境单broker，生产环境建议至少2个副本以保证高可用）
     * 压缩策略: delete（保留指定时间后删除）
     * 保留时间: 7天
     *
     * @return NewTopic 配置
     */
    @Bean
    public NewTopic emailEventsTopic() {
        return TopicBuilder.name(EMAIL_TOPIC)
            .partitions(3) // 分区数
            .replicas(1) // 副本数（开发环境）
            .config("retention.ms", "604800000") // 保留7天
            .config("compression.type", "snappy") // 压缩类型
            .build();
    }

    /**
     * 创建高优先级邮件事件主题
     * <p>
     * 分区数: 2（可根据实际负载调整）
     * 副本数: 1（开发环境单broker，生产环境建议至少2个副本以保证高可用）
     * 压缩策略: delete（保留指定时间后删除）
     * 保留时间: 3天
     *
     * @return NewTopic 配置
     */
    @Bean
    public NewTopic emailHighPriorityEventsTopic() {
        return TopicBuilder.name(EMAIL_HIGH_PRIORITY_TOPIC)
            .partitions(2) // 分区数
            .replicas(1) // 副本数（开发环境）
            .config("retention.ms", "259200000") // 保留3天
            .config("compression.type", "snappy") // 压缩类型
            .build();
    }

    /**
     * 创建邮件事件死信队列主题
     * <p>
     * 分区数: 1（死信队列通常负载较低）
     * 副本数: 1（开发环境单broker，生产环境建议至少2个副本以保证高可用）
     * 压缩策略: delete（保留指定时间后删除）
     * 保留时间: 14天
     *
     * @return NewTopic 配置
     */
    @Bean
    public NewTopic emailDeadLetterTopic() {
        return TopicBuilder.name(EMAIL_DEAD_LETTER_TOPIC)
            .partitions(1) // 分区数
            .replicas(1) // 副本数（开发环境）
            .config("retention.ms", "1209600000") // 保留14天
            .config("compression.type", "snappy") // 压缩类型
            .build();
    }
}
