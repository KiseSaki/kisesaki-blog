/**
 * 评论相关 API 接口函数
 */
import { httpClient } from '@/lib';
import type { PageResponse } from '@/types';
import type {
  CommentDetailResponse,
  CommentListParams,
  CommentListResponse,
  CreateCommentBody,
  MyCommentParams,
  ReportCommentBody,
  UpdateCommentBody,
} from '@/types/comment';

/**
 * 获取文章评论列表
 * @param postId 文章ID
 * @param params 查询参数
 * @returns 评论列表
 */
export const getPostCommentsApi = (
  postId: number,
  params?: CommentListParams
) =>
  httpClient.get<PageResponse<CommentListResponse>>(
    `/posts/${postId}/comments`,
    { params }
  );

/**
 * 获取单条评论详情
 * @param id 评论ID
 * @returns 评论详情
 */
export const getCommentDetailApi = (id: number) =>
  httpClient.get<CommentDetailResponse>(`/comments/${id}`);

/**
 * 获取评论的回复列表（用于懒加载更多回复）
 * 注意：此接口使用简化的分页参数 page 和 size
 * @param id 父评论ID
 * @param page 页码（默认1）
 * @param size 每页大小（默认10）
 * @returns 回复列表
 */
export const getCommentRepliesApi = (
  id: number,
  page: number = 1,
  size: number = 10
) =>
  httpClient.get<PageResponse<CommentListResponse>>(`/comments/${id}/replies`, {
    params: { page, size },
  });

/**
 * 创建评论
 * @param postId 文章ID
 * @param body 评论内容
 * @returns 创建的评论ID
 */
export const createCommentApi = (postId: number, body: CreateCommentBody) =>
  httpClient.post<number>(`/posts/${postId}/comments`, body);

/**
 * 更新评论（15分钟内）
 * @param id 评论ID
 * @param body 更新内容
 * @returns 更新的评论ID
 */
export const updateCommentApi = (id: number, body: UpdateCommentBody) =>
  httpClient.post<number>(`/comments/${id}`, body);

/**
 * 删除评论
 * @param id 评论ID
 * @returns 删除结果
 */
export const deleteCommentApi = (id: number) =>
  httpClient.delete<void>(`/comments/${id}`);

/**
 * 获取我的评论列表
 * @param params 查询参数
 * @returns 我的评论列表
 */
export const getMyCommentsApi = (params?: MyCommentParams) =>
  httpClient.get<PageResponse<CommentListResponse>>('/comments/my', {
    params,
  });

/**
 * 举报评论
 * @param id 评论ID
 * @param body 举报内容
 * @returns 举报结果
 */
export const reportCommentApi = (id: number, body: ReportCommentBody) =>
  httpClient.post<void>(`/comments/${id}/report`, body);

// =================== 管理员评论管理接口 ===================

/**
 * 获取所有评论列表（管理员）
 * @param params 查询参数
 * @returns 评论列表
 */
export const adminGetCommentsApi = (params?: CommentListParams) =>
  httpClient.get<PageResponse<CommentListResponse>>('/admin/comments', {
    params,
  });

/**
 * 审核评论（管理员）
 * @param id 评论ID
 * @param status 审核状态
 * @returns 审核结果
 */
export const adminApproveCommentApi = (
  id: number,
  status: 'APPROVED' | 'REJECTED' | 'SPAM'
) => httpClient.post<void>(`/admin/comments/${id}/approve`, { status });

/**
 * 删除评论（管理员）
 * @param id 评论ID
 * @returns 删除结果
 */
export const adminDeleteCommentApi = (id: number) =>
  httpClient.delete<void>(`/admin/comments/${id}`);

/**
 * 置顶评论（管理员）
 * @param id 评论ID
 * @param isPinned 是否置顶
 * @returns 置顶结果
 */
export const adminPinCommentApi = (id: number, isPinned: boolean) =>
  httpClient.post<void>(`/admin/comments/${id}/pin`, { isPinned });
