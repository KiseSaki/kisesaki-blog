import { updateUserProfileApi } from '@/api/user';
import { useAuth } from '@/hooks';
import type { UserInfo } from '@/types';
import { useState } from 'react';
import { toast } from 'sonner';

export const useUserProfile = () => {
  const { user, refreshUserInfo } = useAuth();
  // 临时存用户信息
  const [userInfo, setUserInfo] = useState<UserInfo | null>(() => user || null);

  // 保存用户信息
  const [isLoading, setIsLoading] = useState(false);
  const setUserInfoCallback = async (info: UserInfo) => {
    console.log(121323);
    try {
      setIsLoading(true);
      await updateUserProfileApi(info);
      refreshUserInfo();
      toast.success('用户信息更新成功');
    } catch (error) {
      console.log(error);
      toast.error('用户信息更新失败，请稍后重试');
    } finally {
      setIsLoading(false);
    }
  };

  return {
    userInfo,
    isLoading,

    setUserInfo,
    setUserInfoCallback,
  };
};
