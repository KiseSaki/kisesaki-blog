/**
 * 评论管理数据获取 Hook
 * 封装评论管理相关数据获取逻辑和操作方法
 */

import {
  adminApproveCommentApi,
  adminDeleteCommentApi,
  adminGetCommentsApi,
  adminPinCommentApi,
} from '@/api';
import type { CommentListResponse, PageResponse } from '@/types';
import { message, Modal } from 'antd';
import { useCallback, useState } from 'react';
import { useImmer } from 'use-immer';

export const useCommentManagement = () => {
  // 评论列表数据
  const [commentList, setCommentList] =
    useState<PageResponse<CommentListResponse> | null>(null);
  const [isFetching, setIsFetching] = useState(false);

  // 查询参数
  const [params, setParams] = useImmer({
    pageable: { currentPage: 1, pageSize: 20, sort: 'createdAt,desc' },
    keyword: undefined as string | undefined,
    status: undefined as string | undefined,
    isPinned: undefined as boolean | undefined,
  });

  // 获取评论列表
  const fetchComments = useCallback(async () => {
    setIsFetching(true);
    try {
      const res = await adminGetCommentsApi(params);
      setCommentList(res);
    } catch {
      message.error('获取评论列表失败');
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

  // 筛选置顶状态
  const handleFilterPinned = useCallback(
    (value: boolean) => {
      setParams(draft => {
        draft.isPinned = value;
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

  // 审核评论
  const handleApprove = useCallback(
    (id: number, status: 'APPROVED' | 'REJECTED' | 'SPAM') => {
      const actionText =
        status === 'APPROVED'
          ? '通过'
          : status === 'REJECTED'
            ? '拒绝'
            : '标记为垃圾';

      Modal.confirm({
        title: `${actionText}审核`,
        content: `确定要${actionText}该评论吗？`,
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          try {
            await adminApproveCommentApi(id, status);
            message.success(`已${actionText}`);
            fetchComments();
          } catch {
            message.error('操作失败');
          }
        },
      });
    },
    [fetchComments]
  );

  // 删除评论
  const handleDelete = useCallback(
    (id: number) => {
      Modal.confirm({
        title: '确认删除',
        content: '确定要删除该评论吗？删除后将无法恢复。',
        okText: '确定',
        cancelText: '取消',
        okButtonProps: { danger: true },
        onOk: async () => {
          try {
            await adminDeleteCommentApi(id);
            message.success('删除成功');
            fetchComments();
          } catch {
            message.error('删除失败');
          }
        },
      });
    },
    [fetchComments]
  );

  // 置顶评论
  const handlePin = useCallback(
    (id: number, isPinned: boolean) => {
      Modal.confirm({
        title: isPinned ? '置顶评论' : '取消置顶',
        content: `确定要${isPinned ? '置顶' : '取消置顶'}该评论吗？`,
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          try {
            await adminPinCommentApi(id, isPinned);
            message.success(isPinned ? '已置顶' : '已取消置顶');
            fetchComments();
          } catch {
            message.error('操作失败');
          }
        },
      });
    },
    [fetchComments]
  );

  return {
    // 数据
    commentList,
    isFetching,

    // 操作方法
    fetchComments,
    handleSearch,
    handleFilterStatus,
    handleFilterPinned,
    handlePageChange,
    handleApprove,
    handleDelete,
    handlePin,
  };
};
