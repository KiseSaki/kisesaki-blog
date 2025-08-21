package com.kisesaki.blog.common.exception;

import com.kisesaki.blog.common.enums.ErrorCode;

/**
 * 业务异常基类
 * <p>
 * 用于处理业务逻辑中的异常情况，提供统一的异常处理机制。
 * 该异常类包含错误码、自定义消息和参数，支持国际化和格式化。
 * </p>
 * 
 * <p>
 * 使用示例：
 * </p>
 * 
 * <pre>{@code
 * // 抛出基础业务异常
 * throw BusinessException.of(ErrorCode.USER_NOT_FOUND);
 * 
 * // 抛出带自定义消息的异常
 * throw BusinessException.of(ErrorCode.PARAM_ERROR, "用户名不能为空");
 * 
 * // 使用快速创建方法
 * throw BusinessException.userNotFound();
 * throw BusinessException.paramError("密码长度不能少于6位");
 * }</pre>
 * 
 * @author KiseSaki
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode; // 错误码
    private final Object[] args; // 参数

    /**
     * 基础构造函数
     * <p>
     * 创建一个使用默认错误消息的业务异常
     * </p>
     * 
     * @param errorCode 错误码枚举，不能为null
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = null;
    }

    /**
     * 自定义消息构造函数
     * <p>
     * 创建一个使用自定义错误消息的业务异常
     * </p>
     * 
     * @param errorCode     错误码枚举，不能为null
     * @param customMessage 自定义错误消息，可以为null
     */
    public BusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.args = null;
    }

    /**
     * 带参数的构造函数
     * <p>
     * 创建一个包含格式化参数的业务异常，用于消息模板替换
     * </p>
     * 
     * @param errorCode 错误码枚举，不能为null
     * @param args      格式化参数数组，用于替换消息模板中的占位符
     */
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * 自定义消息和参数构造函数
     * <p>
     * 创建一个同时包含自定义消息和格式化参数的业务异常
     * </p>
     * 
     * @param errorCode     错误码枚举，不能为null
     * @param customMessage 自定义错误消息，可以为null
     * @param args          格式化参数数组，用于替换消息模板中的占位符
     */
    public BusinessException(ErrorCode errorCode, String customMessage, Object... args) {
        super(customMessage);
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * 包含异常原因的构造函数
     * <p>
     * 创建一个包含底层异常原因的业务异常，用于异常链传递
     * </p>
     * 
     * @param errorCode 错误码枚举，不能为null
     * @param cause     引起此异常的底层异常，可以为null
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    /**
     * 自定义消息和异常原因的构造函数
     * <p>
     * 创建一个同时包含自定义消息和底层异常原因的业务异常
     * </p>
     * 
     * @param errorCode     错误码枚举，不能为null
     * @param customMessage 自定义错误消息，可以为null
     * @param cause         引起此异常的底层异常，可以为null
     */
    public BusinessException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    /**
     * 获取错误码
     * 
     * @return 异常对应的错误码枚举
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 获取格式化参数
     * 
     * @return 用于消息格式化的参数数组，如果没有参数则返回null
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * 快速创建业务异常的静态工厂方法
     * 
     * @param errorCode 错误码枚举，不能为null
     * @return 新创建的BusinessException实例
     */
    public static BusinessException of(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }

    /**
     * 快速创建带自定义消息的业务异常
     * 
     * @param errorCode 错误码枚举，不能为null
     * @param message   自定义错误消息
     * @return 新创建的BusinessException实例
     */
    public static BusinessException of(ErrorCode errorCode, String message) {
        return new BusinessException(errorCode, message);
    }

    /**
     * 快速创建带参数的业务异常
     * 
     * @param errorCode 错误码枚举，不能为null
     * @param args      格式化参数数组
     * @return 新创建的BusinessException实例
     */
    public static BusinessException of(ErrorCode errorCode, Object... args) {
        return new BusinessException(errorCode, args);
    }

    // ========== 快速创建常用异常的静态方法 ==========

    /**
     * 创建资源不存在异常
     * 
     * @param resource 资源名称，如"用户"、"文章"等
     * @return 资源不存在的BusinessException实例
     */
    public static BusinessException notFound(String resource) {
        return new BusinessException(ErrorCode.NOT_FOUND, resource + "不存在");
    }

    /**
     * 创建访问被拒绝异常
     * 
     * @return 访问被拒绝的BusinessException实例
     */
    public static BusinessException accessDenied() {
        return new BusinessException(ErrorCode.ACCESS_DENIED);
    }

    /**
     * 创建访问特定资源被拒绝异常
     * 
     * @param resource 被拒绝访问的资源名称
     * @return 访问被拒绝的BusinessException实例
     */
    public static BusinessException accessDenied(String resource) {
        return new BusinessException(ErrorCode.ACCESS_DENIED, "无权访问" + resource);
    }

    /**
     * 创建参数错误异常
     * 
     * @param message 具体的参数错误信息
     * @return 参数错误的BusinessException实例
     */
    public static BusinessException paramError(String message) {
        return new BusinessException(ErrorCode.PARAM_ERROR, message);
    }

    /**
     * 创建数据校验失败异常
     * 
     * @param message 具体的校验失败信息
     * @return 校验失败的BusinessException实例
     */
    public static BusinessException validationFailed(String message) {
        return new BusinessException(ErrorCode.VALIDATION_FAILED, message);
    }

    /**
     * 创建用户不存在异常
     * 
     * @return 用户不存在的BusinessException实例
     */
    public static BusinessException userNotFound() {
        return new BusinessException(ErrorCode.USER_NOT_FOUND);
    }

    /**
     * 创建文章不存在异常
     * 
     * @return 文章不存在的BusinessException实例
     */
    public static BusinessException postNotFound() {
        return new BusinessException(ErrorCode.POST_NOT_FOUND);
    }

    /**
     * 创建评论不存在异常
     * 
     * @return 评论不存在的BusinessException实例
     */
    public static BusinessException commentNotFound() {
        return new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
    }
}