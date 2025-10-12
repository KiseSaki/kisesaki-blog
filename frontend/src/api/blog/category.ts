/**
 * 分类相关 API 接口函数
 */
import { httpClient } from '@/lib';
import type { PageResponse } from '@/types';
import type {
  CategoryDetailResponse,
  CategoryPostsParams,
  CategoryQueryParams,
  CategoryTreeResponse,
  PopularCategoryParams,
  PopularCategoryResponse,
  PublishedPostListResponse,
} from '@/types/blog';

// =================== 分类查询接口 ===================

/**
 * 获取分类列表
 * @param params 查询参数
 * @returns 分类列表
 */
export const getCategoryListApi = (params?: CategoryQueryParams) =>
  httpClient.get<PageResponse<CategoryTreeResponse>>('/categories', {
    params,
  });

/**
 * 根据ID获取分类详情
 * @param id 分类ID
 * @returns 分类详情
 */
export const getCategoryByIdApi = (id: number) =>
  httpClient.get<CategoryDetailResponse>(`/categories/${id}`);

/**
 * 根据别名获取分类详情
 * @param slug 分类别名
 * @returns 分类详情
 */
export const getCategoryBySlugApi = (slug: string) =>
  httpClient.get<CategoryDetailResponse>(`/categories/slug/${slug}`);

/**
 * 获取热门分类
 * @param params 查询参数
 * @returns 热门分类列表
 */
export const getPopularCategoriesApi = (params?: PopularCategoryParams) =>
  httpClient.get<PopularCategoryResponse[]>('/categories/popular', { params });

/**
 * 获取指定分类下的文章列表
 * @param categoryId 分类ID
 * @param params 查询参数
 * @returns 文章列表
 */
export const getCategoryPostsApi = (
  categoryId: number,
  params?: CategoryPostsParams
) =>
  httpClient.get<PageResponse<PublishedPostListResponse>>(
    `/categories/${categoryId}/posts`,
    { params }
  );

// =================== 管理员分类管理接口 ===================

/**
 * 创建分类
 * @param data 分类数据
 * @returns 创建结果
 */
export const createCategoryApi = (data: {
  name: string;
  slug?: string;
  description?: string;
  parentId?: number;
  sortOrder?: number;
}) => httpClient.post<{ id: number }>('/categories', data);

/**
 * 更新分类
 * @param id 分类ID
 * @param data 更新数据
 * @returns 更新结果
 */
export const updateCategoryApi = (
  id: number,
  data: {
    name?: string;
    slug?: string;
    description?: string;
    parentId?: number;
    sortOrder?: number;
  }
) => httpClient.put<void>(`/categories/${id}`, data);

/**
 * 删除分类
 * @param id 分类ID
 * @returns 删除结果
 */
export const deleteCategoryApi = (id: number) =>
  httpClient.delete<void>(`/categories/${id}`);
