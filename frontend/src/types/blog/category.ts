/**
 * 分类相关类型定义
 */

import type { PageableParams } from '../api';
import type { PostSortType } from './post';

// =================== 分类信息 ===================

/**
 * 分类信息
 * 对应后端：CategoryInfo
 */
export interface CategoryInfo {
  id: number;
  name: string;
  slug: string;
  description: string | null;
}

/**
 * 分类树响应
 * 对应后端：CategoryTreeResponse
 */
export interface CategoryTreeResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  parentId: number | null;
  parentName: string | null;
  sortOrder: number;
  postCount: number;
  children: CategoryTreeResponse[];
}

/**
 * 分类详情响应
 * 对应后端：CategoryDetailResponse
 */
export interface CategoryDetailResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  parentId: number | null;
  parentName: string | null;
  sortOrder: number;
  postCount: number;
  createdAt: string;
  updatedAt: string;
  children: CategoryDetailResponse[];
}

/**
 * 分类面包屑（前端自定义）
 */
export interface CategoryBreadcrumb {
  id: number;
  name: string;
  slug: string;
}

/**
 * 热门分类响应
 */
export interface PopularCategoryResponse {
  id: number;
  name: string;
  slug: string;
  postCount: number;
  coverImage: string | null;
}

// =================== 分类查询参数 ===================

/**
 * 分类查询参数
 */
export interface CategoryQueryParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  parentId?: number;
  level?: number;
  keyword?: string;
}

/**
 * 热门分类查询参数
 */
export interface PopularCategoryParams {
  limit?: number;
  days?: number;
}

/**
 * 分类下文章列表查询参数
 */
export interface CategoryPostsParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  sortBy?: PostSortType;
}
