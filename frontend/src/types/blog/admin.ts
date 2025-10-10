/**
 * 管理员相关类型定义
 */

import type { PageableParams } from '../api';
import type { TagDetailResponse } from './tag';

// =================== 管理员标签相关类型 ===================

/**
 * 管理员标签列表响应
 * 对应后端：AdminTagListResponse
 */
export interface AdminTagListResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  postCount: number;
  createdAt: string;
  updatedAt: string;
  createdBy: number;
  createdByUsername: string;
  isApproved: boolean;
  approvalStatus: 'pending' | 'approved' | 'rejected';
  approvedBy: number | null;
  approvedByUsername: string | null;
  approvedAt: string | null;
  approvalNote: string | null;
  lastUsedAt: string | null;
  popularityScore: number | null;
}

/**
 * 管理员创建标签请求
 * 对应后端：AdminTagCreateRequest
 */
export interface AdminTagCreateRequest {
  name: string;
  slug?: string;
  description?: string;
  color?: string;
  isApproved?: boolean;
  approvalNote?: string;
}

/**
 * 管理员更新标签请求
 * 对应后端：AdminTagUpdateRequest
 */
export interface AdminTagUpdateRequest {
  name?: string;
  slug?: string;
  description?: string;
  color?: string;
  isApproved?: boolean;
  approvalNote?: string;
}

/**
 * 管理员标签审核请求
 * 对应后端：AdminTagApprovalRequest
 */
export interface AdminTagApprovalRequest {
  status: 'approved' | 'rejected';
  note?: string;
}

/**
 * 管理员标签列表查询参数
 * 对应后端：AdminTagListParams
 */
export interface AdminTagListParams {
  pageable?: PageableParams;
  name?: string;
  approvalStatus?: 'pending' | 'approved' | 'rejected';
  createdBy?: number;
  unusedOnly?: boolean;
  minPostCount?: number;
  maxPostCount?: number;
}

/**
 * 待审核标签响应
 * 对应后端：AdminTagPendingResponse
 */
export interface AdminTagPendingResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  createdAt: string;
  createdBy: number;
  createdByUsername: string;
}

/**
 * 未使用标签响应
 * 对应后端：AdminTagUnusedResponse
 */
export interface AdminTagUnusedResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  createdAt: string;
  createdBy: number;
  createdByUsername: string;
  unusedDays: number;
  isApproved: boolean;
}

/**
 * 标签合并请求
 * 对应后端：AdminTagMergeRequest
 */
export interface AdminTagMergeRequest {
  sourceTagId: number;
  targetTagId: number;
}

/**
 * 标签清理结果响应
 * 对应后端：AdminTagCleanupResponse
 */
export interface AdminTagCleanupResponse {
  cleanedCount: number;
  totalUnusedCount: number;
  message: string;
}

export type { TagDetailResponse };
