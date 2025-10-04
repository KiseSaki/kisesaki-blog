/**
 * 博客相关的自定义 Hook
 * 包含文章数据获取、分类筛选等博客相关逻辑
 */

import { getFeaturedPostsApi } from '@/api';
import type { PageResponse, PublishedPostListResponse } from '@/types';
import { useCallback, useState } from 'react';

export const useBlog = () => {
  // 获取精选贴
  const [featuredPosts, setFeaturedPosts] =
    useState<PageResponse<PublishedPostListResponse> | null>(null);
  const fetchFeaturedPosts = useCallback(async () => {
    try {
      const res = await getFeaturedPostsApi({ currentPage: 1, pageSize: 5 });
      setFeaturedPosts(res);
      return res;
    } catch (error) {
      console.error('获取精选文章失败:', error);
      return null;
    }
  }, []);

  return {
    featuredPosts,

    fetchFeaturedPosts,
  };
};
