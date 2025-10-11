/**
 * 用户管理页面
 * 管理员用于管理用户账户、权限、状态的页面
 */

import { Loading, UserLayout } from '@/components';
import { Input, Pagination, Select, Space, Table } from 'antd';
import { useEffect } from 'react';
import { UserRoleForm } from './components/UserRoleForm';
import { accountTypeOptions, statusOptions, userColumns } from './config';
import { useUserManagement } from './hooks/useUserManagement';

const { Search } = Input;

const UserManagement = () => {
  const {
    userList,
    isFetching,
    roleFormVisible,
    editingUserRoles,
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
  } = useUserManagement();

  // 初始加载
  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

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
            enterButton
            allowClear
            onSearch={handleSearch}
            style={{ width: 300 }}
            loading={isFetching}
          />
          <Select
            placeholder='用户状态'
            allowClear
            options={statusOptions}
            onChange={handleFilterStatus}
            style={{ width: 120 }}
          />
          <Select
            placeholder='账号类型'
            allowClear
            options={accountTypeOptions}
            onChange={handleFilterAccountType}
            style={{ width: 120 }}
          />
          <Select
            placeholder='邮箱验证'
            allowClear
            options={[
              { label: '已验证', value: true },
              { label: '未验证', value: false },
            ]}
            onChange={handleFilterEmailVerified}
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
            onChange={handlePageChange}
          />
        </div>
      </div>

      {/* 角色编辑表单 */}
      <UserRoleForm
        visible={roleFormVisible}
        initialRoles={editingUserRoles}
        onSubmit={handleSubmitRoles}
        onCancel={handleCloseRoleForm}
      />
    </UserLayout>
  );
};

export default UserManagement;
