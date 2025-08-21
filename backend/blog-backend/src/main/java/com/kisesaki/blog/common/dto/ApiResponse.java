package com.kisesaki.blog.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一响应格式
 * 
 * @param <T> 数据类型
 * @author KiseSaki
 */
@Schema(description = "统一响应格式") @JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(description = "状态码", example = "200")
    private int code;

    @Schema(description = "提示信息", example = "请求成功")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "时间戳", example = "1625251200000")
    private long timestamp;

    /**
     * 私有构造函数，防止直接实例化
     */
    @SuppressWarnings("unused")
    private ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 构造函数
     *
     * @param code 状态码
     * @param message 提示信息
     * @param data 返回数据
     */
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（带数据）
     *
     * @param <T> 数据类型
     * @param data 返回数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "请求成功", data);
    }

    /**
     * 成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, "请求成功", null);
    }

    /**
     * 成功响应（自定义消息和数据）
     *
     * @param <T> 数据类型
     * @param message 提示信息
     * @param data 返回数据
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    /**
     * 错误响应
     *
     * @param <T> 数据类型
     * @param code 状态码
     * @param message 提示信息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    /**
     * 错误响应（默认500）
     *
     * @param <T> 数据类型
     * @param message 提示信息
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null);
    }

    // Getter and Setter methods
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 判断响应是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }

    @Override
    public String toString() {
        return "ApiResponse{" + "code=" + code + ", message='" + message + '\'' + ", data=" + data
                + ", timestamp=" + timestamp + '}';
    }
}