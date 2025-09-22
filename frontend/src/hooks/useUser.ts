import {
  checkFollowStatus,
  followUser,
  getCurrentUser,
  getFollowersList,
  getFollowingList,
  getUserById,
  getUserByUsername,
  getUserStats,
  searchUsers,
  unfollowUser,
  updateUserProfile,
  updateUserSettings,
  uploadAvatar,
  uploadCoverImage,
} from "@/api/user";
import type {
  FollowUserParams,
  SearchUsersParams,
  SearchUsersResponse,
  UpdateUserProfileParams,
  UpdateUserSettingsParams,
  UserInfo,
  UserStats,
} from "@/types/user";
import { useCallback, useState } from "react";
import { toast } from "sonner";
import { useAuth } from "./useAuth";

/**
 * 用户相关的自定义 Hook
 * 提供用户数据获取、更新、关注等功能
 */
export const useUser = () => {
  const { updateCurrentUser } = useAuth();
  const [loading, setLoading] = useState(false);

  /**
   * 获取当前用户信息
   */
  const fetchCurrentUser = useCallback(async (): Promise<UserInfo | null> => {
    try {
      setLoading(true);
      return await getCurrentUser();
    } catch (error) {
      console.error("获取当前用户信息失败:", error);
      toast.error("获取用户信息失败");
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  /**
   * 根据用户ID获取用户信息
   */
  const fetchUserById = useCallback(
    async (userId: number): Promise<UserInfo | null> => {
      try {
        setLoading(true);
        return await getUserById(userId);
      } catch (error) {
        console.error("获取用户信息失败:", error);
        toast.error("获取用户信息失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 根据用户名获取用户信息
   */
  const fetchUserByUsername = useCallback(
    async (username: string): Promise<UserInfo | null> => {
      try {
        setLoading(true);
        return await getUserByUsername(username);
      } catch (error) {
        console.error("获取用户信息失败:", error);
        toast.error("获取用户信息失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 获取用户统计信息
   */
  const fetchUserStats = useCallback(
    async (userId?: number): Promise<UserStats | null> => {
      try {
        setLoading(true);
        return await getUserStats(userId);
      } catch (error) {
        console.error("获取用户统计信息失败:", error);
        toast.error("获取用户统计信息失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 更新用户个人资料
   */
  const updateProfile = useCallback(
    async (params: UpdateUserProfileParams): Promise<UserInfo | null> => {
      try {
        setLoading(true);
        const data = await updateUserProfile(params);
        // 更新认证状态中的用户信息
        updateCurrentUser(data);
        toast.success("个人资料更新成功");
        return data;
      } catch (error) {
        console.error("更新个人资料失败:", error);
        toast.error("个人资料更新失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    [updateCurrentUser]
  );

  /**
   * 更新用户设置
   */
  const updateSettings = useCallback(
    async (
      params: UpdateUserSettingsParams
    ): Promise<Record<string, string> | null> => {
      try {
        setLoading(true);
        const data = await updateUserSettings(params);
        toast.success("设置更新成功");
        return data;
      } catch (error) {
        console.error("更新设置失败:", error);
        toast.error("设置更新失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 关注用户
   */
  const handleFollowUser = useCallback(
    async (params: FollowUserParams): Promise<boolean> => {
      try {
        setLoading(true);
        await followUser(params);
        toast.success("关注成功");
        return true;
      } catch (error) {
        console.error("关注失败:", error);
        toast.error("关注失败");
        return false;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 取消关注用户
   */
  const handleUnfollowUser = useCallback(
    async (params: FollowUserParams): Promise<boolean> => {
      try {
        setLoading(true);
        await unfollowUser(params);
        toast.success("取消关注成功");
        return true;
      } catch (error) {
        console.error("取消关注失败:", error);
        toast.error("取消关注失败");
        return false;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 检查是否关注某用户
   */
  const checkFollow = useCallback(async (userId: number): Promise<boolean> => {
    try {
      return await checkFollowStatus(userId);
    } catch (error) {
      console.error("检查关注状态失败:", error);
      return false;
    }
  }, []);

  /**
   * 获取关注列表
   */
  const fetchFollowingList = useCallback(
    async (
      userId?: number,
      page = 1,
      size = 20
    ): Promise<SearchUsersResponse | null> => {
      try {
        setLoading(true);
        return await getFollowingList(userId, page, size);
      } catch (error) {
        console.error("获取关注列表失败:", error);
        toast.error("获取关注列表失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 获取粉丝列表
   */
  const fetchFollowersList = useCallback(
    async (
      userId?: number,
      page = 1,
      size = 20
    ): Promise<SearchUsersResponse | null> => {
      try {
        setLoading(true);
        return await getFollowersList(userId, page, size);
      } catch (error) {
        console.error("获取粉丝列表失败:", error);
        toast.error("获取粉丝列表失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 搜索用户
   */
  const searchUser = useCallback(
    async (params: SearchUsersParams): Promise<SearchUsersResponse | null> => {
      try {
        setLoading(true);
        return await searchUsers(params);
      } catch (error) {
        console.error("搜索用户失败:", error);
        toast.error("搜索用户失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  /**
   * 上传头像
   */
  const handleUploadAvatar = useCallback(
    async (file: File): Promise<string | null> => {
      try {
        setLoading(true);
        const avatarUrl = await uploadAvatar(file);
        // 更新当前用户的头像
        updateCurrentUser({ avatarUrl });
        toast.success("头像上传成功");
        return avatarUrl;
      } catch (error) {
        console.error("头像上传失败:", error);
        toast.error("头像上传失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    [updateCurrentUser]
  );

  /**
   * 上传封面图
   */
  const handleUploadCoverImage = useCallback(
    async (file: File): Promise<string | null> => {
      try {
        setLoading(true);
        const coverImageUrl = await uploadCoverImage(file);
        // 更新当前用户的封面图
        updateCurrentUser({ coverImageUrl });
        toast.success("封面图上传成功");
        return coverImageUrl;
      } catch (error) {
        console.error("封面图上传失败:", error);
        toast.error("封面图上传失败");
        return null;
      } finally {
        setLoading(false);
      }
    },
    [updateCurrentUser]
  );

  return {
    // 状态
    loading,

    // 数据获取
    fetchCurrentUser,
    fetchUserById,
    fetchUserByUsername,
    fetchUserStats,
    fetchFollowingList,
    fetchFollowersList,
    searchUser,

    // 数据更新
    updateProfile,
    updateSettings,

    // 关注操作
    handleFollowUser,
    handleUnfollowUser,
    checkFollow,

    // 文件上传
    handleUploadAvatar,
    handleUploadCoverImage,
  };
};
