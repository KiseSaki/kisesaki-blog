package com.kisesaki.blog.content.post.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kisesaki.blog.common.dto.PageResponse;
import com.kisesaki.blog.common.markdown.MarkdownService;
import com.kisesaki.blog.common.util.PageQueryUtils;
import com.kisesaki.blog.content.post.dto.RevisionInfo;
import com.kisesaki.blog.content.post.dto.PostRevision.PostRevisionContentResponse;
import com.kisesaki.blog.content.post.dto.PostRevision.PostRevisionListParams;
import com.kisesaki.blog.content.post.entity.PostRevisions;
import com.kisesaki.blog.content.post.entity.Posts;
import com.kisesaki.blog.content.post.mapper.PostRevisionsMapper;
import com.kisesaki.blog.content.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostRevisionService {

    private final PostRevisionsMapper postRevisionsMapper;
    private final PostsMapper postsMapper;
    private final MarkdownService markdownService;

    /**
     * 获取指定文章的版本列表
     * 
     * @param postId 文章ID
     * @param params 分页参数
     * @return 文章版本列表
     */
    public PageResponse<RevisionInfo> getPostRevisionListByPostId(Long postId, Long userId,
            PostRevisionListParams params) {
        if (postId == null) {
            return PageResponse.of(List.of(), 0L, params.getPageable());
        }

        LambdaQueryWrapper<PostRevisions> queryWrapper = new LambdaQueryWrapper<PostRevisions>()
                .eq(PostRevisions::getPostId, postId)
                .eq(PostRevisions::getCreatedBy, userId);

        // 应用时间范围条件
        PageQueryUtils.applyTimeRangeConditions(queryWrapper, params.getPageable(), PostRevisions::getCreatedAt);

        // 应用排序规则
        PageQueryUtils.createSortBuilder(queryWrapper, params.getPageable())
                .defaultSort(PostRevisions::getVersion, true) // 默认按版本号倒序
                .addSortField("id", PostRevisions::getId)
                .addSortField("version", PostRevisions::getVersion)
                .addSortField("createdAt", PostRevisions::getCreatedAt)
                .apply();

        // 执行分页查询
        return PageQueryUtils.executePageQuery(
                postRevisionsMapper,
                queryWrapper,
                params.getPageable(),
                this::convertToRevisionInfo);
    }

    /**
     * 获取指定文章的指定版本内容
     * 
     * @param postId     文章ID
     * @param revisionId 版本ID
     * @return 文章版本内容
     */
    public PostRevisionContentResponse getPostRevisionContent(Long postId, Long revisionId) {
        LambdaQueryWrapper<PostRevisions> queryWrapper = new LambdaQueryWrapper<PostRevisions>()
                .eq(PostRevisions::getPostId, postId)
                .eq(PostRevisions::getId, revisionId);
        PostRevisions revision = postRevisionsMapper.selectOne(queryWrapper);
        if (revision == null) {
            return null;
        }

        PostRevisionContentResponse response = new PostRevisionContentResponse();
        response.setId(revision.getId());
        response.setVersion(revision.getVersion());
        response.setTitle(revision.getTitle());
        response.setSummary(revision.getSummary());
        response.setCreatedAt(revision.getCreatedAt());
        response.setContent(revision.getContent());

        return response;
    }

    /**
     * 将文章恢复到指定的历史版本（回滚）。
     * 
     * @param postId     要恢复的文章 ID
     * @param revisionId 目标版本 ID
     * @param operatorId 操作人 ID（用于记录回滚作者）
     */
    @Transactional(rollbackFor = Exception.class)
    public void restorePostToRevision(Long postId, Long revisionId, Long operatorId) {
        // 获取目标版本
        LambdaQueryWrapper<PostRevisions> queryWrapper = new LambdaQueryWrapper<PostRevisions>()
                .eq(PostRevisions::getPostId, postId)
                .eq(PostRevisions::getId, revisionId);
        PostRevisions postRevisions = postRevisionsMapper.selectOne(queryWrapper);
        if (postRevisions == null) {
            throw new IllegalArgumentException("指定的版本不存在");
        }

        if (postRevisions.getContent().isEmpty()) {
            throw new IllegalArgumentException("指定的版本的内容为空");
        }

        // 获取指定文章
        Posts post = postsMapper.selectById(postId);
        if (post == null) {
            throw new IllegalArgumentException("指定的文章不存在");
        }

        // 生成HTML
        String newHtml = markdownService.convertToHtml(postRevisions.getContent());
        // 更新文章内容
        post.setContent(postRevisions.getContent());
        post.setHtmlContent(newHtml);
        post.setTitle(postRevisions.getTitle());
        post.setExcerpt(postRevisions.getSummary());
        post.setLastModifiedAt(OffsetDateTime.now());
        post.setUpdatedAt(OffsetDateTime.now());
        post.setWordCount(markdownService.countWords(postRevisions.getContent()));
        post.setReadingTime(markdownService.estimateReadingTime(postRevisions.getContent()));
        postsMapper.updateById(post);

        PostRevisions newRevision = new PostRevisions();
        newRevision.setPostId(postId);
        Integer maxVersion = postRevisionsMapper.selectList(
                new LambdaQueryWrapper<PostRevisions>()
                        .eq(PostRevisions::getPostId, postId))
                .stream().map(PostRevisions::getVersion).max(Integer::compareTo).orElse(0);
        newRevision.setVersion(maxVersion + 1);
        newRevision.setTitle(postRevisions.getTitle());
        newRevision.setContent(postRevisions.getContent());
        newRevision.setSummary(postRevisions.getSummary());
        newRevision.setCreatedBy(operatorId);
        newRevision.setCreatedAt(OffsetDateTime.now());
        postRevisionsMapper.insert(newRevision);

        // TODO 添加缓存后，这里得清除缓存
    }

    /**
     * 删除指定文章的指定版本
     * 
     * @param postId     文章ID
     * @param revisionId 版本ID
     */
    public void deleteRevisionByPostId(Long postId, Long revisionId, Long userId) {
        // 先验证版本是否存在
        LambdaQueryWrapper<PostRevisions> queryWrapper = new LambdaQueryWrapper<PostRevisions>()
                .eq(PostRevisions::getPostId, postId)
                .eq(PostRevisions::getId, revisionId)
                .eq(PostRevisions::getCreatedBy, userId);
        PostRevisions revision = postRevisionsMapper.selectOne(queryWrapper);
        if (revision == null) {
            throw new IllegalArgumentException("指定的版本不存在");
        }
        postRevisionsMapper.deleteById(revision.getId());
    }

    /**
     * 转换实体为版本信息响应对象
     */
    private RevisionInfo convertToRevisionInfo(PostRevisions revision) {
        RevisionInfo info = new RevisionInfo();
        info.setId(revision.getId());
        info.setVersion(revision.getVersion());
        info.setTitle(revision.getTitle());
        info.setSummary(revision.getSummary());
        info.setCreatedAt(revision.getCreatedAt());
        return info;
    }
}
