/**
 * 标签管理配置文件
 */

import type { AdminTagListResponse } from '@/types';
import type { TableProps } from 'antd';
import { Tag } from 'antd';

/**
 * 标签表格列配置
 */
export const tagColumns = (
  onEdit: (record: AdminTagListResponse) => void,
  onDelete: (id: number) => void,
  onApprove: (id: number, status: 'approved' | 'rejected') => void
): TableProps<AdminTagListResponse>['columns'] => [
  {
    title: '标签名称',
    dataIndex: 'name',
    key: 'name',
    width: 150,
    render: (text: string, record: AdminTagListResponse) => (
      <Tag color={record.color || 'default'}>{text}</Tag>
    ),
  },
  {
    title: '别名',
    dataIndex: 'slug',
    key: 'slug',
    width: 150,
  },
  {
    title: '描述',
    dataIndex: 'description',
    key: 'description',
    ellipsis: true,
  },
  {
    title: '审核状态',
    dataIndex: 'approvalStatus',
    key: 'approvalStatus',
    width: 100,
    render: (status: 'pending' | 'approved' | 'rejected') => {
      const statusMap = {
        pending: { text: '待审核', color: 'orange' },
        approved: { text: '已通过', color: 'green' },
        rejected: { text: '已拒绝', color: 'red' },
      };
      const config = statusMap[status];
      return <Tag color={config.color}>{config.text}</Tag>;
    },
  },
  {
    title: '文章数',
    dataIndex: 'postCount',
    key: 'postCount',
    width: 80,
    align: 'center',
  },
  {
    title: '创建者',
    dataIndex: 'createdByUsername',
    key: 'createdByUsername',
    width: 120,
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
    width: 200,
    fixed: 'right',
    render: (_, record) => (
      <div className='flex gap-2'>
        {record.approvalStatus === 'pending' && (
          <>
            <a onClick={() => onApprove(record.id, 'approved')}>通过</a>
            <a onClick={() => onApprove(record.id, 'rejected')}>拒绝</a>
          </>
        )}
        <a onClick={() => onEdit(record)}>编辑</a>
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
 * 审核状态选项
 */
export const approvalStatusOptions = [
  { label: '待审核', value: 'pending' },
  { label: '已通过', value: 'approved' },
  { label: '已拒绝', value: 'rejected' },
];

/**
 * 标签颜色选项
 */
export const tagColorOptions = [
  { label: '蓝色', value: 'blue' },
  { label: '绿色', value: 'green' },
  { label: '橙色', value: 'orange' },
  { label: '红色', value: 'red' },
  { label: '紫色', value: 'purple' },
  { label: '青色', value: 'cyan' },
  { label: '洋红', value: 'magenta' },
  { label: '金色', value: 'gold' },
];
