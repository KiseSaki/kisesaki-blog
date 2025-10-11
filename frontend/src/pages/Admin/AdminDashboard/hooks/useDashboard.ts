/**
 * 仪表盘数据获取 Hook
 * 封装仪表盘相关数据获取逻辑
 */

import {
  getDashboardStatsApi,
  getPopularPostsApi,
  getRecentActivitiesApi,
} from '@/api';
import type {
  DashboardStatsResponse,
  PopularPostResponse,
  RecentActivityResponse,
} from '@/types';
import { useCallback, useState } from 'react';

export const useDashboard = () => {
  // 统计概览数据
  const [stats, setStats] = useState<DashboardStatsResponse | null>(null);
  const [isFetchingStats, setIsFetchingStats] = useState(false);

  const fetchStats = useCallback(async () => {
    setIsFetchingStats(true);
    try {
      const res = await getDashboardStatsApi();
      setStats(res);
      return res;
    } catch (error) {
      console.error('获取统计数据失败:', error);
      return null;
    } finally {
      setIsFetchingStats(false);
    }
  }, []);

  // 热门文章数据
  const [popularPosts, setPopularPosts] = useState<PopularPostResponse[]>([]);
  const [isFetchingPopularPosts, setIsFetchingPopularPosts] = useState(false);

  const fetchPopularPosts = useCallback(async (limit: number = 10) => {
    setIsFetchingPopularPosts(true);
    try {
      const res = await getPopularPostsApi(limit);
      setPopularPosts(res);
      return res;
    } catch (error) {
      console.error('获取热门文章失败:', error);
      return [];
    } finally {
      setIsFetchingPopularPosts(false);
    }
  }, []);

  // 最近活动数据
  const [recentActivities, setRecentActivities] = useState<
    RecentActivityResponse[]
  >([]);
  const [isFetchingActivities, setIsFetchingActivities] = useState(false);

  const fetchRecentActivities = useCallback(async (limit: number = 10) => {
    setIsFetchingActivities(true);
    try {
      const res = await getRecentActivitiesApi(limit);
      setRecentActivities(res);
      return res;
    } catch (error) {
      console.error('获取最近活动失败:', error);
      return [];
    } finally {
      setIsFetchingActivities(false);
    }
  }, []);

  return {
    // 统计概览
    stats,
    isFetchingStats,
    fetchStats,

    // 热门文章
    popularPosts,
    isFetchingPopularPosts,
    fetchPopularPosts,

    // 最近活动
    recentActivities,
    isFetchingActivities,
    fetchRecentActivities,
  };
};
