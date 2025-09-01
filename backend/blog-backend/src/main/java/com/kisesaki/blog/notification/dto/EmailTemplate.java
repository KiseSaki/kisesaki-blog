package com.kisesaki.blog.notification.dto;

import java.util.Map;

import com.kisesaki.blog.notification.enums.EmailType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件模板DTO
 * 
 * @author KiseSaki
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {

    // 模板ID
    private Long id;

    // 模板名称
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 100, message = "模板名称长度不能超过100字符")
    private String name;

    // 模板代码
    @NotBlank(message = "模板代码不能为空")
    @Size(max = 50, message = "模板代码长度不能超过50字符")
    private String code;

    // 邮件类型
    @NotNull(message = "邮件类型不能为空")
    private EmailType emailType;

    // 模板路径
    @NotBlank(message = "模板路径不能为空")
    @Size(max = 200, message = "模板路径长度不能超过200字符")
    private String templatePath;

    // 模板内容
    private String content;

    // 模板主题
    @Size(max = 200, message = "模板主题长度不能超过200字符")
    private String subject;

    // 模板描述
    @Size(max = 500, message = "模板描述长度不能超过500字符")
    private String description;

    // 模板变量定义
    private Map<String, Object> variables;

    // 模板语言
    @Builder.Default
    private String locale = "zh_CN";

    // 模板版本
    @Builder.Default
    private String version = "1.0";

    // 是否启用
    @Builder.Default
    private Boolean enabled = true;

    // 是否为默认模板
    @Builder.Default
    private Boolean isDefault = false;

    // 创建者ID
    private Long createdBy;

    // 更新者ID
    private Long updatedBy;

    // 创建时间
    private java.time.LocalDateTime createTime;

    // 更新时间
    private java.time.LocalDateTime updateTime;

    // 扩展字段
    private Map<String, Object> extra;

    /**
     * 检查模板是否有效
     * 
     * @return 模板是否有效
     */
    public boolean isValid() {
        return enabled != null && enabled &&
                templatePath != null && !templatePath.trim().isEmpty();
    }

    /**
     * 检查是否有模板内容
     * 
     * @return 是否有模板内容
     */
    public boolean hasContent() {
        return content != null && !content.trim().isEmpty();
    }

    /**
     * 检查是否有模板变量
     * 
     * @return 是否有模板变量
     */
    public boolean hasVariables() {
        return variables != null && !variables.isEmpty();
    }

    /**
     * 获取完整模板路径
     * 
     * @return 完整模板路径
     */
    public String getFullTemplatePath() {
        if (templatePath == null) {
            return null;
        }
        return templatePath.startsWith("/") ? templatePath : "/" + templatePath;
    }
}