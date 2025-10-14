package com.kisesaki.blog.notification.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.kisesaki.blog.notification.config.email.EmailConfigurationProperties;
import com.kisesaki.blog.notification.config.email.EmailTemplatePreloadListener;
import com.kisesaki.blog.notification.dto.EmailTemplate;
import com.kisesaki.blog.notification.enums.EmailType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件模板服务
 *
 * @author KiseSaki
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final ResourceLoader resourceLoader;
    private final EmailTemplatePreloadListener templatePreloader;
    private final EmailConfigurationProperties emailProperties;

    /**
     * 前端应用的基础URL
     */
    @Value("${kisesaki.blog.frontend.base-url}")
    private String frontendBaseUrl;

    /**
     * 模板元数据缓存
     */
    private final Map<String, EmailTemplate> templateMetadataCache = new ConcurrentHashMap<>();

    /**
     * 模板变量匹配模式 ${variable}
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    /**
     * 根据邮件类型处理模板
     *
     * @param emailType 邮件类型
     * @param variables 模板变量
     * @param locale    语言环境
     * @return 处理后的HTML内容
     * @throws TemplateNotFoundException 模板未找到异常
     */
    public String processTemplate(EmailType emailType, Map<String, Object> variables, Locale locale) {
        String templatePath = emailType.getTemplatePath();
        return processTemplateByPath(templatePath, variables, locale);
    }

    /**
     * 根据模板路径处理模板
     *
     * @param templatePath 模板路径
     * @param variables    模板变量
     * @param locale       语言环境
     * @return 处理后的HTML内容
     * @throws RuntimeException 模板处理异常
     */
    public String processTemplateByPath(String templatePath, Map<String, Object> variables, Locale locale) {
        try {
            // 1. 获取模板内容
            String templateContent = getTemplateContent(templatePath);

            // 2. 添加通用变量
            Map<String, Object> allVariables = new ConcurrentHashMap<>();
            addCommonVariables(allVariables);
            if (variables != null) {
                allVariables.putAll(variables);
            }

            // 3. 替换模板变量
            String processedContent = replaceVariables(templateContent, allVariables);

            log.debug("模板处理完成: {}", templatePath);
            return processedContent;

        } catch (Exception e) {
            log.error("模板处理失败: {} - {}", templatePath, e.getMessage(), e);
            throw new RuntimeException("模板处理失败: " + templatePath, e);
        }
    }

    /**
     * 获取模板内容
     *
     * @param templatePath 模板路径
     * @return 模板内容
     * @throws RuntimeException 模板处理异常
     */
    public String getTemplateContent(String templatePath) {
        // 1. 先从预加载缓存获取
        String cachedContent = templatePreloader.getCachedTemplate(templatePath);
        if (cachedContent != null) {
            log.debug("从缓存获取模板: {}", templatePath);
            return cachedContent;
        }

        // 2. 从文件系统加载
        try {
            String content = loadTemplateFromFile(templatePath);

            // 3. 缓存模板内容
            templatePreloader.cacheTemplate(templatePath, content);

            log.debug("从文件加载模板: {}", templatePath);
            return content;

        } catch (IOException e) {
            log.error("模板文件加载失败: {} - {}", templatePath, e.getMessage());
            throw new RuntimeException("模板处理失败: " + templatePath, e);
        }
    }

    /**
     * 验证模板语法
     *
     * @param templateContent 模板内容
     * @param sampleVariables 示例变量
     * @return 是否有效
     */
    public boolean validateTemplate(String templateContent, Map<String, Object> sampleVariables) {
        try {
            // 简单验证：检查是否有未替换的变量
            String processed = replaceVariables(templateContent, sampleVariables);

            // 如果还有未替换的 ${} 变量，则认为验证失败
            Matcher matcher = VARIABLE_PATTERN.matcher(processed);
            boolean hasUnresolvedVariables = matcher.find();

            if (hasUnresolvedVariables) {
                log.warn("模板中存在未解析的变量");
                return false;
            }

            return true;

        } catch (Exception e) {
            log.warn("模板验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 获取模板元数据
     *
     * @param emailType 邮件类型
     * @return 模板元数据
     */
    public EmailTemplate getTemplateMetadata(EmailType emailType) {
        String cacheKey = emailType.getCode();

        return templateMetadataCache.computeIfAbsent(cacheKey, k -> {
            EmailTemplate template = EmailTemplate.builder()
                    .code(emailType.getCode())
                    .name(emailType.getDescription())
                    .emailType(emailType)
                    .templatePath(emailType.getTemplatePath())
                    .enabled(true)
                    .isDefault(true)
                    .locale("zh_CN")
                    .version("1.0")
                    .build();

            log.debug("创建模板元数据: {}", emailType.getCode());
            return template;
        });
    }

    /**
     * 清空模板缓存
     */
    public void clearTemplateCache() {
        templatePreloader.clearCache();
        templateMetadataCache.clear();
        log.info("模板缓存已清空");
    }

    /**
     * 预热模板缓存
     *
     * @param templatePaths 要预热的模板路径列表
     */
    public void warmupTemplateCache(String... templatePaths) {
        for (String templatePath : templatePaths) {
            try {
                getTemplateContent(templatePath);
                log.debug("预热模板缓存: {}", templatePath);
            } catch (Exception e) {
                log.warn("预热模板失败: {} - {}", templatePath, e.getMessage());
            }
        }
    }

    /**
     * 替换模板变量
     *
     * @param template  模板内容
     * @param variables 变量映射
     * @return 替换后的内容
     */
    private String replaceVariables(String template, Map<String, Object> variables) {
        if (template == null || variables == null || variables.isEmpty()) {
            return template;
        }

        StringBuffer result = new StringBuffer();
        Matcher matcher = VARIABLE_PATTERN.matcher(template);

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object variableValue = variables.get(variableName);

            String replacement;
            if (variableValue != null) {
                replacement = String.valueOf(variableValue);
            } else {
                // 保留未找到的变量
                replacement = matcher.group(0);
                log.debug("模板变量未找到: {}", variableName);
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 从文件系统加载模板
     *
     * @param templatePath 模板路径
     * @return 模板内容
     * @throws IOException IO异常
     */
    private String loadTemplateFromFile(String templatePath) throws IOException {
        String fullPath = "classpath:" + emailProperties.getTemplate().getBasePath() + "/" + templatePath;
        Resource resource = resourceLoader.getResource(fullPath);

        if (!resource.exists()) {
            throw new IOException("模板文件不存在: " + fullPath);
        }

        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 添加通用模板变量
     *
     * @param variables 变量映射
     */
    private void addCommonVariables(Map<String, Object> variables) {
        LocalDateTime now = LocalDateTime.now();

        // 添加当前时间
        variables.put("currentTime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        variables.put("currentDate", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        variables.put("currentYear", now.getYear());

        // 添加应用信息
        variables.put("appName", "KiseSaki Blog");
        variables.put("appUrl", "https://kisesaki.com");
        variables.put("supportEmail", "support@kisesaki.com");

        // 添加前端基础URL - 邮件模板中的关键变量
        variables.put("frontendBaseUrl", frontendBaseUrl);

        // 添加公共资源链接
        variables.put("baseUrl", "https://kisesaki.com");
        variables.put("logoUrl", "https://kisesaki.com/static/logo.png");

        // 添加社交媒体链接
        variables.put("githubUrl", "https://github.com/KiseSaki");
        variables.put("weiboUrl", "https://weibo.com/kisesaki");

        // 添加版权信息
        variables.put("copyright", "© " + now.getYear() + " KiseSaki Blog. All rights reserved.");
    }
}
