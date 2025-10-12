/**
 * 标签相关类型定义
 */

import type { PageableParams } from '../api';
import type { PostStatus } from './post';

// =================== 标签信息 ===================

/**
 * 标签信息（用于文章中的标签）
 * 对应后端：TagInfo
 */
export interface TagInfo {
  id: number;
  name: string;
  slug: string;
  color: string | null;
}

/**
 * 标签简单信息（已废弃，使用 TagInfo 代替）
 * @deprecated 使用 TagInfo 代替
 */
export interface TagSimple {
  id: number;
  name: string;
  slug: string;
  color?: string;
}

/**
 * 标签列表响应
 * 对应后端：TagListResponse
 */
export interface TagListResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  postCount: number;
  createdAt: string;
}

/**
 * 标签详情响应
 * 对应后端：TagDetailResponse
 */
export interface TagDetailResponse {
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
}

/**
 * 热门标签响应
 * 对应后端：PopularTagResponse
 */
export interface PopularTagResponse {
  id: number;
  name: string;
  slug: string;
  color: string | null;
  postCount: number;
  popularityScore: number;
  lastUsedAt: string;
}

/**
 * 标签云项
 * 对应后端：TagCloudItem
 */
export interface TagCloudItem {
  id: number;
  name: string;
  slug: string;
  color: string | null;
  postCount: number;
  fontWeight: number;
  popularityScore: number;
}

/**
 * 标签搜索项
 * 对应后端：TagSearchItem
 */
export interface TagSearchItem {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  postCount: number;
  matchScore: number;
}

/**
 * 我的标签响应
 * 对应后端：MyTagResponse
 */
export interface MyTagResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  postCount: number;
  createdAt: string;
  isApproved: boolean;
  approvalStatus: 'pending' | 'approved' | 'rejected';
  approvalNote: string | null;
}

// =================== 标签查询参数 ===================

/**
 * 标签列表查询参数
 * 对应后端：TagListParams
 */
export interface TagListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  name?: string;
}

/**
 * 标签搜索参数
 * 对应后端：TagSearchParams
 */
export interface TagSearchParams {
  q: string;
  limit?: number;
  approvedOnly?: boolean;
  sort?: 'name' | 'popularity' | 'created_at';
}

/**
 * 标签下文章列表查询参数
 * 对应后端：TagPostsParams
 */
export interface TagPostsParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  status?: PostStatus;
  visibility?: 'public' | 'private' | 'password_protected';
  featuredOnly?: boolean;
}

/**
 * 创建标签请求
 * 对应后端：TagCreateRequest
 */
export interface TagCreateRequest {
  name: string;
  description?: string;
  slug?: string;
  color?: string;
}
