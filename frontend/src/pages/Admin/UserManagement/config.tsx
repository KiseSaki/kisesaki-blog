/**
 * 用户管理配置文件
 */

import type { UserListItem } from '@/types';
import type { TableProps } from 'antd';
import { Tag } from 'antd';

/**
 * 用户状态映射
 */
export const statusMap = {
  active: { text: '正常', color: 'green' },
  inactive: { text: '未激活', color: 'orange' },
  banned: { text: '已封禁', color: 'red' },
};

/**
 * 用户表格列配置
 */
export const userColumns = (
  onUpdateStatus: (id: number, status: 'active' | 'inactive' | 'banned') => void,
  onUpdateRoles: (id: number) => void,
  onDelete: (id: number) => void
): TableProps<UserListItem>['columns'] => [
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
    width: 150,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    width: 200,
    ellipsis: true,
  },
  {
    title: '显示名称',
    dataIndex: 'displayName',
    key: 'displayName',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 100,
    render: (status: string) => {
      const config = statusMap[status as keyof typeof statusMap] || {
        text: status,
        color: 'default',
      };
      return <Tag color={config.color}>{config.text}</Tag>;
    },
  },
  {
    title: '个人简介',
    dataIndex: 'bio',
    key: 'bio',
    ellipsis: true,
  },
  {
    title: '注册时间',
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
        {record.status !== 'active' && (
          <a onClick={() => onUpdateStatus(record.id, 'active')}>激活</a>
        )}
        {record.status !== 'banned' && (
          <a onClick={() => onUpdateStatus(record.id, 'banned')}>封禁</a>
        )}
        <a onClick={() => onUpdateRoles(record.id)}>编辑角色</a>
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
  { label: '正常', value: 'active' },
  { label: '未激活', value: 'inactive' },
  { label: '已封禁', value: 'banned' },
];

/**
 * 账号类型选项
 */
export const accountTypeOptions = [
  { label: '本地账号', value: 'local' },
  { label: 'OAuth', value: 'oauth' },
];

/**
 * 角色选项
 */
export const roleOptions = [
  { label: '用户', value: 'ROLE_USER' },
  { label: '作者', value: 'ROLE_AUTHOR' },
  { label: '管理员', value: 'ROLE_ADMIN' },
];
