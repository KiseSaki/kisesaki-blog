package com.kisesaki.blog.common.enums;

/**
 * 权限枚举，集中管理系统中的所有权限点
 * 
 * @author KiseSaki
 */
public enum PermissionEnum {

    // ========== 文章管理权限 ==========
    /** 创建文章 */
    POST_CREATE("POST_CREATE", "创建文章", "post", "create"),
    /** 编辑自己的文章 */
    POST_EDIT_OWN("POST_EDIT_OWN", "编辑自己的文章", "post", "edit_own"),
    /** 编辑所有文章 */
    POST_EDIT_ALL("POST_EDIT_ALL", "编辑所有文章", "post", "edit_all"),
    /** 删除自己的文章 */
    POST_DELETE_OWN("POST_DELETE_OWN", "删除自己的文章", "post", "delete_own"),
    /** 删除所有文章 */
    POST_DELETE_ALL("POST_DELETE_ALL", "删除所有文章", "post", "delete_all"),
    /** 发布文章 */
    POST_PUBLISH("POST_PUBLISH", "发布文章", "post", "publish"),
    /** 管理文章 */
    POST_MANAGE("POST_MANAGE", "管理文章", "post", "manage"),
    /** 查看文章版本 */
    POST_REVISION_VIEW("POST_REVISION_VIEW", "查看文章版本", "post", "revision_view"),
    /** 恢复文章版本 */
    POST_REVISION_RESTORE("POST_REVISION_RESTORE", "恢复文章版本", "post", "revision_restore"),

    // ========== 评论管理权限 ==========
    /** 创建评论 */
    COMMENT_CREATE("COMMENT_CREATE", "创建评论", "comment", "create"),
    /** 编辑自己的评论 */
    COMMENT_EDIT_OWN("COMMENT_EDIT_OWN", "编辑自己的评论", "comment", "edit_own"),
    /** 删除自己的评论 */
    COMMENT_DELETE_OWN("COMMENT_DELETE_OWN", "删除自己的评论", "comment", "delete_own"),
    /** 删除所有评论 */
    COMMENT_DELETE_ALL("COMMENT_DELETE_ALL", "删除所有评论", "comment", "delete_all"),
    /** 评论审核 */
    COMMENT_MODERATE("COMMENT_MODERATE", "评论审核", "comment", "moderate"),

    // ========== 用户管理权限 ==========
    /** 用户管理 */
    USER_MANAGE("USER_MANAGE", "用户管理", "user", "manage"),
    /** 查看用户列表 */
    USER_VIEW_ALL("USER_VIEW_ALL", "查看用户列表", "user", "view_all"),
    /** 禁用用户 */
    USER_BAN("USER_BAN", "禁用用户", "user", "ban"),
    /** 解禁用户 */
    USER_UNBAN("USER_UNBAN", "解禁用户", "user", "unban"),
    /** 编辑用户信息 */
    USER_EDIT("USER_EDIT", "编辑用户信息", "user", "edit"),
    /** 删除用户 */
    USER_DELETE("USER_DELETE", "删除用户", "user", "delete"),

    // ========== 分类管理权限 ==========
    /** 查看分类 */
    CATEGORY_VIEW("CATEGORY_VIEW", "查看分类", "category", "view"),
    /** 创建分类 */
    CATEGORY_CREATE("CATEGORY_CREATE", "创建分类", "category", "create"),
    /** 编辑分类 */
    CATEGORY_EDIT("CATEGORY_EDIT", "编辑分类", "category", "edit"),
    /** 删除分类 */
    CATEGORY_DELETE("CATEGORY_DELETE", "删除分类", "category", "delete"),

    // ========== 标签管理权限 ==========
    /** 创建标签 */
    TAG_CREATE("TAG_CREATE", "创建标签", "tag", "create"),
    /** 编辑标签 */
    TAG_EDIT("TAG_EDIT", "编辑标签", "tag", "edit"),
    /** 删除标签 */
    TAG_DELETE("TAG_DELETE", "删除标签", "tag", "delete"),
    /** 管理标签 */
    TAG_MANAGE("TAG_MANAGE", "管理标签", "tag", "manage"),

    // ========== 文件管理权限 ==========
    /** 上传文件 */
    FILE_UPLOAD("FILE_UPLOAD", "上传文件", "file", "upload"),
    /** 删除自己的文件 */
    FILE_DELETE_OWN("FILE_DELETE_OWN", "删除自己的文件", "file", "delete_own"),
    /** 删除所有文件 */
    FILE_DELETE_ALL("FILE_DELETE_ALL", "删除所有文件", "file", "delete_all"),
    /** 查看所有文件 */
    FILE_VIEW_ALL("FILE_VIEW_ALL", "查看所有文件", "file", "view_all"),

    // ========== 通知管理权限 ==========
    /** 发送通知 */
    NOTIFICATION_SEND("NOTIFICATION_SEND", "发送通知", "notification", "send"),
    /** 管理通知 */
    NOTIFICATION_MANAGE("NOTIFICATION_MANAGE", "管理通知", "notification", "manage"),

    // ========== 订阅管理权限 ==========
    /** 管理订阅 */
    SUBSCRIPTION_MANAGE("SUBSCRIPTION_MANAGE", "管理订阅", "subscription", "manage"),
    /** 发送新闻通讯 */
    NEWSLETTER_SEND("NEWSLETTER_SEND", "发送新闻通讯", "newsletter", "send"),

    // ========== 系统管理权限 ==========
    /** 查看系统统计 */
    SYSTEM_STATS_VIEW("SYSTEM_STATS_VIEW", "查看系统统计", "system", "stats_view"),
    /** 管理系统配置 */
    SYSTEM_CONFIG_MANAGE("SYSTEM_CONFIG_MANAGE", "管理系统配置", "system", "config_manage"),
    /** 系统监控 */
    SYSTEM_MONITOR("SYSTEM_MONITOR", "系统监控", "system", "monitor"),
    /** 数据备份 */
    SYSTEM_BACKUP("SYSTEM_BACKUP", "数据备份", "system", "backup"),
    /** 数据恢复 */
    SYSTEM_RESTORE("SYSTEM_RESTORE", "数据恢复", "system", "restore"),

    // ========== SEO管理权限 ==========
    /** 管理SEO设置 */
    SEO_MANAGE("SEO_MANAGE", "管理SEO设置", "seo", "manage"),

    // ========== 重定向管理权限 ==========
    /** 管理重定向 */
    REDIRECT_MANAGE("REDIRECT_MANAGE", "管理重定向", "redirect", "manage"),

    // ========== 举报管理权限 ==========
    /** 处理举报 */
    REPORT_HANDLE("REPORT_HANDLE", "处理举报", "report", "handle"),
    /** 查看举报 */
    REPORT_VIEW("REPORT_VIEW", "查看举报", "report", "view"),

    // ========== 审计权限 ==========
    /** 查看审计日志 */
    AUDIT_VIEW("AUDIT_VIEW", "查看审计日志", "audit", "view"),
    /** 导出数据 */
    DATA_EXPORT("DATA_EXPORT", "导出数据", "data", "export"),

    // ========== 分析权限 ==========
    /** 查看分析数据 */
    ANALYTICS_VIEW("ANALYTICS_VIEW", "查看分析数据", "analytics", "view"),
    /** 管理分析配置 */
    ANALYTICS_MANAGE("ANALYTICS_MANAGE", "管理分析配置", "analytics", "manage"),

    // ========== 仪表板权限 ==========
    /** 管理员仪表板访问 */
    DASHBOARD_ADMIN_ACCESS("DASHBOARD_ADMIN_ACCESS", "管理员仪表板访问", "dashboard", "admin_access");

    private final String name;
    private final String description;
    private final String resource;
    private final String action;

    PermissionEnum(String name, String description, String resource, String action) {
        this.name = name;
        this.description = description;
        this.resource = resource;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }
}
