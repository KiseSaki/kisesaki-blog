import {
  CategoryIcon,
  CommentIcon,
  DashboardIcon,
  PostIcon,
  TagIcon,
} from '@/components/icons';
import { SettingIcon } from '@/components/icons/AdminIcons';
import {
  ADMIN_CATEGORIES_LINK,
  ADMIN_COMMENTS_LINK,
  ADMIN_DASHBOARD_LINK,
  ADMIN_POSTS_LINK,
  ADMIN_SETTINGS_LINK,
  ADMIN_TAGS_LINK,
  ADMIN_USERS_LINK,
} from '@/config';
import { UserIcon } from 'lucide-react';
import type { SidebarConfig } from '../types';

// 管理员后台管理用的侧边栏配置
export const adminSidebarConfig: SidebarConfig = {
  type: 'ADMIN',
  groups: [
    {
      id: 'admin-dashboard',
      label: '仪表盘',
      items: [
        {
          id: 'dashboard',
          label: '仪表盘',
          icon: DashboardIcon,
          path: ADMIN_DASHBOARD_LINK,
          permission: 'DASHBOARD_ADMIN_ACCESS',
        },
        {
          id: 'posts',
          label: '文章管理',
          icon: PostIcon,
          path: ADMIN_POSTS_LINK,
          permission: 'POST_MANAGE',
        },
        {
          id: 'categories',
          label: '分类管理',
          icon: CategoryIcon,
          path: ADMIN_CATEGORIES_LINK,
          permission: 'CATEGORY_VIEW',
        },
        {
          id: 'tags',
          label: '标签管理',
          icon: TagIcon,
          path: ADMIN_TAGS_LINK,
          permission: 'TAG_MANAGE',
        },
        {
          id: 'comments',
          label: '评论管理',
          icon: CommentIcon,
          path: ADMIN_COMMENTS_LINK,
          permission: 'COMMENT_MODERATE',
        },
        {
          id: 'users',
          label: '用户管理',
          icon: UserIcon,
          path: ADMIN_USERS_LINK,
          permission: 'USER_MANAGE',
        },
        {
          id: 'settings',
          label: '系统设置',
          icon: SettingIcon,
          path: ADMIN_SETTINGS_LINK,
          permission: 'SYSTEM_CONFIG_MANAGE',
        },
      ],
    },
  ],
};
