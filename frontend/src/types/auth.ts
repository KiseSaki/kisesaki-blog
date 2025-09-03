/**
 * 校验相关类型定义
 */

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
