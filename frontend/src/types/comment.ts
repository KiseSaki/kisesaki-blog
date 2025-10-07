/**
 * 评论相关类型定义
 */

import type { PageableParams } from './api';
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
  userId: number;
  parentId?: number;
  replyToId?: number;
  content: string;
  htmlContent?: string;
  likeCount: number;
  dislikeCount: number;
  replyCount: number;
  level: number;
  path: string;
  status: string;
  isPinned: boolean;
  isAuthorReply: boolean;
  editedAt?: string;
  user: UserInfo;
  // 当前用户对该评论的交互状态（仅在用户已登录时返回）
  currentUserInteraction?: CommentUserInteraction;
  createdAt: string;
  updatedAt: string;
  hasMoreReplies: boolean;
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
 * 当前用户对评论的交互状态
 */
export interface CommentUserInteraction {
  isLiked?: boolean;
  isDisliked?: boolean;
  canEdit?: boolean;
  canDelete?: boolean;
  canReply?: boolean;
  canPin?: boolean;
}

/**
 * 获取评论列表参数
 * 对应后端 CommentListParams，包含嵌套的 pageable 字段
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

/**
 * 创建评论请求
 */
export interface CreateCommentBody {
  // 评论内容
  content: string;
  replyToId?: number; // 回复目标评论ID
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
