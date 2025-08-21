package com.kisesaki.blog.common.enums;

/**
 * 响应状态枚举
 * 用于标识API响应的处理状态
 */
public enum ResponseStatus {

    /**
     * 成功
     */
    SUCCESS("success", "操作成功"),

    /**
     * 失败
     */
    FAIL("fail", "操作失败"),

    /**
     * 错误
     */
    ERROR("error", "系统错误"),

    /**
     * 警告
     */
    WARNING("warning", "操作警告");

    private final String status;
    private final String description;

    ResponseStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据HTTP状态码判断响应状态
     * 
     * @param httpStatus HTTP状态码
     * @return 响应状态
     */
    public static ResponseStatus fromHttpStatus(int httpStatus) {
        if (httpStatus >= 200 && httpStatus < 300) {
            return SUCCESS;
        } else if (httpStatus >= 400 && httpStatus < 500) {
            return FAIL;
        } else {
            return ERROR;
        }
    }

    /**
     * 根据ErrorCode判断响应状态
     * 
     * @param errorCode 错误码
     * @return 响应状态
     */
    public static ResponseStatus fromErrorCode(ErrorCode errorCode) {
        int code = errorCode.getCode();
        if (code == 200) {
            return SUCCESS;
        } else if (code >= 400 && code < 500) {
            return FAIL;
        } else {
            return ERROR;
        }
    }
}