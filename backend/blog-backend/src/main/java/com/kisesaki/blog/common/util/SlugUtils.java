package com.kisesaki.blog.common.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * URL slug 生成工具类
 * 用于将标题等文本转换为 URL 友好的 slug
 * 
 * @author KiseSaki
 */
public class SlugUtils {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-+");

    /**
     * 生成 URL slug
     * 
     * @param input 输入文本
     * @return slug
     */
    public static String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String slug = input.trim().toLowerCase();
        
        // 处理中文等非ASCII字符，转为拼音或保留
        slug = transliterate(slug);
        
        // 替换空格为连字符
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        
        // 移除非字母数字和连字符的字符
        slug = NON_LATIN.matcher(slug).replaceAll("");
        
        // 合并多个连续的连字符
        slug = MULTIPLE_HYPHENS.matcher(slug).replaceAll("-");
        
        // 移除开头和结尾的连字符
        slug = slug.replaceAll("^-+|-+$", "");
        
        return slug;
    }

    /**
     * 音译处理（简化版本）
     * 这里使用 Unicode 规范化，实际项目中可以集成拼音库
     */
    private static String transliterate(String input) {
        // 使用 Unicode 规范化分解重音字符
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        
        // 移除重音符号
        return normalized.replaceAll("\\p{M}", "");
    }

    /**
     * 验证 slug 是否有效
     * 
     * @param slug 待验证的 slug
     * @return 是否有效
     */
    public static boolean isValidSlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return false;
        }
        
        // slug 只能包含字母、数字和连字符
        return slug.matches("^[a-z0-9-]+$") && !slug.startsWith("-") && !slug.endsWith("-");
    }
}