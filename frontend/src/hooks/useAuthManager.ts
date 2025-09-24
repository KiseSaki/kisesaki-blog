import { useAuth } from './useAuth';
import { usePermissions } from './usePermissions';
import { useRegister } from './useRegister';

/**
 * 组合式认证管理 Hook
 * 整合了认证、注册和权限功能，提供统一的接口
 * 适用于需要多种认证功能的复杂组件
 */
export const useAuthManager = () => {
  const auth = useAuth();
  const permissions = usePermissions();
  const register = useRegister();

  return {
    // 认证相关
    ...auth,

    // 权限相关
    ...permissions,

    // 注册相关
    ...register,

    // 组合的加载状态
    loading: auth.isLoading || register.loading,
    anyLoading: auth.loginLoading || register.loading || auth.isLoading,
  };
};
