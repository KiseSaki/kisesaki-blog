package com.kisesaki.blog.content.tag.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCreateRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.entity.Tags;
import com.kisesaki.blog.content.tag.mapper.TagsMapper;
import com.kisesaki.blog.content.tag.util.TagUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTagService {

    private final TagsMapper tagsMapper;
    private final TagUtils tagUtils;

    /**
     * 获取管理员标签列表
     *
     * @param params 分页参数
     * @return 分页响应结果
     */
    public PageResponse<AdminTagListResponse> getAdminTagList(AdminTagListParams params) {
        // 构建分页对象
        Page<AdminTagListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize());

        // 根据 includeTotal 参数决定是否需要计算总数
        if (!params.getPageable().getIncludeTotal()) {
            page.setSearchCount(false);
        }

        // 执行分页查询
        Page<AdminTagListResponse> result = tagsMapper.getAdminTagList(page, params);

        Long totalCount = params.getPageable().getIncludeTotal() ? result.getTotal() : null;

        return PageResponse.of(
                result.getRecords(),
                totalCount != null ? totalCount : 0L,
                params.getPageable());
    }

    /**
     * 创建新标签
     *
     * @param request 创建请求参数
     * @return 新创建标签的ID
     */
    public Long createAdminTag(AdminTagCreateRequest request, Long userId) {
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
        tag.setCreatedBy(userId);
        tag.setCreatedAt(OffsetDateTime.now());
        tag.setUpdatedAt(OffsetDateTime.now());

        if (request.getIsApproved() != null && request.getIsApproved()) {
            tag.setIsApproved(true);
            tag.setApprovalStatus("approved");
            tag.setApprovedBy(userId);
            tag.setApprovedAt(OffsetDateTime.now());
            tag.setApprovalNote(request.getApprovalNote());
        } else {
            tag.setIsApproved(false); // 需要审核
            tag.setApprovalStatus("pending");
        }

        // 保存标签
        tagsMapper.insert(tag);

        log.info("管理员用户 {} 创建了标签: {}", userId, tag.getName());

        return tag.getId();
    }
}
