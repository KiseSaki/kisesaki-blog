package com.kisesaki.blog.content.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kisesaki.blog.content.post.dto.PostQuery.GetMyPostsListParams;
import com.kisesaki.blog.content.post.dto.PostQuery.MyPostsListResponse;
import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostDetailResponse;
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

        /**
         * 根据文章ID获取已发布文章详情
         *
         * @param postId 文章ID
         * @return 文章详情
         */
        PublishedPostDetailResponse getPublishedPostDetailById(@Param("postId") Long postId);

        /**
         * 根据文章slug获取以发布文章详情
         *
         * @param slug 文章slug
         * @return 文章详情
         */
        PublishedPostDetailResponse getPublishedPostDetailBySlug(@Param("slug") String slug);

        /**
         * 获取上一篇文章（按发布时间排序）
         *
         * @param publishedAt   当前文章发布时间
         * @param currentPostId 当前文章ID（排除自身）
         * @return 上一篇文章信息
         */
        PublishedPostDetailResponse.AdjacentPost getPrevPost(@Param("publishedAt") java.time.OffsetDateTime publishedAt,
                        @Param("currentPostId") Long currentPostId);

        /**
         * 获取下一篇文章（按发布时间排序）
         *
         * @param publishedAt   当前文章发布时间
         * @param currentPostId 当前文章ID（排除自身）
         * @return 下一篇文章信息
         */
        PublishedPostDetailResponse.AdjacentPost getNextPost(@Param("publishedAt") java.time.OffsetDateTime publishedAt,
                        @Param("currentPostId") Long currentPostId);

        /**
         * 获取相关推荐文章
         *
         * @param categoryId    当前文章分类ID
         * @param tagIds        当前文章标签ID列表
         * @param currentPostId 当前文章ID（排除自身）
         * @param limit         推荐数量限制
         * @return 相关推荐文章列表
         */
        java.util.List<PublishedPostDetailResponse.RelatedPost> getRelatedPosts(@Param("categoryId") Long categoryId,
                        @Param("tagIds") java.util.List<Long> tagIds,
                        @Param("currentPostId") Long currentPostId,
                        @Param("limit") Integer limit);

        /**
         * 获取文章的自定义元数据
         *
         * @param postId 文章ID
         * @return 元数据Map
         */
        java.util.Map<String, String> getPostMeta(@Param("postId") Long postId);

        /**
         * 获取热门文章（用于降级推荐）
         *
         * @param currentPostId 当前文章ID（排除自身）
         * @param limit         数量限制
         * @return 热门文章列表
         */
        java.util.List<PublishedPostDetailResponse.RelatedPost> getPopularPosts(
                        @Param("currentPostId") Long currentPostId,
                        @Param("limit") Integer limit);

        /**
         * 分页查询我的文章列表（包括草稿）
         *
         * @param page   分页对象，MyBatis Plus 会自动处理分页和计数
         * @param params 查询参数
         * @param userId 当前用户ID
         * @return 分页结果
         */
        Page<MyPostsListResponse> selectMyPostsPage(
                        Page<MyPostsListResponse> page,
                        @Param("params") GetMyPostsListParams params,
                        @Param("userId") Long userId);

        /**
         * 计算我的文章数量
         *
         * @param params 查询参数
         * @param userId 当前用户ID
         * @return 文章总数
         */
        long countMyPosts(@Param("params") GetMyPostsListParams params, @Param("userId") Long userId);
}
