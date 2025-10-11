/**
 * 分析统计相关类型定义
 */

/**
 * 页面浏览记录请求
 */
export interface ViewRecordRequest {
  // 页面路径
  path: string;
  // 页面标题
  title?: string;
  // 引荐来源
  referrer?: string;
  // 文章ID（如果是文章页面）
  postId?: number;
  // 停留时长（秒）
  duration?: number;
}

/**
 * 自定义事件记录请求
 */
export interface EventRecordRequest {
  // 事件类型
  eventType: string;
  // 事件目标（如文章ID、评论ID等）
  targetId?: number;
  // 事件目标类型
  targetType?: string;
  // 事件数据（JSON字符串）
  eventData?: Record<string, unknown>;
}

/**
 * 文章浏览统计响应
 */
export interface PostViewStatsResponse {
  // 文章ID
  postId: number;
  // 总浏览量
  totalViews: number;
  // 独立访客数
  uniqueVisitors: number;
  // 今日浏览量
  todayViews: number;
  // 本周浏览量
  weekViews: number;
  // 本月浏览量
  monthViews: number;
  // 平均停留时长（秒）
  averageDuration: number;
}

/**
 * 仪表盘统计概览响应
 */
export interface DashboardStatsResponse {
  // 文章统计
  postStats: {
    totalPosts: number;
    publishedPosts: number;
    draftPosts: number;
    archivedPosts: number;
  };
  // 用户统计
  userStats: {
    totalUsers: number;
    activeUsers: number;
    newUsersToday: number;
  };
  // 评论统计
  commentStats: {
    totalComments: number;
    pendingComments: number;
    todayComments: number;
  };
  // 浏览统计
  viewStats: {
    totalViews: number;
    todayViews: number;
    weekViews: number;
    monthViews: number;
  };
}

/**
 * 热门文章响应
 */
export interface PopularPostResponse {
  id: number;
  title: string;
  slug: string;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  publishedAt: string;
}

/**
 * 最近活动响应
 */
export interface RecentActivityResponse {
  id: number;
  type: 'post' | 'comment' | 'user';
  action: string;
  content: string;
  userId: number;
  username: string;
  createdAt: string;
}
