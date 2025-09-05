package com.kisesaki.blog.content.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostListResponse;
import com.kisesaki.blog.content.post.entity.Posts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 统计已发布的文章数量
     *
     * @param params 查询参数
     * @return 文章数量
     */
    long countPublishedPosts(@Param("params") PublishedPostListParams params);
}
