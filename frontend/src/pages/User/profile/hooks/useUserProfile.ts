import { useAuth } from '@/hooks';
import type { UserInfo } from '@/types';
import { useState } from 'react';

export const useUserProfile = () => {
  const { user } = useAuth();
  // 临时存用户信息
  const [userInfo, setUserInfo] = useState<UserInfo | null>(() => user || null);

  return {
    userInfo,

    setUserInfo,
  };
};
