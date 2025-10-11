/**
 * 用户管理数据获取 Hook
 * 封装用户管理相关数据获取逻辑和操作方法
 */

import {
  adminDeleteUserApi,
  adminGetUsersApi,
  adminUpdateUserRolesApi,
  adminUpdateUserStatusApi,
} from '@/api';
import type { UserListItem } from '@/types';
import { message, Modal } from 'antd';
import { useCallback, useState } from 'react';
import { useImmer } from 'use-immer';

export const useUserManagement = () => {
  // 用户列表数据
  const [userList, setUserList] = useState<{
    data: UserListItem[];
    currentPage: number;
    pageSize: number;
    totalRecords: number;
    totalPages: number;
  } | null>(null);
  const [isFetching, setIsFetching] = useState(false);

  // 查询参数
  const [params, setParams] = useImmer({
    pageable: { currentPage: 1, pageSize: 20, sort: 'createdAt,desc' },
    keyword: undefined as string | undefined,
    status: undefined as string | undefined,
    accountType: undefined as string | undefined,
    emailVerified: undefined as boolean | undefined,
  });

  // 角色编辑表单状态
  const [roleFormVisible, setRoleFormVisible] = useState(false);
  const [editingUserId, setEditingUserId] = useState<number | null>(null);
  const [editingUserRoles, setEditingUserRoles] = useState<string[]>([]);

  // 获取用户列表
  const fetchUsers = useCallback(async () => {
    setIsFetching(true);
    try {
      const res = await adminGetUsersApi(params);
      setUserList(res);
    } catch {
      message.error('获取用户列表失败');
    } finally {
      setIsFetching(false);
    }
  }, [params]);

  // 搜索处理
  const handleSearch = useCallback(
    (value: string) => {
      setParams(draft => {
        draft.keyword = value || undefined;
        draft.pageable.currentPage = 1;
      });
    },
    [setParams]
  );

  // 筛选状态
  const handleFilterStatus = useCallback(
    (value: string) => {
      setParams(draft => {
        draft.status = value;
        draft.pageable.currentPage = 1;
      });
    },
    [setParams]
  );

  // 筛选账号类型
  const handleFilterAccountType = useCallback(
    (value: string) => {
      setParams(draft => {
        draft.accountType = value;
        draft.pageable.currentPage = 1;
      });
    },
    [setParams]
  );

  // 筛选邮箱验证状态
  const handleFilterEmailVerified = useCallback(
    (value: boolean) => {
      setParams(draft => {
        draft.emailVerified = value;
        draft.pageable.currentPage = 1;
      });
    },
    [setParams]
  );

  // 分页处理
  const handlePageChange = useCallback(
    (page: number, pageSize: number) => {
      setParams(draft => {
        draft.pageable.currentPage = page;
        draft.pageable.pageSize = pageSize;
      });
    },
    [setParams]
  );

  // 更新用户状态
  const handleUpdateStatus = useCallback(
    (id: number, status: 'active' | 'inactive' | 'banned') => {
      const actionText =
        status === 'active'
          ? '激活'
          : status === 'banned'
            ? '封禁'
            : '设置为未激活';

      Modal.confirm({
        title: `${actionText}用户`,
        content: `确定要${actionText}该用户吗？`,
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          try {
            await adminUpdateUserStatusApi(id, status);
            message.success(`已${actionText}`);
            fetchUsers();
          } catch {
            message.error('操作失败');
          }
        },
      });
    },
    [fetchUsers]
  );

  // 编辑用户角色
  const handleUpdateRoles = useCallback((id: number) => {
    // TODO: 获取用户当前角色
    setEditingUserId(id);
    setEditingUserRoles([]); // 这里应该从API获取当前用户的角色
    setRoleFormVisible(true);
  }, []);

  // 提交角色更新
  const handleSubmitRoles = useCallback(
    async (roles: string[]) => {
      if (!editingUserId) return;

      try {
        await adminUpdateUserRolesApi(editingUserId, roles);
        message.success('角色更新成功');
        setRoleFormVisible(false);
        fetchUsers();
      } catch {
        message.error('角色更新失败');
      }
    },
    [editingUserId, fetchUsers]
  );

  // 删除用户
  const handleDelete = useCallback(
    (id: number) => {
      Modal.confirm({
        title: '确认删除',
        content: '确定要删除该用户吗？删除后将无法恢复。',
        okText: '确定',
        cancelText: '取消',
        okButtonProps: { danger: true },
        onOk: async () => {
          try {
            await adminDeleteUserApi(id);
            message.success('删除成功');
            fetchUsers();
          } catch {
            message.error('删除失败');
          }
        },
      });
    },
    [fetchUsers]
  );

  // 关闭角色表单
  const handleCloseRoleForm = useCallback(() => {
    setRoleFormVisible(false);
  }, []);

  return {
    // 数据
    userList,
    isFetching,

    // 角色表单状态
    roleFormVisible,
    editingUserRoles,

    // 操作方法
    fetchUsers,
    handleSearch,
    handleFilterStatus,
    handleFilterAccountType,
    handleFilterEmailVerified,
    handlePageChange,
    handleUpdateStatus,
    handleUpdateRoles,
    handleSubmitRoles,
    handleDelete,
    handleCloseRoleForm,
  };
};
