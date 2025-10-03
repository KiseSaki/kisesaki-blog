/**
 * 互动相关类型定义（点赞、收藏等）
 */

import type { UserInfo } from './user';

/**
 * 反应类型
 */
export type ReactionType = 'LIKE' | 'DISLIKE';

/**
 * 反应状态响应
 */
export interface ReactionStatusResponse {
  // 是否点赞
  isLiked: boolean;
  // 是否点踩
  isDisliked: boolean;
  // 点赞总数
  likeCount: number;
  // 点踩总数
  dislikeCount: number;
}

/**
 * 批量反应状态响应
 */
export type BatchReactionStatusResponse = Record<
  number,
  ReactionStatusResponse
>;

/**
 * 收藏状态响应
 */
export interface FavoriteStatusResponse {
  // 是否已收藏
  isFavorited: boolean;
  // 收藏总数
  favoriteCount: number;
}

/**
 * 收藏文章响应
 */
export interface FavoritePostResponse {
  id: number;
  postId: number;
  postTitle: string;
  postSlug: string;
  postExcerpt: string;
  postCover: string | null;
  authorId: number;
  authorName: string;
  authorAvatar: string | null;
  favoritedAt: string;
}

/**
 * 收藏用户响应
 */
export interface FavoriteUserResponse {
  user: UserInfo;
  favoritedAt: string;
}
