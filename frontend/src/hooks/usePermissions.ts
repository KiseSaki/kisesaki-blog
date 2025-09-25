import { useAuthStore } from '@/stores';
import { useCallback, useMemo } from 'react';

/**
 * 权限和角色检查相关的自定义 Hook
 * 专门处理用户权限验证和角色判断
 */
export const usePermissions = () => {
  const { user, isAuthenticated } = useAuthStore();

  // 从用户信息中提取角色列表
  const roles = useMemo(() => user?.roles || [], [user]);

  /**
   * 检查用户是否拥有指定角色
   * @param role 角色名称
   * @returns 是否拥有该角色
   */
  const hasRole = useCallback(
    (role: string): boolean => {
      return roles.includes(role);
    },
    [roles]
  );

  /**
   * 检查用户是否拥有任意一个指定角色
   * @param roleList 角色列表
   * @returns 是否拥有任意一个角色
   */
  const hasAnyRole = useCallback(
    (roleList: string[]): boolean => {
      return roleList.some(role => roles.includes(role));
    },
    [roles]
  );

  /**
   * 检查用户是否拥有所有指定角色
   * @param roleList 角色列表
   * @returns 是否拥有所有角色
   */
  const hasAllRoles = useCallback(
    (roleList: string[]): boolean => {
      return roleList.every(role => roles.includes(role));
    },
    [roles]
  );

  /**
   * 检查用户是否为管理员
   * @returns 是否为管理员
   */
  const isAdmin = useCallback((): boolean => {
    return hasRole('admin') || hasRole('super_admin');
  }, [hasRole]);

  /**
   * 检查用户是否为作者
   * @returns 是否为作者
   */
  const isAuthor = useCallback((): boolean => {
    return hasRole('author') || isAdmin();
  }, [hasRole, isAdmin]);

  /**
   * 检查用户是否拥有指定权限
   * @param permission 权限名称
   */
  const hasPermission = useCallback(
    (permission: string): boolean => {
      // 未登录用户无权限
      if (!isAuthenticated || !user) return false;
      if (isAdmin()) return true; // 管理员拥有所有权限

      return user.permissions.includes(permission);
    },
    [isAuthenticated, user, isAdmin]
  );

  return {
    // 状态
    roles,
    isAuthenticated,

    // 角色检查
    hasRole,
    hasAnyRole,
    hasAllRoles,
    isAdmin,
    isAuthor,

    // 权限检查
    hasPermission,
  };
};
