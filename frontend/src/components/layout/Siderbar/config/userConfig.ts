import { SettingsIcon, UserIcon } from '@/components/icons';
import { FavoritesIcon } from '@/components/icons/NavigationIcons';
import {
  PROFILE_LINK,
  USER_FAVORITES_LINK,
  USER_SETTINGS_LINK,
} from '@/config';
import type { SidebarConfig } from '../types';

// 普通用户侧边栏配置
export const userSidebarConfig: SidebarConfig = {
  type: 'USER',
  groups: [
    {
      id: 'user-profile',
      label: '个人中心',
      items: [
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
        {
          id: 'favorites',
          label: '收藏',
          icon: FavoritesIcon,
          path: USER_FAVORITES_LINK,
        },
        // TODO 添加作者专属侧边栏项，组件还没实现
      ],
    },
  ],
};
