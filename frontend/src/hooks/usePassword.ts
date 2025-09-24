import {
  changePasswordApi,
  forgotPasswordApi,
  resetPasswordApi,
} from '@/api/auth';
import { useCallback, useState } from 'react';
import { toast } from 'sonner';

/**
 * 密码相关操作 Hook
 * - 使用统一的 perform 函数减少重复
 * - 对外仍然暴露每个操作的独立 loading 状态，便于组件按需禁用/显示 spinner
 */
export const usePassword = () => {
  type Key = 'forgot' | 'reset' | 'change';
  const [loadingMap, setLoadingMap] = useState<Record<Key, boolean>>({
    forgot: false,
    reset: false,
    change: false,
  });

  const setLoading = useCallback((key: Key, value: boolean) => {
    setLoadingMap(s => ({ ...s, [key]: value }));
  }, []);

  /**
   * 通用执行器：接收一个返回 Promise 的函数（延迟执行），并管理对应 key 的 loading 与错误处理
   * successMsg 可选用于成功提示
   */
  const perform = useCallback(
    async (
      key: Key,
      fn: () => Promise<unknown>,
      successMsg?: string
    ): Promise<boolean> => {
      try {
        setLoading(key, true);
        await fn();
        if (successMsg) toast.success(successMsg);
        return true;
      } catch (error) {
        console.error(error);
        return false;
      } finally {
        setLoading(key, false);
      }
    },
    [setLoading]
  );

  const forgotPassword = useCallback(
    async (email: string) =>
      perform(
        'forgot',
        () => forgotPasswordApi({ email }),
        '重置密码邮件发送成功，请检查您的邮箱。'
      ),
    [perform]
  );

  const resetPassword = useCallback(
    async (resetToken: string, newPassword: string) =>
      perform(
        'reset',
        () => resetPasswordApi({ resetToken, newPassword }),
        '密码重置成功，请使用新密码登录。'
      ),
    [perform]
  );

  const changePassword = useCallback(
    async (oldPassword: string, newPassword: string) =>
      perform(
        'change',
        () => changePasswordApi({ oldPassword, newPassword }),
        '密码更改成功，请使用新密码登录。'
      ),
    [perform]
  );

  return {
    forgotLoading: loadingMap.forgot,
    resetLoading: loadingMap.reset,
    changeLoading: loadingMap.change,

    forgotPassword,
    resetPassword,
    changePassword,
  };
};
