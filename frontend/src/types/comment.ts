/**
 * 评论相关类型定义
 */

import type { UserInfo } from './user';

/**
 * 评论状态
 */
export type CommentStatus = 'NORMAL' | 'HIDDEN' | 'DELETED' | 'UNDER_REVIEW';

/**
 * 评论排序方式
 */
export type CommentSortType = 'LATEST' | 'OLDEST' | 'HOT';

/**
 * 评论列表项
 */
export interface CommentListResponse {
  id: number;
  postId: number;
  parentId: number | null;
  rootId: number | null;
  content: string;
  author: UserInfo;
  status: CommentStatus;
  likeCount: number;
  dislikeCount: number;
  replyCount: number;
  createdAt: string;
  updatedAt: string;
  isEdited: boolean;
  isLiked?: boolean;
  isDisliked?: boolean;
  // 嵌套回复列表（仅展示部分，完整列表需要懒加载）
  replies?: CommentListResponse[];
}

/**
 * 评论详情
 */
export interface CommentDetailResponse extends CommentListResponse {
  // 可以包含更多详细信息
  parentComment?: CommentListResponse;
  rootComment?: CommentListResponse;
}

/**
 * 获取评论列表参数
 */
export interface CommentListParams {
  page?: number;
  size?: number;
  // 排序方式
  sortBy?: CommentSortType;
  // 是否只看作者
  onlyAuthor?: boolean;
  // 父评论ID（用于获取回复）
  parentId?: number;
}

/**
 * 获取我的评论列表参数
 */
export interface MyCommentParams {
  page?: number;
  size?: number;
  // 文章ID筛选
  postId?: number;
  // 状态筛选
  status?: CommentStatus;
}

/**
 * 创建评论请求
 */
export interface CreateCommentBody {
  // 评论内容
  content: string;
  // 父评论ID（回复时使用）
  parentId?: number;
  // 根评论ID（回复时使用）
  rootId?: number;
}

/**
 * 更新评论请求
 */
export interface UpdateCommentBody {
  // 更新后的内容
  content: string;
}

/**
 * 举报评论请求
 */
export interface ReportCommentBody {
  // 举报原因
  reason: string;
  // 详细描述
  description?: string;
}
