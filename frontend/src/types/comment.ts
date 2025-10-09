/**
 * 评论相关类型定义
 * 对应后端：comment 相关的 DTO
 */

import type { PageableParams } from './api';

/**
 * 评论用户信息（简化版）
 * 对应后端：CommentUserDto
 */
export interface CommentUserDto {
  id: number;
  username: string;
  displayName: string;
  avatarUrl: string;
  bio: string | null;
  status: string;
  createdAt: string;
  isBlogAuthor: boolean;
}

/**
 * 当前用户对评论的交互状态
 * 对应后端：CommentUserInteractionDto
 */
export interface CommentUserInteractionDto {
  isLiked: boolean;
  isDisliked: boolean;
  canEdit: boolean;
  canDelete: boolean;
  canReply: boolean;
  canPin: boolean;
}

/**
 * 评论状态
 */
export type CommentStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'SPAM';

/**
 * 评论排序方式
 */
export type CommentSortType = 'LATEST' | 'OLDEST' | 'HOT';

/**
 * 评论列表项
 * 对应后端：CommentListResponse
 */
export interface CommentListResponse {
  id: number;
  postId: number;
  userId: number;
  parentId: number | null;
  replyToId: number | null;
  content: string;
  htmlContent: string | null;
  likeCount: number;
  dislikeCount: number;
  replyCount: number;
  level: number;
  path: string;
  status: string; // PENDING/APPROVED/REJECTED/SPAM
  isPinned: boolean;
  isAuthorReply: boolean;
  editedAt: string | null;
  user: CommentUserDto;
  // 当前用户对该评论的交互状态（仅在用户已登录时返回）
  currentUserInteraction?: CommentUserInteractionDto;
  createdAt: string;
  updatedAt: string;
  hasMoreReplies: boolean;
  // 嵌套回复列表（仅展示部分，完整列表需要懒加载）
  replies?: CommentListResponse[];
}

/**
 * 评论详情
 * 对应后端：CommentDetailResponse
 * 注意：目前后端的 CommentDetailResponse 与 CommentListResponse 字段相同
 * 如果后端增加额外字段，需要在此处添加
 */
export type CommentDetailResponse = CommentListResponse;

/**
 * 获取评论列表参数
 * 对应后端：CommentListParams，包含嵌套的 pageable 字段
 */
export interface CommentListParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 评论用户ID
  userId?: number;
  // 回复目标评论ID（@某条评论）
  replyToId?: number;
  // 评论状态（PENDING/APPROVED/REJECTED/SPAM）
  status?: string;
  // 是否置顶
  isPinned?: boolean;
  // 是否为作者回复
  isAuthorReply?: boolean;
  // 内容关键字（用于模糊搜索评论内容）
  keyword?: string;
}

/**
 * 获取我的评论列表参数
 */
export interface MyCommentParams {
  // 分页参数（嵌套对象）
  pageable?: PageableParams;
  // 文章ID筛选
  postId?: number;
  // 状态筛选
  status?: CommentStatus;
}
