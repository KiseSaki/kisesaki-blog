/**
 * 分类管理页面
 * 管理员用于创建、编辑、删除博客分类的页面
 */

import { Loading, UserLayout } from '@/components';
import { Button, Input, Space, Table } from 'antd';
import { useEffect } from 'react';
import { CategoryForm } from './components/CategoryForm';
import { categoryColumns } from './config';
import { useCategoryManagement } from './hooks/useCategoryManagement';

const { Search } = Input;

const CategoryManagement = () => {
  const {
    pageCategories,
    isFetchingCategories,
    formVisible,
    editingCategory,
    loadCategories,
    handleCreate,
    handleEdit,
    handleDelete,
    handleSubmit,
    handleSearch,
    handleCloseForm,
  } = useCategoryManagement();

  // 初始加载
  useEffect(() => {
    loadCategories();
  }, [loadCategories]);

  if (!pageCategories) {
    return <Loading />;
  }

  return (
    <UserLayout title='分类管理' description='管理博客文章分类'>
      {/* 工具栏 */}
      <Space
        style={{
          width: '100%',
          display: 'flex',
          justifyContent: 'space-between',
        }}
      >
        <Search
          placeholder='搜索分类'
          enterButton
          allowClear
          onSearch={handleSearch}
          style={{ width: 300 }}
          loading={isFetchingCategories}
        />
        <Button type='primary' onClick={handleCreate}>
          新建分类
        </Button>
      </Space>

      {/* 分类表格 */}
      <div className='bg-card rounded-lg flex-1 flex flex-col p-2 mt-4'>
        {isFetchingCategories ? (
          <Loading />
        ) : (
          <Table
            columns={categoryColumns(handleEdit, handleDelete)}
            dataSource={pageCategories.data.map(cat => ({
              ...cat,
              key: cat.id,
            }))}
            pagination={false}
            expandable={{
              childrenColumnName: 'children',
              defaultExpandAllRows: true,
            }}
          />
        )}
      </div>

      {/* 分类表单弹窗 */}
      <CategoryForm
        visible={formVisible}
        initialValues={editingCategory || undefined}
        categories={pageCategories.data}
        onSubmit={handleSubmit}
        onCancel={handleCloseForm}
      />
    </UserLayout>
  );
};

export default CategoryManagement;
