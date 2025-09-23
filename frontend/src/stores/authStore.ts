import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { AuthState, AuthUser } from '../types/auth';

/**
 * 用户认证状态管理
 * 使用 Zustand 进行全局状态管理，并持久化 token 和 user 信息
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      user: null,
      token: null,
      roles: [],
      isAuthenticated: false,
      isLoading: false,

      /**
       * 设置用户信息和 token
       */
      setUserAndToken: (user: AuthUser, token: string) => {
        set({
          user,
          token,
          roles: user.roles || [user.role], // 兼容单个角色或多个角色
          isAuthenticated: true,
          isLoading: false,
        });
      },

      /**
       * 更新用户信息
       */
      updateUser: (userData: Partial<AuthUser>) => {
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
          roles: [],
          isAuthenticated: false,
          isLoading: false,
        });
      },

      /**
       * 设置加载状态
       */
      setLoading: (loading: boolean) => {
        set({ isLoading: loading });
      },
    }),
    {
      name: 'auth-storage', // localStorage 中的键名
      partialize: state => ({
        token: state.token,
        user: state.user,
        roles: state.roles,
        isAuthenticated: state.isAuthenticated,
      }), // 持久化 token、user、roles 和认证状态
    }
  )
);
