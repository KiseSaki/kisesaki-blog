import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { AuthState, User } from '../types/auth';

/**
 * 用户认证状态管理
 * 使用 Zustand 进行全局状态管理，并持久化 token 和 user 信息
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      
      /**
       * 设置用户信息和 token
       */
      setUserAndToken: (user: User, token: string) => {
        set({ user, token });
      },
      
      /**
       * 退出登录，清除用户信息和 token
       */
      logout: () => {
        set({ user: null, token: null });
      },
    }),
    {
      name: 'auth-storage', // localStorage 中的键名
      partialize: (state) => ({ 
        token: state.token, 
        user: state.user 
      }), // 只持久化 token 和 user
    }
  )
);
