/**
 * 文章相关的自定义 Hook
 * 包含文章数据获取、筛选等逻辑
 */

import {
  createCommentApi,
  getFeaturedPostsApi,
  getPostCommentsApi,
  getPublishedPostBySlugApi,
  getRecentPostsApi,
  likeCommentApi,
} from '@/api';
import type {
  CommentListResponse,
  CreateCommentBody,
  PageResponse,
  PublishedPostDetailResponse,
  PublishedPostListResponse,
} from '@/types';
import { useCallback, useState } from 'react';

export const usePost = () => {
  // 精选文章
  const [featuredPosts, setFeaturedPosts] =
    useState<PageResponse<PublishedPostListResponse> | null>(null);
  const [isFetchingFeatured, setIsFetchingFeatured] = useState(false);

  const fetchFeaturedPosts = useCallback(async () => {
    setIsFetchingFeatured(prev => {
      if (prev) return prev;
      return true;
    });

    try {
      const res = await getFeaturedPostsApi({
        pageable: { currentPage: 1, pageSize: 5 },
      });
      setFeaturedPosts(res);
      return res;
    } catch (error) {
      console.error('获取精选文章失败:', error);
      return null;
    } finally {
      setIsFetchingFeatured(false);
    }
  }, []);

  // 最新文章
  const [recentPosts, setRecentPosts] =
    useState<PageResponse<PublishedPostListResponse> | null>(null);
  const [isFetchingRecent, setIsFetchingRecent] = useState(false);

  const fetchRecentPosts = useCallback(async () => {
    setIsFetchingRecent(prev => {
      if (prev) return prev;
      return true;
    });

    try {
      const res = await getRecentPostsApi({
        pageable: { currentPage: 1, pageSize: 6 },
      });
      setRecentPosts(res);
      return res;
    } catch (error) {
      console.error('获取最新文章失败:', error);
      return null;
    } finally {
      setIsFetchingRecent(false);
    }
  }, []);

  // 文章详情（根据 slug）
  const [postDetail, setPostDetail] =
    useState<PublishedPostDetailResponse | null>(null);
  const [isFetchingDetail, setIsFetchingDetail] = useState(false);

  const fetchPostDetailBySlug = useCallback(async (slug: string) => {
    setIsFetchingDetail(prev => {
      if (prev) return prev;
      return true;
    });

    try {
      const res = await getPublishedPostBySlugApi(slug);
      setPostDetail(res);
      return res;
    } catch (error) {
      console.error('获取文章详情失败:', error);
      return null;
    } finally {
      setIsFetchingDetail(false);
    }
  }, []);

  // 获取文章对应评论
  const [postComments, setPostComments] = useState<CommentListResponse[]>([]);
  const [isFetchingComments, setIsFetchingComments] = useState(false);

  const fetchPostComments = useCallback(
    async (
      postId: number,
      currentPage: number = 1,
      pageSize: number = 10,
      sort: 'createdAt,desc' | 'likeCount,desc' = 'likeCount,desc'
    ) => {
      setIsFetchingComments(prev => {
        if (prev) return prev;
        return true;
      });

      try {
        const res = await getPostCommentsApi(postId, {
          pageable: { currentPage, pageSize, sort },
        });
        setPostComments(res.data);
        return res;
      } catch (error) {
        console.error('获取文章评论失败:', error);
        return null;
      } finally {
        setIsFetchingComments(false);
      }
    },
    []
  );

  // 创建评论
  const [isCreatingComment, setIsCreatingComment] = useState(false);

  const createComment = useCallback(
    async (body: CreateCommentBody & { postId: number }) => {
      setIsCreatingComment(true);

      try {
        const commentId = await createCommentApi(body.postId, {
          content: body.content,
          replyToId: body.replyToId,
        });
        return commentId;
      } catch (error) {
        console.error('创建评论失败:', error);
        throw error;
      } finally {
        setIsCreatingComment(false);
      }
    },
    []
  );

  // 点赞评论
  const likeComment = useCallback(async (commentId: number) => {
    await likeCommentApi(commentId);
    console.log(`点赞评论 ID: ${commentId}`);
  }, []);

  return {
    // 精选文章
    featuredPosts,
    isFetchingFeatured,
    fetchFeaturedPosts,

    // 最新文章
    recentPosts,
    isFetchingRecent,
    fetchRecentPosts,

    // 文章详情
    postDetail,
    isFetchingDetail,
    fetchPostDetailBySlug,

    // 文章评论
    postComments,
    isFetchingComments,
    fetchPostComments,
    isCreatingComment,
    createComment,
    likeComment,
  };
};
