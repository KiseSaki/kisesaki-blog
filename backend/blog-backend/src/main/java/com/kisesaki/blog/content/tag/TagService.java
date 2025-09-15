package com.kisesaki.blog.content.tag;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.util.PageQueryUtils;
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
