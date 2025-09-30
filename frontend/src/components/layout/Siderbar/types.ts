import type { PermissionsType } from '@/config';

// 侧边栏菜单项
export interface SidebarItemType {
  id: string;
  label: string;
  icon: React.ComponentType<{ className?: string }>;
  path: string;
  permission?: PermissionsType; // 权限标识

  children?: SidebarItemType[];

  disabled?: boolean; // 是否禁用
  expanded?: boolean; // 是否展开子菜单
  defaultExpanded?: boolean; // 默认是否展开子菜单
}

// 侧边栏分组
export interface SidebarSectionType {
  id: string;
  label: string;
  items: SidebarItemType[];
  collapsible?: boolean; // 是否可折叠
  defaultCollapsed?: boolean; // 默认是否折叠
}

// 侧边栏配置
export interface SidebarConfig {
  type: 'ADMIN' | 'USER';
  groups: SidebarSectionType[];
}
