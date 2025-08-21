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
    /** 更新文章 */
    POST_UPDATE("POST_UPDATE", "更新文章", "post", "update"),
    /** 删除文章 */
    POST_DELETE("POST_DELETE", "删除文章", "post", "delete"),
    /** 发布文章 */
    POST_PUBLISH("POST_PUBLISH", "发布文章", "post", "publish"),
    /** 查看所有文章 */
    POST_VIEW_ALL("POST_VIEW_ALL", "查看所有文章", "post", "view_all"),

    // ========== 评论管理权限 ==========
    /** 创建评论 */
    COMMENT_CREATE("COMMENT_CREATE", "创建评论", "comment", "create"),
    /** 删除评论 */
    COMMENT_DELETE("COMMENT_DELETE", "删除评论", "comment", "delete"),
    /** 评论审核 */
    COMMENT_MODERATE("COMMENT_MODERATE", "评论审核", "comment", "moderate"),

    // ========== 用户管理权限 ==========
    /** 用户管理 */
    USER_MANAGE("USER_MANAGE", "用户管理", "user", "manage"),
    /** 查看用户列表 */
    USER_VIEW_ALL("USER_VIEW_ALL", "查看用户列表", "user", "view_all"),
    /** 禁用用户 */
    USER_BAN("USER_BAN", "禁用用户", "user", "ban"),

    // ========== 分类管理权限 ==========
    /** 创建分类 */
    CATEGORY_CREATE("CATEGORY_CREATE", "创建分类", "category", "create"),
    /** 更新分类 */
    CATEGORY_UPDATE("CATEGORY_UPDATE", "更新分类", "category", "update"),
    /** 删除分类 */
    CATEGORY_DELETE("CATEGORY_DELETE", "删除分类", "category", "delete"),

    // ========== 标签管理权限 ==========
    /** 创建标签 */
    TAG_CREATE("TAG_CREATE", "创建标签", "tag", "create"),
    /** 更新标签 */
    TAG_UPDATE("TAG_UPDATE", "更新标签", "tag", "update"),
    /** 删除标签 */
    TAG_DELETE("TAG_DELETE", "删除标签", "tag", "delete"),

    // ========== 媒体管理权限 ==========
    /** 上传媒体文件 */
    MEDIA_UPLOAD("MEDIA_UPLOAD", "上传媒体文件", "media", "upload"),
    /** 删除媒体文件 */
    MEDIA_DELETE("MEDIA_DELETE", "删除媒体文件", "media", "delete"),
    /** 查看所有媒体文件 */
    MEDIA_VIEW_ALL("MEDIA_VIEW_ALL", "查看所有媒体文件", "media", "view_all"),

    // ========== 系统管理权限 ==========
    /** 系统统计查看 */
    SYSTEM_STATS_VIEW("SYSTEM_STATS_VIEW", "系统统计查看", "system", "stats_view"),
    /** 系统配置管理 */
    SYSTEM_CONFIG_MANAGE("SYSTEM_CONFIG_MANAGE", "系统配置管理", "system", "config_manage"),

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
