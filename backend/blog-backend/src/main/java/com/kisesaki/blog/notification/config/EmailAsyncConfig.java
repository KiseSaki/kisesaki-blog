package com.kisesaki.blog.notification.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件异步处理配置
 * 
 * @author KiseSaki
 */
@Slf4j
@Configuration
@EnableAsync // 启用异步处理
@RequiredArgsConstructor
public class EmailAsyncConfig {

    private final EmailConfigurationProperties emailConfigurationProperties;

    /**
     * 邮件发送异步线程池
     */
    @Bean("emailTaskExecutor")
    public Executor emailTaskExecutor() {
        // 邮件发送异步线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        EmailConfigurationProperties.Async async = emailConfigurationProperties.getAsync();

        // 线程池配置
        // 设置核心线程数，目的：保证在高并发情况下，线程池能够及时响应请求
        executor.setCorePoolSize(async.getCorePoolSize());
        // 设置最大线程数，目的：防止资源耗尽，保证系统稳定
        executor.setMaxPoolSize(async.getMaxPoolSize());
        // 设置队列容量，目的：控制请求积压，避免OOM
        executor.setQueueCapacity(async.getQueueCapacity());
        // 设置线程名称前缀，目的：便于排查问题
        executor.setThreadNamePrefix(async.getThreadNamePrefix());
        // 设置线程存活时间，目的：防止线程资源浪费
        executor.setKeepAliveSeconds(async.getKeepAliveSeconds());

        // 拒绝策略：由调用线程处理，目的：防止请求丢失
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务完成后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

		// 初始化线程池
        executor.initialize();

        log.info("邮件异步线程池初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}",
                async.getCorePoolSize(), async.getMaxPoolSize(), async.getQueueCapacity());

        return executor;
    }

}
