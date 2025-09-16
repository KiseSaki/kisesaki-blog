package com.kisesaki.blog.content.tag.util;

import java.util.Random;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.SlugUtils;
import com.kisesaki.blog.content.tag.entity.Tags;
import com.kisesaki.blog.content.tag.mapper.TagsMapper;

import lombok.RequiredArgsConstructor;

/**
 * 标签工具类
 * 提供标签相关的通用方法，避免代码重复
 * 
 * @author KiseSaki
 */
@Component
@RequiredArgsConstructor
public class TagUtils {

    private final TagsMapper tagsMapper;

    /**
     * 预定义的标签颜色数组
     */
    private static final String[] TAG_COLORS = {
            "#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6",
            "#1abc9c", "#34495e", "#e67e22", "#95a5a6", "#16a085",
            "#27ae60", "#2980b9", "#8e44ad", "#f1c40f", "#e74c3c",
            "#ecf0f1", "#bdc3c7", "#d35400", "#c0392b", "#7f8c8d"
    };

    /**
     * 验证标签名称是否已存在
     * 
     * @param name 标签名称
     * @throws BusinessException 如果名称已存在
     */
    public void validateTagNameUnique(String name) {
        LambdaQueryWrapper<Tags> wrapper = new LambdaQueryWrapper<Tags>()
                .eq(Tags::getName, name);
        if (tagsMapper.selectCount(wrapper) > 0) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "标签名称已存在");
        }
    }

    /**
     * 验证并生成唯一的 slug
     * 
     * @param requestSlug 请求中的 slug（可能为空）
     * @param tagName     标签名称（用于生成默认 slug）
     * @return 唯一的 slug
     */
    public String generateUniqueSlug(String requestSlug, String tagName) {
        String slug = requestSlug;
        if (slug == null || slug.trim().isEmpty()) {
            slug = SlugUtils.generateSlug(tagName);
        }

        // 检查 slug 是否已存在
        LambdaQueryWrapper<Tags> wrapper = new LambdaQueryWrapper<Tags>()
                .eq(Tags::getSlug, slug);
        if (tagsMapper.selectCount(wrapper) > 0) {
            // 自动添加后缀避免冲突
            slug = generateUniqueSlugWithSuffix(slug);
        }

        return slug;
    }

    /**
     * 生成唯一的 slug（带数字后缀）
     * 
     * @param baseSlug 基础 slug
     * @return 唯一的 slug
     */
    private String generateUniqueSlugWithSuffix(String baseSlug) {
        int counter = 1;
        String candidateSlug = baseSlug + "-" + counter;

        while (tagsMapper.selectCount(new LambdaQueryWrapper<Tags>().eq(Tags::getSlug, candidateSlug)) > 0) {
            counter++;
            candidateSlug = baseSlug + "-" + counter;
        }

        return candidateSlug;
    }

    /**
     * 生成随机颜色
     * 
     * @param requestColor 请求中的颜色（可能为空）
     * @return 颜色值
     */
    public String generateColor(String requestColor) {
        if (requestColor != null && !requestColor.trim().isEmpty()) {
            return requestColor;
        }

        Random random = new Random();
        return TAG_COLORS[random.nextInt(TAG_COLORS.length)];
    }

    /**
     * 生成随机颜色（不检查请求参数）
     * 
     * @return 随机颜色值
     */
    public String generateRandomColor() {
        Random random = new Random();
        return TAG_COLORS[random.nextInt(TAG_COLORS.length)];
    }

    /**
     * 验证标签创建的基础信息
     * 
     * @param name 标签名称
     * @return 验证通过的标签信息对象
     */
    public TagValidationResult validateAndPrepareTagInfo(String name, String requestSlug, String requestColor) {
        // 验证名称唯一性
        validateTagNameUnique(name);

        // 生成唯一 slug
        String slug = generateUniqueSlug(requestSlug, name);

        // 生成颜色
        String color = generateColor(requestColor);

        return new TagValidationResult(name, slug, color);
    }

    /**
     * 标签验证结果
     */
    public static class TagValidationResult {
        private final String name;
        private final String slug;
        private final String color;

        public TagValidationResult(String name, String slug, String color) {
            this.name = name;
            this.slug = slug;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }

        public String getColor() {
            return color;
        }
    }
}