/**
 * 仪表盘配置文件
 * 定义统计卡片、图表配置等
 */

/**
 * 统计卡片配置接口
 */
export interface DashboardStatCard {
  title: string;
  value: number | string;
  prefix?: React.ReactNode;
  suffix?: React.ReactNode;
  trend?: 'up' | 'down';
  trendValue?: string;
  description?: string;
  color?: string;
}

/**
 * 生成统计卡片数据
 */
export const generateStatCards = (
  stats: {
    postStats?: {
      totalPosts: number;
      publishedPosts: number;
      draftPosts: number;
    };
    userStats?: {
      totalUsers: number;
      newUsersToday: number;
    };
    commentStats?: {
      totalComments: number;
      todayComments: number;
    };
    viewStats?: {
      totalViews: number;
      todayViews: number;
    };
  } | null
): DashboardStatCard[] => {
  if (!stats) {
    return [];
  }

  return [
    {
      title: '总文章数',
      value: stats.postStats?.totalPosts || 0,
      description: `已发布: ${stats.postStats?.publishedPosts || 0}`,
      color: '#1890ff',
    },
    {
      title: '总用户数',
      value: stats.userStats?.totalUsers || 0,
      description: `今日新增: ${stats.userStats?.newUsersToday || 0}`,
      color: '#52c41a',
    },
    {
      title: '总评论数',
      value: stats.commentStats?.totalComments || 0,
      description: `今日评论: ${stats.commentStats?.todayComments || 0}`,
      color: '#faad14',
    },
    {
      title: '总浏览量',
      value: stats.viewStats?.totalViews || 0,
      description: `今日浏览: ${stats.viewStats?.todayViews || 0}`,
      color: '#eb2f96',
    },
  ];
};

/**
 * 活动类型映射
 */
export const activityTypeMap: Record<string, string> = {
  post: '文章',
  comment: '评论',
  user: '用户',
};

/**
 * 活动动作映射
 */
export const activityActionMap: Record<string, string> = {
  create: '创建了',
  update: '更新了',
  delete: '删除了',
  publish: '发布了',
  comment: '评论了',
  register: '注册了',
};
