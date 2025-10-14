package com.kisesaki.blog.auth.event;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.kisesaki.blog.notification.kafka.EmailKafkaProducer;
import com.kisesaki.blog.redis.RedisService;
import com.kisesaki.blog.user.Keys.UserKey;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationEventListener {
    private final RedisService redisService;
    private final EmailKafkaProducer emailKafkaProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistrationEvent(UserRegistrationEvent event) {
        try {
            // 生成邮箱验证令牌
            String emailToken = UUID.randomUUID().toString();
            // 构建Redis键
            String redisKey = UserKey.buildEmailVerificationKey(emailToken);

            Map<String, Object> verificationData = new HashMap<>();
            verificationData.put("userId", event.getUserId());
            verificationData.put("email", event.getEmail());
            verificationData.put("createdAt", System.currentTimeMillis());

            // 存储到Redis
            boolean stored = redisService.hMSet(redisKey, verificationData);
            if (stored) {
                // 设置过期时间为24小时
                redisService.expire(redisKey, 24 * 60 * 60);
                // 发送验证邮箱邮件事件
                emailKafkaProducer.publishUserRegistrationEvent(event.getEmail(), event.getUserId(), event.getUsername(), emailToken);
                log.info("已生成邮箱验证令牌并存储到Redis，用户：{}，邮箱：{}", event.getUsername(), event.getEmail());
            } else {
                log.error("存储邮箱验证数据到Redis失败，用户：{}，邮箱：{}", event.getUsername(), event.getEmail());
            }
        } catch (Exception emailException) {
            // 邮件发送失败不应该影响注册结果，只记录错误日志
            log.error("发送欢迎邮件失败，用户：{}，邮箱：{}，错误：{}",
                event.getUsername(), event.getEmail(), emailException.getMessage(), emailException);
        }
    }
}
