/**
 * 博客相关类型定义
 * 包含文章、分类、标签等博客相关的数据类型
 */

import type { PageableParams } from './api';

// =================== 文章相关类型 ===================

/**
 * 文章状态
 */
export type PostStatus = 'DRAFT' | 'PUBLISHED' | 'ARCHIVED';

/**
 * 文章排序方式
 */
export type PostSortType = 'LATEST' | 'POPULAR' | 'RECOMMENDED';

/**
 * 已发布文章列表项
 * 对应后端：PublishedPostListResponse
 */
export interface PublishedPostListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt: string;
  coverImageUrl: string | null;

  // 统计数据
  viewCount: number;
  likeCount: number;
  commentCount: number;
  shareCount: number;
  readingTime: number;

  // 标记
  isFeatured: boolean;
  isTop: boolean;

  // 时间
  publishedAt: string;

  // 关联数据（嵌套对象）
  author: AuthorInfo;
  category: CategoryInfo;
  tags: TagInfo[];
}

/**
 * 相邻文章简要信息
 * 对应后端：PublishedPostDetailResponse.AdjacentPost
 */
export interface AdjacentPost {
  id: number;
  title: string;
  slug: string;
  coverImageUrl: string | null;
  excerpt: string;
}

/**
 * 相关推荐文章
 * 对应后端：PublishedPostDetailResponse.RelatedPost
 */
export interface RelatedPost {
  id: number;
  title: string;
  slug: string;
  coverImageUrl: string | null;
  excerpt: string;
}

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
 * 作者信息
 * 对应后端：AuthorInfo
 */
export interface AuthorInfo {
  id: number;
  username: string;
  displayName: string;
  avatarUrl: string | null;
  bio: string | null;
}

/**
 * 权限信息
 * 对应后端：Permissions
 */
export interface Permissions {
  canEdit: boolean;
  canDelete: boolean;
  canComment: boolean;
}

/**
 * 已发布文章详情
 * 对应后端：PublishedPostDetailResponse
 */
export interface PublishedPostDetailResponse {
  // ========== 基本信息 ==========
  id: number;
  title: string;
  slug: string;
  excerpt: string;
  htmlContent: string; // 后端返回渲染后的 HTML
  readingTime: number;

  // ========== 媒体资源 ==========
  coverImageUrl: string | null;
  featuredImageUrl: string | null;
  isFeatured: boolean;
  isTop: boolean;

  // ========== 统计数据 ==========
  viewCount: number;
  likeCount: number;
  commentCount: number;
  shareCount: number;

  // ========== SEO 元数据 ==========
  seoTitle: string | null;
  seoDescription: string | null;
  seoKeywords: string | null;

  // ========== 时间信息 ==========
  publishedAt: string;
  lastModifiedAt: string;

  // ========== 关联数据 ==========
  author: AuthorInfo;
  category: CategoryInfo;
  tags: TagInfo[];
  revisions?: RevisionInfo[]; // 版本历史（可选）

  // ========== 智能推荐 ==========
  prevPost: AdjacentPost | null;
  nextPost: AdjacentPost | null;
  relatedPosts: RelatedPost[];
  meta?: Record<string, string>; // 自定义元数据

  // ========== 权限 ==========
  permissions: Permissions;
}

/**
 * 我的文章列表项
 * 对应后端：MyPostsListResponse
 */
export interface MyPostsListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt: string;
  coverImageUrl: string | null;
  status: string; // draft, published, archived
  visibility: string; // public, private, password_protected

  // 统计数据
  viewCount: number;
  likeCount: number;
  commentCount: number;
  shareCount: number;
  readingTime: number;
  wordCount: number;

  // 标记
  isFeatured: boolean;
  isTop: boolean;
  allowComments: boolean;

  // 时间信息
  createdAt: string;
  updatedAt: string;
  publishedAt: string | null;
  scheduledAt: string | null;

  // 关联数据
  author: AuthorInfo;
  category: CategoryInfo;
  tags: TagInfo[];
}

/**
 * 文章元数据
 */
export interface MetaDataDto {
  keywords?: string[];
  description?: string;
  ogImage?: string;
  ogTitle?: string;
  ogDescription?: string;
}

/**
 * 创建文章请求
 */
export interface CreatePostRequest {
  title: string;
  content: string;
  excerpt?: string;
  coverImage?: string;
  categoryId: number;
  tagIds: number[];
  status: PostStatus;
  isFeatured?: boolean;
  metadata?: MetaDataDto;
}

/**
 * 创建文章响应
 */
export interface CreatePostResponse {
  id: number;
  slug: string;
}

/**
 * 更新文章请求
 */
export interface UpdatePostRequest {
  title?: string;
  content?: string;
  excerpt?: string;
  coverImage?: string;
  categoryId?: number;
  tagIds?: number[];
  status?: PostStatus;
  isFeatured?: boolean;
  metadata?: MetaDataDto;
}

/**
 * 更新文章响应
 */
export interface UpdatePostResponse {
  id: number;
  slug: string;
  version: number;
}

/**
 * 已发布文章列表查询参数
 */
export interface PublishedPostListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  categoryId?: number;
  tagId?: number;
  keyword?: string;
  sortBy?: PostSortType;
}

/**
 * 获取我的文章列表参数
 */
export interface GetMyPostsListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  q?: string; // 对应后端 q
  status?: PostStatus;
  categoryId?: number;
  categoryName?: string; // 分类名称
  tagId?: number; // 标签ID
  tagName?: string; // 标签名称
  isTop?: boolean; // 是否置顶
  isFeatured?: boolean; // 是否为精选文章
  visibility?: 'public' | 'private' | 'password_protected'; // 可见性
}

/**
 * 文章修订信息
 * 对应后端：RevisionInfo
 */
export interface RevisionInfo {
  id: number;
  version: number;
  title: string;
  summary: string;
  createdAt: string;
}

/**
 * 文章修订内容响应
 */
export interface PostRevisionContentResponse {
  id: number;
  postId: number;
  version: number;
  title: string;
  content: string;
  excerpt: string;
  coverImage: string | null;
  createdAt: string;
  createdBy: string;
}

/**
 * 文章修订列表查询参数 直接用 PageableParams
 */
export interface PostRevisionListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
}

// =================== 分类相关类型 ===================

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

// =================== 标签相关类型 ===================

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
