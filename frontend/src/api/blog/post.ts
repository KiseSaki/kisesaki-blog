/**
 * 文章相关 API 接口函数
 */
import { httpClient } from '@/lib';
import type { PageResponse } from '@/types';
import type {
  CreatePostRequest,
  CreatePostResponse,
  GetMyPostsListParams,
  MyPostsListResponse,
  PostEditDetailResponse,
  PostRevisionContentResponse,
  PostRevisionListParams,
  PublishedPostDetailResponse,
  PublishedPostListParams,
  PublishedPostListResponse,
  RevisionInfo,
  UpdatePostRequest,
  UpdatePostResponse,
} from '@/types/blog';

// =================== 文章查询接口 ===================

/**
 * 获取已发布文章列表
 * @param params 查询参数
 * @returns 文章列表
 */
export const getPublishedPostsApi = (params?: PublishedPostListParams) =>
  httpClient.get<PageResponse<PublishedPostListResponse>>('/posts', {
    params,
  });

/**
 * 根据文章ID获取已发布文章详情
 * @param id 文章ID
 * @returns 文章详情
 */
export const getPublishedPostByIdApi = (id: number) =>
  httpClient.get<PublishedPostDetailResponse>(`/posts/${id}`);

/**
 * 根据文章slug获取已发布文章详情
 * @param slug 文章slug
 * @returns 文章详情
 */
export const getPublishedPostBySlugApi = (slug: string) =>
  httpClient.get<PublishedPostDetailResponse>(`/posts/slug/${slug}`);

/**
 * 获取精选文章列表
 * @param params 查询参数
 * @returns 精选文章列表
 */
export const getFeaturedPostsApi = (params?: PublishedPostListParams) =>
  httpClient.get<PageResponse<PublishedPostListResponse>>('/posts/featured', {
    params,
  });

/**
 * 获取最新文章列表
 * @param params 查询参数
 * @returns 最新文章列表
 */
export const getRecentPostsApi = (params?: PublishedPostListParams) =>
  httpClient.get<PageResponse<PublishedPostListResponse>>('/posts/recent', {
    params,
  });

/**
 * 获取我的文章列表
 * @param params 查询参数
 * @returns 我的文章列表
 */
export const getMyPostsApi = (params?: GetMyPostsListParams) =>
  httpClient.get<PageResponse<MyPostsListResponse>>('/posts/my', { params });

/**
 * 获取文章编辑详情
 * 用于后台编辑表单回显，返回所有可编辑字段（包括 Markdown 原始内容）
 * @param id 文章ID
 * @returns 文章编辑详情
 */
export const getPostEditDetailApi = (id: number) =>
  httpClient.get<PostEditDetailResponse>(`/posts/${id}/edit`);

// =================== 文章创建与更新接口 ===================

/**
 * 创建文章
 * @param request 创建文章请求
 * @returns 创建结果
 */
export const createPostApi = (request: CreatePostRequest) =>
  httpClient.post<CreatePostResponse>('/posts', request);

/**
 * 更新文章
 * @param id 文章ID
 * @param request 更新文章请求
 * @returns 更新结果
 */
export const updatePostApi = (id: number, request: UpdatePostRequest) =>
  httpClient.put<UpdatePostResponse>(`/posts/${id}`, request);

/**
 * 删除文章
 * @param id 文章ID
 * @returns 删除结果
 */
export const deletePostApi = (id: number) =>
  httpClient.delete<void>(`/posts/${id}`);

// =================== 文章状态管理接口 ===================

/**
 * 发布文章
 * @param id 文章ID
 * @returns 发布结果
 */
export const publishPostApi = (id: number) =>
  httpClient.put<void>(`/posts/${id}/publish`);

/**
 * 取消发布文章
 * @param id 文章ID
 * @returns 取消发布结果
 */
export const unpublishPostApi = (id: number) =>
  httpClient.put<void>(`/posts/${id}/unpublish`);

/**
 * 归档文章
 * @param id 文章ID
 * @returns 归档结果
 */
export const archivePostApi = (id: number) =>
  httpClient.put<void>(`/posts/${id}/archive`);

// =================== 文章版本管理接口 ===================

/**
 * 获取文章修订历史列表
 * @param postId 文章ID
 * @param params 查询参数
 * @returns 修订历史列表
 */
export const getPostRevisionsApi = (
  postId: number,
  params?: PostRevisionListParams
) =>
  httpClient.get<PageResponse<RevisionInfo>>(`/posts/${postId}/revisions`, {
    params,
  });

/**
 * 获取文章修订详情
 * @param postId 文章ID
 * @param revisionId 修订ID
 * @returns 修订详情
 */
export const getPostRevisionDetailApi = (postId: number, revisionId: number) =>
  httpClient.get<PostRevisionContentResponse>(
    `/posts/${postId}/revisions/${revisionId}`
  );

/**
 * 恢复到指定修订版本
 * @param postId 文章ID
 * @param revisionId 修订ID
 * @returns 恢复结果
 */
export const restorePostRevisionApi = (postId: number, revisionId: number) =>
  httpClient.post<void>(`/posts/${postId}/revisions/${revisionId}/restore`);
