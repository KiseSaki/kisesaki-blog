/**
 * 评论管理页面
 * 管理员用于审核、回复、删除评论的页面
 */

import { Loading, UserLayout } from '@/components';
import { Input, Pagination, Select, Space, Table } from 'antd';
import { useEffect } from 'react';
import { commentColumns, statusOptions } from './config';
import { useCommentManagement } from './hooks/useCommentManagement';

const { Search } = Input;

const CommentManagement = () => {
  const {
    commentList,
    isFetching,
    fetchComments,
    handleSearch,
    handleFilterStatus,
    handleFilterPinned,
    handlePageChange,
    handleApprove,
    handleDelete,
    handlePin,
  } = useCommentManagement();

  // 初始加载
  useEffect(() => {
    fetchComments();
  }, [fetchComments]);

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
            enterButton
            allowClear
            onSearch={handleSearch}
            style={{ width: 300 }}
            loading={isFetching}
          />
          <Select
            placeholder='审核状态'
            allowClear
            options={statusOptions}
            onChange={handleFilterStatus}
            style={{ width: 120 }}
          />
          <Select
            placeholder='置顶状态'
            allowClear
            options={[
              { label: '已置顶', value: true },
              { label: '未置顶', value: false },
            ]}
            onChange={handleFilterPinned}
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
            onChange={handlePageChange}
          />
        </div>
      </div>
    </UserLayout>
  );
};

export default CommentManagement;
