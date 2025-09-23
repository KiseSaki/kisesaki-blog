import type { AuthState, TokenInfo, UserInfo } from '@/types';
import { create } from 'zustand';
import { persist } from 'zustand/middleware';

/**
 * 用户认证状态管理
 * 使用 Zustand 进行全局状态管理，并持久化 token 和 user 信息
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      userLoaded: false,

      /**
       * 设置令牌信息
       */
      setToken: (token: TokenInfo) => {
        set({
          token,
          isAuthenticated: true,
          userLoaded: false, // 重置用户加载状态
        });
      },

      /**
       * 设置用户信息
       */
      setUser: (user: UserInfo) => {
        set({
          user,
          userLoaded: true,
          isLoading: false,
        });
      },

      /**
       * 设置用户信息和 token（保留兼容性）
       */
      setUserAndToken: (user: UserInfo, token: TokenInfo) => {
        set({
          user,
          token,
          isAuthenticated: true,
          userLoaded: true,
          isLoading: false,
        });
      },

      /**
       * 更新用户信息
       */
      updateUser: (userData: Partial<UserInfo>) => {
        const currentUser = get().user;
        if (currentUser) {
          const updatedUser = { ...currentUser, ...userData };
          set({ user: updatedUser });
        }
      },

      /**
       * 退出登录，清除用户信息和 token
       */
      logout: () => {
        set({
          user: null,
          token: null,
          isAuthenticated: false,
          isLoading: false,
          userLoaded: false,
        });
      },

      /**
       * 设置加载状态
       */
      setLoading: (loading: boolean) => {
        set({ isLoading: loading });
      },

      /**
       * 设置用户加载状态
       */
      setUserLoaded: (loaded: boolean) => {
        set({ userLoaded: loaded });
      },
    }),
    {
      name: 'auth-storage', // localStorage 中的键名
      partialize: state => ({
        token: state.token,
        user: state.user,
        isAuthenticated: state.isAuthenticated,
        userLoaded: state.userLoaded,
      }), // 持久化相关状态
    }
  )
);
