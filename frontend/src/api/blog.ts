/**
 * 博客相关 API 接口函数
 * 包含文章、分类、标签等博客相关的 API 调用
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
  CategoryDetailResponse,
  CategoryPostsParams,
  CategoryQueryParams,
  CategoryTreeResponse,
  CreatePostRequest,
  CreatePostResponse,
  GetMyPostsListParams,
  MyPostsListResponse,
  MyTagResponse,
  PopularCategoryParams,
  PopularCategoryResponse,
  PopularTagResponse,
  PostEditDetailResponse,
  PostRevisionContentResponse,
  PostRevisionListParams,
  PublishedPostDetailResponse,
  PublishedPostListParams,
  PublishedPostListResponse,
  RevisionInfo,
  TagCloudItem,
  TagCreateRequest,
  TagDetailResponse,
  TagListParams,
  TagListResponse,
  TagPostsParams,
  TagSearchItem,
  TagSearchParams,
  UpdatePostRequest,
  UpdatePostResponse,
} from '@/types/blog';

// =================== 文章相关接口 ===================

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
 * 获取热门文章列表
 * @param params 查询参数
 * @returns 热门文章列表
 */
export const getPopularPostsApi = (params?: PublishedPostListParams) =>
  httpClient.get<PageResponse<PublishedPostListResponse>>('/posts/popular', {
    params,
  });

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
 * 获取文章编辑详情
 * 用于后台编辑表单回显，返回所有可编辑字段（包括 Markdown 原始内容）
 * @param id 文章ID
 * @returns 文章编辑详情
 */
export const getPostEditDetailApi = (id: number) =>
  httpClient.get<PostEditDetailResponse>(`/posts/${id}/edit`);

/**
 * 删除文章
 * @param id 文章ID
 * @returns 删除结果
 */
export const deletePostApi = (id: number) =>
  httpClient.delete<void>(`/posts/${id}`);

/**
 * 发布文章
 * @param id 文章ID
 * @returns 发布结果
 */
export const publishPostApi = (id: number) =>
  httpClient.post<void>(`/posts/${id}/publish`);

/**
 * 取消发布文章
 * @param id 文章ID
 * @returns 取消发布结果
 */
export const unpublishPostApi = (id: number) =>
  httpClient.post<void>(`/posts/${id}/unpublish`);

/**
 * 归档文章
 * @param id 文章ID
 * @returns 归档结果
 */
export const archivePostApi = (id: number) =>
  httpClient.post<void>(`/posts/${id}/archive`);

/**
 * 获取我的文章列表
 * @param params 查询参数
 * @returns 我的文章列表
 */
export const getMyPostsApi = (params?: GetMyPostsListParams) =>
  httpClient.get<PageResponse<MyPostsListResponse>>('/posts/my', { params });

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

// =================== 分类相关接口 ===================

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

// =================== 标签相关接口 ===================

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

// =================== 管理员标签相关接口 ===================

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
