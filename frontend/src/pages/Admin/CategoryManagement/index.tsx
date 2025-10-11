/**
 * 分类管理页面
 * 管理员用于创建、编辑、删除博客分类的页面
 */

import { createCategoryApi, deleteCategoryApi, updateCategoryApi } from '@/api';
import { Loading, UserLayout } from '@/components';
import { useCategory } from '@/hooks';
import type { CategoryTreeResponse } from '@/types';
import { Button, Input, message, Modal, Space, Table } from 'antd';
import { useEffect, useState } from 'react';
import { CategoryForm } from './components/CategoryForm';
import { categoryColumns } from './config';

const { Search } = Input;

const CategoryManagement = () => {
  const { pageCategories, isFetchingCategories, fetchPageCategories } =
    useCategory();

  // 表单状态
  const [formVisible, setFormVisible] = useState(false);
  const [editingCategory, setEditingCategory] =
    useState<CategoryTreeResponse | null>(null);

  // 搜索关键词
  const [keyword, setKeyword] = useState<string>('');

  // 初始加载
  useEffect(() => {
    fetchPageCategories({
      pageable: { currentPage: 1, pageSize: 100 },
      keyword,
    });
  }, [fetchPageCategories, keyword]);

  // 创建分类
  const handleCreate = () => {
    setEditingCategory(null);
    setFormVisible(true);
  };

  // 编辑分类
  const handleEdit = (record: CategoryTreeResponse) => {
    setEditingCategory(record);
    setFormVisible(true);
  };

  // 删除分类
  const handleDelete = (id: number) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除该分类吗？删除后将无法恢复。',
      okText: '确定',
      cancelText: '取消',
      okButtonProps: { danger: true },
      onOk: async () => {
        try {
          await deleteCategoryApi(id);
          message.success('删除成功');
          fetchPageCategories({
            pageable: { currentPage: 1, pageSize: 100 },
            keyword,
          });
        } catch {
          message.error('删除失败');
        }
      },
    });
  };

  // 提交表单
  const handleSubmit = async (values: {
    name: string;
    slug?: string;
    description?: string;
    parentId?: number;
    sortOrder?: number;
  }) => {
    try {
      if (editingCategory) {
        await updateCategoryApi(editingCategory.id, values);
        message.success('更新成功');
      } else {
        await createCategoryApi(values);
        message.success('创建成功');
      }
      setFormVisible(false);
      fetchPageCategories({
        pageable: { currentPage: 1, pageSize: 100 },
        keyword,
      });
    } catch {
      message.error(editingCategory ? '更新失败' : '创建失败');
    }
  };

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
          onSearch={value => setKeyword(value)}
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
        onCancel={() => setFormVisible(false)}
      />
    </UserLayout>
  );
};

export default CategoryManagement;
