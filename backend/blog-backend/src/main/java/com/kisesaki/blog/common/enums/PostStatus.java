package com.kisesaki.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章状态枚举
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Getter @AllArgsConstructor
public enum PostStatus {

    /**
     * 草稿
     */
    DRAFT("DRAFT", "草稿"),

    /**
     * 已发布
     */
    PUBLISHED("PUBLISHED", "已发布"),

    /**
     * 已归档
     */
    ARCHIVED("ARCHIVED", "已归档"),

    /**
     * 已删除
     */
    DELETED("DELETED", "已删除");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String description;
}
