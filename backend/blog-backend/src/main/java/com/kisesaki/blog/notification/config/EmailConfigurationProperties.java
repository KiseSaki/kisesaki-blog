package com.kisesaki.blog.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "kisesaki.blog.email")
public class EmailConfigurationProperties {

    /* SMTP服务器配置 */
    private Smtp smtp = new Smtp();

    @Data
    public static class Smtp {
        // SMTP服务器地址
        private String host;
        // SMTP服务器端口
        private Integer port;
        // 发件人邮箱
        private String username;
        // 发件人邮箱密码或授权码
        private String password;
        // 是否启用SSL
        private Boolean sslEnable = true;
        // 是否启用TLS
        private Boolean starttlsEnable = false;
        // 是否启用认证
        private Boolean auth = true;
        // 连接超时时间（毫秒）
        private Integer connectionTimeout = 30000;
        // 读取超时时间（毫秒）
        private Integer readTimeout = 30000;
    }

    /* 模板配置 */
    private Template template = new Template();

    @Data
    public static class Template {
        // 模板根路径
        private String basePath = "templates/email/";
        // 默认模板编码
        private String encoding = "UTF-8";
        // 是否启用模板缓存
        private Boolean cacheEnable = true;
        // 模板缓存时间
        private Integer cacheTime = 3600; // 单位秒
    }
}
