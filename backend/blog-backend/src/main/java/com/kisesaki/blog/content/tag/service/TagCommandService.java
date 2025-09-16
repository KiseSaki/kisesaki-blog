package com.kisesaki.blog.content.tag.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.auth.security.user.CustomUserPrincipal;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.AuthUtils;
import com.kisesaki.blog.common.util.PageQueryUtils;
import com.kisesaki.blog.content.tag.dto.TagCreateRequest;
import com.kisesaki.blog.content.tag.dto.TagQuery.MyTagResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagListParams;
import com.kisesaki.blog.content.tag.entity.Tags;
import com.kisesaki.blog.content.tag.mapper.TagsMapper;
import com.kisesaki.blog.content.tag.util.TagUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 标签命令服务
 * 负责处理标签的创建、更新、删除等写操作
 * 
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagCommandService {

    private final TagsMapper tagsMapper;
    private final TagUtils tagUtils;

    /**
     * 创建新标签（用户创作文章时）
     * 
     * @param request 创建标签请求
     * @return 创建的标签信息
     */
    @Transactional
    public Tags createTag(TagCreateRequest request) {
        // 获取当前用户
        CustomUserPrincipal currentUser = getCurrentUser();

        // 使用工具类验证和准备标签信息
        TagUtils.TagValidationResult validationResult = tagUtils.validateAndPrepareTagInfo(
                request.getName(),
                request.getSlug(),
                request.getColor());

        // 创建标签实体
        Tags tag = new Tags();
        tag.setName(validationResult.getName());
        tag.setSlug(validationResult.getSlug());
        tag.setDescription(request.getDescription());
        tag.setColor(validationResult.getColor());
        tag.setPostCount(0);
        tag.setPopularityScore(0.0);
        tag.setCreatedBy(currentUser.getId());
        tag.setIsApproved(false); // 用户创建的标签需要审核
        tag.setCreatedAt(OffsetDateTime.now());
        tag.setUpdatedAt(OffsetDateTime.now());

        // 保存标签
        tagsMapper.insert(tag);

        log.info("用户 {} 创建了标签: {}", currentUser.getUsername(), tag.getName());

        return tag;
    }

    /**
     * 获取我创建的标签
     * 
     * @param params 查询参数
     * @return 我创建的标签列表
     */
    public PageResponse<MyTagResponse> getMyTags(TagListParams params) {
        CustomUserPrincipal currentUser = getCurrentUser();

        LambdaQueryWrapper<Tags> queryWrapper = new LambdaQueryWrapper<>();

        // 只查询当前用户创建的标签
        queryWrapper.eq(Tags::getCreatedBy, currentUser.getId());

        // 标签名称模糊搜索
        if (params.getName() != null && !params.getName().isEmpty()) {
            queryWrapper.like(Tags::getName, params.getName());
        }

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
                this::convertToMyTagResponse);
    }

    /**
     * 获取当前用户
     */
    private CustomUserPrincipal getCurrentUser() {
        CustomUserPrincipal currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            throw BusinessException.of(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        return currentUser;
    }

    /**
     * 转换实体为我的标签响应对象
     */
    private MyTagResponse convertToMyTagResponse(Tags tag) {
        MyTagResponse response = new MyTagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        response.setSlug(tag.getSlug());
        response.setDescription(tag.getDescription());
        response.setColor(tag.getColor());
        response.setPostCount(tag.getPostCount());
        response.setCreatedAt(tag.getCreatedAt());
        response.setIsApproved(tag.getIsApproved());

        // 设置审核状态
        if (tag.getIsApproved() == null || !tag.getIsApproved()) {
            response.setApprovalStatus("pending");
        } else {
            response.setApprovalStatus("approved");
        }

        return response;
    }
}