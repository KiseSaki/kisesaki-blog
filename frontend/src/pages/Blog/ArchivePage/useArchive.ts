import { getPublishedPostsApi } from '@/api';
import { useInfiniteScroll } from '@/hooks';
import { useCallback, useMemo, useState } from 'react';

/**
 * 归档页业务逻辑 Hook
 * 管理归档页的数据获取、筛选和状态
 */
export const useArchive = () => {
  const [selectedYear, setSelectedYear] = useState<number | undefined>();
  const [selectedMonth, setSelectedMonth] = useState<number | undefined>();

  // 获取所有已发布的文章（支持分页）
  const fetchAllPosts = useCallback(async (page: number) => {
    const res = await getPublishedPostsApi({
      pageable: {
        currentPage: page,
        pageSize: 20, // 每次加载 20 篇
        sort: 'publishedAt,desc',
      },
    });

    return {
      data: res.data,
      total: res.totalRecords,
    };
  }, []);

  // 使用无限滚动 Hook
  const {
    data: posts,
    isLoading,
    hasMore,
    loadMoreRef,
  } = useInfiniteScroll({
    fetchData: fetchAllPosts,
    pageSize: 20,
  });

  // 处理年份点击
  const handleYearClick = useCallback(
    (year: number) => {
      if (selectedYear === year && !selectedMonth) {
        // 取消选中
        setSelectedYear(undefined);
      } else {
        // 选中年份
        setSelectedYear(year);
        setSelectedMonth(undefined);
      }
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    [selectedYear, selectedMonth]
  );

  // 处理月份点击
  const handleMonthClick = useCallback(
    (year: number, month: number) => {
      if (selectedYear === year && selectedMonth === month) {
        // 取消选中月份，保留年份选中状态
        setSelectedMonth(undefined);
      } else {
        // 选中月份
        setSelectedYear(year);
        setSelectedMonth(month);
      }
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    [selectedYear, selectedMonth]
  );

  // 计算已加载和总数（根据筛选条件）
  const filteredCount = useMemo(() => {
    return posts.filter(post => {
      const date = new Date(post.publishedAt);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;

      if (selectedYear && year !== selectedYear) return false;
      if (selectedMonth && month !== selectedMonth) return false;
      return true;
    }).length;
  }, [posts, selectedYear, selectedMonth]);

  return {
    posts,
    isLoading,
    hasMore,
    loadMoreRef,
    selectedYear,
    selectedMonth,
    filteredCount,
    handleYearClick,
    handleMonthClick,
  };
};
