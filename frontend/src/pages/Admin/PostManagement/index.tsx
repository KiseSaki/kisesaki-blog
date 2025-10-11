import { Loading, UserLayout } from '@/components';
import { ADMIN_POST_CREATE_LINK } from '@/config';
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
  message,
  Modal,
  Pagination,
  Select,
  Space,
  Table,
  type TableProps,
} from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router';
import { useImmer } from 'use-immer';
import { batchActionOptions, columns, statusOptions } from './config';
import { useBatchPostActions } from './hooks/useBatchPostActions';

const { Search } = Input;
const { RangePicker } = DatePicker;

const PostManagement = () => {
  const navigate = useNavigate();

  // 自己发布的文章
  const { myPosts, isFetchingMyPosts, fetchMyPosts } = usePost();
  // 查询参数
  const [params, setParams] = useImmer<GetMyPostsListParams>({
    pageable: { currentPage: 1, pageSize: 10, sort: 'createdAt,desc' },
  });

  // 批量操作
  const { isProcessing, executeBatchAction } = useBatchPostActions();
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [batchAction, setBatchAction] = useState<string | undefined>();

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

  // 处理批量操作
  const handleBatchAction = async () => {
    if (!batchAction || selectedRowKeys.length === 0) {
      message.warning('请选择操作和文章');
      return;
    }

    Modal.confirm({
      title: '确认批量操作',
      content: `确定要对选中的 ${selectedRowKeys.length} 篇文章执行 "${
        batchActionOptions.find(opt => opt.value === batchAction)?.label
      }" 操作吗？`,
      okText: '确定',
      cancelText: '取消',
      onOk: async () => {
        const ids = selectedRowKeys.map(key => Number(key));
        await executeBatchAction(batchAction, ids);
        // 清空选择
        setSelectedRowKeys([]);
        setBatchAction(undefined);
        // 刷新列表
        fetchMyPosts(params);
      },
    });
  };

  // 表格行选择配置
  const rowSelection: TableProps<MyPostsListResponse>['rowSelection'] = {
    selectedRowKeys,
    onChange: keys => {
      setSelectedRowKeys(keys);
    },
  };

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
          }}
          loading={isFetchingMyPosts}
        />
        <Space>
          <Select
            className='w-32'
            placeholder='分类'
            showSearch
            allowClear
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
            }}
          />
          <Select
            className='w-20'
            placeholder='状态'
            showSearch
            allowClear
            options={statusOptions}
            onChange={value => {
              setParams(draft => {
                draft.status = value;
                draft.pageable!.currentPage = 1; // 重置到第一页
              });
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
            }}
          />
        </Space>
      </Space>

      {/* 操作区 */}
      <div className='flex justify-between gap-4 items-center'>
        <Space>
          <Select
            className='w-32'
            placeholder='批量操作'
            value={batchAction}
            options={batchActionOptions}
            onChange={value => setBatchAction(value)}
            disabled={selectedRowKeys.length === 0}
          />
          <Button
            type='dashed'
            onClick={handleBatchAction}
            disabled={!batchAction || selectedRowKeys.length === 0}
            loading={isProcessing}
          >
            执行 ({selectedRowKeys.length})
          </Button>
        </Space>
        <Button type='primary' onClick={() => navigate(ADMIN_POST_CREATE_LINK)}>
          新建文章
        </Button>
      </div>

      {/* 内容区 - 使用 flex-1 填充剩余空间 */}
      <div className='bg-card rounded-lg flex-1 flex flex-col gap-2 justify-between p-2'>
        {isFetchingMyPosts ? (
          <Loading />
        ) : (
          <Table<MyPostsListResponse>
            rowSelection={rowSelection}
            columns={columns(navigate)}
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
            }}
          />
        </div>
      </div>
    </UserLayout>
  );
};
export default PostManagement;
