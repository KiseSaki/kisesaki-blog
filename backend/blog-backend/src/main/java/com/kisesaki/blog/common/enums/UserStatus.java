package com.kisesaki.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Getter @AllArgsConstructor
public enum UserStatus {

    /**
     * 激活
     */
    ACTIVE("ACTIVE", "激活"),

    /**
     * 未激活
     */
    INACTIVE("INACTIVE", "未激活"),

    /**
     * 禁用
     */
    DISABLED("DISABLED", "禁用"),

    /**
     * 锁定
     */
    LOCKED("LOCKED", "锁定");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;
}
