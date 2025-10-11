/**
 * 管理员相关 API 接口函数
 * 包含管理员操作、数据统计等管理相关的 API 调用
 */
import { httpClient } from '@/lib';

// =================== 管理员文章批量操作接口 ===================

/**
 * 管理员批量删除文章
 * @param ids 文章ID列表
 * @returns 操作结果
 */
export const adminBatchDeletePostsApi = (ids: number[]) =>
  httpClient.post<void>('/admin/posts/batch-delete', { ids });

/**
 * 管理员批量更新文章状态
 * @param ids 文章ID列表
 * @param status 状态
 * @returns 操作结果
 */
export const adminBatchUpdateStatusApi = (ids: number[], status: string) =>
  httpClient.put<void>('/admin/posts/batch-status', { ids, status });

/**
 * 管理员批量设置精选
 * @param ids 文章ID列表
 * @param isFeatured 是否精选
 * @returns 操作结果
 */
export const adminBatchSetFeaturedApi = (ids: number[], isFeatured: boolean) =>
  httpClient.put<void>('/admin/posts/batch-featured', { ids, isFeatured });

/**
 * 管理员批量设置置顶
 * @param ids 文章ID列表
 * @param isTop 是否置顶
 * @returns 操作结果
 */
export const adminBatchSetTopApi = (ids: number[], isTop: boolean) =>
  httpClient.put<void>('/admin/posts/batch-top', { ids, isTop });

/**
 * 管理员批量转移作者
 * @param ids 文章ID列表
 * @param authorId 新作者ID
 * @returns 操作结果
 */
export const adminBatchTransferAuthorApi = (ids: number[], authorId: number) =>
  httpClient.put<void>('/admin/posts/batch-author', { ids, authorId });
