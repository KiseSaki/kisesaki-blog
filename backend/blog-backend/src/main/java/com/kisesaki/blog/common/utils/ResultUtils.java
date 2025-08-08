package com.kisesaki.blog.common.utils;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.enums.ErrorCode;

/**
 * 响应结果工具类
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
public class ResultUtils {

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.success();
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.success(message, data);
    }

    /**
     * 分页成功响应
     */
    public static <T> ApiResponse<PageResponse<T>> success(PageResponse<T> pageData) {
        return ApiResponse.success(pageData);
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return ApiResponse.error(errorCode);
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.error(code, message);
    }

    /**
     * 参数错误响应
     */
    public static <T> ApiResponse<T> paramError(String message) {
        return ApiResponse.error(ErrorCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 未认证响应
     */
    public static <T> ApiResponse<T> unauthorized() {
        return ApiResponse.error(ErrorCode.UNAUTHORIZED);
    }

    /**
     * 权限不足响应
     */
    public static <T> ApiResponse<T> accessDenied() {
        return ApiResponse.error(ErrorCode.ACCESS_DENIED);
    }

    /**
     * 资源不存在响应
     */
    public static <T> ApiResponse<T> notFound() {
        return ApiResponse.error(ErrorCode.NOT_FOUND);
    }

    /**
     * 系统错误响应
     */
    public static <T> ApiResponse<T> systemError() {
        return ApiResponse.error(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 系统错误响应（自定义消息）
     */
    public static <T> ApiResponse<T> systemError(String message) {
        return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), message);
    }
}
