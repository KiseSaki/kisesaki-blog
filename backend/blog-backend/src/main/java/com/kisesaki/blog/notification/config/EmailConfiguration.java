package com.kisesaki.blog.notification.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮件SMTP服务器配置类
 *
 * @author KiseSaki
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class EmailConfiguration {

    private final EmailConfigurationProperties emailConfigurationProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        // JavaMailSenderImpl 是 JavaMailSender 接口的实现类，属于 Spring Framework 的邮件发送模块。
        // 它封装了 SMTP 邮件发送的具体实现，允许你配置邮件服务器地址、端口、用户名、密码等参数，并通过它发送邮件。
        // 通常在 Spring Boot 项目中用于邮件服务的配置和发送。
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // 获取SMTP配置
        EmailConfigurationProperties.Smtp smtp = emailConfigurationProperties.getSmtp();

        // 基本配置
        mailSender.setHost(smtp.getHost());
        mailSender.setPort(smtp.getPort());
        mailSender.setUsername(smtp.getUsername());
        mailSender.setPassword(smtp.getPassword());

        // SMTP属性配置
        // getJavaMailProperties返回的是该实现类内部使用的 Properties 对象的引用，
        // 直接对 props 进行修改就等于修改了 mailSender 内部的属性
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", smtp.getAuth());
        props.put("mail.smtp.starttls.enable", smtp.getStarttlsEnable());
        props.put("mail.smtp.ssl.enable", smtp.getSslEnable());
        props.put("mail.smtp.connectionout", smtp.getConnectionTimeout());
        props.put("mail.smtp.timeout", smtp.getReadTimeout());
        props.put("mail.debug", "false"); // 生产环境建议关闭debug

        // SSL配置
        if (smtp.getSslEnable()) {
            props.put("mail.smtp.ssl.trust", smtp.getHost());
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", smtp.getPort());
        }

        log.info("邮件配置初始化完成 - Host: {}, Port: {}, Username: {}",
                smtp.getHost(), smtp.getPort(), smtp.getUsername());

        return mailSender;
    }
}
