import { loginApi } from '@/api/auth';
import { getUserInfoByTokenApi } from '@/api/user';
import { useAuthStore } from '@/stores';
import type { LoginParams, UserInfo } from '@/types';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { toast } from 'sonner';

/**
 * 身份验证相关的自定义 Hook
 * 包含登录、登出、权限验证等逻辑
 */
export const useAuth = () => {
  const {
    user,
    token,
    isAuthenticated,
    isLoading,
    userLoaded,
    setToken,
    setUser,
    updateUser,
    logout,
    setLoading,
    setUserLoaded,
  } = useAuthStore();

  const [loginLoading, setLoginLoading] = useState(false);

  // 从用户信息中提取角色列表
  const roles = useMemo(() => user?.roles || [], [user]);

  /**
   * 获取用户信息
   */
  const fetchUserInfo = useCallback(async (): Promise<boolean> => {
    // 直接从 Zustand store 读取最新 token 和 userLoaded，避免闭包中老旧值
    const currentToken = useAuthStore.getState().token;
    const currentUserLoaded = useAuthStore.getState().userLoaded;
    if (!currentToken || currentUserLoaded) return true;

    try {
      setLoading(true);
      const userRes = await getUserInfoByTokenApi();
      setUser(userRes);
      return true;
    } catch (error) {
      console.error('获取用户信息失败:', error);
      // 如果获取用户信息失败，可能 token 已过期，清除认证状态
      logout();
      return false;
    } finally {
      setLoading(false);
    }
  }, [setLoading, setUser, logout]);

  // 当 token 在 store 中变化且 user 未加载时，自动拉取用户信息
  useEffect(() => {
    if (token && !userLoaded) {
      // fetchUserInfo 内部会再次从 store 读取最新 token，确保一致性
      void fetchUserInfo();
    }
  }, [token, userLoaded, fetchUserInfo]);

  /**
   * 用户登录 - 仅获取 token
   */
  const login = useCallback(
    async (params: LoginParams): Promise<boolean> => {
      try {
        setLoginLoading(true);
        setLoading(true);

        // 只获取令牌
        const tokenRes = await loginApi(params);
        setToken(tokenRes);

        toast.success('登录成功');
        return true;
      } catch (error) {
        console.error('登录失败:', error);
        return false;
      } finally {
        setLoginLoading(false);
        setLoading(false);
      }
    },
    [setToken, setLoading]
  );

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
      return roleList.some(role => roles.includes(role));
    },
    [roles]
  );

  /**
   * 检查用户是否拥有所有指定角色
   */
  const hasAllRoles = useCallback(
    (roleList: string[]): boolean => {
      return roleList.every(role => roles.includes(role));
    },
    [roles]
  );

  /**
   * 检查用户是否为管理员
   */
  const isAdmin = useCallback((): boolean => {
    return hasRole('admin') || hasRole('super_admin');
  }, [hasRole]);

  /**
   * 检查用户是否为作者
   */
  const isAuthor = useCallback((): boolean => {
    return hasRole('author') || isAdmin();
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
        case 'create_post':
        case 'edit_own_post':
        case 'delete_own_post':
          return isAuthor();
        case 'comment':
          return isAuthenticated;
        case 'like':
          return isAuthenticated;
        default:
          return false;
      }
    },
    [isAuthenticated, isAdmin, isAuthor]
  );

  /**
   * 更新当前用户信息
   */
  const updateCurrentUser = useCallback(
    (userData: Partial<UserInfo>) => {
      updateUser(userData);
    },
    [updateUser]
  );

  /**
   * 手动刷新用户信息
   */
  const refreshUserInfo = useCallback(async (): Promise<boolean> => {
    setUserLoaded(false);
    return await fetchUserInfo();
  }, [setUserLoaded, fetchUserInfo]);

  return {
    // 状态
    user,
    token,
    isAuthenticated,
    isLoading,
    loginLoading,
    userLoaded,

    // 权限检查
    hasRole,
    hasAnyRole,
    hasAllRoles,
    isAdmin,
    isAuthor,
    canPerform,

    // 操作
    login,
    fetchUserInfo,
    refreshUserInfo,
    updateCurrentUser,
    logout,
    setLoading,
  };
};
