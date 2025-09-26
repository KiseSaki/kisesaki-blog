// 侧边栏菜单项
export interface SidebarItem {
  id: string;
  label: string;
  icon: React.ComponentType<{ className?: string }>;
  path: string;

  children?: SidebarItem[];

  disabled?: boolean; // 是否禁用
  active?: boolean; // 是否高亮
  expanded?: boolean; // 是否展开子菜单
  defaultExpanded?: boolean; // 默认是否展开子菜单
}

// 侧边栏分组
export interface SidebarSection {
  id: string;
  label: string;
  items: SidebarItem[];
  collapsible?: boolean; // 是否可折叠
  defaultCollapsed?: boolean; // 默认是否折叠
}

// 侧边栏配置
export interface SidebarConfig {
  type: 'AUTHOR' | 'ADMIN' | 'USER' | 'GUEST';
  groups: SidebarSection[];
}
