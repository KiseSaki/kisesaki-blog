/**
 * 身份验证相关 API 接口函数
 * 包含登录、注册、OAuth 等认证相关的 API 调用
 */
import client from "@/lib/client";
import type {
  ChangePasswordParams,
  ForgotPasswordParams,
  LoginParams,
  LoginResponse,
  LogoutParams,
  RefreshTokenParams,
  RegisterParams,
  ResetPasswordParams,
  VerifyEmailParams,
} from "@/types/auth";

/**
 * 用户登录
 * @param params 登录参数
 * @returns 登录结果
 */
export const login = (params: LoginParams): Promise<LoginResponse> =>
  client.post("/auth/login", params);

/**
 * 用户注册
 * @param params 注册参数
 * @returns 注册结果
 */
export const register = (params: RegisterParams): Promise<string> =>
  client.post("/auth/register", params);

/**
 * 验证邮箱
 * @param params 验证邮箱参数
 * @returns 验证结果
 */
export const verifyEmail = (params: VerifyEmailParams): Promise<string> =>
  client.post("/auth/verify-email", params);

/**
 * 刷新令牌
 * @param params 刷新令牌参数
 * @returns 新的登录响应
 */
export const refreshToken = (
  params: RefreshTokenParams
): Promise<LoginResponse> => client.post("/auth/refreshToken", params);

/**
 * 用户登出
 * @param params 登出参数
 * @returns 登出结果
 */
export const logout = (params: LogoutParams): Promise<string> =>
  client.post("/auth/logout", params);

/**
 * 登出所有设备
 * @returns 登出结果
 */
export const logoutAllDevices = (): Promise<string> =>
  client.post("/auth/logout-all");

/**
 * 踢出指定设备
 * @param deviceId 设备ID
 * @returns 踢出结果
 */
export const kickDevice = (deviceId: string): Promise<string> =>
  client.delete(`/auth/devices/${deviceId}`);

/**
 * 获取用户设备列表
 * @returns 设备列表
 */
export const getUserDevices = (): Promise<string[]> =>
  client.get("/auth/devices");

/**
 * 修改密码
 * @param params 修改密码参数
 * @returns 修改结果
 */
export const changePassword = (params: ChangePasswordParams): Promise<string> =>
  client.post("/auth/change-password", params);

/**
 * 忘记密码
 * @param params 忘记密码参数
 * @returns 忘记密码结果
 */
export const forgotPassword = (params: ForgotPasswordParams): Promise<string> =>
  client.post("/auth/forgot-password", params);

/**
 * 确认重置密码
 * @param params 重置密码参数
 * @returns 重置结果
 */
export const resetPassword = (params: ResetPasswordParams): Promise<string> =>
  client.post("/auth/reset-password", params);

/**
 * 清理过期令牌
 * @returns 清理结果
 */
export const cleanExpiredTokens = (): Promise<string> =>
  client.post("/auth/clean-expired");
