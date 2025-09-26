import { usePermissions } from '@/hooks';
import React from 'react';

/**
 * 权限包装器组件的属性接口
 */
interface PermissionWrapperProps {
  /** 需要的权限名称 */
  permission?: string;
  /** 多个权限列表 */
  permissions?: string[];
  /** 是否使用"或"逻辑检查多个权限（任一权限满足即可），默认为 true */
  anyPermission?: boolean;
  /** 子组件内容 */
  children: React.ReactNode;
  /** 无权限时显示的内容，默认为 null（不显示） */
  fallback?: React.ReactNode;
  /** 是否反转权限逻辑（当没有权限时才显示），默认为 false */
  reverse?: boolean;
}

/**
 * 通用权限包装器组件
 *
 * 比 PermissionButton 更通用，可以包装任何内容，不仅仅是按钮
 *
 * @example
 * // 包装整个表单区域
 * <PermissionWrapper permission="POST_CREATE">
 *   <PostCreateForm />
 * </PermissionWrapper>
 *
 * @example
 * // 反向权限控制 - 只对非管理员显示
 * <PermissionWrapper
 *   permissions={["DASHBOARD_ADMIN_ACCESS"]}
 *   reverse={true}
 *   fallback={<AdminPanel />}
 * >
 *   <UserPanel />
 * </PermissionWrapper>
 */
export const PermissionWrapper: React.FC<PermissionWrapperProps> = ({
  permission,
  permissions = [],
  children,
  fallback = null,
  anyPermission = true,
  reverse = false,
}) => {
  const { hasPermission } = usePermissions();

  // 构建完整的权限列表
  const allPermissions = permission
    ? [permission, ...permissions]
    : permissions;

  // 权限检查逻辑
  const hasRequiredPermission = (): boolean => {
    if (allPermissions.length === 0) {
      // 没有权限要求时，默认允许访问
      return true;
    }

    if (anyPermission) {
      // "或"逻辑：任一权限满足即可
      return allPermissions.some(perm => hasPermission(perm));
    } else {
      // "且"逻辑：所有权限都必须满足
      return allPermissions.every(perm => hasPermission(perm));
    }
  };

  const shouldShowChildren = reverse
    ? !hasRequiredPermission()
    : hasRequiredPermission();

  return shouldShowChildren ? <>{children}</> : <>{fallback}</>;
};

export default PermissionWrapper;
