package com.kisesaki.blog.content.post.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kisesaki.blog.content.post.dto.PostQuery.PublishedPostDetailResponse;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 文章推荐服务
 * 负责处理文章的相关推荐算法和上下文导航
 *
 * @author KiseSaki
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostRecommendationService {

    private final PostsMapper postsMapper;

    /**
     * 获取文章的上一篇
     *
     * @param publishedAt   当前文章发布时间
     * @param currentPostId 当前文章ID
     * @return 上一篇文章信息
     */
    public PublishedPostDetailResponse.AdjacentPost getPrevPost(java.time.OffsetDateTime publishedAt,
            Long currentPostId) {
        try {
            return postsMapper.getPrevPost(publishedAt, currentPostId);
        } catch (Exception e) {
            log.error("获取上一篇文章失败，文章ID: {}", currentPostId, e);
            return null;
        }
    }

    /**
     * 获取文章的下一篇
     *
     * @param publishedAt   当前文章发布时间
     * @param currentPostId 当前文章ID
     * @return 下一篇文章信息
     */
    public PublishedPostDetailResponse.AdjacentPost getNextPost(java.time.OffsetDateTime publishedAt,
            Long currentPostId) {
        try {
            return postsMapper.getNextPost(publishedAt, currentPostId);
        } catch (Exception e) {
            log.error("获取下一篇文章失败，文章ID: {}", currentPostId, e);
            return null;
        }
    }

    /**
     * 获取相关推荐文章
     * 推荐算法说明：
     * 1. 相同分类的文章 +3 分
     * 2. 每个共同标签 +1 分
     * 3. 浏览量权重（归一化到 0-1 分）
     * 4. 按综合得分、浏览量、发布时间排序
     *
     * @param categoryId    当前文章分类ID
     * @param tagIds        当前文章标签ID列表
     * @param currentPostId 当前文章ID
     * @param limit         推荐数量限制
     * @return 相关推荐文章列表
     */
    public List<PublishedPostDetailResponse.RelatedPost> getRelatedPosts(Long categoryId,
            List<Long> tagIds,
            Long currentPostId,
            Integer limit) {
        try {
            // 如果没有分类且没有标签，返回热门文章作为推荐
            if (categoryId == null && (tagIds == null || tagIds.isEmpty())) {
                log.info("文章 {} 没有分类和标签，返回热门文章推荐", currentPostId);
                return getPopularPosts(currentPostId, limit);
            }

            // 确保 tagIds 不为 null
            if (tagIds == null) {
                tagIds = List.of();
            }

            List<PublishedPostDetailResponse.RelatedPost> relatedPosts = postsMapper.getRelatedPosts(categoryId, tagIds,
                    currentPostId, limit);

            log.info("为文章 {} 推荐了 {} 篇相关文章", currentPostId, relatedPosts.size());
            return relatedPosts;

        } catch (Exception e) {
            log.error("获取相关推荐文章失败，文章ID: {}", currentPostId, e);
            // 降级策略：返回热门文章
            return getPopularPosts(currentPostId, limit);
        }
    }

    /**
     * 获取文章的自定义元数据
     *
     * @param postId 文章ID
     * @return 元数据Map，key为meta_key，value为meta_value
     */
    public Map<String, String> getPostMeta(Long postId) {
        try {
            Map<String, String> metaMap = postsMapper.getPostMeta(postId);
            log.debug("获取文章 {} 的元数据: {}", postId, metaMap.size());
            return metaMap;
        } catch (Exception e) {
            log.error("获取文章元数据失败，文章ID: {}", postId, e);
            return Map.of(); // 返回空的 Map
        }
    }

    /**
     * 降级策略：获取热门文章作为推荐
     *
     * @param currentPostId 当前文章ID（排除）
     * @param limit         数量限制
     * @return 热门文章列表
     */
    private List<PublishedPostDetailResponse.RelatedPost> getPopularPosts(Long currentPostId, Integer limit) {
        try {
            List<PublishedPostDetailResponse.RelatedPost> popularPosts = postsMapper.getPopularPosts(currentPostId,
                    limit);
            log.info("使用降级策略，为文章 {} 返回 {} 篇热门文章推荐", currentPostId, popularPosts.size());
            return popularPosts;
        } catch (Exception e) {
            log.error("获取热门文章推荐失败", e);
            return List.of();
        }
    }

    /**
     * 批量获取文章导航和推荐信息
     * 可用于文章列表页面的预加载
     *
     * @param postIds 文章ID列表
     * @return 文章ID到推荐信息的映射
     */
    public Map<Long, List<PublishedPostDetailResponse.RelatedPost>> batchGetRelatedPosts(List<Long> postIds) {
        return postIds.stream()
                .collect(Collectors.toMap(
                        postId -> postId,
                        postId -> {
                            // 这里需要先获取文章的基本信息才能进行推荐
                            // 暂时返回空列表，实际使用时需要完善
                            return List.of();
                        }));
    }
}
