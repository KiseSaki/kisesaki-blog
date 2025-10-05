/**
 * 文章相关的自定义 Hook
 * 包含文章数据获取、筛选等逻辑
 */

import {
  getFeaturedPostsApi,
  getPublishedPostBySlugApi,
  getRecentPostsApi,
} from '@/api';
import type {
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
      const res = await getFeaturedPostsApi({ currentPage: 1, pageSize: 5 });
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
      const res = await getRecentPostsApi({ currentPage: 1, pageSize: 6 });
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
  };
};
