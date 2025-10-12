import { useAuth, usePermissions } from '@/hooks';
import { useUI } from '@/stores';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useLocation, useNavigate } from 'react-router';
import { adminSidebarConfig } from '../config/adminConfig';
import { userSidebarConfig } from '../config/userConfig';
import type { SidebarConfig, SidebarItemType } from '../types';

export const useSidebar = () => {
  const { user } = useAuth();
  const { hasPermission } = usePermissions();
  const {
    sidebarCollapsed,
    toggleSidebar,
    setSidebarCollapsed,
    sidebarWidth,
    sidebarCollapsedWidth,
  } = useUI();
  const location = useLocation(); // 获取当前URL
  const navigate = useNavigate();

  const [activeItemId, setActiveItemId] = useState<string>('');

  // 根据URL获取侧边栏所需类型
  const getSidebarType = useMemo(() => {
    if (location.pathname.startsWith('/user')) {
      return 'USER';
    }
    if (location.pathname.startsWith('/manage')) {
      return 'ADMIN';
    }
  }, [location.pathname]);

  // 根据用户权限判断用户是否有权限访问某个侧边栏项
  const canAccessSidebarItem = useCallback(
    (sidebarItem: SidebarItemType) => {
      if (sidebarItem.permission && !hasPermission(sidebarItem.permission)) {
        return false;
      }
      // 递归检查子菜单项
      if (sidebarItem.children && sidebarItem.children.length > 0) {
        sidebarItem.children = sidebarItem.children.filter(child =>
          canAccessSidebarItem(child)
        );
      }
      return true;
    },
    [hasPermission]
  );

  // 过滤侧边栏配置，只保留用户有权限访问的项
  const sidebarConfig = useMemo(() => {
    if (!user || !getSidebarType) return null;
    let config: SidebarConfig;

    if (getSidebarType === 'ADMIN') {
      config = { ...adminSidebarConfig };
    } else {
      config = { ...userSidebarConfig };
    }

    config.groups = config.groups
      .map(group => {
        return {
          ...group,
          items: group.items.filter(item => canAccessSidebarItem(item)),
        };
      })
      .filter(group => group.items.length > 0); // 移除没有任何可访问项的分组

    return config;
  }, [user, getSidebarType, canAccessSidebarItem]);

  /* item相关 */
  // 设置当前激活的侧边栏项
  const setActive = useCallback((itemId: string) => {
    setActiveItemId(itemId);
  }, []);

  // 点击侧边栏项时的处理函数
  const handleItemClick = useCallback(
    (item: SidebarItemType) => {
      if (activeItemId === item.id) return; // 如果点击的项已经是激活状态，则不再设置
      if (item.disabled) return; // 如果项被禁用，则不处理点击

      setActive(item.id);

      navigate(item.path);
    },
    [setActive, activeItemId, navigate]
  );

  // 获取判断sidebarItem以及其子项是否符合路径
  const isActiveSidebarItem = useCallback(
    (item: SidebarItemType): string | null => {
      if (item.children && item.children.length > 0) {
        const childItems = item.children.filter(child =>
          isActiveSidebarItem(child)
        );
        if (childItems.length === 0) {
          return null;
        } else {
          return childItems[0].id; // 返回第一个匹配的子项ID
        }
      }

      if (item.path && location.pathname.startsWith(item.path)) {
        return item.id;
      }
      return null;
    },
    [location.pathname]
  );

  // 根据当前URL设置初始激活项
  useEffect(() => {
    if (!sidebarConfig) return;
    const activeItemIds: string[] = [];

    sidebarConfig.groups.forEach(group => {
      group.items.forEach(item => {
        const id = isActiveSidebarItem(item);
        if (id) activeItemIds.push(id);
      });
    });

    if (activeItemIds.length > 0) {
      if (activeItemIds.length > 0) {
        if (activeItemIds.length > 1) {
          setActive(activeItemIds[activeItemIds.length - 1]);
        } else {
          setActive(activeItemIds[0]);
        }
      }
    }
  }, [isActiveSidebarItem, sidebarConfig, setActive]);

  return {
    sidebarType: getSidebarType,
    sidebarConfig,
    activeItemId,

    // UI 状态 - 从全局 store 获取
    collapsed: sidebarCollapsed,
    width: sidebarWidth,
    collapsedWidth: sidebarCollapsedWidth,

    // 操作函数
    setActive,
    handleItemClick,
    toggleCollapsed: toggleSidebar,
    setSidebarCollapsed,
  };
};
