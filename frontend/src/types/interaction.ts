/**
 * 互动相关类型定义（点赞、收藏等）
 * 对应后端：interaction 相关的 DTO
 */

/**
 * 反应类型（后端枚举）
 */
export type ReactionType = 'LIKE' | 'DISLIKE';

/**
 * 目标类型（后端枚举）
 */
export type TargetType = 'POST' | 'COMMENT';

/**
 * 反应状态响应
 * 对应后端：ReactionStatusResponse
 */
export interface ReactionStatusResponse {
  targetId: number;
  targetType: TargetType;
  isLiked: boolean;
  isDisliked: boolean;
  likeCount: number;
  dislikeCount: number;
  userReactionType: ReactionType | null;
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
 * 对应后端：FavoriteStatusResponse
 */
export interface FavoriteStatusResponse {
  postId: number;
  favorited: boolean; // 注意：后端使用 favorited 而非 isFavorited
  favoriteCount: number;
}

/**
 * 收藏文章响应
 * 对应后端：FavoritePostResponse
 */
export interface FavoritePostResponse {
  postId: number;
  title: string;
  excerpt: string;
  coverImageUrl: string | null;
  authorName: string;
  publishedAt: string;
  favoriteTime: string;
  readingTime: number;
  viewCount: number;
  likeCount: number;
}

/**
 * 收藏用户响应
 * 对应后端：FavoriteUserResponse
 */
export interface FavoriteUserResponse {
  userId: number;
  username: string;
  nickname: string;
  avatarUrl: string | null;
  favoriteTime: string;
}
