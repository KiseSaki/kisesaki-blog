package com.kisesaki.blog.content.tag.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.mapper.TagsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
}
