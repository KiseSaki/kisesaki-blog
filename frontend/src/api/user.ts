/**
 * 用户相关 API 接口函数
 * 包含用户信息、个人资料等用户相关的 API 调用
 */

import { httpClient } from '@/lib';
import type {
  UpdateUserProfileParams,
  UpdateUserSettingsParams,
  UserInfo,
  UserListItem,
  UserStats,
} from '@/types';

/**
 * 根据token获取当前用户信息
 */
export const getUserInfoByTokenApi = () => {
  return httpClient.post<UserInfo>('/users/getUserInfoByToken');
};

/**
 * 根据用户ID获取用户信息
 */
export const getUserByIdApi = (userId: number) => {
  return httpClient.get<UserInfo>(`/users/${userId}`);
};

/**
 * 根据用户名获取用户信息
 */
export const getUserByUsernameApi = (username: string) => {
  return httpClient.get<UserInfo>(`/users/username/${username}`);
};

/**
 * 获取用户简要信息
 */
export const getUserProfileApi = (userId: number) => {
  return httpClient.get<UserInfo>(`/users/${userId}/profile`);
};

/**
 * 更新用户个人资料
 */
export const updateUserProfileApi = (params: UpdateUserProfileParams) => {
  return httpClient.put<UserInfo>('/users/profile', params);
};

/**
 * 更新用户头像URL
 * @param avatarUrl 头像相对路径（如 /files/hash/uuid.jpg）
 */
export const updateAvatarApi = (avatarUrl: string) => {
  return httpClient.post<UserInfo>('/users/avatar', { avatarUrl });
};

/**
 * 上传用户头像文件
 * @param file 头像文件
 * @returns 返回文件相对路径（如 /files/hash/uuid.jpg）
 */
export const uploadAvatarFileApi = (file: File) => {
  const formData = new FormData();
  formData.append('file', file);

  return httpClient.post<string>('/file/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
};

/**
 * 上传用户头像（旧版本，保留向后兼容）
 * @deprecated 请使用 uploadAvatarFileApi + updateAvatarApi 两步操作
 */
export const uploadAvatarApi = (file: File) => {
  return httpClient.post<UserInfo>(
    '/users/upload-avatar',
    {
      file,
    },
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }
  );
};

/**
 * 获取用户统计信息
 */
export const getUserStatsApi = (userId: number) => {
  return httpClient.get<UserStats>(`/users/${userId}/stats`);
};

/**
 * 获取用户设置
 */
export const getUserSettingsApi = () => {
  return httpClient.get<Record<string, string>>('/users/settings');
};

/**
 * 更新用户设置
 */
export const updateUserSettingsApi = (params: UpdateUserSettingsParams) => {
  return httpClient.put<string>('/users/settings', params);
};

/**
 * 检查用户名是否可用
 */
export const checkUsernameAvailabilityApi = (username: string) => {
  return httpClient.get<boolean>(`/users/check/username?username=${username}`);
};

/**
 * 检查邮箱是否可用
 */
export const checkEmailAvailabilityApi = (email: string) => {
  return httpClient.get<boolean>(`/users/check/email?email=${email}`);
};

/**
 * 关注用户
 */
export const followUserApi = (userId: number) => {
  return httpClient.post<string>(`/users/${userId}/follow`);
};

/**
 * 取消关注用户
 */
export const unfollowUserApi = (userId: number) => {
  return httpClient.post<string>(`/users/${userId}/unfollow`);
};

/**
 * 检查是否已关注
 */
export const isFollowingApi = (userId: number) => {
  return httpClient.get<boolean>(`/users/${userId}/is-following`);
};

/**
 * 获取用户关注列表
 */
export const getFollowingListApi = (userId: number) => {
  return httpClient.get<UserListItem[]>(`/users/${userId}/following`);
};

/**
 * 获取用户粉丝列表
 */
export const getFollowersListApi = (userId: number) => {
  return httpClient.get<UserListItem[]>(`/users/${userId}/followers`);
};

/**
 * 获取用户关注统计
 */
export const getFollowCountsApi = (userId: number) => {
  return httpClient.get<{ followingCount: number; followersCount: number }>(
    `/users/${userId}/follow-counts`
  );
};

// =================== 管理员用户管理接口 ===================

/**
 * 获取所有用户列表（管理员）
 * @param params 查询参数
 * @returns 用户列表
 */
export const adminGetUsersApi = (params?: {
  pageable?: { currentPage?: number; pageSize?: number; sort?: string };
  keyword?: string;
  status?: string;
  accountType?: string;
  emailVerified?: boolean;
}) =>
  httpClient.get<{
    data: UserListItem[];
    currentPage: number;
    pageSize: number;
    totalRecords: number;
    totalPages: number;
  }>('/admin/users', { params });

/**
 * 更新用户状态（管理员）
 * @param id 用户ID
 * @param status 用户状态
 * @returns 更新结果
 */
export const adminUpdateUserStatusApi = (
  id: number,
  status: 'active' | 'inactive' | 'banned'
) => httpClient.post<void>(`/admin/users/${id}/status`, { status });

/**
 * 删除用户（管理员）
 * @param id 用户ID
 * @returns 删除结果
 */
export const adminDeleteUserApi = (id: number) =>
  httpClient.delete<void>(`/admin/users/${id}`);

/**
 * 更新用户角色（管理员）
 * @param id 用户ID
 * @param roles 角色列表
 * @returns 更新结果
 */
export const adminUpdateUserRolesApi = (id: number, roles: string[]) =>
  httpClient.post<void>(`/admin/users/${id}/roles`, { roles });
