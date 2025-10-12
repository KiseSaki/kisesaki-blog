/**
 * 标签相关 API 接口函数（用户端）
 */
import { httpClient } from '@/lib';
import type { PageResponse } from '@/types';
import type {
  MyTagResponse,
  PopularTagResponse,
  PublishedPostListResponse,
  TagCloudItem,
  TagCreateRequest,
  TagDetailResponse,
  TagListParams,
  TagListResponse,
  TagPostsParams,
  TagSearchItem,
  TagSearchParams,
} from '@/types/blog';

// =================== 标签查询接口 ===================

/**
 * 获取标签列表
 * @param params 查询参数
 * @returns 标签列表
 */
export const getTagListApi = (params?: TagListParams) =>
  httpClient.get<PageResponse<TagListResponse>>('/tags', { params });

/**
 * 根据ID获取标签详情
 * @param id 标签ID
 * @returns 标签详情
 */
export const getTagByIdApi = (id: number) =>
  httpClient.get<TagDetailResponse>(`/tags/${id}`);

/**
 * 根据Slug获取标签详情
 * @param slug 标签Slug
 * @returns 标签详情
 */
export const getTagBySlugApi = (slug: string) =>
  httpClient.get<TagDetailResponse>(`/tags/slug/${slug}`);

/**
 * 获取热门标签
 * @returns 热门标签列表
 */
export const getPopularTagsApi = () =>
  httpClient.get<PopularTagResponse[]>('/tags/popular');

/**
 * 获取标签云
 * @returns 标签云数据
 */
export const getTagCloudApi = () =>
  httpClient.get<TagCloudItem[]>('/tags/cloud');

/**
 * 搜索标签（用于创作时的标签建议）
 * @param params 搜索参数
 * @returns 标签搜索结果
 */
export const searchTagsApi = (params: TagSearchParams) =>
  httpClient.get<TagSearchItem[]>('/tags/search', { params });

/**
 * 获取指定标签下的文章列表
 * @param tagId 标签ID
 * @param params 查询参数
 * @returns 文章列表
 */
export const getTagPostsApi = (tagId: number, params?: TagPostsParams) =>
  httpClient.get<PageResponse<PublishedPostListResponse>>(
    `/tags/${tagId}/posts`,
    { params }
  );

// =================== 标签创建与管理接口 ===================

/**
 * 创建新标签（用户创作文章时）
 * @param request 创建标签请求
 * @returns 创建的标签信息
 */
export const createTagApi = (request: TagCreateRequest) =>
  httpClient.post<TagDetailResponse>('/tags', request);

/**
 * 获取我创建的标签
 * @param params 查询参数
 * @returns 我的标签列表
 */
export const getMyTagsApi = (params?: TagListParams) =>
  httpClient.get<PageResponse<MyTagResponse>>('/tags/my', { params });
