/**
 * 用户相关类型定义
 */
export interface User {
  id: number;
  username: string;
  email: string;
  nickname?: string;
  avatar?: string;
  role: string;
  roles: string[];
  createdAt: string;
  updatedAt: string;
}

/**
 * 认证状态接口
 */
export interface AuthState {
  user: User | null;
  token: string | null;
  roles: string[];
  setUserAndToken: (user: User, token: string) => void;
  logout: () => void;
}
