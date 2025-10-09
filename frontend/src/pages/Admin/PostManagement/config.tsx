import { ADMIN_POST_EDIT_LINK } from '@/config';
import type { MyPostsListResponse } from '@/types';
import type { TableProps } from 'antd';
import { Tag } from 'antd';

/**
 * 帖子管理表格列配置
 */
export const columns: TableProps<MyPostsListResponse>['columns'] = [
  { title: '标题', dataIndex: 'title', key: 'title' },
  { title: '分类', dataIndex: ['category', 'name'], key: 'category' },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    render: (value: string) => {
      let text: string;
      let color: string;
      switch (value) {
        case 'draft':
          text = '草稿';
          color = 'orange';
          break;
        case 'published':
          text = '已发布';
          color = 'green';
          break;
        case 'archived':
          text = '已归档';
          color = 'blue';
          break;
        case 'deleted':
          text = '已删除';
          color = 'red';
          break;
        default:
          text = value;
          color = 'default';
      }
      return (
        <Tag color={color} bordered={false}>
          {text}
        </Tag>
      );
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    key: 'createdAt',
    render: (value: string) => new Date(value).toLocaleString(),
  },
  {
    title: '更新时间',
    dataIndex: 'updatedAt',
    key: 'updatedAt',
    render: (value: string) => new Date(value).toLocaleString(),
  },
  { title: '查看次数', dataIndex: 'viewCount', key: 'viewCount' },
  { title: '点赞数', dataIndex: 'likeCount', key: 'likeCount' },
  { title: '评论数', dataIndex: 'commentCount', key: 'commentCount' },
  {
    title: '置顶',
    dataIndex: 'isTop',
    key: 'isTop',
    render: (value: boolean) => (value ? '是' : '否'),
  },
  {
    title: '精选',
    dataIndex: 'isFeatured',
    key: 'isFeatured',
    render: (value: boolean) => (value ? '是' : '否'),
  },
  {
    title: '',
    key: 'action',
    render: (_, record) => {
      return <a href={`${ADMIN_POST_EDIT_LINK}/${record.id}`}>编辑</a>;
    },
  },
];

/**
 * 状态选项
 */
export const statusOptions = [
  { label: '草稿', value: 'draft' },
  { label: '已发布', value: 'published' },
  { label: '已归档', value: 'archived' },
  { label: '已删除', value: 'deleted' },
];
