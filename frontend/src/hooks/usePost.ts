/**
 * 文章相关的自定义 Hook
 * 包含文章数据获取、筛选等逻辑
 */

import { getFeaturedPostsApi, getRecentPostsApi } from '@/api';
import type { PageResponse, PublishedPostListResponse } from '@/types';
import { useCallback, useState } from 'react';

export const usePost = () => {
  // 精选文章
  const [featuredPosts, setFeaturedPosts] =
    useState<PageResponse<PublishedPostListResponse> | null>(null);
  const [isFetchingFeatured, setIsFetchingFeatured] = useState(false);

  const fetchFeaturedPosts = useCallback(async () => {
    if (isFetchingFeatured) return null;

    setIsFetchingFeatured(true);
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
  }, [isFetchingFeatured]);

  // 最新文章
  const [recentPosts, setRecentPosts] =
    useState<PageResponse<PublishedPostListResponse> | null>(null);
  const [isFetchingRecent, setIsFetchingRecent] = useState(false);

  const fetchRecentPosts = useCallback(async () => {
    if (isFetchingRecent) return null;

    setIsFetchingRecent(true);
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
  }, [isFetchingRecent]);

  return {
    // 精选文章
    featuredPosts,
    isFetchingFeatured,
    fetchFeaturedPosts,

    // 最新文章
    recentPosts,
    isFetchingRecent,
    fetchRecentPosts,
  };
};
