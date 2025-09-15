package com.kisesaki.blog.content.tag;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.tag.dto.TagCreateRequest;
import com.kisesaki.blog.content.tag.dto.TagQuery.MyTagResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.PopularTagResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagCloudItem;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagDetailResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagListParams;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagListResponse;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagPostsParams;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagSearchItem;
import com.kisesaki.blog.content.tag.dto.TagQuery.TagSearchParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.tag.service.TagQueryService;
import com.kisesaki.blog.content.tag.service.TagCommandService;
import com.kisesaki.blog.content.tag.entity.Tags;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 标签控制器
 * 
 * @author KiseSaki
 */
@RestController
@RequestMapping("/tags")
@Tag(name = "标签", description = "标签相关接口")
@RequiredArgsConstructor
public class TagController {

    private final TagQueryService tagService;
    private final TagCommandService tagCommandService;

    /**
     * 获取标签列表
     */
    @GetMapping("")
    public ApiResponse<PageResponse<TagListResponse>> getTagList(TagListParams params) {
        return ResultUtils.success(tagService.getTagList(params));
    }

    /**
     * 根据ID获取标签详情
     */
    @GetMapping("/{id}")
    public ApiResponse<TagDetailResponse> getTagDetailById(@PathVariable Long id) {
        return ResultUtils.success(tagService.getTagDetailById(id));
    }

    /**
     * 根据Slug获取标签详情
     */
    @GetMapping("/slug/{slug}")
    public ApiResponse<TagDetailResponse> getTagDetailBySlug(@PathVariable String slug) {
        return ResultUtils.success(tagService.getTagDetailBySlug(slug));
    }

    /**
     * 获取热门标签
     */
    @GetMapping("/popular")
    public ApiResponse<List<PopularTagResponse>> getPopularTag() {
        return ResultUtils.success(tagService.getPopularTag());
    }

    /**
     * 获取标签云
     */
    @GetMapping("/cloud")
    public ApiResponse<List<TagCloudItem>> getTagCloud() {
        return ResultUtils.success(tagService.getTagCloud());
    }

    /**
     * 搜索标签（用于创作时的标签建议）
     */
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<TagSearchItem>> searchTags(TagSearchParams params) {
        return ResultUtils.success(tagService.searchTags(params));
    }

    /**
     * 获取指定标签下的文章列表
     */
    @GetMapping("/{tagId}/posts")
    public ApiResponse<PageResponse<PublishedPostListResponse>> getTagPosts(
            @PathVariable Long tagId, 
            TagPostsParams params) {
        return ResultUtils.success(tagService.getTagPosts(tagId, params));
    }

    /**
     * 创建新标签（用户创作文章时）
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('TAG_CREATE')")
    public ApiResponse<Tags> createTag(@Valid @RequestBody TagCreateRequest request) {
        return ResultUtils.success(tagCommandService.createTag(request));
    }

    /**
     * 获取我创建的标签
     */
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<PageResponse<MyTagResponse>> getMyTags(TagListParams params) {
        return ResultUtils.success(tagCommandService.getMyTags(params));
    }
}
