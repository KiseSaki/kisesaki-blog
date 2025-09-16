package com.kisesaki.blog.content.tag.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.common.enums.ErrorCode;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.common.util.SlugUtils;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCreateRequest;
import com.kisesaki.blog.content.tag.entity.Tags;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.mapper.TagsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminTagService {

    private final TagsMapper tagsMapper;

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
        // 检查标签名称是否已存在
        LambdaQueryWrapper<Tags> nameWrapper = new LambdaQueryWrapper<Tags>()
                .eq(Tags::getName, request.getName());
        if (tagsMapper.selectCount(nameWrapper) > 0) {
            throw BusinessException.of(ErrorCode.PARAM_ERROR, "标签名称已存在");
        }

        // 生成slug并检查是否已存在
        String slug = request.getSlug();
        if (slug == null || slug.trim().isEmpty()) {
            slug = SlugUtils.generateSlug(request.getName());
        }
        LambdaQueryWrapper<Tags> slugWrapper = new LambdaQueryWrapper<Tags>()
                .eq(Tags::getSlug, slug);
        if (tagsMapper.selectCount(slugWrapper) > 0) {
            // 自动添加后缀避免冲突
            slug = generateUniqueSlug(slug);
        }

        // 生成颜色
        String color = request.getColor();
        if (color == null || color.trim().isEmpty()) {
            color = generateRandomColor();
        }

        // 创建标签实体
        Tags tag = new Tags();
        tag.setName(request.getName());
        tag.setSlug(slug);
        tag.setDescription(request.getDescription());
        tag.setColor(color);
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

    /**
     * 生成唯一的slug
     */
    private String generateUniqueSlug(String baseSlug) {
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
     */
    private String generateRandomColor() {
        String[] colors = {
                "#3498db", "#e74c3c", "#2ecc71", "#f39c12", "#9b59b6",
                "#1abc9c", "#34495e", "#e67e22", "#95a5a6", "#16a085",
                "#27ae60", "#2980b9", "#8e44ad", "#f1c40f", "#e74c3c",
                "#ecf0f1", "#bdc3c7", "#d35400", "#c0392b", "#7f8c8d"
        };
        Random random = new Random();
        return colors[random.nextInt(colors.length)];
    }
}
