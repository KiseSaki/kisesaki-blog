/**
 * 文章相关类型定义
 */

import type { PageableParams } from '../api';
import type { AuthorInfo } from './common';
import type { CategoryInfo } from './category';
import type { TagInfo } from './tag';

// =================== 文章状态与可见性 ===================

/**
 * 文章状态
 */
export type PostStatus = 'draft' | 'published' | 'archived' | 'deleted';

/**
 * 文章可见性
 */
export type PostVisibility = 'public' | 'private' | 'password_protected';

/**
 * 文章排序方式
 */
export type PostSortType = 'latest' | 'popular' | 'recommended';

// =================== 文章列表类型 ===================

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
 * 我的文章列表项
 * 对应后端：MyPostsListResponse
 */
export interface MyPostsListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt: string;
  coverImageUrl: string | null;
  status: PostStatus; // draft, published, archived
  visibility: PostVisibility; // public, private, password_protected

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

// =================== 文章详情类型 ===================

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
 * 文章编辑用详情响应
 * 用于后台编辑表单回显，包含所有可编辑字段
 * 对应后端：PostEditDetailResponse
 */
export interface PostEditDetailResponse {
  id: number;
  title: string;
  slug: string;
  excerpt: string | undefined;
  content: string; // Markdown 原始内容
  categoryId: number;
  categoryName: string;
  tagIds: number[]; // 标签ID列表，用于表单回显
  coverImageUrl: string | undefined;
  featuredImageUrl: string | undefined;
  status: 'draft' | 'published' | 'archived';
  visibility: 'public' | 'private' | 'password_protected';
  password: string | undefined;
  isFeatured: boolean;
  isTop: boolean;
  allowComments: boolean;
  seoTitle: string | undefined;
  seoDescription: string | undefined;
  seoKeywords: string | undefined;
  scheduledAt: string | undefined;
  publishedAt: string | undefined;
  createdAt: string;
  updatedAt: string;
  readingTime: number | undefined;
  authorId: number;
  authorUsername: string;
}

// =================== 文章创建与更新 ===================

/**
 * 文章基本类型
 */
export interface PostBase {
  // 基础信息
  authorId: number | undefined;
  categoryId: number | undefined;
  title: string;
  slug: string;
  excerpt: string;
  content: string;

  // 图片
  coverImageUrl: string;
  featuredImageUrl: string;

  // 状态
  status: PostStatus;
  visibility: PostVisibility;
  password: string;

  // 标记
  isFeatured: boolean;
  isTop: boolean;
  allowComments: boolean;

  // SEO
  seoTitle: string;
  seoDescription: string;
  seoKeywords: string;

  // 时间
  scheduledAt: string | null;

  // 标签
  tagIds: number[];
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
export interface CreatePostRequest extends PostBase {
  publishNow: boolean; // 是否立即发布
}

/**
 * 创建文章响应
 */
export interface CreatePostResponse {
  id: number;
  slug: string;
  title: string;
  status: PostStatus;
  visibility: PostVisibility;
  isPublished: boolean;
  publishedAt: string | null;
  createdAt: string;
  updatedAt: string;
}

/**
 * 更新文章请求
 */
export interface UpdatePostRequest extends Partial<PostBase> {
  createRevision?: boolean; // 是否创建新版本
  revisionNote?: string; // 版本备注
}

/**
 * 更新文章响应
 */
export interface UpdatePostResponse {
  id: number;
  slug: string;
  title: string;
  status: PostStatus;
  visibility: PostVisibility;
  revisionCreated: boolean;
  currentVersion: number;
  lastModifiedAt: string;
  updatedAt: string;
}

// =================== 文章查询参数 ===================

/**
 * 已发布文章列表查询参数
 */
export interface PublishedPostListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 业务筛选参数
  categoryId?: number;
  categoryName?: string;
  tagId?: number;
  tagName?: string;
  authorId?: number;
  authorUsername?: string;
  authorDisplayName?: string;
  isFeatured?: boolean;
  isTop?: boolean;
  q?: string;
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

// =================== 文章版本相关 ===================

/**
 * 文章版本信息
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
 * 获取文章版本列表参数
 */
export interface PostRevisionListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
}

/**
 * 文章版本内容响应
 */
export interface PostRevisionContentResponse extends RevisionInfo {
  content: string; // Markdown 原始内容
}
