import {
  checkEmailAvailabilityApi,
  checkUsernameAvailabilityApi,
  followUserApi,
  getFollowersListApi,
  getFollowingListApi,
  getUserByIdApi,
  getUserByUsernameApi,
  getUserInfoByTokenApi,
  getUserStatsApi,
  isFollowingApi,
  unfollowUserApi,
  updateAvatarApi,
  updateUserProfileApi,
  updateUserSettingsApi,
  uploadAvatarApi,
} from '@/api/user';
import type {
  UpdateUserProfileParams,
  UpdateUserSettingsParams,
  UserInfo,
  UserListItem,
  UserStats,
} from '@/types/user';
import { useCallback, useState } from 'react';
import { toast } from 'sonner';
import { useAuth } from './useAuth';

/**
 * 用户相关操作的加载状态
 */
interface UserOperationStates {
  fetchingUser: boolean;
  fetchingStats: boolean;
  updatingProfile: boolean;
  updatingSettings: boolean;
  following: boolean;
  unfollowing: boolean;
  fetchingFollows: boolean;
  uploadingAvatar: boolean;
  checkingAvailability: boolean;
}

/**
 * API 响应处理器
 */
interface ApiHandler<T> {
  execute: () => Promise<T>;
  successMessage?: string;
  errorMessage?: string;
  updateAuth?: boolean;
}

/**
 * 用户相关的自定义 Hook
 * 提供用户数据获取、更新、关注等功能
 */
export const useUser = () => {
  const { updateCurrentUser } = useAuth();

  // 分离不同操作的加载状态
  const [operationStates, setOperationStates] = useState<UserOperationStates>({
    fetchingUser: false,
    fetchingStats: false,
    updatingProfile: false,
    updatingSettings: false,
    following: false,
    unfollowing: false,
    fetchingFollows: false,
    uploadingAvatar: false,
    checkingAvailability: false,
  });

  /**
   * 通用API执行器，统一处理错误和加载状态
   */
  const executeApiCall = useCallback(
    async <T>(
      handler: ApiHandler<T>,
      loadingKey: keyof UserOperationStates
    ): Promise<T | null> => {
      try {
        setOperationStates(prev => ({ ...prev, [loadingKey]: true }));

        const result = await handler.execute();

        // 处理成功消息
        if (handler.successMessage) {
          toast.success(handler.successMessage);
        }

        // 更新认证状态
        if (handler.updateAuth && result) {
          updateCurrentUser(result as Partial<UserInfo>);
        }

        return result;
      } catch (error) {
        console.error(`API call failed (${loadingKey}):`, error);
        toast.error(handler.errorMessage || '操作失败，请稍后重试');
        return null;
      } finally {
        setOperationStates(prev => ({ ...prev, [loadingKey]: false }));
      }
    },
    [updateCurrentUser]
  );

  // ============ 用户信息获取 ============

  /**
   * 获取当前用户信息
   */
  const fetchCurrentUser = useCallback(async (): Promise<UserInfo | null> => {
    return executeApiCall(
      {
        execute: () => getUserInfoByTokenApi(),
        errorMessage: '获取用户信息失败',
      },
      'fetchingUser'
    );
  }, [executeApiCall]);

  /**
   * 根据用户ID获取用户信息
   */
  const fetchUserById = useCallback(
    async (userId: number): Promise<UserInfo | null> => {
      return executeApiCall(
        {
          execute: () => getUserByIdApi(userId),
          errorMessage: '获取用户信息失败',
        },
        'fetchingUser'
      );
    },
    [executeApiCall]
  );

  /**
   * 根据用户名获取用户信息
   */
  const fetchUserByUsername = useCallback(
    async (username: string): Promise<UserInfo | null> => {
      return executeApiCall(
        {
          execute: () => getUserByUsernameApi(username),
          errorMessage: '获取用户信息失败',
        },
        'fetchingUser'
      );
    },
    [executeApiCall]
  );

  /**
   * 获取用户统计信息
   */
  const fetchUserStats = useCallback(
    async (userId: number): Promise<UserStats | null> => {
      return executeApiCall(
        {
          execute: () => getUserStatsApi(userId),
          errorMessage: '获取用户统计信息失败',
        },
        'fetchingStats'
      );
    },
    [executeApiCall]
  );
  
  // ============ 关注功能 ============

  /**
   * 关注用户
   */
  const followUser = useCallback(
    async (userId: number): Promise<boolean> => {
      const result = await executeApiCall(
        {
          execute: () => followUserApi(userId),
          successMessage: '关注成功',
          errorMessage: '关注失败',
        },
        'following'
      );
      return result !== null;
    },
    [executeApiCall]
  );

  /**
   * 取消关注用户
   */
  const unfollowUser = useCallback(
    async (userId: number): Promise<boolean> => {
      const result = await executeApiCall(
        {
          execute: () => unfollowUserApi(userId),
          successMessage: '取消关注成功',
          errorMessage: '取消关注失败',
        },
        'unfollowing'
      );
      return result !== null;
    },
    [executeApiCall]
  );

  /**
   * 检查是否关注某用户
   */
  const checkFollowStatus = useCallback(
    async (userId: number): Promise<boolean> => {
      try {
        return await isFollowingApi(userId);
      } catch (error) {
        console.error('检查关注状态失败:', error);
        return false;
      }
    },
    []
  );

  /**
   * 获取关注列表
   */
  const fetchFollowingList = useCallback(
    async (userId: number): Promise<UserListItem[] | null> => {
      return executeApiCall(
        {
          execute: () => getFollowingListApi(userId),
          errorMessage: '获取关注列表失败',
        },
        'fetchingFollows'
      );
    },
    [executeApiCall]
  );

  /**
   * 获取粉丝列表
   */
  const fetchFollowersList = useCallback(
    async (userId: number): Promise<UserListItem[] | null> => {
      return executeApiCall(
        {
          execute: () => getFollowersListApi(userId),
          errorMessage: '获取粉丝列表失败',
        },
        'fetchingFollows'
      );
    },
    [executeApiCall]
  );

  // ============ 可用性检查 ============

  /**
   * 检查用户名是否可用
   */
  const checkUsernameAvailability = useCallback(
    async (username: string): Promise<boolean> => {
      const result = await executeApiCall(
        {
          execute: () => checkUsernameAvailabilityApi(username),
          errorMessage: '检查用户名可用性失败',
        },
        'checkingAvailability'
      );
      return result ?? false;
    },
    [executeApiCall]
  );

  /**
   * 检查邮箱是否可用
   */
  const checkEmailAvailability = useCallback(
    async (email: string): Promise<boolean> => {
      const result = await executeApiCall(
        {
          execute: () => checkEmailAvailabilityApi(email),
          errorMessage: '检查邮箱可用性失败',
        },
        'checkingAvailability'
      );
      return result ?? false;
    },
    [executeApiCall]
  );

  // ============ 返回值 ============

  return {
    // 加载状态
    loading: {
      fetchingUser: operationStates.fetchingUser,
      fetchingStats: operationStates.fetchingStats,
      updatingProfile: operationStates.updatingProfile,
      updatingSettings: operationStates.updatingSettings,
      following: operationStates.following,
      unfollowing: operationStates.unfollowing,
      fetchingFollows: operationStates.fetchingFollows,
      uploadingAvatar: operationStates.uploadingAvatar,
      checkingAvailability: operationStates.checkingAvailability,
      // 便捷的组合状态
      anyLoading: Object.values(operationStates).some(Boolean),
    },

    // 用户信息获取
    userInfo: {
      fetchCurrentUser,
      fetchUserById,
      fetchUserByUsername,
      fetchUserStats,
    },

    // 用户资料管理
    profile: {
      updateProfile,
      updateSettings,
      uploadAvatar,
      updateAvatarUrl,
    },

    // 关注功能
    follow: {
      followUser,
      unfollowUser,
      checkFollowStatus,
      fetchFollowingList,
      fetchFollowersList,
    },

    // 可用性检查
    availability: {
      checkUsernameAvailability,
      checkEmailAvailability,
    },
  };
};
