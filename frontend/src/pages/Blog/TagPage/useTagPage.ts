import { getTagPostsApi } from '@/api';
import { useInfiniteScroll, useTag as useTagData } from '@/hooks';
import { useCallback, useEffect } from 'react';

/**
 * 标签页业务逻辑 Hook
 * 管理标签页的数据获取和筛选
 */
export const useTagPage = (slug?: string) => {
  const { tagCloud, fetchTagCloud, isFetchingTags } = useTagData();

  // 当前选中的标签
  const currentTag = tagCloud.find(tag => tag.slug === slug);

  // 获取标签下的文章列表（支持分页）
  const fetchTagPosts = useCallback(
    async (page: number) => {
      if (!currentTag) return { data: [], total: 0 };

      const res = await getTagPostsApi(currentTag.id, {
        pageable: {
          currentPage: page,
          pageSize: 12,
          sort: 'publishedAt,desc',
        },
      });

      return {
        data: res.data,
        total: res.totalRecords,
      };
    },
    [currentTag]
  );

  // 使用无限滚动 Hook
  const { data: posts, isLoading, hasMore, reset, loadMoreRef } =
    useInfiniteScroll({
      fetchData: fetchTagPosts,
      pageSize: 12,
    });

  // 初始化：加载标签数据
  useEffect(() => {
    fetchTagCloud();
  }, [fetchTagCloud]);

  // 标签切换时重置数据
  useEffect(() => {
    if (currentTag) {
      reset();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentTag?.id]); // 只依赖 ID，避免重复触发

  return {
    tagCloud,
    currentTag,
    isFetchingTags,
    posts,
    isLoading,
    hasMore,
    loadMoreRef,
  };
};
