package com.kisesaki.blog.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮件发送状态枚举
 * 
 * @author KiseSaki
 */
@Getter
@AllArgsConstructor
public enum EmailStatus {

    /**
     * 待发送
     */
    PENDING("pending", "待发送"),

    /**
     * 发送中
     */
    SENDING("sending", "发送中"),

    /**
     * 发送成功
     */
    SUCCESS("success", "发送成功"),

    /**
     * 发送失败
     */
    FAILED("failed", "发送失败"),

    /**
     * 重试中
     */
    RETRYING("retrying", "重试中"),

    /**
     * 已取消
     */
    CANCELLED("cancelled", "已取消");

    /**
     * 状态代码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据代码获取邮件状态
     * 
     * @param code 状态代码
     * @return 邮件状态
     */
    public static EmailStatus fromCode(String code) {
        for (EmailStatus status : EmailStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的邮件状态代码: " + code);
    }

    /**
     * 检查是否为最终状态
     * 
     * @return 是否为最终状态
     */
    public boolean isFinalStatus() {
        return this == SUCCESS || this == FAILED || this == CANCELLED;
    }

    /**
     * 检查是否为进行中状态
     * 
     * @return 是否为进行中状态
     */
    public boolean isInProgress() {
        return this == PENDING || this == SENDING || this == RETRYING;
    }

    /**
     * 检查是否可以重试
     * 
     * @return 是否可以重试
     */
    public boolean canRetry() {
        return this == FAILED;
    }
}