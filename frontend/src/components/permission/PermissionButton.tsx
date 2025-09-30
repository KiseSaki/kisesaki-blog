import { usePermissions } from '@/hooks';
import React from 'react';

/**
 * 权限控制按钮组件的属性接口
 */
interface PermissionButtonProps {
  /** 需要的权限名称 */
  permission: string;
  /** 子组件内容 */
  children: React.ReactNode;
  /** 无权限时显示的内容，默认为 null（不显示） */
  fallback?: React.ReactNode;
  /** 是否使用"或"逻辑检查多个权限（任一权限满足即可） */
  anyPermission?: boolean;
  /** 多个权限列表，与 permission 配合使用 */
  permissions?: string[];
}

/**
 * 权限控制按钮组件
 *
 * 根据用户权限动态显示或隐藏按钮/操作元素
 * 支持单个权限检查和多权限逻辑检查
 *
 * @example
 * // 基础用法 - 单个权限
 * <PermissionButton permission="POST_DELETE_ALL">
 *   <Button danger onClick={handleDelete}>删除文章</Button>
 * </PermissionButton>
 *
 * @example
 * // 多权限 - 任一权限满足即可
 * <PermissionButton
 *   permissions={["POST_EDIT_OWN", "POST_EDIT_ALL"]}
 *   anyPermission={true}
 * >
 *   <Button onClick={handleEdit}>编辑</Button>
 * </PermissionButton>
 *
 * @example
 * // 无权限时显示替代内容
 * <PermissionButton
 *   permission="USER_DELETE"
 *   fallback={<Tooltip title="权限不足"><Button disabled>删除</Button></Tooltip>}
 * >
 *   <Button danger onClick={handleDelete}>删除用户</Button>
 * </PermissionButton>
 */
export const PermissionButton: React.FC<PermissionButtonProps> = ({
  permission,
  permissions = [],
  children,
  fallback = null,
  anyPermission = true,
}) => {
  const { hasPermission } = usePermissions();

  // 构建完整的权限列表
  const allPermissions = permission
    ? [permission, ...permissions]
    : permissions;

  // 权限检查逻辑
  const hasRequiredPermission = (): boolean => {
    if (allPermissions.length === 0) {
      console.warn('PermissionButton: 未提供任何权限要求');
      return false;
    }

    if (anyPermission) {
      // "或"逻辑：任一权限满足即可
      return allPermissions.some(perm => hasPermission(perm));
    } else {
      // "且"逻辑：所有权限都必须满足
      return allPermissions.every(perm => hasPermission(perm));
    }
  };

  return hasRequiredPermission() ? <>{children}</> : <>{fallback}</>;
};

export default PermissionButton;
