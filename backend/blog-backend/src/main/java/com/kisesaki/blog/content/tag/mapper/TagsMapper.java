package com.kisesaki.blog.content.tag.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListParams;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagListResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagPendingResponse;
import com.kisesaki.blog.content.tag.dto.AdminCommand.AdminTagUnusedResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagDetailResponse;
import com.kisesaki.blog.content.tag.entity.Tags;

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

    /**
     * 获取未使用的标签列表
     */
    List<AdminTagUnusedResponse> getUnusedTags(@Param("unusedDays") Integer unusedDays);

    /**
     * 删除未使用的标签
     */
    int deleteUnusedTags(@Param("unusedDays") Integer unusedDays);

    /**
     * 将源标签的所有文章转移到目标标签
     */
    int transferPostsFromSourceToTarget(@Param("sourceTagId") Long sourceTagId, @Param("targetTagId") Long targetTagId);

    /**
     * 删除指定标签的所有文章关联
     */
    int deletePostTagsByTagId(@Param("tagId") Long tagId);

    /**
     * 更新目标标签的文章计数
     */
    void updateTagPostCount(@Param("tagId") Long tagId);
}
