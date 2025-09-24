/**
 * 身份验证相关 API 接口函数
 * 包含登录、注册、OAuth 等认证相关的 API 调用
 */
import { httpClient } from '@/lib';
import type {
  ChangePasswordParams,
  ForgotPasswordParams,
  LoginParams,
  LogoutParams,
  RefreshTokenParams,
  RegisterParams,
  ResetPasswordParams,
  TokenInfo,
  VerifyEmailParams,
} from '@/types';

/**
 * 用户登录
 * @param params 登录参数
 * @returns 登录结果
 */
export const loginApi = (params: LoginParams) =>
  httpClient.post<TokenInfo>('/auth/login', params);

/**
 * 用户注册
 * @param params 注册参数
 * @returns 注册结果
 */
export const registerApi = (params: RegisterParams) =>
  httpClient.post<string>('/auth/register', params);

/**
 * 验证邮箱
 * @param params 验证邮箱参数
 * @returns 验证结果
 */
export const verifyEmailApi = (params: VerifyEmailParams) =>
  httpClient.post<string>('/auth/verify-email', params);

/**
 * 刷新令牌
 * @param params 刷新令牌参数
 * @returns 新的登录响应
 */
export const refreshTokenApi = (params: RefreshTokenParams) =>
  httpClient.post<TokenInfo>('/auth/refreshToken', params);

/**
 * 用户登出
 * @param params 登出参数
 * @returns 登出结果
 */
export const logoutApi = (params: LogoutParams) =>
  httpClient.post<string>('/auth/logout', params);

/**
 * 登出所有设备
 * @returns 登出结果
 */
export const logoutAllDevicesApi = () =>
  httpClient.post<string>('/auth/logout-all');

/**
 * 踢出指定设备
 * @param deviceId 设备ID
 * @returns 踢出结果
 */
export const kickDeviceApi = (deviceId: string) =>
  httpClient.delete(`/auth/devices/${deviceId}`);

/**
 * 获取用户设备列表
 * @returns 设备列表
 */
export const getUserDevicesApi = () =>
  httpClient.get<string[]>('/auth/devices');

/**
 * 修改密码
 * @param params 修改密码参数
 * @returns 修改结果
 */
export const changePasswordApi = (params: ChangePasswordParams) =>
  httpClient.post<string>('/auth/change-password', params);

/**
 * 忘记密码
 * @param params 忘记密码参数
 * @returns 忘记密码结果
 */
export const forgotPasswordApi = (params: ForgotPasswordParams) =>
  httpClient.post<string>('/auth/forgot-password', params);

/**
 * 确认重置密码
 * @param params 重置密码参数
 * @returns 重置结果
 */
export const resetPasswordApi = (params: ResetPasswordParams) =>
  httpClient.post<string>('/auth/reset-password', params);

/**
 * 清理过期令牌
 * @returns 清理结果
 */
export const cleanExpiredTokensApi = () =>
  httpClient.post<string>('/auth/clean-expired');

/**
 * 重新发送邮箱验证码
 * @param email 邮箱地址
 * @returns 发送结果
 */
export const resendVerificationCodeApi = (email: string) =>
  httpClient.post<string>('/auth/resend-verification', { email });
