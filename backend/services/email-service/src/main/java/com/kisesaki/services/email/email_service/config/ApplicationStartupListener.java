package com.kisesaki.services.email.email_service.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.kisesaki.services.email.email_service.kafka.model.EmailType;
import com.kisesaki.services.email.email_service.service.EmailTemplateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 应用启动监听器
 * 在应用启动完成后执行初始化任务
 *
 * @author KiseSaki
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupListener {

    private final EmailTemplateService emailTemplateService;

    /**
     * 应用启动完成后的事件处理
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("邮件服务启动完成，开始执行初始化任务...");
        
        // 预热常用邮件模板缓存
        warmupTemplateCache();
        
        log.info("邮件服务初始化完成，已准备就绪");
    }

    /**
     * 预热模板缓存
     * 提前加载常用邮件模板，提高首次发送速度
     */
    private void warmupTemplateCache() {
        try {
            log.info("开始预热邮件模板缓存...");
            
            // 预热认证相关的高频模板
            emailTemplateService.warmupTemplateCache(
                EmailType.EMAIL_VERIFICATION,
                EmailType.PASSWORD_RESET,
                EmailType.WELCOME,
                EmailType.PASSWORD_CHANGED
            );
            
            // 可以根据业务需要添加更多模板
            
        } catch (Exception e) {
            log.warn("模板缓存预热失败，不影响服务运行: {}", e.getMessage());
        }
    }
}
