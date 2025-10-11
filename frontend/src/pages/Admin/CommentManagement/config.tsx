/**
 * 评论管理配置文件
 */

import type { CommentListResponse } from '@/types';
import type { TableProps } from 'antd';
import { Tag } from 'antd';

/**
 * 评论状态映射
 */
export const statusMap = {
  PENDING: { text: '待审核', color: 'orange' },
  APPROVED: { text: '已通过', color: 'green' },
  REJECTED: { text: '已拒绝', color: 'red' },
  SPAM: { text: '垃圾评论', color: 'volcano' },
};

/**
 * 评论表格列配置
 */
export const commentColumns = (
  onApprove: (id: number, status: 'APPROVED' | 'REJECTED' | 'SPAM') => void,
  onDelete: (id: number) => void,
  onPin: (id: number, isPinned: boolean) => void
): TableProps<CommentListResponse>['columns'] => [
  {
    title: '用户',
    dataIndex: ['user', 'username'],
    key: 'username',
    width: 120,
  },
  {
    title: '评论内容',
    dataIndex: 'content',
    key: 'content',
    ellipsis: true,
    width: 300,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    render: (status: string) => {
      const config = statusMap[status as keyof typeof statusMap];
      return <Tag color={config?.color}>{config?.text || status}</Tag>;
    },
  },
  {
    title: '点赞数',
    dataIndex: 'likeCount',
    key: 'likeCount',
    width: 80,
    align: 'center',
  },
  {
    title: '回复数',
    dataIndex: 'replyCount',
    key: 'replyCount',
    width: 80,
    align: 'center',
  },
  {
    title: '置顶',
    dataIndex: 'isPinned',
    key: 'isPinned',
    width: 70,
    render: (isPinned: boolean) => (isPinned ? '是' : '否'),
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    key: 'createdAt',
    width: 150,
    render: (text: string) => new Date(text).toLocaleString(),
  },
  {
    title: '操作',
    key: 'action',
    width: 250,
    fixed: 'right',
    render: (_, record) => (
      <div className='flex gap-2 flex-wrap'>
        {record.status === 'PENDING' && (
          <>
            <a onClick={() => onApprove(record.id, 'APPROVED')}>通过</a>
            <a onClick={() => onApprove(record.id, 'REJECTED')}>拒绝</a>
            <a onClick={() => onApprove(record.id, 'SPAM')}>标记垃圾</a>
          </>
        )}
        <a onClick={() => onPin(record.id, !record.isPinned)}>
          {record.isPinned ? '取消置顶' : '置顶'}
        </a>
        <a
          onClick={() => onDelete(record.id)}
          style={{ color: 'red' }}
          className='text-theme-error'
        >
          删除
        </a>
      </div>
    ),
  },
];

/**
 * 状态筛选选项
 */
export const statusOptions = [
  { label: '待审核', value: 'PENDING' },
  { label: '已通过', value: 'APPROVED' },
  { label: '已拒绝', value: 'REJECTED' },
  { label: '垃圾评论', value: 'SPAM' },
];
