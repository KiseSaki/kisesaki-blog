package com.kisesaki.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一错误码枚举
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Getter @AllArgsConstructor
public enum ErrorCode {

    // 通用错误码 (1000-1999)
    SUCCESS(200, "操作成功"), SYSTEM_ERROR(1000, "系统内部错误"), PARAM_ERROR(1001, "参数错误"),
    NOT_FOUND(1002, "资源不存在"), METHOD_NOT_ALLOWED(1003, "请求方法不允许"), REQUEST_TIMEOUT(1004, "请求超时"),
    TOO_MANY_REQUESTS(1005, "请求过于频繁"),

    // 认证相关错误码 (2000-2999)
    UNAUTHORIZED(2000, "未认证"), ACCESS_DENIED(2001, "权限不足"), TOKEN_INVALID(2002, "Token无效"),
    TOKEN_EXPIRED(2003, "Token已过期"), LOGIN_FAILED(2004, "登录失败"), USER_NOT_FOUND(2005, "用户不存在"),
    USER_DISABLED(2006, "用户已被禁用"), PASSWORD_ERROR(2007, "密码错误"), USER_ALREADY_EXISTS(2008, "用户已存在"),
    EMAIL_NOT_VERIFIED(2009, "邮箱未验证"), VERIFICATION_CODE_ERROR(2010, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(2011, "验证码已过期"),

    // 业务相关错误码 (3000-3999)
    POST_NOT_FOUND(3000, "文章不存在"), POST_ACCESS_DENIED(3001, "无权访问该文章"),
    CATEGORY_NOT_FOUND(3002, "分类不存在"), TAG_NOT_FOUND(3003, "标签不存在"),
    COMMENT_NOT_FOUND(3004, "评论不存在"), COMMENT_ACCESS_DENIED(3005, "无权操作该评论"),
    FILE_UPLOAD_FAILED(3006, "文件上传失败"), FILE_TYPE_NOT_SUPPORTED(3007, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(3008, "文件大小超出限制"), CONTENT_TOO_LONG(3009, "内容长度超出限制"),
    DUPLICATE_OPERATION(3010, "重复操作"),

    // 第三方服务错误码 (4000-4999)
    EMAIL_SEND_FAILED(4000, "邮件发送失败"), OAUTH_ERROR(4001, "第三方登录失败"), CACHE_ERROR(4002, "缓存操作失败"),
    EXTERNAL_API_ERROR(4003, "外部API调用失败");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;
}
