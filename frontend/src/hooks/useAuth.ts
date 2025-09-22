import { useAuthStore } from "@/stores";
import type { AuthUser } from "@/types/auth";
import { useCallback } from "react";

/**
 * 身份验证相关的自定义 Hook
 * 包含登录、登出、权限验证等逻辑
 */
export const useAuth = () => {
  const {
    user,
    token,
    roles,
    isAuthenticated,
    isLoading,
    setUserAndToken,
    updateUser,
    logout,
    setLoading,
  } = useAuthStore();

  /**
   * 检查用户是否拥有指定角色
   */
  const hasRole = useCallback(
    (role: string): boolean => {
      return roles.includes(role);
    },
    [roles]
  );

  /**
   * 检查用户是否拥有任意一个指定角色
   */
  const hasAnyRole = useCallback(
    (roleList: string[]): boolean => {
      return roleList.some((role) => roles.includes(role));
    },
    [roles]
  );

  /**
   * 检查用户是否拥有所有指定角色
   */
  const hasAllRoles = useCallback(
    (roleList: string[]): boolean => {
      return roleList.every((role) => roles.includes(role));
    },
    [roles]
  );

  /**
   * 检查用户是否为管理员
   */
  const isAdmin = useCallback((): boolean => {
    return hasRole("admin") || hasRole("super_admin");
  }, [hasRole]);

  /**
   * 检查用户是否为作者
   */
  const isAuthor = useCallback((): boolean => {
    return hasRole("author") || isAdmin();
  }, [hasRole, isAdmin]);

  /**
   * 检查用户是否可以执行某个操作
   */
  const canPerform = useCallback(
    (permission: string): boolean => {
      // 这里可以根据实际的权限系统实现更复杂的逻辑
      // 现在简单地基于角色进行判断
      if (isAdmin()) return true;

      switch (permission) {
        case "create_post":
        case "edit_own_post":
        case "delete_own_post":
          return isAuthor();
        case "comment":
          return isAuthenticated;
        case "like":
          return isAuthenticated;
        default:
          return false;
      }
    },
    [isAuthenticated, isAdmin, isAuthor]
  );

  /**
   * 设置用户信息和令牌
   */
  const login = useCallback(
    (userData: AuthUser, accessToken: string) => {
      setUserAndToken(userData, accessToken);
    },
    [setUserAndToken]
  );

  /**
   * 更新当前用户信息
   */
  const updateCurrentUser = useCallback(
    (userData: Partial<AuthUser>) => {
      updateUser(userData);
    },
    [updateUser]
  );

  return {
    // 状态
    user,
    token,
    roles,
    isAuthenticated,
    isLoading,

    // 权限检查
    hasRole,
    hasAnyRole,
    hasAllRoles,
    isAdmin,
    isAuthor,
    canPerform,

    // 操作
    login,
    updateCurrentUser,
    logout,
    setLoading,
  };
};
