package com.kisesaki.blog.common.dto;

import org.springframework.data.domain.Page;

/**
 * 响应构建工具类 提供统一的响应构建方法
 * 
 * @author KiseSaki
 */
public class ResultUtils {
    private ResultUtils() {
    } // 私有构造函数，防止实例化

    /**
     * 构建成功响应（带数据）
     * 
     * @param data 响应数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    /**
     * 构建成功响应（不带数据）
     * 
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.success();
    }

    /**
     * 构建成功响应（自定义消息）
     * 
     * @param message 自定义消息
     * @param data    响应数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.success(message, data);
    }

    /**
     * 构建分页成功响应
     * 
     * @param page 分页数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<PageResponse<T>> success(Page<T> page) {
        return ApiResponse.success(PageResponse.of(page));
    }

    /**
     * 构建分页成功响应（自定义消息）
     * 
     * @param message 自定义消息
     * @param page    分页数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<PageResponse<T>> success(String message, Page<T> page) {
        return ApiResponse.success(message, PageResponse.of(page));
    }

    /**
     * 构建失败响应
     * 
     * @param code    错误码
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.error(code, message);
    }

    /**
     * 构建失败响应（默认500错误码）
     * 
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.error(message);
    }

    /**
     * 构建参数错误响应
     * 
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return ApiResponse.error(400, message);
    }

    /**
     * 构建未授权响应
     * 
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return ApiResponse.error(401, message != null ? message : "未授权访问");
    }

    /**
     * 构建禁止访问响应
     * 
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return ApiResponse.error(403, message != null ? message : "禁止访问");
    }

    /**
     * 构建资源未找到响应
     * 
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.error(404, message != null ? message : "资源未找到");
    }

    /**
     * 构建服务器内部错误响应
     * 
     * @param message 错误消息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> internalError(String message) {
        return ApiResponse.error(500, message != null ? message : "服务器内部错误");
    }
}
