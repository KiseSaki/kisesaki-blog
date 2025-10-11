/**
 * 管理员相关类型定义
 */

import type { PageableParams } from '../api';
import type { CategoryInfo } from './category';
import type { AuthorInfo } from './common';
import type { PostBase, UpdatePostRequest } from './post';
import type { TagDetailResponse } from './tag';

// =================== 管理员文章相关类型 ===================

/**
 * 管理员文章列表查询参数
 * 对应后端：AdminPostListParams
 */
export interface AdminPostListParams {
  pageable?: PageableParams;
  q?: string;
  status?: string;
  authorId?: number;
  categoryId?: number;
  tagId?: number;
  isFeatured?: boolean;
  isTop?: boolean;
  visibility?: string;
}

/**
 * 管理员文章列表响应项
 * 对应后端：AdminPostListResponse
 */
export interface AdminPostListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt?: string;
  coverImageUrl?: string;
  status: string;
  visibility: string;
  isFeatured: boolean;
  isTop: boolean;
  allowComments: boolean;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  readingTime?: number;
  wordCount?: number;
  author: AuthorInfo;
  category?: CategoryInfo;
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
}

/**
 * 管理员创建文章请求
 * 继承自 PostBase，移除 authorId（由后端自动设置）
 * 对应后端：AdminCreatePostRequest
 */
export interface AdminCreatePostRequest extends Omit<PostBase, 'authorId'> {
  authorId?: number; // 管理员可以指定作者ID
  publishNow?: boolean; // 是否立即发布
}

/**
 * 管理员更新文章请求
 * 继承自 UpdatePostRequest，添加 authorId 支持
 * 对应后端：AdminUpdatePostRequest
 */
export interface AdminUpdatePostRequest extends UpdatePostRequest {
  authorId?: number; // 管理员可以修改作者ID
}

/**
 * 文章表单数据（用于前端表单）
 * 复用 PostBase 并添加 publishNow 字段
 */
export interface PostFormData extends PostBase {
  publishNow: boolean; // 是否立即发布（仅创建时）
  createRevision?: boolean; // 是否创建新版本
  revisionNote?: string; // 版本备注

  categoryName?: string; // 分类名称（仅前端显示）

  tags: {
    id: number;
    name: string;
  }[]; // 标签列表
}

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
