/**
 * 标签管理页面
 * 管理员用于创建、编辑、删除博客标签的页面
 */

import { Loading, UserLayout } from '@/components';
import { Button, Input, Pagination, Select, Space, Table } from 'antd';
import { useEffect } from 'react';
import { TagForm } from './components/TagForm';
import { approvalStatusOptions, tagColumns } from './config';
import { useTagManagement } from './hooks/useTagManagement';

const { Search } = Input;

const TagManagement = () => {
  const {
    tagList,
    isFetching,
    formVisible,
    editingTag,
    fetchTags,
    handleSearch,
    handleFilterStatus,
    handlePageChange,
    handleCreate,
    handleEdit,
    handleDelete,
    handleApprove,
    handleSubmit,
    handleCloseForm,
  } = useTagManagement();

  // 初始加载
  useEffect(() => {
    fetchTags();
  }, [fetchTags]);

  if (!tagList) {
    return <Loading />;
  }

  const { currentPage, pageSize, totalRecords } = tagList;

  return (
    <UserLayout title='标签管理' description='管理博客文章标签'>
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
            placeholder='搜索标签'
            enterButton
            allowClear
            onSearch={handleSearch}
            style={{ width: 250 }}
            loading={isFetching}
          />
          <Select
            placeholder='审核状态'
            allowClear
            options={approvalStatusOptions}
            onChange={handleFilterStatus}
            style={{ width: 120 }}
          />
        </Space>
        <Button type='primary' onClick={handleCreate}>
          新建标签
        </Button>
      </Space>

      {/* 标签表格 */}
      <div className='bg-card rounded-lg flex-1 flex flex-col gap-2 justify-between p-2 mt-4'>
        {isFetching ? (
          <Loading />
        ) : (
          <Table
            columns={tagColumns(handleEdit, handleDelete, handleApprove)}
            dataSource={tagList.data.map(tag => ({ ...tag, key: tag.id }))}
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

      {/* 标签表单弹窗 */}
      <TagForm
        visible={formVisible}
        initialValues={editingTag || undefined}
        onSubmit={handleSubmit}
        onCancel={handleCloseForm}
      />
    </UserLayout>
  );
};

export default TagManagement;
