/**
 * 评论管理页面
 * 管理员用于审核、回复、删除评论的页面
 */

import {
  adminApproveCommentApi,
  adminDeleteCommentApi,
  adminGetCommentsApi,
  adminPinCommentApi,
} from '@/api';
import { Loading, UserLayout } from '@/components';
import type { CommentListResponse, PageResponse } from '@/types';
import { Input, message, Modal, Pagination, Select, Space, Table } from 'antd';
import { useCallback, useEffect, useState } from 'react';
import { useImmer } from 'use-immer';
import { commentColumns, statusOptions } from './config';

const { Search } = Input;

const CommentManagement = () => {
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

  // 初始加载
  useEffect(() => {
    fetchComments();
  }, [fetchComments]);

  // 审核评论
  const handleApprove = (
    id: number,
    status: 'APPROVED' | 'REJECTED' | 'SPAM'
  ) => {
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
  };

  // 删除评论
  const handleDelete = (id: number) => {
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
  };

  // 置顶评论
  const handlePin = (id: number, isPinned: boolean) => {
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
  };

  if (!commentList) {
    return <Loading />;
  }

  const { currentPage, pageSize, totalRecords } = commentList;

  return (
    <UserLayout title='评论管理' description='管理博客评论和审核'>
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
            placeholder='搜索评论内容'
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
            placeholder='审核状态'
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
            placeholder='置顶状态'
            allowClear
            options={[
              { label: '已置顶', value: true },
              { label: '未置顶', value: false },
            ]}
            onChange={value =>
              setParams(draft => {
                draft.isPinned = value;
                draft.pageable.currentPage = 1;
              })
            }
            style={{ width: 120 }}
          />
        </Space>
      </Space>

      {/* 评论表格 */}
      <div className='bg-card rounded-lg flex-1 flex flex-col gap-2 justify-between p-2 mt-4'>
        {isFetching ? (
          <Loading />
        ) : (
          <Table
            columns={commentColumns(handleApprove, handleDelete, handlePin)}
            dataSource={commentList.data.map(comment => ({
              ...comment,
              key: comment.id,
            }))}
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
    </UserLayout>
  );
};

export default CommentManagement;
