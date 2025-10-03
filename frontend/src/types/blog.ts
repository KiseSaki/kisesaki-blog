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
  coverImage: string | null;
  author: UserInfo;
  categoryId: number;
  categoryName: string;
  categorySlug: string;
  tags: TagSimple[];
  viewCount: number;
  likeCount: number;
  commentCount: number;
  favoriteCount: number;
  publishedAt: string;
  updatedAt: string;
  isFeatured: boolean;
  readingTime: number;
}

/**
 * 已发布文章详情
 */
export interface PublishedPostDetailResponse {
  id: number;
  title: string;
  slug: string;
  content: string;
  excerpt: string;
  coverImage: string | null;
  author: UserInfo;
  categoryId: number;
  categoryName: string;
  categorySlug: string;
  tags: TagSimple[];
  viewCount: number;
  likeCount: number;
  dislikeCount: number;
  commentCount: number;
  favoriteCount: number;
  publishedAt: string;
  updatedAt: string;
  isFeatured: boolean;
  readingTime: number;
  // 用户交互状态
  isLiked?: boolean;
  isDisliked?: boolean;
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
  page?: number;
  size?: number;
  categoryId?: number;
  tagId?: number;
  keyword?: string;
  sortBy?: PostSortType;
}

/**
 * 获取我的文章列表参数
 */
export interface GetMyPostsListParams {
  page?: number;
  size?: number;
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
  page?: number;
  size?: number;
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
  page?: number;
  size?: number;
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
  page?: number;
  size?: number;
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
  page?: number;
  size?: number;
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
  page?: number;
  size?: number;
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
