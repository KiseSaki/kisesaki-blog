import { SettingsIcon, UserIcon } from '@/components/icons';
import { FavoritesIcon } from '@/components/icons/NavigationIcons';
import {
  PROFILE_LINK,
  USER_FAVORITES_LINK,
  USER_SETTINGS_LINK,
} from '@/config';
import type { SidebarConfig, SidebarItem } from './types';

const baseProfileSidebarConfig: SidebarItem[] = [
  {
    id: 'profile',
    label: '个人资料',
    icon: UserIcon,
    path: PROFILE_LINK,
  },
  {
    id: 'settings',
    label: '设置',
    icon: SettingsIcon,
    path: USER_SETTINGS_LINK,
  },
];

// 未认证用户侧边栏配置
export const guestSidebarConfig: SidebarConfig = {
  type: 'GUEST',
  groups: [
    {
      id: 'guest-profile',
      label: '个人中心',
      items: baseProfileSidebarConfig,
    },
  ],
};

// 普通用户侧边栏配置
export const userSidebarConfig: SidebarConfig = {
  type: 'USER',
  groups: [
    {
      id: 'user-profile',
      label: '个人中心',
      items: [
        ...baseProfileSidebarConfig,
        {
          id: 'favorites',
          label: '收藏',
          icon: FavoritesIcon,
          path: USER_FAVORITES_LINK,
        },
      ],
    },
  ],
};

// 作者侧边栏配置
export const authorSidebarConfig: SidebarConfig = {
  type: 'AUTHOR',
  groups: [
    {
      id: 'author-management',
      label: '作者管理',
      items: [
        ...baseProfileSidebarConfig,
        // TODO 添加作者专属侧边栏项，组件还没实现
      ],
    },
  ],
};