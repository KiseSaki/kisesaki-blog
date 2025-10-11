/**
 * 标签管理数据获取 Hook
 * 封装标签管理相关数据获取逻辑和操作方法
 */

import {
  adminApproveTagApi,
  adminCreateTagApi,
  adminDeleteTagApi,
  adminGetTagListApi,
  adminUpdateTagApi,
} from '@/api';
import type { AdminTagListResponse, PageResponse } from '@/types';
import { message, Modal } from 'antd';
import { useCallback, useState } from 'react';
import { useImmer } from 'use-immer';

export const useTagManagement = () => {
  // 标签列表数据
  const [tagList, setTagList] =
    useState<PageResponse<AdminTagListResponse> | null>(null);
  const [isFetching, setIsFetching] = useState(false);

  // 查询参数
  const [params, setParams] = useImmer({
    pageable: { currentPage: 1, pageSize: 10 },
    name: undefined as string | undefined,
    approvalStatus: undefined as
      | 'pending'
      | 'approved'
      | 'rejected'
      | undefined,
  });

  // 表单状态
  const [formVisible, setFormVisible] = useState(false);
  const [editingTag, setEditingTag] = useState<AdminTagListResponse | null>(
    null
  );

  // 获取标签列表
  const fetchTags = useCallback(async () => {
    setIsFetching(true);
    try {
      const res = await adminGetTagListApi(params);
      setTagList(res);
    } catch {
      message.error('获取标签列表失败');
    } finally {
      setIsFetching(false);
    }
  }, [params]);

  // 搜索处理
  const handleSearch = useCallback(
    (value: string) => {
      setParams(draft => {
        draft.name = value || undefined;
        draft.pageable.currentPage = 1;
      });
    },
    [setParams]
  );

  // 筛选审核状态
  const handleFilterStatus = useCallback(
    (value: 'pending' | 'approved' | 'rejected') => {
      setParams(draft => {
        draft.approvalStatus = value;
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

  // 创建标签
  const handleCreate = useCallback(() => {
    setEditingTag(null);
    setFormVisible(true);
  }, []);

  // 编辑标签
  const handleEdit = useCallback((record: AdminTagListResponse) => {
    setEditingTag(record);
    setFormVisible(true);
  }, []);

  // 删除标签
  const handleDelete = useCallback(
    (id: number) => {
      Modal.confirm({
        title: '确认删除',
        content: '确定要删除该标签吗？删除后将无法恢复。',
        okText: '确定',
        cancelText: '取消',
        okButtonProps: { danger: true },
        onOk: async () => {
          try {
            await adminDeleteTagApi(id);
            message.success('删除成功');
            fetchTags();
          } catch {
            message.error('删除失败');
          }
        },
      });
    },
    [fetchTags]
  );

  // 审核标签
  const handleApprove = useCallback(
    (id: number, status: 'approved' | 'rejected') => {
      Modal.confirm({
        title: status === 'approved' ? '通过审核' : '拒绝审核',
        content: `确定要${status === 'approved' ? '通过' : '拒绝'}该标签吗？`,
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          try {
            await adminApproveTagApi(id, { status });
            message.success(status === 'approved' ? '已通过' : '已拒绝');
            fetchTags();
          } catch {
            message.error('操作失败');
          }
        },
      });
    },
    [fetchTags]
  );

  // 提交表单
  const handleSubmit = useCallback(
    async (values: {
      name: string;
      slug?: string;
      description?: string;
      color?: string;
      isApproved?: boolean;
      approvalNote?: string;
    }) => {
      try {
        if (editingTag) {
          await adminUpdateTagApi(editingTag.id, values);
          message.success('更新成功');
        } else {
          await adminCreateTagApi(values);
          message.success('创建成功');
        }
        setFormVisible(false);
        fetchTags();
      } catch {
        message.error(editingTag ? '更新失败' : '创建失败');
      }
    },
    [editingTag, fetchTags]
  );

  // 关闭表单
  const handleCloseForm = useCallback(() => {
    setFormVisible(false);
  }, []);

  return {
    // 数据
    tagList,
    isFetching,

    // 表单状态
    formVisible,
    editingTag,

    // 操作方法
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
  };
};
