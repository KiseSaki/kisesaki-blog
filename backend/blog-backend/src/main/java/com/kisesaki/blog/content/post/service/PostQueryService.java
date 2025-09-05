package com.kisesaki.blog.content.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.mapper.PostsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryService {

    private final PostsMapper postsMapper;

    /**
     * 按条件获取已发布文章列表
     *
     * @param params 查询参数
     * @return 文章列表
     */
    public PageResponse<PublishedPostListResponse> selectPublishedPosts(PublishedPostListParams params) {
        Page<PublishedPostListResponse> page = new Page<>(params.getPageable().getCurrentPage(), params.getPageable().getPageSize());
        Page<PublishedPostListResponse> result = postsMapper.selectPublishedPostsPage(page, params);
        return PageResponse.of(result);
    }
}
