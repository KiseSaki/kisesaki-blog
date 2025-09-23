/**
 * 用户相关 API 接口函数
 * 包含用户信息、个人资料等用户相关的 API 调用
 */

import httpClient from '../lib/client';
import type {
  FollowUserParams,
  SearchUsersParams,
  SearchUsersResponse,
  UpdateUserProfileParams,
  UpdateUserSettingsParams,
  UserInfo,
  UserStats,
} from '../types/user';

/**
 * 获取当前用户信息
 */
export const getCurrentUserApi = () => {
  return httpClient.get<UserInfo>('/user/profile');
};

/**
 * 根据用户ID获取用户信息
 */
export const getUserByIdApi = (userId: number) => {
  return httpClient.get<UserInfo>(`/user/${userId}`);
};

/**
 * 根据用户名获取用户信息
 */
export const getUserByUsername = (username: string) => {
  return httpClient.get<UserInfo>(`/user/username/${username}`);
};

/**
 * 更新用户个人资料
 */
export const updateUserProfile = (params: UpdateUserProfileParams) => {
  return httpClient.put<UserInfo>('/user/profile', params);
};

/**
 * 更新用户设置
 */
export const updateUserSettings = (params: UpdateUserSettingsParams) => {
  return httpClient.put<Record<string, string>>('/user/settings', params);
};

/**
 * 获取用户统计信息
 */
export const getUserStats = (userId?: number) => {
  const url = userId ? `/user/${userId}/stats` : '/user/stats';
  return httpClient.get<UserStats>(url);
};

/**
 * 关注用户
 */
export const followUser = (params: FollowUserParams) => {
  return httpClient.post<void>('/user/follow', params);
};

/**
 * 取消关注用户
 */
export const unfollowUser = (params: FollowUserParams) => {
  return httpClient.delete<void>(`/user/follow/${params.userId}`);
};

/**
 * 获取关注列表
 */
export const getFollowingList = (
  userId?: number,
  params: { currentPage?: number; pageSize?: number } = {}
) => {
  const url = userId ? `/user/${userId}/following` : '/user/following';
  return httpClient.getPageable<SearchUsersResponse>(url, {
    currentPage: params.currentPage || 1,
    pageSize: params.pageSize || 20,
  });
};

/**
 * 获取粉丝列表
 */
export const getFollowersList = (
  userId?: number,
  params: { currentPage?: number; pageSize?: number } = {}
) => {
  const url = userId ? `/user/${userId}/followers` : '/user/followers';
  return httpClient.getPageable<SearchUsersResponse>(url, {
    currentPage: params.currentPage || 1,
    pageSize: params.pageSize || 20,
  });
};

/**
 * 搜索用户
 */
export const searchUsers = (params: SearchUsersParams) => {
  return httpClient.search<SearchUsersResponse>('/user/search', params);
};

/**
 * 检查是否关注某用户
 */
export const checkFollowStatus = (userId: number) => {
  return httpClient.get<boolean>(`/user/follow/status/${userId}`);
};

/**
 * 上传头像
 */
export const uploadAvatar = (file: File) => {
  return httpClient.post<{ avatarUrl: string }>(
    '/user/avatar',
    {
      avatar: file,
    },
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }
  );
};

/**
 * 上传封面图
 */
export const uploadCoverImage = (file: File) => {
  return httpClient.post<{ coverImageUrl: string }>(
    '/user/cover-image',
    {
      coverImage: file,
    },
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    }
  );
};
