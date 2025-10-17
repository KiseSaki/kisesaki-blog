package com.kisesaki.services.email.email_service.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.kisesaki.services.email.email_service.config.EmailProperties;
import com.kisesaki.services.email.email_service.kafka.model.EmailType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件模板服务
 * 负责渲染Thymeleaf模板，支持缓存和降级
 *
 * @author KiseSaki
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;
    private final EmailProperties emailProperties;

    /**
     * 模板内容缓存
     */
    private final Map<String, String> templateCache = new ConcurrentHashMap<>();

    /**
     * 模板变量匹配模式 ${variable}
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    /**
     * 渲染邮件模板
     *
     * @param emailType 邮件类型
     * @param variables 模板变量
     * @return 渲染后的HTML内容
     */
    public String renderTemplate(EmailType emailType, Map<String, Object> variables) {
        try {
            Context context = new Context();
            
            // 添加通用变量
            Map<String, Object> allVariables = new ConcurrentHashMap<>();
            addCommonVariables(allVariables);
            
            // 添加用户提供的变量（会覆盖同名的通用变量）
            if (variables != null && !variables.isEmpty()) {
                allVariables.putAll(variables);
            }
            
            context.setVariables(allVariables);

            // 使用邮件类型的模板路径
            String templatePath = emailType.getTemplatePath();
            
            log.debug("渲染邮件模板: template={}, variableCount={}", templatePath, allVariables.size());

            return templateEngine.process(templatePath, context);

        } catch (Exception e) {
            log.error("邮件模板渲染失败: type={}, error={}", emailType, e.getMessage(), e);
            
            // 返回降级模板
            return getFallbackTemplate(emailType, variables);
        }
    }

    /**
     * 获取模板内容（带缓存）
     * 主要用于非Thymeleaf的场景或模板预热
     *
     * @param templatePath 模板路径
     * @return 模板内容
     */
    public String getTemplateContent(String templatePath) {
        // 先从缓存获取
        String cachedContent = templateCache.get(templatePath);
        if (cachedContent != null) {
            log.debug("从缓存获取模板: {}", templatePath);
            return cachedContent;
        }

        // 从文件系统加载
        try {
            String content = loadTemplateFromFile(templatePath);
            
            // 缓存模板内容
            templateCache.put(templatePath, content);
            
            log.debug("从文件加载并缓存模板: {}", templatePath);
            return content;

        } catch (IOException e) {
            log.error("模板文件加载失败: {} - {}", templatePath, e.getMessage());
            throw new RuntimeException("模板加载失败: " + templatePath, e);
        }
    }

    /**
     * 验证模板是否有效
     *
     * @param emailType 邮件类型
     * @param sampleVariables 示例变量
     * @return 是否有效
     */
    public boolean validateTemplate(EmailType emailType, Map<String, Object> sampleVariables) {
        try {
            String result = renderTemplate(emailType, sampleVariables);
            
            // 检查是否有未替换的变量
            Matcher matcher = VARIABLE_PATTERN.matcher(result);
            if (matcher.find()) {
                log.warn("模板 {} 中存在未解析的变量: {}", emailType.getTemplatePath(), matcher.group(1));
                return false;
            }
            
            return StringUtils.hasText(result);

        } catch (Exception e) {
            log.warn("模板验证失败: {} - {}", emailType, e.getMessage());
            return false;
        }
    }

    /**
     * 预热模板缓存
     * 在应用启动时调用，提前加载常用模板
     *
     * @param emailTypes 要预热的邮件类型
     */
    public void warmupTemplateCache(EmailType... emailTypes) {
        log.info("开始预热模板缓存，共 {} 个模板", emailTypes.length);
        
        for (EmailType emailType : emailTypes) {
            try {
                String templatePath = emailType.getTemplatePath();
                getTemplateContent(templatePath);
                log.debug("预热模板缓存成功: {}", emailType.getDescription());
            } catch (Exception e) {
                log.warn("预热模板失败: {} - {}", emailType.getDescription(), e.getMessage());
            }
        }
        
        log.info("模板缓存预热完成，已缓存 {} 个模板", templateCache.size());
    }

    /**
     * 清空模板缓存
     */
    public void clearTemplateCache() {
        int size = templateCache.size();
        templateCache.clear();
        log.info("模板缓存已清空，清除了 {} 个缓存项", size);
    }

    /**
     * 添加通用模板变量
     * 这些变量在所有邮件模板中都可用
     */
    private void addCommonVariables(Map<String, Object> variables) {
        LocalDateTime now = LocalDateTime.now();

        // 时间相关
        variables.put("currentTime", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        variables.put("currentDate", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        variables.put("currentYear", now.getYear());

        // 应用信息
        variables.put("appName", "KiseSaki博客");
        variables.put("blogName", "KiseSaki博客");
        variables.put("blogUrl", "https://blog.kisesaki.com");
        variables.put("baseUrl", "https://blog.kisesaki.com");
        variables.put("frontendBaseUrl", "https://blog.kisesaki.com");
        
        // 联系信息
        variables.put("supportEmail", "support@kisesaki.com");
        variables.put("securityEmail", "security@kisesaki.com");
        
        // 社交媒体
        variables.put("githubUrl", "https://github.com/KiseSaki");
        variables.put("twitterUrl", "https://twitter.com/KiseSaki");
        variables.put("weiboUrl", "https://weibo.com/kisesaki");
        
        // 资源链接
        variables.put("logoUrl", "https://blog.kisesaki.com/static/logo.png");
        variables.put("faviconUrl", "https://blog.kisesaki.com/favicon.ico");
        
        // 版权信息
        variables.put("copyright", "© " + now.getYear() + " KiseSaki博客. All rights reserved.");
        
        // 常用链接
        variables.put("helpUrl", "https://blog.kisesaki.com/help");
        variables.put("privacyUrl", "https://blog.kisesaki.com/privacy");
        variables.put("termsUrl", "https://blog.kisesaki.com/terms");
        variables.put("unsubscribeUrl", "https://blog.kisesaki.com/user/settings/notifications");
    }

    /**
     * 从文件系统加载模板
     *
     * @param templatePath 模板路径
     * @return 模板内容
     * @throws IOException IO异常
     */
    private String loadTemplateFromFile(String templatePath) throws IOException {
        String basePath = emailProperties.getTemplates().getBasePath();
        String fullPath = "classpath:" + basePath + "/" + templatePath;
        
        Resource resource = resourceLoader.getResource(fullPath);

        if (!resource.exists()) {
            throw new IOException("模板文件不存在: " + fullPath);
        }

        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 获取降级模板
     * 当主模板渲染失败时使用，确保邮件一定能发出去
     */
    private String getFallbackTemplate(EmailType emailType, Map<String, Object> variables) {
        log.warn("使用降级模板: {}", emailType.getDescription());
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"zh-CN\">");
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.append("<title>").append(emailType.getDescription()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }");
        html.append(".container { background: #f9f9f9; border-radius: 8px; padding: 30px; }");
        html.append(".header { color: #6366f1; font-size: 24px; margin-bottom: 20px; }");
        html.append(".content { background: white; padding: 20px; border-radius: 4px; }");
        html.append(".footer { margin-top: 20px; font-size: 12px; color: #666; text-align: center; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class=\"container\">");
        html.append("<div class=\"header\">").append(emailType.getDescription()).append("</div>");
        html.append("<div class=\"content\">");
        html.append("<p>尊敬的用户，</p>");
        html.append("<p>您收到一封来自 <strong>KiseSaki博客</strong> 的邮件。</p>");
        
        // 添加变量内容
        if (variables != null && !variables.isEmpty()) {
            html.append("<div style=\"margin: 20px 0; padding: 15px; background-color: #f5f5f5; border-left: 4px solid #6366f1;\">");
            variables.forEach((key, value) -> {
                if (!key.startsWith("current") && !key.endsWith("Url") && !key.equals("copyright")) {
                    html.append("<p><strong>").append(key).append(":</strong> ").append(value).append("</p>");
                }
            });
            html.append("</div>");
        }
        
        html.append("<p>此致<br/><strong>KiseSaki博客团队</strong></p>");
        html.append("</div>");
        html.append("<div class=\"footer\">");
        html.append("<p>© ").append(java.time.Year.now().getValue()).append(" KiseSaki博客. All rights reserved.</p>");
        html.append("<p><a href=\"https://blog.kisesaki.com\" style=\"color: #6366f1;\">访问博客</a> | ");
        html.append("<a href=\"mailto:support@kisesaki.com\" style=\"color: #6366f1;\">联系我们</a></p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
}
