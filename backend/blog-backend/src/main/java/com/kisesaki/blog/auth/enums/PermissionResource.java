package com.kisesaki.blog.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限资源类型枚举
 * 
 * @author KiseSaki
 */
@Getter
@AllArgsConstructor
public enum PermissionResource {
    POST("post", "文章"),
    USER("user", "用户"),
    ROLE("role", "角色"),
    PERMISSION("permission", "权限"),
    CATEGORY("category", "分类"),
    TAG("tag", "标签"),
    COMMENT("comment", "评论"),
    FILE("file", "文件"),
    ANALYTICS("analytics", "分析"),
    STATS("stats", "统计"),
    NOTIFICATION("notification", "通知"),
    SUBSCRIPTION("subscription", "订阅"),
    NEWSLETTER("newsletter", "新闻通讯"),
    SYSTEM("system", "系统"),
    SEO("seo", "SEO"),
    REDIRECT("redirect", "重定向"),
    AUDIT("audit", "审计"),
    REPORT("report", "举报"),
    SEARCH("search", "搜索"),
    TOKEN("token", "令牌");

    private final String code;
    private final String description;
}