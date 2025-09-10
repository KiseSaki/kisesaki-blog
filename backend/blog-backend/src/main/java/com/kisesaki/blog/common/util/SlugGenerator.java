package com.kisesaki.blog.common.util;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * Slug生成工具类
 * 支持中文转拼音、英文优化、特殊字符处理等功能
 */
@Slf4j
public class SlugGenerator {

    private static final Pattern VALID_SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");
    private static final int MAX_SLUG_LENGTH = 100;

    /**
     * 验证slug格式是否正确
     *
     * @param slug 待验证的slug
     * @return 是否有效
     */
    public static boolean isValidSlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return false;
        }
        String trimmedSlug = slug.trim();
        // 长度限制
        if (trimmedSlug.length() > MAX_SLUG_LENGTH) {
            return false;
        }
        // 只允许小写字母、数字和短横线，不能以短横线开头或结尾
        return VALID_SLUG_PATTERN.matcher(trimmedSlug).matches();
    }

    /**
     * 根据标题生成slug
     *
     * @param title 文章标题
     * @return 生成的slug
     */
    public static String generateFromTitle(String title) {
        if (!StringUtils.hasText(title)) {
            return "untitled-post";
        }

        try {
            String processedTitle = title.trim();

            // 1. 将中文转换为拼音
            processedTitle = convertChineseToPinyin(processedTitle);

            // 2. 转为小写并处理特殊字符
            String slug = processedTitle.toLowerCase()
                    // 保留字母数字和空格，其他字符替换为短横线
                    .replaceAll("[^a-z0-9\\s]+", "-")
                    // 将空格替换为短横线
                    .replaceAll("\\s+", "-")
                    // 压缩多个连续的短横线为一个
                    .replaceAll("-+", "-")
                    // 移除开头和结尾的短横线
                    .replaceAll("^-+|-+$", "");

            // 3. 长度限制
            if (slug.length() > MAX_SLUG_LENGTH) {
                slug = slug.substring(0, MAX_SLUG_LENGTH);
                // 确保不以短横线结尾
                slug = slug.replaceAll("-+$", "");
            }

            // 4. 如果为空或过短，使用默认值
            if (slug.isEmpty() || slug.length() < 2) {
                slug = "untitled-post";
            }

            log.debug("Generated slug '{}' from title '{}'", slug, title);
            return slug;

        } catch (Exception e) {
            log.warn("Failed to generate slug from title '{}', using default", title, e);
            return "untitled-post";
        }
    }

    /**
     * 将中文字符转换为拼音
     *
     * @param text 包含中文的文本
     * @return 转换后的文本
     */
    private static String convertChineseToPinyin(String text) {
        if (!CHINESE_PATTERN.matcher(text).find()) {
            // 没有中文字符，直接返回
            return text;
        }

        StringBuilder result = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : text.toCharArray()) {
            if (Character.toString(c).matches("[\\u4e00-\\u9fa5]")) {
                // 中文字符
                try {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null && pinyinArray.length > 0) {
                        result.append(pinyinArray[0]);
                    } else {
                        // 无法转换的中文字符，跳过
                        log.debug("Cannot convert Chinese character '{}' to pinyin", c);
                    }
                } catch (Exception e) {
                    log.debug("Error converting Chinese character '{}' to pinyin", c, e);
                }
            } else {
                // 非中文字符，直接添加
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * 生成唯一的slug（添加数字后缀）
     *
     * @param baseSlug 基础slug
     * @param counter  计数器
     * @return 带数字后缀的slug
     */
    public static String generateUniqueSlug(String baseSlug, int counter) {
        if (counter <= 1) {
            return baseSlug;
        }
        return baseSlug + "-" + counter;
    }

    /**
     * 清理并规范化用户提供的slug
     *
     * @param userSlug 用户提供的slug
     * @return 清理后的slug，如果无效则返回null
     */
    public static String sanitizeUserSlug(String userSlug) {
        if (!StringUtils.hasText(userSlug)) {
            return null;
        }

        String cleaned = userSlug.trim().toLowerCase()
                // 移除不允许的字符
                .replaceAll("[^a-z0-9-]", "")
                // 压缩多个连续的短横线
                .replaceAll("-+", "-")
                // 移除开头和结尾的短横线
                .replaceAll("^-+|-+$", "");

        // 验证清理后的slug
        if (isValidSlug(cleaned)) {
            return cleaned;
        }

        return null;
    }
}
