import { useCallback, useEffect, useRef, useState } from 'react';

/**
 * 无限滚动加载 Hook
 * 监听触底事件，自动加载更多数据
 */
export function useInfiniteScroll<T>({
  fetchData,
  pageSize = 12,
  threshold = 300,
}: {
  // 数据获取函数，返回数据数组和总数
  fetchData: (
    page: number,
    size: number
  ) => Promise<{ data: T[]; total: number }>;
  // 每页大小
  pageSize?: number;
  // 触底阈值（距离底部多少像素时触发加载）
  threshold?: number;
}) {
  const [data, setData] = useState<T[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [isLoading, setIsLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const [isInitialized, setIsInitialized] = useState(false);
  const observerRef = useRef<IntersectionObserver | null>(null);
  const loadMoreRef = useRef<HTMLDivElement | null>(null);

  // 加载更多数据
  const loadMore = useCallback(async () => {
    if (isLoading || !hasMore) return;

    setIsLoading(true);
    try {
      const result = await fetchData(page, pageSize);

      setData(prev => {
        const newData = [...prev, ...result.data];
        // 使用最新的数据长度计算 hasMore
        setHasMore(newData.length < result.total);
        return newData;
      });
      setTotal(result.total);
      setPage(prev => prev + 1);
      setIsInitialized(true);
    } catch (error) {
      console.error('加载数据失败:', error);
      setHasMore(false);
    } finally {
      setIsLoading(false);
    }
  }, [page, pageSize, fetchData, isLoading, hasMore]);

  // 重置数据（用于筛选条件变化时）
  const reset = useCallback(() => {
    setData([]);
    setPage(1);
    setHasMore(true);
    setTotal(0);
    setIsInitialized(false);
  }, []);

  // 设置 IntersectionObserver
  useEffect(() => {
    if (!loadMoreRef.current) return;

    observerRef.current = new IntersectionObserver(
      entries => {
        const [entry] = entries;
        if (entry.isIntersecting && hasMore && !isLoading) {
          loadMore();
        }
      },
      {
        rootMargin: `${threshold}px`,
      }
    );

    observerRef.current.observe(loadMoreRef.current);

    return () => {
      if (observerRef.current) {
        observerRef.current.disconnect();
      }
    };
  }, [loadMore, hasMore, isLoading, threshold]);

  // 初始加载
  useEffect(() => {
    if (!isInitialized && !isLoading && data.length === 0) {
      loadMore();
    }
  }, [isInitialized, isLoading, data.length, loadMore]);

  return {
    data,
    isLoading,
    hasMore,
    total,
    loadMore,
    reset,
    loadMoreRef,
  };
}
