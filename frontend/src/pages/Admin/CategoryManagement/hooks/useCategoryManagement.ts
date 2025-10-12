/**
 * 分类管理数据获取 Hook
 * 封装分类管理相关数据获取逻辑和操作方法
 */

import { createCategoryApi, deleteCategoryApi, updateCategoryApi } from '@/api';
import { useCategory } from '@/hooks';
import type { CategoryTreeResponse } from '@/types';
import { message, Modal } from 'antd';
import { useCallback, useState } from 'react';

export const useCategoryManagement = () => {
  const { pageCategories, isFetchingCategories, fetchPageCategories } =
    useCategory();

  // 表单状态
  const [formVisible, setFormVisible] = useState(false);
  const [editingCategory, setEditingCategory] =
    useState<CategoryTreeResponse | null>(null);

  // 搜索关键词
  const [keyword, setKeyword] = useState<string>('');

  // 加载分类列表
  const loadCategories = useCallback(
    (searchKeyword?: string) => {
      const finalKeyword =
        searchKeyword !== undefined ? searchKeyword : keyword;
      fetchPageCategories({
        pageable: { currentPage: 1, pageSize: 100 },
        keyword: finalKeyword,
      });
    },
    [fetchPageCategories, keyword]
  );

  // 创建分类
  const handleCreate = useCallback(() => {
    setEditingCategory(null);
    setFormVisible(true);
  }, []);

  // 编辑分类
  const handleEdit = useCallback((record: CategoryTreeResponse) => {
    setEditingCategory(record);
    setFormVisible(true);
  }, []);

  // 删除分类
  const handleDelete = useCallback(
    (id: number) => {
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
            loadCategories();
          } catch {
            message.error('删除失败');
          }
        },
      });
    },
    [loadCategories]
  );

  // 提交表单
  const handleSubmit = useCallback(
    async (values: {
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
        loadCategories();
      } catch {
        message.error(editingCategory ? '更新失败' : '创建失败');
      }
    },
    [editingCategory, loadCategories]
  );

  // 搜索处理
  const handleSearch = useCallback(
    (value: string) => {
      setKeyword(value);
      loadCategories(value);
    },
    [loadCategories]
  );

  // 关闭表单
  const handleCloseForm = useCallback(() => {
    setFormVisible(false);
  }, []);

  return {
    // 数据
    pageCategories,
    isFetchingCategories,
    keyword,

    // 表单状态
    formVisible,
    editingCategory,

    // 操作方法
    loadCategories,
    handleCreate,
    handleEdit,
    handleDelete,
    handleSubmit,
    handleSearch,
    handleCloseForm,
  };
};
