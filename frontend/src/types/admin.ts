import type { PageableParams } from './api';

/**
 * 管理员创建文章请求
 * 对应后端：AdminCreatePostRequest
 */
export interface AdminCreatePostRequest {
  // 基础字段
  authorId: number;
  categoryId: number;
  title: string;
  slug?: string;
  excerpt?: string;
  content: string;

  // 图片
  coverImageUrl?: string;
  featuredImageUrl?: string;

  // 状态
  status?: 'draft' | 'published' | 'archived';
  visibility?: 'public' | 'private' | 'password_protected';
  password?: string;

  // 标记
  isFeatured?: boolean;
  isTop?: boolean;
  allowComments?: boolean;

  // SEO
  seoTitle?: string;
  seoDescription?: string;
  seoKeywords?: string;

  // 时间
  scheduledAt?: string;

  // 标签
  tagIds?: number[];

  // 是否立即发布
  publishNow?: boolean;
}

/**
 * 管理员更新文章请求
 * 对应后端：AdminUpdatePostRequest
 */
export interface AdminUpdatePostRequest {
  // 基础字段
  authorId?: number;
  categoryId?: number;
  title?: string;
  slug?: string;
  excerpt?: string;
  content?: string;

  // 图片
  coverImageUrl?: string;
  featuredImageUrl?: string;

  // 状态
  status?: 'draft' | 'published' | 'archived';
  visibility?: 'public' | 'private' | 'password_protected';
  password?: string;

  // 标记
  isFeatured?: boolean;
  isTop?: boolean;
  allowComments?: boolean;

  // SEO
  seoTitle?: string;
  seoDescription?: string;
  seoKeywords?: string;

  // 时间
  scheduledAt?: string;

  // 标签
  tagIds?: number[];
}

/**
 * 文章表单数据（用于前端表单）
 */
export interface PostFormData {
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
  status: 'draft' | 'published' | 'archived';
  visibility: 'public' | 'private' | 'password_protected';
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

  // 是否立即发布（仅创建时）
  publishNow: boolean;
}

/**
 * 管理员文章查询参数
 * 对应后端：AdminPostListParams
 */
export interface AdminPostListParams {
  pageable?: PageableParams;
  q?: string;
  status?: string;
  authorId?: number;
  categoryId?: number;
  tagId?: number;
  isFeatured?: boolean;
  isTop?: boolean;
  visibility?: string;
}

/**
 * 管理员文章列表响应项
 * 对应后端：AdminPostListResponse
 */
export interface AdminPostListResponse {
  id: number;
  title: string;
  slug: string;
  excerpt?: string;
  coverImageUrl?: string;
  status: string;
  visibility: string;
  isFeatured: boolean;
  isTop: boolean;
  allowComments: boolean;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  readingTime?: number;
  wordCount?: number;
  author: {
    id: number;
    username: string;
    nickname?: string;
    avatarUrl?: string;
  };
  category?: {
    id: number;
    name: string;
    slug: string;
  };
  createdAt: string;
  updatedAt: string;
  publishedAt?: string;
}
