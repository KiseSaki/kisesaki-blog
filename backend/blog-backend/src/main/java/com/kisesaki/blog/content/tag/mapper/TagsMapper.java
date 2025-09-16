package com.kisesaki.blog.content.tag.mapper;

import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagPendingResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagDetailResponse;
import com.kisesaki.blog.content.tag.entity.Tags;

import java.util.List;

@Mapper
public interface TagsMapper extends BaseMapper<Tags> {

    /**
     * 根据ID查询标签详情
     * 
     * @param slug 标签Slug
     * @return 标签详情
     */
    TagDetailResponse getTagDetailById(Long id);

    /**
     * 根据Slug查询标签详情
     * 
     * @param slug 标签Slug
     * @return 标签详情
     */
    TagDetailResponse getTagDetailBySlug(String slug);

    /**
     * 获取管理员标签列表
     */
    Page<AdminTagListResponse> getAdminTagList(
            @Param("page") Page<AdminTagListResponse> page,
            @Param("params") AdminTagListParams params);

    /**
     * 获取所有待审核标签
     */
    List<AdminTagPendingResponse> getPendingTags();
}
