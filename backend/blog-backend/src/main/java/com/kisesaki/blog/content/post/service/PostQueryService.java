package com.kisesaki.blog.content.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        // 手动获取总数以避免MyBatis Plus自动count查询的DISTINCT问题
        long totalCount = postsMapper.countPublishedPosts(params);

        // 如果总数为0，直接返回空结果
        if (totalCount == 0) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        // 查询分页数据（禁用自动count查询）
        Page<PublishedPostListResponse> page = new Page<>(
                params.getPageable().getCurrentPage(),
                params.getPageable().getPageSize(),
                false // 禁用自动count查询
        );
        Page<PublishedPostListResponse> result = postsMapper.selectPublishedPostsPage(page, params);

        // 手动设置总数
        result.setTotal(totalCount);

        return PageResponse.of(result);
    }
}
