package com.kisesaki.blog.common.enums;

/**
 * 错误码枚举
 * 统一管理系统中的所有错误码
 */
public enum ErrorCode {

    // ========== 通用错误码 ==========
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统内部错误"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    // ========== 业务错误码 ==========
    BUSINESS_ERROR(4000, "业务处理失败"),

    // ========== 认证授权错误码 ==========
    UNAUTHORIZED(401, "未认证"),
    ACCESS_DENIED(403, "权限不足"),
    TOKEN_EXPIRED(4001, "令牌已过期"),
    TOKEN_INVALID(4002, "令牌无效"),
    LOGIN_FAILED(4003, "登录失败"),
    USER_NOT_FOUND(4004, "用户不存在"),
    PASSWORD_ERROR(4005, "密码错误"),
    ACCOUNT_DISABLED(4006, "账户已禁用"),
    ACCOUNT_LOCKED(4007, "账户已锁定"),

    // ========== 用户相关错误码 ==========
    USER_ALREADY_EXISTS(4010, "用户已存在"),
    EMAIL_ALREADY_EXISTS(4011, "邮箱已被注册"),
    USERNAME_ALREADY_EXISTS(4012, "用户名已存在"),
    INVALID_EMAIL_FORMAT(4013, "邮箱格式无效"),
    WEAK_PASSWORD(4014, "密码强度不足"),

    // ========== 文章相关错误码 ==========
    POST_NOT_FOUND(4020, "文章不存在"),
    POST_SLUG_ALREADY_EXISTS(4021, "文章URL已存在"),
    POST_TITLE_EMPTY(4022, "文章标题不能为空"),
    POST_CONTENT_EMPTY(4023, "文章内容不能为空"),
    POST_CATEGORY_NOT_FOUND(4024, "文章分类不存在"),
    POST_ACCESS_DENIED(4025, "无权访问该文章"),

    // ========== 评论相关错误码 ==========
    COMMENT_NOT_FOUND(4030, "评论不存在"),
    COMMENT_CONTENT_EMPTY(4031, "评论内容不能为空"),
    COMMENT_ACCESS_DENIED(4032, "无权操作该评论"),
    PARENT_COMMENT_NOT_FOUND(4033, "父评论不存在"),

    // ========== 文件上传错误码 ==========
    FILE_UPLOAD_FAILED(4040, "文件上传失败"),
    FILE_TOO_LARGE(4041, "文件大小超出限制"),
    FILE_TYPE_NOT_ALLOWED(4042, "文件类型不允许"),
    FILE_NOT_FOUND(4043, "文件不存在"),

    // ========== 数据验证错误码 ==========
    VALIDATION_FAILED(4050, "数据验证失败"),
    REQUIRED_FIELD_MISSING(4051, "必填字段缺失"),
    INVALID_DATA_FORMAT(4052, "数据格式无效"),
    DATA_LENGTH_EXCEEDED(4053, "数据长度超出限制"),

    // ========== 外部服务错误码 ==========
    EXTERNAL_SERVICE_ERROR(5001, "外部服务调用失败"),
    DATABASE_ERROR(5002, "数据库操作失败"),
    REDIS_ERROR(5003, "Redis操作失败"),
    EMAIL_SEND_FAILED(5004, "邮件发送失败"),
    SMS_SEND_FAILED(5005, "短信发送失败");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据错误码获取枚举
     */
    public static ErrorCode getByCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return SYSTEM_ERROR;
    }
}