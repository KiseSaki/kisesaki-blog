/**
 * 用户管理页面
 * 管理员用于管理用户账户、权限、状态的页面
 */

import { Loading, UserLayout } from '@/components';
import type { UserListItem } from '@/types';
import {
  adminDeleteUserApi,
  adminGetUsersApi,
  adminUpdateUserRolesApi,
  adminUpdateUserStatusApi,
} from '@/api';
import { Input, message, Modal, Pagination, Select, Space, Table } from 'antd';
import { useCallback, useEffect, useState } from 'react';
import { useImmer } from 'use-immer';
import {
  accountTypeOptions,
  statusOptions,
  userColumns,
} from './UserManagement/config';
import { UserRoleForm } from './UserManagement/components/UserRoleForm';

const { Search } = Input;

const UserManagement = () => {
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

  // 初始加载
  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  // 更新用户状态
  const handleUpdateStatus = (
    id: number,
    status: 'active' | 'inactive' | 'banned'
  ) => {
    const actionText =
      status === 'active' ? '激活' : status === 'banned' ? '封禁' : '设置为未激活';

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
  };

  // 编辑用户角色
  const handleUpdateRoles = (id: number) => {
    // TODO: 获取用户当前角色
    setEditingUserId(id);
    setEditingUserRoles([]); // 这里应该从API获取当前用户的角色
    setRoleFormVisible(true);
  };

  // 提交角色更新
  const handleSubmitRoles = async (roles: string[]) => {
    if (!editingUserId) return;

    try {
      await adminUpdateUserRolesApi(editingUserId, roles);
      message.success('角色更新成功');
      setRoleFormVisible(false);
      fetchUsers();
    } catch {
      message.error('角色更新失败');
    }
  };

  // 删除用户
  const handleDelete = (id: number) => {
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
  };

  if (!userList) {
    return <Loading />;
  }

  const { currentPage, pageSize, totalRecords } = userList;

  return (
    <UserLayout title='用户管理' description='管理用户账户、权限和状态'>
      {/* 工具栏 */}
      <Space
        style={{
          width: '100%',
          display: 'flex',
          justifyContent: 'space-between',
        }}
      >
        <Space>
          <Search
            placeholder='搜索用户名或邮箱'
            allowClear
            onSearch={value =>
              setParams(draft => {
                draft.keyword = value || undefined;
                draft.pageable.currentPage = 1;
              })
            }
            style={{ width: 300 }}
            loading={isFetching}
          />
          <Select
            placeholder='用户状态'
            allowClear
            options={statusOptions}
            onChange={value =>
              setParams(draft => {
                draft.status = value;
                draft.pageable.currentPage = 1;
              })
            }
            style={{ width: 120 }}
          />
          <Select
            placeholder='账号类型'
            allowClear
            options={accountTypeOptions}
            onChange={value =>
              setParams(draft => {
                draft.accountType = value;
                draft.pageable.currentPage = 1;
              })
            }
            style={{ width: 120 }}
          />
          <Select
            placeholder='邮箱验证'
            allowClear
            options={[
              { label: '已验证', value: true },
              { label: '未验证', value: false },
            ]}
            onChange={value =>
              setParams(draft => {
                draft.emailVerified = value;
                draft.pageable.currentPage = 1;
              })
            }
            style={{ width: 120 }}
          />
        </Space>
      </Space>

      {/* 用户表格 */}
      <div className='bg-card rounded-lg flex-1 flex flex-col gap-2 justify-between p-2 mt-4'>
        {isFetching ? (
          <Loading />
        ) : (
          <Table
            columns={userColumns(
              handleUpdateStatus,
              handleUpdateRoles,
              handleDelete
            )}
            dataSource={userList.data.map(user => ({ ...user, key: user.id }))}
            pagination={false}
          />
        )}

        <div className='flex justify-between items-center'>
          <span className='text-sm text-theme-secondary-text'>
            共 {totalRecords} 条记录
          </span>
          <Pagination
            current={currentPage}
            pageSize={pageSize}
            total={totalRecords}
            onChange={(page, pageSize) => {
              setParams(draft => {
                draft.pageable.currentPage = page;
                draft.pageable.pageSize = pageSize;
              });
            }}
          />
        </div>
      </div>

      {/* 角色编辑表单 */}
      <UserRoleForm
        visible={roleFormVisible}
        initialRoles={editingUserRoles}
        onSubmit={handleSubmitRoles}
        onCancel={() => setRoleFormVisible(false)}
      />
    </UserLayout>
  );
};

export default UserManagement;

