package com.kisesaki.blog.content.post.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.markdown.MarkdownService;
import com.kisesaki.blog.content.category.entity.Categories;
import com.kisesaki.blog.content.category.mapper.CategoriesMapper;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostRequest;
import com.kisesaki.blog.content.post.dto.PostCommand.CreatePostResponse;
import com.kisesaki.blog.content.post.entity.PostTags;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostTagsMapper;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCommandService {

    private final PostsMapper postsMapper;
    private final PostTagsMapper postTagsMapper;
    private final CategoriesMapper categoriesMapper;
    private final MarkdownService markdownService;

    /**
     * 创建文章
     *
     * @param request 创建文章请求DTO
     * @param userId  当前用户ID
     * @return 创建文章响应DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse<CreatePostResponse> createPost(CreatePostRequest request, Long userId) {
        OffsetDateTime now = OffsetDateTime.now();

        Posts post = new Posts();
        post.setAuthorId(userId);
        post.setCategoryId(request.getCategoryId());
        post.setTitle(request.getTitle());

        // 生成或使用用户提供的slug
        post.setSlug(generateSlug(request.getSlug(), request.getTitle()));

        post.setExcerpt(request.getExcerpt());
        post.setContent(request.getContent());

        // 生成HTML内容
        post.setHtmlContent(convertMarkdownToHtml(request.getContent()));

        // 计算阅读时间和字数统计
        post.setReadingTime(markdownService.estimateReadingTime(request.getContent()));
        post.setWordCount(markdownService.countWords(request.getContent()));

        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setFeaturedImageUrl(request.getFeaturedImageUrl());
        post.setStatus(Boolean.TRUE.equals(request.getPublishNow()) ? "published" : "draft");
        post.setVisibility(request.getVisibility() == null ? "public" : request.getVisibility());
        post.setIsFeatured(request.getIsFeatured() != null && request.getIsFeatured());
        post.setIsTop(request.getIsTop() != null && request.getIsTop());
        post.setAllowComments(request.getAllowComments() != null && request.getAllowComments());
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        // 生成SEO相关字段
        generateSeoFields(post, request);

        // 如果visibility是password_protected，password不能为空
        if ("password_protected".equals(post.getVisibility())
                && (request.getPassword() == null || request.getPassword().isEmpty())) {
            throw new IllegalArgumentException("当可见性为密码保护时，访问密码不能为空");
        }
        // 如果visibility不是password_protected，password必须为空
        if (!"password_protected".equals(post.getVisibility())) {
            post.setPassword(null);
        } else {
            post.setPassword(request.getPassword());
        }

        // 设置发布时间
        if (request.getScheduledAt() == null && Boolean.TRUE.equals(request.getPublishNow())) {
            post.setPublishedAt(now);
        }

        // 插入数据库
        postsMapper.insert(post);
        // 获取插入后的ID
        Long postId = post.getId();

        // 处理标签
        List<Long> tagIds = request.getTagIds();
        if (tagIds != null && !tagIds.isEmpty()) {
            tagIds.stream().map(id -> {
                PostTags pt = new PostTags();
                pt.setPostId(postId);
                pt.setTagId(id);
                return pt;
            }).forEach(postTagsMapper::insert);
        }

        // 添加该分类的文章数量
        new LambdaUpdateChainWrapper<>(categoriesMapper)
                .eq(Categories::getId, request.getCategoryId())
                .setSql("post_count = post_count + 1")
                .update();

        return ApiResponse.success(CreatePostResponse.fromEntity(post));
    }

    /**
     * 生成或验证slug
     *
     * @param providedSlug 用户提供的slug
     * @param title        文章标题
     * @return 最终的slug
     */
    private String generateSlug(String providedSlug, String title) {
        if (StringUtils.hasText(providedSlug)) {
            // 验证用户提供的slug格式
            if (isValidSlug(providedSlug)) {
                return providedSlug;
            } else {
                log.warn("用户提供的slug格式不正确: {}, 将使用标题生成", providedSlug);
            }
        }

        // 根据标题生成slug
        return generateSlugFromTitle(title);
    }

    /**
     * 验证slug格式是否正确
     *
     * @param slug 待验证的slug
     * @return 是否有效
     */
    private boolean isValidSlug(String slug) {
        if (slug == null || slug.trim().isEmpty()) {
            return false;
        }
        // 只允许小写字母、数字和短横线，不能以短横线开头或结尾
        return Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$").matcher(slug.trim()).matches();
    }

    /**
     * 根据标题生成slug
     *
     * @param title 文章标题
     * @return 生成的slug
     */
    private String generateSlugFromTitle(String title) {
        if (!StringUtils.hasText(title)) {
            return "untitled-post";
        }

        String s = title.toLowerCase()
                // 替换中文字符为拼音或移除（这里简化处理，实际可以使用pinyin4j库）
                .replaceAll("[\\u4e00-\\u9fa5]", "")
                // 保留字母数字，其他字符替换为短横线
                .replaceAll("[^a-z0-9]+", "-")
                // 移除开头和结尾的短横线
                .replaceAll("^-+|-+$", "")
                // 压缩多个连续的短横线为一个
                .replaceAll("-+", "-");
        return s
                // 如果为空则使用默认值
                .isEmpty() ? "untitled-post"
                : s;
    }

    /**
     * 生成SEO相关字段
     *
     * @param post    文章实体
     * @param request 请求DTO
     */
    private void generateSeoFields(Posts post, CreatePostRequest request) {
        // SEO标题：优先使用用户提供的，否则使用文章标题
        if (StringUtils.hasText(request.getSeoTitle())) {
            post.setSeoTitle(request.getSeoTitle());
        } else {
            post.setSeoTitle(post.getTitle());
        }

        // SEO描述：优先使用用户提供的，否则使用摘要，再否则从内容截取
        if (StringUtils.hasText(request.getSeoDescription())) {
            post.setSeoDescription(request.getSeoDescription());
        } else if (StringUtils.hasText(request.getExcerpt())) {
            post.setSeoDescription(request.getExcerpt());
        } else {
            post.setSeoDescription(generateDescriptionFromContent(request.getContent()));
        }

        // SEO关键词：优先使用用户提供的，否则从标题和内容提取
        if (StringUtils.hasText(request.getSeoKeywords())) {
            post.setSeoKeywords(request.getSeoKeywords());
        } else {
            post.setSeoKeywords(generateKeywordsFromContent(post.getTitle()));
        }
    }

    /**
     * 从内容中生成描述
     *
     * @param content Markdown内容
     * @return 生成的描述
     */
    private String generateDescriptionFromContent(String content) {
        return markdownService.generateExcerpt(content, 150);
    }

    /**
     * 从标题和内容中生成关键词
     *
     * @param title 文章标题
     * @return 生成的关键词
     */
    private String generateKeywordsFromContent(String title) {
        // 这里简化处理，实际可以使用更复杂的NLP算法
        StringBuilder keywords = new StringBuilder();

        if (StringUtils.hasText(title)) {
            // 从标题中提取关键词
            String[] titleWords = title.split("[\\s,，。！？；：\"']+");
            for (String word : titleWords) {
                if (word.length() > 1) {
                    if (!keywords.isEmpty()) {
                        keywords.append(",");
                    }
                    keywords.append(word.trim());
                }
            }
        }

        return keywords.toString();
    }

    /**
     * 将Markdown转换为HTML
     *
     * @param markdownContent Markdown内容
     * @return HTML内容
     */
    private String convertMarkdownToHtml(String markdownContent) {
        return markdownService.convertToHtml(markdownContent);
    }
}
