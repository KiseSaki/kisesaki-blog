/**
 * 认证相关类型定义
 */

import type { UserInfo } from './user';

// 登录
export interface LoginParams {
  username: string;
  password: string;
}

// 注册
export interface RegisterParams {
  username: string;
  password: string;
  email: string;
}

// 验证邮箱
export interface VerifyEmailParams {
  email: string;
  code: string;
}

/**
 * 刷新Token
 * 如果未来需要独立于LoginParams的新的属性，改成interface 并 extends LoginParams
 */
export type RefreshTokenParams = LoginParams;

// 登出当前设备
export interface LogoutParams {
  refreshToken: string;
  deviceId: string;
}

// 修改密码
export interface ChangePasswordParams {
  oldPassword: string;
  newPassword: string;
}

// 忘记密码
export interface ForgotPasswordParams {
  email: string;
}

// 确认重置密码
export interface ResetPasswordParams {
  refreshToken: string;
  newPassword: string;
}

/**
 * OAuth 登录参数
 */
export interface OAuthLoginParams {
  // OAuth 提供商
  provider: 'github' | 'gitee' | 'google';
  // 授权码
  code: string;
  // 重定向URI
  redirectUri?: string;
  // 状态参数
  state?: string;
}

/**
 * OAuth 登录响应
 */
export interface OAuthLoginResponse extends TokenInfo {
  // 是否为新用户
  isNewUser: boolean;
}

/**
 * 认证用户相关
 */
export interface TokenInfo {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  deviceId: string;
}

/**
 * 认证状态接口
 */
export interface AuthState {
  // 当前认证用户
  user: UserInfo | null;
  // 访问令牌
  token: TokenInfo | null;
  // 是否已认证
  isAuthenticated: boolean;
  // 是否正在加载
  isLoading: boolean;
  // 用户信息是否已加载
  userLoaded: boolean;
  // 设置令牌
  setToken: (token: TokenInfo) => void;
  // 设置用户信息
  setUser: (user: UserInfo) => void;
  // 设置用户和令牌（保留兼容性）
  setUserAndToken: (user: UserInfo, token: TokenInfo) => void;
  // 更新用户信息
  updateUser: (user: Partial<UserInfo>) => void;
  // 登出
  logout: () => void;
  // 设置加载状态
  setLoading: (loading: boolean) => void;
  // 设置用户加载状态
  setUserLoaded: (loaded: boolean) => void;
}
