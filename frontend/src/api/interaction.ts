/**
 * 互动相关 API 接口函数（点赞、收藏等）
 */
import { httpClient } from '@/lib';
import type { PageResponse, PageableParams } from '@/types';
import type {
  BatchReactionStatusResponse,
  FavoritePostResponse,
  FavoriteStatusResponse,
  FavoriteUserResponse,
  ReactionStatusResponse,
} from '@/types/interaction';

// =================== 文章反应相关接口 ===================

/**
 * 点赞文章
 * @param postId 文章ID
 * @returns 操作结果
 */
export const likePostApi = (postId: number) =>
  httpClient.post<void>(`/posts/${postId}/like`);

/**
 * 取消点赞文章
 * @param postId 文章ID
 * @returns 操作结果
 */
export const unlikePostApi = (postId: number) =>
  httpClient.delete<void>(`/posts/${postId}/like`);

/**
 * 点踩文章
 * @param postId 文章ID
 * @returns 操作结果
 */
export const dislikePostApi = (postId: number) =>
  httpClient.post<void>(`/posts/${postId}/dislike`);

/**
 * 取消点踩文章
 * @param postId 文章ID
 * @returns 操作结果
 */
export const unDislikePostApi = (postId: number) =>
  httpClient.delete<void>(`/posts/${postId}/dislike`);

/**
 * 获取文章反应状态
 * @param postId 文章ID
 * @returns 反应状态
 */
export const getPostReactionStatusApi = (postId: number) =>
  httpClient.get<ReactionStatusResponse>(`/posts/${postId}/reaction-status`);

// =================== 评论反应相关接口 ===================

/**
 * 点赞评论
 * @param commentId 评论ID
 * @returns 操作结果
 */
export const likeCommentApi = (commentId: number) =>
  httpClient.post<void>(`/comments/${commentId}/like`);

/**
 * 取消点赞评论
 * @param commentId 评论ID
 * @returns 操作结果
 */
export const unlikeCommentApi = (commentId: number) =>
  httpClient.delete<void>(`/comments/${commentId}/like`);

/**
 * 点踩评论
 * @param commentId 评论ID
 * @returns 操作结果
 */
export const dislikeCommentApi = (commentId: number) =>
  httpClient.post<void>(`/comments/${commentId}/dislike`);

/**
 * 取消点踩评论
 * @param commentId 评论ID
 * @returns 操作结果
 */
export const unDislikeCommentApi = (commentId: number) =>
  httpClient.delete<void>(`/comments/${commentId}/dislike`);

/**
 * 获取评论反应状态
 * @param commentId 评论ID
 * @returns 反应状态
 */
export const getCommentReactionStatusApi = (commentId: number) =>
  httpClient.get<ReactionStatusResponse>(
    `/comments/${commentId}/reaction-status`
  );

// =================== 批量查询接口 ===================

/**
 * 批量获取文章反应状态
 * @param postIds 文章ID列表
 * @returns 反应状态映射
 */
export const getPostsReactionStatusApi = (postIds: number[]) =>
  httpClient.get<BatchReactionStatusResponse>('/posts/reaction-status', {
    params: { postIds: postIds.join(',') },
  });

// =================== 收藏相关接口 ===================

/**
 * 收藏文章
 * @param postId 文章ID
 * @returns 收藏状态
 */
export const favoritePostApi = (postId: number) =>
  httpClient.post<FavoriteStatusResponse>(`/api/posts/${postId}/favorite`);

/**
 * 取消收藏文章
 * @param postId 文章ID
 * @returns 收藏状态
 */
export const unfavoritePostApi = (postId: number) =>
  httpClient.delete<FavoriteStatusResponse>(`/api/posts/${postId}/favorite`);

/**
 * 获取文章收藏状态
 * @param postId 文章ID
 * @returns 收藏状态
 */
export const getFavoriteStatusApi = (postId: number) =>
  httpClient.get<FavoriteStatusResponse>(
    `/api/posts/${postId}/favorite-status`
  );

/**
 * 获取文章收藏用户列表
 * @param postId 文章ID
 * @param params 分页参数
 * @returns 收藏用户列表
 */
export const getPostFavoriteUsersApi = (
  postId: number,
  params?: PageableParams
) =>
  httpClient.get<PageResponse<FavoriteUserResponse>>(
    `/api/posts/${postId}/favorites`,
    { params }
  );

/**
 * 获取我的收藏列表
 * @param params 分页参数
 * @returns 我的收藏列表
 */
export const getCurrentUserFavoritesApi = (params?: PageableParams) =>
  httpClient.get<PageResponse<FavoritePostResponse>>('/api/users/favorites', {
    params,
  });

/**
 * 获取用户的收藏列表（公开）
 * @param userId 用户ID
 * @param params 分页参数
 * @returns 用户收藏列表
 */
export const getUserFavoritesApi = (userId: number, params?: PageableParams) =>
  httpClient.get<PageResponse<FavoritePostResponse>>(
    `/api/users/${userId}/favorites`,
    { params }
  );

/**
 * 获取用户收藏总数
 * @param userId 用户ID
 * @returns 收藏总数
 */
export const getUserFavoriteCountApi = (userId: number) =>
  httpClient.get<number>(`/api/users/${userId}/favorites/count`);
