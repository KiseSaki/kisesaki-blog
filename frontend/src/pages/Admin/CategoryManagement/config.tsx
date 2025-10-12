/**
 * 分类管理配置文件
 */

import type { CategoryTreeResponse } from '@/types';
import type { TableProps } from 'antd';
import { Tag } from 'antd';

/**
 * 分类表格列配置
 */
export const categoryColumns = (
  onEdit: (record: CategoryTreeResponse) => void,
  onDelete: (id: number) => void
): TableProps<CategoryTreeResponse>['columns'] => [
  {
    title: '分类名称',
    dataIndex: 'name',
    key: 'name',
    width: 200,
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
    title: '父分类',
    dataIndex: 'parentName',
    key: 'parentName',
    width: 120,
    render: (text: string | null) =>
      text ? <Tag>{text}</Tag> : <Tag color='default'>顶级分类</Tag>,
  },
  {
    title: '文章数',
    dataIndex: 'postCount',
    key: 'postCount',
    width: 100,
    align: 'center',
  },
  {
    title: '排序',
    dataIndex: 'sortOrder',
    key: 'sortOrder',
    width: 80,
    align: 'center',
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    fixed: 'right',
    render: (_, record) => (
      <div className='flex gap-2'>
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
