package com.kisesaki.blog.content.post;

import com.kisesaki.blog.common.dto.ApiResponse;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.dto.ResultUtils;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.service.PostQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章控制器
 *
 * @author KiseSaki
 */
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "文章", description = "文章相关接口")
public class PostController {

    private final PostQueryService postQueryService;

    @GetMapping("")
    public ApiResponse<PageResponse<PublishedPostListResponse>> selectPublishedPosts(
            @Valid PublishedPostListParams params) {
        try{
            PageResponse<PublishedPostListResponse> pageResponse = postQueryService.selectPublishedPosts(params);
            return ResultUtils.success("获取文章列表成功", pageResponse);
        }catch (Exception e){
            log.error("获取已发布文章列表失败", e);
            return ResultUtils.error("获取已发布文章列表失败: " + e.getMessage());
        }
    }
}
