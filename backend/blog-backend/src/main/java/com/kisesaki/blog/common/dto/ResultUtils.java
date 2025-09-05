package com.kisesaki.blog.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import com.kisesaki.blog.common.enums.ErrorCode;

/**
 * API响应构建工具类
 * 
 * <p>
 * 提供统一的API响应构建方法，简化Controller层的响应创建过程。
 * 支持成功响应、错误响应、分页响应等多种类型的响应构建。
 * 
 * <p>
 * 所有方法都是静态方法，无需实例化即可使用。
 * 支持链式调用和流式API设计。
 * 
 * @author KiseSaki
 * @since 1.0.0
 */
public final class ResultUtils {

    /**
     * 私有构造函数，防止实例化工具类
     */
    private ResultUtils() {
        throw new UnsupportedOperationException("ResultUtils is a utility class and cannot be instantiated");
    }

    // ===== 成功响应构建方法 =====

    /**
     * 构建成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.success();
    }

    /**
     * 构建成功响应（带数据）
     * 
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(@Nullable T data) {
        return ApiResponse.success(data);
    }

    /**
     * 构建成功响应（自定义消息）
     * 
     * @param message 成功消息
     * @param <T>     数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.success(message);
    }

    /**
     * 构建成功响应（自定义消息和数据）
     * 
     * @param message 成功消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message, @Nullable T data) {
        return ApiResponse.success(message, data);
    }

    // ===== 分页响应构建方法 =====

    /**
     * 构建分页成功响应
     * 
     * @param page Spring Data Page对象
     * @param <T>  数据类型
     * @return 分页响应对象
     */
    public static <T> ApiResponse<PageResponse<T>> success(Page<T> page) {
        return ApiResponse.success(PageResponse.of(page));
    }

    /**
     * 构建分页成功响应（自定义消息）
     * 
     * @param message 成功消息
     * @param page    Spring Data Page对象
     * @param <T>     数据类型
     * @return 分页响应对象
     */
    public static <T> ApiResponse<PageResponse<T>> success(String message, Page<T> page) {
        return ApiResponse.success(message, PageResponse.of(page));
    }

    /**
     * 构建空分页响应
     * 
     * @param pageSize 每页大小
     * @param <T>      数据类型
     * @return 空分页响应对象
     */
    public static <T> ApiResponse<PageResponse<T>> emptyPage(int pageSize) {
        return ApiResponse.success(PageResponse.empty(pageSize));
    }

    /**
     * 构建单页数据响应
     * 
     * @param data 数据列表
     * @param <T>  数据类型
     * @return 单页响应对象
     */
    public static <T> ApiResponse<PageResponse<T>> singlePage(List<T> data) {
        return ApiResponse.success(PageResponse.single(data));
    }

    // ===== 错误响应构建方法 =====

    /**
     * 构建错误响应（默认500错误）
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.error(message);
    }

    /**
     * 构建错误响应（自定义状态码）
     * 
     * @param code    错误状态码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.error(code, message);
    }

    /**
     * 构建错误响应（带错误数据）
     * 
     * @param code    错误状态码
     * @param message 错误消息
     * @param data    错误数据（如校验失败的字段信息）
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message, @Nullable T data) {
        return ApiResponse.error(code, message, data);
    }

    /**
     * 构建错误响应（使用HttpStatus枚举）
     * 
     * @param status  HTTP状态
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.error(status, message);
    }

    /**
     * 构建错误响应（使用HttpStatus枚举，带数据）
     * 
     * @param status  HTTP状态
     * @param message 错误消息
     * @param data    错误数据
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message, @Nullable T data) {
        return ApiResponse.error(status, message, data);
    }

    /**
     * 构建错误响应（使用ErrorCode枚举）
     * 
     * @param errorCode 错误码枚举
     * @param <T>       数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.error(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 构建错误响应（使用ErrorCode枚举，自定义消息）
     * 
     * @param errorCode 错误码枚举
     * @param message   自定义错误消息
     * @param <T>       数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return ApiResponse.error(errorCode.getCode(), message);
    }

    /**
     * 构建错误响应（使用ErrorCode枚举，自定义消息和数据）
     * 
     * @param errorCode 错误码枚举
     * @param message   自定义错误消息
     * @param data      错误数据
     * @param <T>       数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message, @Nullable T data) {
        return ApiResponse.error(errorCode.getCode(), message, data);
    }

    // ===== 常用HTTP状态响应快捷方法 =====

    /**
     * 构建400 Bad Request响应
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 400错误响应
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return ApiResponse.badRequest(message);
    }

    /**
     * 构建400 Bad Request响应（带数据）
     * 
     * @param message 错误消息
     * @param data    错误数据
     * @param <T>     数据类型
     * @return 400错误响应
     */
    public static <T> ApiResponse<T> badRequest(String message, @Nullable T data) {
        return ApiResponse.error(HttpStatus.BAD_REQUEST, message, data);
    }

    /**
     * 构建401 Unauthorized响应
     * 
     * @param <T> 数据类型
     * @return 401错误响应
     */
    public static <T> ApiResponse<T> unauthorized() {
        return ApiResponse.unauthorized(null);
    }

    /**
     * 构建401 Unauthorized响应（自定义消息）
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 401错误响应
     */
    public static <T> ApiResponse<T> unauthorized(@Nullable String message) {
        return ApiResponse.unauthorized(message);
    }

    /**
     * 构建403 Forbidden响应
     * 
     * @param <T> 数据类型
     * @return 403错误响应
     */
    public static <T> ApiResponse<T> forbidden() {
        return ApiResponse.forbidden(null);
    }

    /**
     * 构建403 Forbidden响应（自定义消息）
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 403错误响应
     */
    public static <T> ApiResponse<T> forbidden(@Nullable String message) {
        return ApiResponse.forbidden(message);
    }

    /**
     * 构建404 Not Found响应
     * 
     * @param <T> 数据类型
     * @return 404错误响应
     */
    public static <T> ApiResponse<T> notFound() {
        return ApiResponse.notFound(null);
    }

    /**
     * 构建404 Not Found响应（自定义消息）
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 404错误响应
     */
    public static <T> ApiResponse<T> notFound(@Nullable String message) {
        return ApiResponse.notFound(message);
    }

    /**
     * 构建500 Internal Server Error响应
     * 
     * @param <T> 数据类型
     * @return 500错误响应
     */
    public static <T> ApiResponse<T> internalServerError() {
        return ApiResponse.internalServerError(null);
    }

    /**
     * 构建500 Internal Server Error响应（自定义消息）
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 500错误响应
     */
    public static <T> ApiResponse<T> internalServerError(@Nullable String message) {
        return ApiResponse.internalServerError(message);
    }

    // ===== 条件响应构建方法 =====

    /**
     * 根据条件构建响应
     * 
     * @param condition    条件
     * @param successData  成功时返回的数据
     * @param errorCode    失败时的错误码
     * @param errorMessage 失败时的错误消息
     * @param <T>          数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> condition(boolean condition, @Nullable T successData,
            ErrorCode errorCode, String errorMessage) {
        return condition ? success(successData) : error(errorCode, errorMessage);
    }

    /**
     * 根据数据是否存在构建响应
     * 
     * @param data 数据对象
     * @param <T>  数据类型
     * @return 如果数据存在返回成功响应，否则返回404响应
     */
    public static <T> ApiResponse<T> ofNullable(@Nullable T data) {
        return data != null ? success(data) : notFound("数据不存在");
    }

    /**
     * 根据数据是否存在构建响应（自定义消息）
     * 
     * @param data        数据对象
     * @param notFoundMsg 数据不存在时的消息
     * @param <T>         数据类型
     * @return 如果数据存在返回成功响应，否则返回404响应
     */
    public static <T> ApiResponse<T> ofNullable(@Nullable T data, String notFoundMsg) {
        return data != null ? success(data) : notFound(notFoundMsg);
    }

    /**
     * 根据操作结果构建响应
     * 
     * @param result     操作结果（true表示成功）
     * @param successMsg 成功消息
     * @param errorCode  失败错误码
     * @param errorMsg   失败消息
     * @param <T>        数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> ofResult(boolean result, String successMsg,
            ErrorCode errorCode, String errorMsg) {
        return result ? success(successMsg) : error(errorCode, errorMsg);
    }
}
