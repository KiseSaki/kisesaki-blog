package com.kisesaki.blog.content.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.entity.Posts;

@Mapper
public interface PostsMapper extends BaseMapper<Posts> {

    /**
     * 分页查询已发布文章列表（MyBatis Plus 自动分页）
     *
     * @param page   分页对象，MyBatis Plus 会自动处理分页和计数
     * @param params 查询参数
     * @return 分页结果
     */
    Page<PublishedPostListResponse> selectPublishedPostsPage(
            Page<PublishedPostListResponse> page,
            @Param("params") PublishedPostListParams params);

    /**
     * 计算已发布文章数量（处理DISTINCT和JOIN的情况）
     *
     * @param params 查询参数
     * @return 文章总数
     */
    long countPublishedPosts(@Param("params") PublishedPostListParams params);
}
