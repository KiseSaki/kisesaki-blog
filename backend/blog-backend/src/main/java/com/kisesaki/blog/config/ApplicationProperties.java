package com.kisesaki.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 应用通用配置属性
 *
 * @author KiseSaki
 */
@Data
@Component
@ConfigurationProperties(prefix = "kisesaki.blog")
public class ApplicationProperties {

    /* 前端应用配置 */
    private Frontend frontend = new Frontend();

    @Data
    public static class Frontend {
        // 前端基础URL
        private String baseUrl;
    }
}