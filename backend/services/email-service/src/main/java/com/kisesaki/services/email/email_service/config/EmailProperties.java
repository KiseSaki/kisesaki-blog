package com.kisesaki.services.email.email_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 邮件服务配置属性
 *
 * @author KiseSaki
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailProperties {

    private From from = new From();
    private Retry retry = new Retry();
    private Sending sending = new Sending();
    private Templates templates = new Templates();

    @Data
    public static class From {
        private String address = "noreply@kisesaki.com";
        private String name = "KiseSaki博客";
    }

    @Data
    public static class Retry {
        private int maxAttempts = 3;
        private long initialInterval = 1000;
        private double multiplier = 2.0;
        private long maxInterval = 10000;
    }

    @Data
    public static class Sending {
        private int batchSize = 10;
        private int rateLimit = 100; // 每分钟
    }

    @Data
    public static class Templates {
        private String basePath = "templates/email";
    }
}
