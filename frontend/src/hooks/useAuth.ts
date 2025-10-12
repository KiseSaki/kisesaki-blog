import { loginApi, resendVerificationEmailApi } from '@/api/auth';
import { getUserInfoByTokenApi } from '@/api/user';
import { useAuthStore } from '@/stores';
import type { LoginParams, UserInfo } from '@/types';
import { useCallback, useEffect, useState } from 'react';
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
  const [resendVerificationLoading, setResendVerificationLoading] =
    useState(false);

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

  /**
   * 重新发送验证邮件
   */
  const resendVerificationEmail = useCallback(async (): Promise<boolean> => {
    try {
      setResendVerificationLoading(true);
      await resendVerificationEmailApi();
      toast.success('验证邮件已发送，请检查您的邮箱');
      return true;
    } catch (error) {
      console.error('发送验证邮件失败:', error);
      toast.error('发送验证邮件失败，请稍后重试');
      return false;
    } finally {
      setResendVerificationLoading(false);
    }
  }, []);

  return {
    // 状态
    user,
    token,
    isAuthenticated,
    isLoading,
    loginLoading,
    resendVerificationLoading,
    userLoaded,

    // 操作
    login,
    fetchUserInfo,
    refreshUserInfo,
    updateCurrentUser,
    resendVerificationEmail,
    logout,
    setLoading,
  };
};
