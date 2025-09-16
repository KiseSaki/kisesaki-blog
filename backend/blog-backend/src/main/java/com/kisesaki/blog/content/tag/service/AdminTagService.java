package com.kisesaki.blog.content.tag.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.exception.BusinessException;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagApprovalRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCleanupResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagCreateRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagMergeRequest;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagPendingResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagUnusedResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagUpdateRequest;
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
    public PageResponse<AdminTagListResponse> adminGetTagList(AdminTagListParams params) {
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
    public Long adminTagCreate(AdminTagCreateRequest request, Long userId) {
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

    /**
     * 更新标签
     *
     * @param id      标签ID
     * @param request 更新请求参数
     * @param userId  操作用户ID
     */
    public void adminTagUpdate(Long id, AdminTagUpdateRequest request, Long userId) {
        Tags tag = tagsMapper.selectById(id);
        if (tag == null) {
            throw new IllegalArgumentException("标签不存在");
        }

        // 使用工具类验证和准备标签信息
        TagUtils.TagValidationResult validationResult = tagUtils.validateAndPrepareTagInfo(
                request.getName(),
                request.getSlug(),
                request.getColor());

        tag.setName(validationResult.getName());
        tag.setSlug(validationResult.getSlug());
        tag.setDescription(request.getDescription());
        tag.setColor(validationResult.getColor());
        tag.setUpdatedAt(OffsetDateTime.now());

        if (request.getIsApproved() != null) {
            if (request.getIsApproved() && !tag.getIsApproved()) {
                // 从未审核到审核通过
                tag.setIsApproved(true);
                tag.setApprovalStatus("approved");
                tag.setApprovedBy(userId);
                tag.setApprovedAt(OffsetDateTime.now());
                tag.setApprovalNote(request.getApprovalNote());
            } else if (!request.getIsApproved() && tag.getIsApproved()) {
                // 从审核通过到未审核
                tag.setIsApproved(false);
                tag.setApprovalStatus("pending");
                tag.setApprovedBy(null);
                tag.setApprovedAt(null);
                tag.setApprovalNote(null);
            } else if (request.getIsApproved()) {
                // 已审核状态下更新备注
                tag.setApprovalNote(request.getApprovalNote());
            }
        }

        tagsMapper.updateById(tag);

        log.info("管理员用户 {} 更新了标签: {}", userId, tag.getName());
    }

    /**
     * 删除标签
     *
     * @param id     标签ID
     * @param userId 操作用户ID
     */
    public void adminTagDelete(Long id, Long userId) {
        Tags tag = tagsMapper.selectById(id);
        if (tag == null) {
            throw new IllegalArgumentException("标签不存在");
        }

        tagsMapper.deleteById(id);

        log.info("管理员用户 {} 删除了标签: {}", userId, tag.getName());
    }

    /**
     * 审核标签
     *
     * @param id      标签ID
     * @param request 审核请求参数
     * @param userId  操作用户ID
     */
    public void adminTagApprove(Long id, AdminTagApprovalRequest request, Long userId) {
        Tags tag = tagsMapper.selectById(id);
        if (tag == null) {
            throw BusinessException.notFound("被删除标签没找到");
        }
        if ("approved".equals(request.getStatus())) {
            tag.setIsApproved(true);
            tag.setApprovalStatus("approved");
            tag.setApprovedBy(userId);
            tag.setApprovedAt(OffsetDateTime.now());
            tag.setApprovalNote(request.getNote());
        } else if ("rejected".equals(request.getStatus())) {
            tag.setIsApproved(false);
            tag.setApprovalStatus("rejected");
            tag.setApprovedBy(userId);
            tag.setApprovedAt(OffsetDateTime.now());
            tag.setApprovalNote(request.getNote());
        } else {
            throw BusinessException.paramError("无效的审核状态");
        }

        tagsMapper.updateById(tag);

        log.info("管理员用户 {} 审核了标签: {}，状态: {}", userId, tag.getName(), request.getStatus());
    }

    /**
     * 获取待审核标签数量
     *
     * @return 待审核标签
     */
    public List<AdminTagPendingResponse> adminGetPendingTags() {
        return tagsMapper.getPendingTags();
    }

    /**
     * 获取未使用的标签列表
     *
     * @param unusedDays 未使用的天数阈值，如果为null则获取所有未使用标签
     * @return 未使用标签列表
     */
    public List<AdminTagUnusedResponse> adminGetUnusedTags(Integer unusedDays) {
        return tagsMapper.getUnusedTags(unusedDays);
    }

    /**
     * 清理未使用的标签
     *
     * @param unusedDays 未使用的天数阈值，默认30天
     * @param userId     操作用户ID
     * @return 清理结果
     */
    public AdminTagCleanupResponse adminCleanupUnusedTags(Integer unusedDays, Long userId) {
        if (unusedDays == null) {
            unusedDays = 30; // 默认30天
        }

        // 先获取将要被清理的标签数量
        List<AdminTagUnusedResponse> unusedTags = tagsMapper.getUnusedTags(unusedDays);
        int totalUnusedCount = unusedTags.size();

        if (totalUnusedCount == 0) {
            return new AdminTagCleanupResponse(0, 0, "没有找到需要清理的未使用标签");
        }

        // 执行清理
        int cleanedCount = tagsMapper.deleteUnusedTags(unusedDays);

        String message = String.format("已清理%d个超过%d天未使用的标签", cleanedCount, unusedDays);

        log.info("管理员用户 {} 清理了 {} 个未使用标签，条件：超过{}天未使用", userId, cleanedCount, unusedDays);

        return new AdminTagCleanupResponse(cleanedCount, totalUnusedCount, message);
    }

    /**
     * 合并标签
     *
     * @param request 合并请求参数
     * @param userId  操作用户ID
     */
    public void adminMergeTags(AdminTagMergeRequest request, Long userId) {
        Long sourceTagId = request.getSourceTagId();
        Long targetTagId = request.getTargetTagId();

        // 验证标签存在性
        Tags sourceTag = tagsMapper.selectById(sourceTagId);
        if (sourceTag == null) {
            throw BusinessException.notFound("源标签不存在");
        }

        Tags targetTag = tagsMapper.selectById(targetTagId);
        if (targetTag == null) {
            throw BusinessException.notFound("目标标签不存在");
        }

        if (sourceTagId.equals(targetTagId)) {
            throw BusinessException.paramError("源标签和目标标签不能是同一个");
        }

        log.info("开始合并标签：源标签[{}:{}] -> 目标标签[{}:{}]", sourceTagId, sourceTag.getName(), targetTagId,
                targetTag.getName());

        // 1. 将源标签的所有文章关联转移到目标标签
        int transferredPosts = tagsMapper.transferPostsFromSourceToTarget(sourceTagId, targetTagId);

        // 2. 删除源标签的所有文章关联
        tagsMapper.deletePostTagsByTagId(sourceTagId);

        // 3. 更新目标标签的文章计数和最后使用时间
        tagsMapper.updateTagPostCount(targetTagId);

        // 4. 删除源标签
        tagsMapper.deleteById(sourceTagId);

        log.info("管理员用户 {} 成功合并标签：{} -> {}，转移了{}篇文章",
                userId, sourceTag.getName(), targetTag.getName(), transferredPosts);
    }
}
