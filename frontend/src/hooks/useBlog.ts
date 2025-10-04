/**
 * 博客相关的自定义 Hook
 * 包含文章数据获取、分类筛选等博客相关逻辑
 */

import {
  getFeaturedPostsApi,
  getPopularCategoriesApi,
  getRecentPostsApi,
  getTagCloudApi,
} from '@/api';
import type {
  PageResponse,
  PopularCategoryResponse,
  PublishedPostListResponse,
  TagCloudItem,
} from '@/types';
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

  // 获取最新文章
  const [recentPosts, setRecentPosts] =
    useState<PageResponse<PublishedPostListResponse> | null>(null);
  const fetchRecentPosts = useCallback(async () => {
    try {
      const res = await getRecentPostsApi({ currentPage: 1, pageSize: 6 });
      setRecentPosts(res);
      return res;
    } catch (error) {
      console.error('获取最新文章失败:', error);
      return null;
    }
  }, []);

  // 获取热门分类
  const [popularCategories, setPopularCategories] = useState<
    PopularCategoryResponse[]
  >([]);
  const fetchPopularCategories = useCallback(async () => {
    try {
      const res = await getPopularCategoriesApi({ limit: 10 });
      setPopularCategories(res);
      return res;
    } catch (error) {
      console.error('获取热门分类失败:', error);
      return [];
    }
  }, []);

  // 获取标签云
  const [tagCloud, setTagCloud] = useState<TagCloudItem[]>([]);
  const fetchTagCloud = useCallback(async () => {
    try {
      const res = await getTagCloudApi();
      setTagCloud(res);
      return res;
    } catch (error) {
      console.error('获取标签云失败:', error);
      return [];
    }
  }, []);

  return {
    featuredPosts,
    recentPosts,
    popularCategories,
    tagCloud,

    fetchFeaturedPosts,
    fetchRecentPosts,
    fetchPopularCategories,
    fetchTagCloud,
  };
};
