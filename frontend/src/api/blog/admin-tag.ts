/**
 * 管理员标签相关 API 接口函数
 */
import { httpClient } from '@/lib';
import type { PageResponse, PageableParams } from '@/types';
import type {
  AdminTagApprovalRequest,
  AdminTagCleanupResponse,
  AdminTagCreateRequest,
  AdminTagListParams,
  AdminTagListResponse,
  AdminTagMergeRequest,
  AdminTagPendingResponse,
  AdminTagUnusedResponse,
  AdminTagUpdateRequest,
  TagDetailResponse,
} from '@/types/blog';

// =================== 管理员标签列表与详情接口 ===================

/**
 * 获取管理员标签列表
 * @param params 查询参数
 * @returns 标签列表
 */
export const adminGetTagListApi = (params?: AdminTagListParams) =>
  httpClient.get<PageResponse<AdminTagListResponse>>('/admin/tags', {
    params,
  });

/**
 * 获取待审核标签列表
 * @param params 查询参数
 * @returns 待审核标签列表
 */
export const adminGetPendingTagsApi = (params?: PageableParams) =>
  httpClient.get<PageResponse<AdminTagPendingResponse>>('/admin/tags/pending', {
    params,
  });

/**
 * 获取未使用标签列表
 * @param days 未使用天数阈值
 * @returns 未使用标签列表
 */
export const adminGetUnusedTagsApi = (days?: number) =>
  httpClient.get<AdminTagUnusedResponse[]>('/admin/tags/unused', {
    params: { days },
  });

// =================== 管理员标签创建与更新接口 ===================

/**
 * 管理员创建标签
 * @param request 创建标签请求
 * @returns 创建的标签信息
 */
export const adminCreateTagApi = (request: AdminTagCreateRequest) =>
  httpClient.post<TagDetailResponse>('/admin/tags', request);

/**
 * 管理员更新标签
 * @param id 标签ID
 * @param request 更新标签请求
 * @returns 更新后的标签信息
 */
export const adminUpdateTagApi = (id: number, request: AdminTagUpdateRequest) =>
  httpClient.put<TagDetailResponse>(`/admin/tags/${id}`, request);

/**
 * 管理员删除标签
 * @param id 标签ID
 * @returns 删除结果
 */
export const adminDeleteTagApi = (id: number) =>
  httpClient.delete<void>(`/admin/tags/${id}`);

// =================== 管理员标签审核与管理接口 ===================

/**
 * 管理员审核标签
 * @param id 标签ID
 * @param request 审核请求
 * @returns 审核结果
 */
export const adminApproveTagApi = (
  id: number,
  request: AdminTagApprovalRequest
) => httpClient.post<TagDetailResponse>(`/admin/tags/${id}/approve`, request);

/**
 * 合并标签
 * @param request 合并请求
 * @returns 合并结果
 */
export const adminMergeTagsApi = (request: AdminTagMergeRequest) =>
  httpClient.post<void>('/admin/tags/merge', request);

/**
 * 清理未使用标签
 * @param days 未使用天数阈值
 * @returns 清理结果
 */
export const adminCleanupTagsApi = (days?: number) =>
  httpClient.delete<AdminTagCleanupResponse>('/admin/tags/cleanup', {
    params: { days },
  });
