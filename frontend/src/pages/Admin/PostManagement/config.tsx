import { ADMIN_POST_EDIT_LINK } from '@/config';
import type { MyPostsListResponse } from '@/types';
import type { TableProps } from 'antd';
import { Tag } from 'antd';
import { type NavigateFunction } from 'react-router';

/**
 * 帖子管理表格列配置
 *
 */
export const columns = (
  navigate: NavigateFunction
): TableProps<MyPostsListResponse>['columns'] => {
  return [
    {
      title: '标题',
      dataIndex: 'title',
      key: 'title',
      ellipsis: true,
      width: 200,
    },
    {
      title: '分类',
      dataIndex: ['category', 'name'],
      key: 'category',
      ellipsis: true,
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      ellipsis: true,
      width: 80,
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
      ellipsis: true,
      width: 150,
      render: (value: string) => new Date(value).toLocaleString(),
    },
    {
      title: '更新时间',
      dataIndex: 'updatedAt',
      key: 'updatedAt',
      ellipsis: true,
      width: 150,
      render: (value: string) => new Date(value).toLocaleString(),
    },
    {
      title: '查看次数',
      dataIndex: 'viewCount',
      key: 'viewCount',
      width: 90,
    },
    {
      title: '点赞数',
      dataIndex: 'likeCount',
      key: 'likeCount',
      width: 80,
    },
    {
      title: '评论数',
      dataIndex: 'commentCount',
      key: 'commentCount',
      width: 80,
    },
    {
      title: '置顶',
      dataIndex: 'isTop',
      key: 'isTop',
      width: 70,
      render: (value: boolean) => (value ? '是' : '否'),
    },
    {
      title: '精选',
      dataIndex: 'isFeatured',
      key: 'isFeatured',
      width: 70,
      render: (value: boolean) => (value ? '是' : '否'),
    },
    {
      title: '',
      key: 'action',
      width: 70,
      fixed: 'right',
      render: (_, record) => {
        return (
          <a
            onClick={() => {
              navigate(`${ADMIN_POST_EDIT_LINK}/${record.id}`);
            }}
          >
            编辑
          </a>
        );
      },
    },
  ];
};

/**
 * 状态选项
 */
export const statusOptions = [
  { label: '草稿', value: 'draft' },
  { label: '已发布', value: 'published' },
  { label: '已归档', value: 'archived' },
  { label: '已删除', value: 'deleted' },
];

/**
 * 批量操作选项
 */
export const batchActionOptions = [
  { label: '批量发布', value: 'publish' },
  { label: '批量取消发布', value: 'unpublish' },
  { label: '批量归档', value: 'archive' },
  { label: '批量删除', value: 'delete' },
  { label: '批量设为精选', value: 'setFeatured' },
  { label: '批量取消精选', value: 'unsetFeatured' },
  { label: '批量设为置顶', value: 'setTop' },
  { label: '批量取消置顶', value: 'unsetTop' },
];
