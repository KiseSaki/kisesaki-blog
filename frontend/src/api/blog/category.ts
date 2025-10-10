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
