package com.kisesaki.blog.notification.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件模板预加载监听器
 * 在应用启动时预加载常用邮件模板到内存中
 * 
 * @author KiseSaki
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailTemplatePreloadListener {

    private final ResourceLoader resourceLoader;
    private final EmailConfigurationProperties emailProperties;

    /**
     * 模板缓存
     */
    private final ConcurrentHashMap<String, String> templateCache = new ConcurrentHashMap<>();

    /**
     * 预加载的模板列表
     */
    private static final String[] PRELOAD_TEMPLATES = {
            "auth/welcome.html",
            "auth/email-verification.html",
            "auth/password-reset.html",
            "auth/password-changed.html",
            "notification/comment-reply.html",
            "notification/post-published.html",
            "notification/follow-notification.html",
            "admin/user-registered.html",
            "admin/system-alert.html"
    };

    /**
     * 应用启动完成后预加载模板
     */
    @EventListener(ApplicationReadyEvent.class)
    public void preloadTemplates() {
        if (!emailProperties.getTemplate().getCacheEnable()) {
            log.info("邮件模板缓存已禁用，跳过预加载");
            return;
        }

        log.info("开始预加载邮件模板...");

        int successCount = 0;
        int failCount = 0;

        for (String templatePath : PRELOAD_TEMPLATES) {
            try {
                String content = loadTemplate(templatePath);
                if (StringUtils.hasText(content)) {
                    templateCache.put(templatePath, content);
                    successCount++;
                    log.debug("成功预加载模板: {}", templatePath);
                } else {
                    failCount++;
                    log.warn("模板内容为空: {}", templatePath);
                }
            } catch (Exception e) {
                failCount++;
                log.warn("预加载模板失败: {} - {}", templatePath, e.getMessage());
            }
        }

        log.info("邮件模板预加载完成 - 成功: {}, 失败: {}, 缓存大小: {}",
                successCount, failCount, templateCache.size());
    }

    /**
     * 从缓存获取模板内容
     * 
     * @param templatePath 模板路径
     * @return 模板内容
     */
    public String getCachedTemplate(String templatePath) {
        return templateCache.get(templatePath);
    }

    /**
     * 检查模板是否已缓存
     * 
     * @param templatePath 模板路径
     * @return 是否已缓存
     */
    public boolean isCached(String templatePath) {
        return templateCache.containsKey(templatePath);
    }

    /**
     * 清空模板缓存
     */
    public void clearCache() {
        templateCache.clear();
        log.info("邮件模板缓存已清空");
    }

    /**
     * 手动缓存模板
     * 
     * @param templatePath 模板路径
     * @param content      模板内容
     */
    public void cacheTemplate(String templatePath, String content) {
        if (emailProperties.getTemplate().getCacheEnable()) {
            templateCache.put(templatePath, content);
            log.debug("手动缓存模板: {}", templatePath);
        }
    }

    /**
     * 加载模板文件内容
     * 
     * @param templatePath 模板路径
     * @return 模板内容
     * @throws IOException IO异常
     */
    private String loadTemplate(String templatePath) throws IOException {
        String fullPath = "classpath:" + emailProperties.getTemplate().getBasePath() + "/" + templatePath;
        Resource resource = resourceLoader.getResource(fullPath);

        if (!resource.exists()) {
            throw new IOException("模板文件不存在: " + fullPath);
        }

        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 获取缓存统计信息
     * 
     * @return 缓存大小
     */
    public int getCacheSize() {
        return templateCache.size();
    }
}