package com.kisesaki.blog.content.tag;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.util.PageQueryUtils;
import com.kisesaki.blog.content.tag.dto.TagQuery.PopularTagResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagCloudItem;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagDetailResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagListParams;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagListResponse;
import com.kisesaki.blog.content.tag.entity.Tags;
import com.kisesaki.blog.content.tag.mapper.TagsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 标签服务
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagsMapper tagsMapper;

    /**
     * 获取标签列表
     * 
     * @param params 请求参数
     * @return 标签列表
     */
    public PageResponse<TagListResponse> getTagList(TagListParams params) {
        LambdaQueryWrapper<Tags> queryWrapper = buildQueryWrapper(params);

        // 应用时间范围条件
        PageQueryUtils.applyTimeRangeConditions(queryWrapper, params.getPageable(), Tags::getCreatedAt);

        // 应用排序规则
        PageQueryUtils.createSortBuilder(queryWrapper, params.getPageable())
                .defaultSort(Tags::getCreatedAt, true) // 默认按创建时间倒序
                .addSortField("id", Tags::getId)
                .addSortField("name", Tags::getName)
                .addSortField("postCount", Tags::getPostCount)
                .addSortField("createdAt", Tags::getCreatedAt)
                .apply();

        // 执行分页查询
        return PageQueryUtils.executePageQuery(
                tagsMapper,
                queryWrapper,
                params.getPageable(),
                this::convertToResponse);
    }

    /**
     * 获取标签详情
     * 
     * @param tagId 标签ID
     * @return 标签详情
     */
    public TagDetailResponse getTagDetailById(Long tagId) {
        return tagsMapper.getTagDetailById(tagId);
    }

    /**
     * 根据Slug获取标签详情
     * 
     * @param slug 标签Slug
     * @return 标签详情
     */
    public TagDetailResponse getTagDetailBySlug(String slug) {
        return tagsMapper.getTagDetailBySlug(slug);
    }

    /**
     * 获取热门标签
     * 
     * @return 热门标签
     */
    public List<PopularTagResponse> getPopularTag() {
        // 构建查询条件，按热度权重排序，限制返回数量为10
        LambdaQueryWrapper<Tags> queryWrapper = new LambdaQueryWrapper<Tags>()
                .orderByDesc(Tags::getPopularityScore).last("LIMIT 10");

        // 执行查询
        List<Tags> popularTag = tagsMapper.selectList(queryWrapper);
        return popularTag.stream().map(tag -> {
            PopularTagResponse response = new PopularTagResponse();
            response.setId(tag.getId());
            response.setName(tag.getName());
            response.setSlug(tag.getSlug());
            response.setColor(tag.getColor());
            response.setPostCount(tag.getPostCount());
            response.setPopularityScore(tag.getPopularityScore());
            response.setLastUsedAt(tag.getLastUsedAt());
            return response;
        }).toList();
    }

    /**
     * 获取标签云
     * 
     * @return 标签云列表，按热度权重排序
     */
    public List<TagCloudItem> getTagCloud() {
        try {
            // 查询所有标签，按热度权重降序排序，限制数量为50
            LambdaQueryWrapper<Tags> queryWrapper = new LambdaQueryWrapper<Tags>()
                    .orderByDesc(Tags::getPopularityScore)
                    .last("LIMIT 50");

            List<Tags> tags = tagsMapper.selectList(queryWrapper);

            if (tags.isEmpty()) {
                log.info("标签云查询结果为空");
                return List.of();
            }

            // 计算字体权重：基于 popularityScore 或 postCount 映射到 1-10
            // 找到最大和最小值用于线性映射
            double maxScore = tags.stream()
                    .mapToDouble(
                            tag -> tag.getPopularityScore() != null ? tag.getPopularityScore() : tag.getPostCount())
                    .max().orElse(1.0);
            double minScore = tags.stream()
                    .mapToDouble(
                            tag -> tag.getPopularityScore() != null ? tag.getPopularityScore() : tag.getPostCount())
                    .min().orElse(0.0);

            return tags.stream().map(tag -> {
                TagCloudItem item = new TagCloudItem();
                item.setId(tag.getId());
                item.setName(tag.getName());
                item.setSlug(tag.getSlug());
                item.setColor(tag.getColor());
                item.setPostCount(tag.getPostCount());
                item.setPopularityScore(tag.getPopularityScore());

                // 计算字体权重（1-10），避免除零
                double score = tag.getPopularityScore() != null ? tag.getPopularityScore() : tag.getPostCount();
                int fontWeight = (maxScore == minScore) ? 5
                        : (int) Math.round(1 + 9 * (score - minScore) / (maxScore - minScore));
                item.setFontWeight(Math.max(1, Math.min(10, fontWeight))); // 确保在 1-10 范围内

                return item;
            }).toList();
        } catch (Exception e) {
            log.error("获取标签云失败", e);
            throw new RuntimeException("获取标签云失败，请稍后重试");
        }
    }

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<Tags> buildQueryWrapper(TagListParams params) {
        LambdaQueryWrapper<Tags> queryWrapper = new LambdaQueryWrapper<>();

        // 标签名称模糊搜索
        if (params.getName() != null && !params.getName().isEmpty()) {
            queryWrapper.like(Tags::getName, params.getName());
        }

        return queryWrapper;
    }

    /**
     * 转换实体为响应对象
     */
    private TagListResponse convertToResponse(Tags tag) {
        TagListResponse response = new TagListResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        response.setSlug(tag.getSlug());
        response.setDescription(tag.getDescription());
        response.setColor(tag.getColor());
        response.setPostCount(tag.getPostCount());
        response.setCreatedAt(tag.getCreatedAt());
        return response;
    }
}
