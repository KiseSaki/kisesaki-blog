package com.kisesaki.blog.user.service;

/**
 * 活动日志操作类型常量
 * 
 * @author KiseSaki
 */
public final class UserActivityType {

    // 用户认证相关
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String PASSWORD_CHANGE = "password_change";
    public static final String EMAIL_VERIFY = "email_verify";

    // 用户资料相关
    public static final String PROFILE_UPDATE = "profile_update";
    public static final String AVATAR_UPDATE = "avatar_update";
    public static final String SETTINGS_UPDATE = "settings_update";

    // 内容操作相关
    public static final String POST_CREATE = "post_create";
    public static final String POST_UPDATE = "post_update";
    public static final String POST_DELETE = "post_delete";
    public static final String POST_PUBLISH = "post_publish";
    public static final String COMMENT_CREATE = "comment_create";
    public static final String COMMENT_UPDATE = "comment_update";
    public static final String COMMENT_DELETE = "comment_delete";

    // 社交互动相关
    public static final String FOLLOW_USER = "follow_user";
    public static final String UNFOLLOW_USER = "unfollow_user";
    public static final String LIKE_POST = "like_post";
    public static final String UNLIKE_POST = "unlike_post";
    public static final String FAVORITE_POST = "favorite_post";
    public static final String UNFAVORITE_POST = "unfavorite_post";

    // 管理员操作相关
    public static final String ADMIN_USER_UPDATE = "admin_user_update";
    public static final String ADMIN_USER_STATUS_CHANGE = "admin_user_status_change";
    public static final String ADMIN_POST_MODERATE = "admin_post_moderate";
    public static final String ADMIN_COMMENT_MODERATE = "admin_comment_moderate";

    // 系统操作相关
    public static final String SYSTEM_NOTIFICATION = "system_notification";
    public static final String EMAIL_SENT = "email_sent";

    private UserActivityType() {
        // 防止实例化
    }
}