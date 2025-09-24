import {
  registerApi,
  resendVerificationCodeApi,
  verifyEmailApi,
} from '@/api/auth';
import type { RegisterParams, VerifyEmailParams } from '@/types';
import { useCallback, useState } from 'react';
import { toast } from 'sonner';

/**
 * 用户注册相关的自定义 Hook
 * 专门处理注册流程和邮箱验证
 */
export const useRegister = () => {
  const [registerLoading, setRegisterLoading] = useState(false);
  const [verifyEmailLoading, setVerifyEmailLoading] = useState(false);
  const [resendLoading, setResendLoading] = useState(false);

  /**
   * 用户注册
   * @param params 注册参数
   * @returns 注册是否成功
   */
  const register = useCallback(
    async (params: RegisterParams): Promise<boolean> => {
      try {
        setRegisterLoading(true);

        await registerApi(params);
        toast.success('注册成功，请查收邮箱验证码');
        return true;
      } catch (error) {
        console.error('注册失败:', error);
        toast.error('注册失败，请重试');
        return false;
      } finally {
        setRegisterLoading(false);
      }
    },
    []
  );

  /**
   * 验证邮箱
   * @param params 验证邮箱参数
   * @returns 验证是否成功
   */
  const verifyEmail = useCallback(
    async (params: VerifyEmailParams): Promise<boolean> => {
      try {
        setVerifyEmailLoading(true);

        await verifyEmailApi(params);
        toast.success('邮箱验证成功，请登录');
        return true;
      } catch (error) {
        console.error('邮箱验证失败:', error);
        return false;
      } finally {
        setVerifyEmailLoading(false);
      }
    },
    []
  );

  /**
   * 重新发送验证码
   * @param email 邮箱地址
   * @returns 发送是否成功
   */
  const resendVerificationCode = useCallback(
    async (email: string): Promise<boolean> => {
      try {
        setResendLoading(true);

        await resendVerificationCodeApi(email);
        toast.success('验证码已重新发送，请查收邮箱');
        return true;
      } catch (error) {
        console.error('重新发送验证码失败:', error);
        toast.error('重新发送验证码失败，请稍后重试');
        return false;
      } finally {
        setResendLoading(false);
      }
    },
    []
  );

  return {
    // 状态
    registerLoading,
    verifyEmailLoading,
    resendLoading,
    loading: registerLoading || verifyEmailLoading || resendLoading,

    // 操作
    register,
    verifyEmail,
    resendVerificationCode,
  };
};
