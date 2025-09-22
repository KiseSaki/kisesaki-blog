/**
 * 认证相关类型定义
 */

import type { User } from "./user";

// 登录
export interface LoginParams {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  deviceId: string;
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
  provider: "github" | "gitee" | "google";
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
export interface OAuthLoginResponse extends LoginResponse {
  // 是否为新用户
  isNewUser: boolean;
}

/**
 * 认证用户信息
 * 继承基础用户信息，并添加认证相关字段
 */
export interface AuthUser extends User {
  // 用户角色
  role: string;
  // 用户权限列表
  roles: string[];
  // 显示名称（来自 profile）
  displayName?: string;
  // 头像URL（来自 profile）
  avatarUrl?: string;
  // 封面图URL（来自 profile）
  coverImageUrl?: string;
  // 个人简介（来自 profile）
  bio?: string;
}

/**
 * 认证状态接口
 */
export interface AuthState {
  // 当前认证用户
  user: AuthUser | null;
  // 访问令牌
  token: string | null;
  // 用户角色列表
  roles: string[];
  // 是否已认证
  isAuthenticated: boolean;
  // 是否正在加载
  isLoading: boolean;
  // 设置用户和令牌
  setUserAndToken: (user: AuthUser, token: string) => void;
  // 更新用户信息
  updateUser: (user: Partial<AuthUser>) => void;
  // 登出
  logout: () => void;
  // 设置加载状态
  setLoading: (loading: boolean) => void;
}
