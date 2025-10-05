/**
 * 博客相关类型定义
 * 包含文章、分类、标签等博客相关的数据类型
 */

import type { UserInfo } from './user';

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
 */
export interface PublishedPostListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt: string;
  coverImageUrl: string | null;

  // 作者信息
  authorId: number;
  authorUsername: string;
  authorDisplayName: string;
  authorAvatarUrl: string | null;

  // 分类信息
  categoryId: number;
  categoryName: string;
  categorySlug: string;

  // 标签列表
  tags: TagSimple[];

  // 统计数据
  viewCount: number;
  likeCount: number;
  commentCount: number;
  shareCount: number;

  // 时间与标记
  publishedAt: string;
  updatedAt: string;
  isFeatured: boolean;
  isTop: boolean;
  readingTime: number;
}

/**
 * 相邻文章简要信息
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
 */
export interface CategoryInfo {
  id: number;
  name: string;
  slug: string;
  description: string | null;
}

/**
 * 作者信息
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
 */
export interface Permissions {
  canEdit: boolean;
  canDelete: boolean;
}

/**
 * 已发布文章详情
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
  tags: TagSimple[];
  revisions?: RevisionInfo[]; // 版本历史（可选）

  // ========== 智能推荐 ==========
  prevPost: AdjacentPost | null;
  nextPost: AdjacentPost | null;
  relatedPosts: RelatedPost[];
  meta?: Record<string, string>; // 自定义元数据

  // ========== 权限与交互状态 ==========
  permissions: Permissions;
  isLiked?: boolean;
  isFavorited?: boolean;
}

/**
 * 我的文章列表项
 */
export interface MyPostsListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt: string;
  coverImage: string | null;
  status: PostStatus;
  categoryId: number;
  categoryName: string;
  tags: TagSimple[];
  viewCount: number;
  likeCount: number;
  commentCount: number;
  favoriteCount: number;
  publishedAt: string | null;
  createdAt: string;
  updatedAt: string;
  isDraft: boolean;
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
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
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
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
  // 业务筛选参数
  status?: PostStatus;
  categoryId?: number;
  keyword?: string;
}

/**
 * 文章修订信息
 */
export interface RevisionInfo {
  id: number;
  version: number;
  createdAt: string;
  createdBy: string;
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
 * 文章修订列表查询参数
 */
export interface PostRevisionListParams {
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
}

// =================== 分类相关类型 ===================

/**
 * 分类树响应
 */
export interface CategoryTreeResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  coverImage: string | null;
  parentId: number | null;
  level: number;
  sortOrder: number;
  postCount: number;
  children: CategoryTreeResponse[];
}

/**
 * 分类详情响应
 */
export interface CategoryDetailResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  coverImage: string | null;
  parentId: number | null;
  level: number;
  sortOrder: number;
  postCount: number;
  path: string;
  breadcrumbs: CategoryBreadcrumb[];
}

/**
 * 分类面包屑
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
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
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
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
  // 业务筛选参数
  sortBy?: PostSortType;
}

// =================== 标签相关类型 ===================

/**
 * 标签简单信息
 */
export interface TagSimple {
  id: number;
  name: string;
  slug: string;
  color?: string;
}

/**
 * 标签列表响应
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
  createdBy: UserInfo;
}

/**
 * 热门标签响应
 */
export interface PopularTagResponse {
  id: number;
  name: string;
  slug: string;
  color: string | null;
  postCount: number;
}

/**
 * 标签云项
 */
export interface TagCloudItem {
  id: number;
  name: string;
  slug: string;
  color: string | null;
  postCount: number;
  weight: number;
}

/**
 * 标签搜索项
 */
export interface TagSearchItem {
  id: number;
  name: string;
  slug: string;
  color: string | null;
  postCount: number;
}

/**
 * 我的标签响应
 */
export interface MyTagResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  color: string | null;
  postCount: number;
  usageCount: number;
  createdAt: string;
}

/**
 * 标签列表查询参数
 */
export interface TagListParams {
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
  // 业务筛选参数
  keyword?: string;
  sortBy?: 'NAME' | 'POST_COUNT' | 'CREATED_AT';
}

/**
 * 标签搜索参数
 */
export interface TagSearchParams {
  keyword: string;
  limit?: number;
}

/**
 * 标签下文章列表查询参数
 */
export interface TagPostsParams {
  // 分页参数
  currentPage?: number;
  pageSize?: number;
  sort?: string;
  includeTotal?: boolean;
  startTime?: string;
  endTime?: string;
  date?: string;
  // 业务筛选参数
  sortBy?: PostSortType;
}

/**
 * 创建标签请求
 */
export interface TagCreateRequest {
  name: string;
  description?: string;
  color?: string;
}
