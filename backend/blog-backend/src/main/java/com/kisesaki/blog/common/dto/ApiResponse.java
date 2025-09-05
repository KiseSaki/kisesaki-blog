package com.kisesaki.blog.common.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应格式 DTO
 * 
 * <p>
 * 提供标准化的HTTP响应格式，包含状态码、消息、数据和时间戳。
 * 支持成功和失败两种响应类型，便于前端统一处理。
 * 
 * @param <T> 响应数据类型
 * @author KiseSaki
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "code", "message", "data", "timestamp" })
@Schema(description = "统一API响应格式")
public class ApiResponse<T> {

    @Schema(description = "HTTP状态码", example = "200")
    private int code;

    @Schema(description = "响应消息", example = "请求成功")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "响应时间戳（Unix时间戳，秒）", example = "1625251200")
    private long timestamp;

    /**
     * 私有构造函数，用于创建响应对象
     * 
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     */
    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().getEpochSecond();
    }

    // ===== 成功响应静态工厂方法 =====

    /**
     * 创建成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(HttpStatus.OK.value(), "请求成功", null);
    }

    /**
     * 创建成功响应（带数据）
     * 
     * @param data 响应数据
     * @param <T>  数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "请求成功", data);
    }

    /**
     * 创建成功响应（自定义消息和数据）
     * 
     * @param message 自定义消息
     * @param data    响应数据
     * @param <T>     数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, data);
    }

    /**
     * 创建成功响应（仅自定义消息）
     * 
     * @param message 自定义消息
     * @param <T>     数据类型
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(HttpStatus.OK.value(), message, null);
    }

    // ===== 错误响应静态工厂方法 =====

    /**
     * 创建错误响应（默认500错误）
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    /**
     * 创建错误响应（自定义状态码）
     * 
     * @param code    错误状态码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 创建错误响应（带数据）
     * 
     * @param code    错误状态码
     * @param message 错误消息
     * @param data    错误数据（如校验失败的字段信息）
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    /**
     * 创建错误响应（使用HttpStatus枚举）
     * 
     * @param status  HTTP状态
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), message, null);
    }

    /**
     * 创建错误响应（使用HttpStatus枚举，带数据）
     * 
     * @param status  HTTP状态
     * @param message 错误消息
     * @param data    错误数据
     * @param <T>     数据类型
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status.value(), message, data);
    }

    // ===== 常用HTTP状态响应快捷方法 =====

    /**
     * 创建400 Bad Request响应
     * 
     * @param message 错误消息
     * @param <T>     数据类型
     * @return 400错误响应
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(HttpStatus.BAD_REQUEST, message);
    }

    /**
     * 创建401 Unauthorized响应
     * 
     * @param message 错误消息（可选）
     * @param <T>     数据类型
     * @return 401错误响应
     */
    public static <T> ApiResponse<T> unauthorized(String message) {
        return error(HttpStatus.UNAUTHORIZED, message != null ? message : "未授权访问");
    }

    /**
     * 创建403 Forbidden响应
     * 
     * @param message 错误消息（可选）
     * @param <T>     数据类型
     * @return 403错误响应
     */
    public static <T> ApiResponse<T> forbidden(String message) {
        return error(HttpStatus.FORBIDDEN, message != null ? message : "禁止访问");
    }

    /**
     * 创建404 Not Found响应
     * 
     * @param message 错误消息（可选）
     * @param <T>     数据类型
     * @return 404错误响应
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(HttpStatus.NOT_FOUND, message != null ? message : "资源不存在");
    }

    /**
     * 创建500 Internal Server Error响应
     * 
     * @param message 错误消息（可选）
     * @param <T>     数据类型
     * @return 500错误响应
     */
    public static <T> ApiResponse<T> internalServerError(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, message != null ? message : "服务器内部错误");
    }

    // ===== 工具方法 =====

    /**
     * 判断响应是否成功
     * 
     * @return 是否成功响应
     */
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }

    /**
     * 判断响应是否为客户端错误
     * 
     * @return 是否为客户端错误
     */
    public boolean isClientError() {
        return code >= 400 && code < 500;
    }

    /**
     * 判断响应是否为服务器错误
     * 
     * @return 是否为服务器错误
     */
    public boolean isServerError() {
        return code >= 500;
    }

    /**
     * 获取HTTP状态对象
     * 
     * @return HttpStatus对象，如果状态码无效则返回null
     */
    public HttpStatus getHttpStatus() {
        try {
            return HttpStatus.valueOf(code);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}