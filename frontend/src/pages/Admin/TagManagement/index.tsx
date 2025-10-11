/**
 * 标签管理页面
 * 管理员用于创建、编辑、删除博客标签的页面
 */

import {
  adminApproveTagApi,
  adminCreateTagApi,
  adminDeleteTagApi,
  adminGetTagListApi,
  adminUpdateTagApi,
} from '@/api';
import { Loading, UserLayout } from '@/components';
import type { AdminTagListResponse, PageResponse } from '@/types';
import {
  Button,
  Input,
  message,
  Modal,
  Pagination,
  Select,
  Space,
  Table,
} from 'antd';
import { useCallback, useEffect, useState } from 'react';
import { useImmer } from 'use-immer';
import { TagForm } from './components/TagForm';
import { approvalStatusOptions, tagColumns } from './config';

const { Search } = Input;

const TagManagement = () => {
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

  // 初始加载
  useEffect(() => {
    fetchTags();
  }, [fetchTags]);

  // 创建标签
  const handleCreate = () => {
    setEditingTag(null);
  };

  // 编辑标签
  const handleEdit = (record: AdminTagListResponse) => {
    setEditingTag(record);
  };
  // 当 editingTag 变化时打开表单（包括第一次）
  useEffect(() => {
    if (editingTag) {
      setFormVisible(true);
    }
  }, [editingTag]);

  // 删除标签
  const handleDelete = (id: number) => {
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
  };

  // 审核标签
  const handleApprove = (id: number, status: 'approved' | 'rejected') => {
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
  };

  // 提交表单
  const handleSubmit = async (values: {
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
  };

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
            onSearch={value =>
              setParams(draft => {
                draft.name = value || undefined;
                draft.pageable.currentPage = 1;
              })
            }
            style={{ width: 250 }}
            loading={isFetching}
          />
          <Select
            placeholder='审核状态'
            allowClear
            options={approvalStatusOptions}
            onChange={value =>
              setParams(draft => {
                draft.approvalStatus = value;
                draft.pageable.currentPage = 1;
              })
            }
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
            onChange={(page, pageSize) => {
              setParams(draft => {
                draft.pageable.currentPage = page;
                draft.pageable.pageSize = pageSize;
              });
            }}
          />
        </div>
      </div>

      {/* 标签表单弹窗 */}
      <TagForm
        visible={formVisible}
        initialValues={editingTag || undefined}
        onSubmit={handleSubmit}
        onCancel={() => setFormVisible(false)}
      />
    </UserLayout>
  );
};

export default TagManagement;
