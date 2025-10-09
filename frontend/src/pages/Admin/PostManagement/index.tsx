import { Loading, UserLayout } from '@/components';
import { useCategory, usePost } from '@/hooks';
import type {
  CategoryQueryParams,
  GetMyPostsListParams,
  MyPostsListResponse,
} from '@/types';
import {
  Button,
  DatePicker,
  Input,
  Pagination,
  Select,
  Space,
  Table,
} from 'antd';
import { useEffect, useRef } from 'react';
import { useImmer } from 'use-immer';
import { columns, statusOptions } from './config';

const { Search } = Input;
const { RangePicker } = DatePicker;

const PostManagement = () => {
  // 自己发布的文章
  const { myPosts, isFetchingMyPosts, fetchMyPosts } = usePost();
  // 查询参数
  const [params, setParams] = useImmer<GetMyPostsListParams>({
    pageable: { currentPage: 1, pageSize: 10, sort: 'createdAt,desc' },
  });

  // 分类
  const { pageCategories, isFetchingCategories, fetchPageCategories } =
    useCategory();
  // 分页查询参数 - 用于追踪当前加载的页码，不触发自动请求
  const [categoryParams, setCategoryParams] = useImmer<CategoryQueryParams>({
    pageable: { currentPage: 1, pageSize: 10 },
  });

  // 防止重复触发加载的标志
  const isLoadingMoreCategories = useRef(false);

  // 处理分类下拉框滚动加载
  const handleCategoryPopupScroll = (e: React.UIEvent<HTMLDivElement>) => {
    const { target } = e;
    const { scrollTop, scrollHeight, clientHeight } = target as HTMLDivElement;

    // 滚动到底部，且还有更多数据，且未在加载中
    if (
      scrollHeight - scrollTop <= clientHeight + 10 &&
      pageCategories &&
      pageCategories.currentPage < pageCategories.totalPages &&
      !isFetchingCategories &&
      !isLoadingMoreCategories.current // 防止重复触发
    ) {
      // 设置加载标志
      isLoadingMoreCategories.current = true;

      // 加载下一页分类数据
      const nextPage = categoryParams.pageable!.currentPage! + 1;

      // 只调用 fetchPageCategories，不更新 categoryParams（避免触发 useEffect）
      fetchPageCategories(
        {
          ...categoryParams,
          pageable: { ...categoryParams.pageable, currentPage: nextPage },
        },
        true // shouldAppend = true，表示追加数据
      )
        .then(() => {
          // 加载成功后才更新页码状态
          setCategoryParams(draft => {
            draft.pageable!.currentPage = nextPage;
          });
        })
        .finally(() => {
          // 重置加载标志
          isLoadingMoreCategories.current = false;
        });
    }
  };

  useEffect(() => {
    fetchMyPosts(params);
  }, [fetchMyPosts, params]);

  // 初始加载分类数据（只执行一次）
  useEffect(() => {
    fetchPageCategories({ pageable: { currentPage: 1, pageSize: 10 } });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []); // 空依赖数组，只在组件挂载时执行一次

  if (!myPosts) {
    return <Loading />;
  }

  // 解构分页数据
  const { currentPage, pageSize, totalRecords, totalPages } = myPosts;

  return (
    <UserLayout title='文章管理' description='管理自己发布的文章'>
      {/* 筛选工具条 */}
      <Space
        align='center'
        style={{
          width: '100%',
          display: 'flex',
          justifyContent: 'space-between',
        }}
      >
        <Search
          placeholder='搜索文章'
          enterButton
          allowClear
          onSearch={value => {
            setParams(draft => {
              draft.q = value;
              draft.pageable!.currentPage = 1; // 重置到第一页
            });
            fetchMyPosts(params);
          }}
          loading={isFetchingMyPosts}
        />
        <Space>
          <Select
            className='w-32'
            placeholder='分类'
            showSearch
            options={
              pageCategories?.data.map(cat => ({
                label: cat.name,
                value: cat.id,
              })) || []
            }
            loading={isFetchingCategories}
            onPopupScroll={handleCategoryPopupScroll}
            onChange={value => {
              setParams(draft => {
                draft.categoryId = value;
                draft.pageable!.currentPage = 1;
              });
              fetchMyPosts(params);
            }}
          />
          <Select
            className='w-20'
            placeholder='状态'
            showSearch
            options={statusOptions}
            onChange={value => {
              setParams(draft => {
                draft.status = value;
                draft.pageable!.currentPage = 1; // 重置到第一页
              });
              fetchMyPosts(params);
            }}
          />
          <RangePicker
            showTime
            className='w-84'
            placeholder={['开始时间', '结束时间']}
            onChange={(_, dateStrings) => {
              setParams(draft => {
                draft.pageable!.startTime = dateStrings[0];
                draft.pageable!.endTime = dateStrings[1];
                draft.pageable!.currentPage = 1; // 重置到第一页
              });
              fetchMyPosts(params);
            }}
          />
        </Space>
      </Space>

      {/* 操作区 */}
      <div className='flex justify-end gap-4 items-center'>
        <Button type='dashed'>批量操作</Button>
        <Button type='primary'>新建文章</Button>
      </div>

      {/* 内容区 - 使用 flex-1 填充剩余空间 */}
      <div className='bg-card rounded-lg flex-1 flex flex-col justify-between p-2'>
        {isFetchingMyPosts ? (
          <Loading />
        ) : (
          <Table<MyPostsListResponse>
            columns={columns}
            dataSource={
              myPosts?.data.map(item => ({ key: item.id, ...item })) || []
            }
            pagination={false}
          />
        )}

        <div className='flex justify-between items-center'>
          <span className='text-sm text-theme-secondary-text'>
            共 {totalRecords} 条记录，当前第 {currentPage} / {totalPages} 页
          </span>
          <Pagination
            current={currentPage}
            pageSize={pageSize}
            total={totalRecords}
            onChange={(page, pageSize) => {
              setParams(draft => {
                draft.pageable!.currentPage = page;
                draft.pageable!.pageSize = pageSize;
              });
              fetchMyPosts(params);
            }}
          />
        </div>
      </div>
    </UserLayout>
  );
};
export default PostManagement;
