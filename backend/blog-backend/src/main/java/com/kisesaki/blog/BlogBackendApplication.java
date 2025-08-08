package com.kisesaki.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * KiseSaki Blog 后端应用启动类
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@SpringBootApplication @EnableJpaAuditing // 启用 JPA 审计功能（创建时间、更新时间等）
@EnableCaching // 启用缓存功能
@EnableAsync // 启用异步处理
@EnableKafka // 启用 Kafka 支持
public class BlogBackendApplication {

    public static void main(String[] args) {
        // 加载 .env 文件到环境变量中
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(BlogBackendApplication.class, args);
    }
}
