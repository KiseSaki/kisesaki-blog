/**
 * 分析统计相关 API 接口函数
 */
import { httpClient } from '@/lib';
import type {
  DashboardStatsResponse,
  EventRecordRequest,
  PopularPostResponse,
  PostViewStatsResponse,
  RecentActivityResponse,
  ViewRecordRequest,
} from '@/types/analytics';

/**
 * 记录页面浏览
 * @param request 浏览记录请求
 * @returns 操作结果
 */
export const recordPageViewApi = (request: ViewRecordRequest) =>
  httpClient.post<void>('/analytics/view', request);

/**
 * 记录自定义事件
 * @param request 事件记录请求
 * @returns 操作结果
 */
export const recordCustomEventApi = (request: EventRecordRequest) =>
  httpClient.post<void>('/analytics/event', request);

/**
 * 获取文章浏览统计
 * @param postId 文章ID
 * @returns 浏览统计数据
 */
export const getPostViewStatsApi = (postId: number) =>
  httpClient.get<PostViewStatsResponse>(`/posts/${postId}/views`);

/**
 * 获取仪表盘统计概览
 * @returns 统计概览数据
 */
export const getDashboardStatsApi = () =>
  httpClient.get<DashboardStatsResponse>('/analytics/dashboard/stats');

/**
 * 获取热门文章列表
 * @param limit 返回数量限制
 * @returns 热门文章列表
 */
export const getPopularPostsApi = (limit: number = 10) =>
  httpClient.get<PopularPostResponse[]>(
    `/analytics/dashboard/popular-posts`,
    { params: { limit } }
  );

/**
 * 获取最近活动列表
 * @param limit 返回数量限制
 * @returns 最近活动列表
 */
export const getRecentActivitiesApi = (limit: number = 10) =>
  httpClient.get<RecentActivityResponse[]>(
    `/analytics/dashboard/recent-activities`,
    { params: { limit } }
  );
