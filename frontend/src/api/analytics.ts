/**
 * 分析统计相关 API 接口函数
 */
import { httpClient } from '@/lib';
import type {
  EventRecordRequest,
  PostViewStatsResponse,
  ViewRecordRequest,
} from '@/types/analytics';

/**
 * 记录页面浏览
 * @param request 浏览记录请求
 * @returns 操作结果
 */
export const recordPageViewApi = (request: ViewRecordRequest) =>
  httpClient.post<void>('/api/analytics/view', request);

/**
 * 记录自定义事件
 * @param request 事件记录请求
 * @returns 操作结果
 */
export const recordCustomEventApi = (request: EventRecordRequest) =>
  httpClient.post<void>('/api/analytics/event', request);

/**
 * 获取文章浏览统计
 * @param postId 文章ID
 * @returns 浏览统计数据
 */
export const getPostViewStatsApi = (postId: number) =>
  httpClient.get<PostViewStatsResponse>(`/api/posts/${postId}/views`);
