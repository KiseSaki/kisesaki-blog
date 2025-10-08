import { getCategoryPostsApi } from '@/api';
import { BLOG_CATEGORY_BASE } from '@/config';
import { useCategory as useCategoryData, useInfiniteScroll } from '@/hooks';
import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router';

/**
 * 分类页业务逻辑 Hook
 * 管理分类页的数据获取、筛选和导航
 */
export const useCategoryPage = (slug?: string) => {
  const navigate = useNavigate();
  const { popularCategories, fetchPopularCategories, isFetchingCategories } =
    useCategoryData();

  // 当前选中的分类
  const currentCategory = popularCategories.find(cat => cat.slug === slug);

  // 获取分类下的文章列表（支持分页）
  const fetchCategoryPosts = useCallback(
    async (page: number) => {
      if (!currentCategory) return { data: [], total: 0 };

      const res = await getCategoryPostsApi(currentCategory.id, {
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
    [currentCategory]
  );

  // 使用无限滚动 Hook
  const {
    data: posts,
    isLoading,
    hasMore,
    reset,
    loadMoreRef,
  } = useInfiniteScroll({
    fetchData: fetchCategoryPosts,
    pageSize: 12,
  });

  // 初始化：加载分类数据
  useEffect(() => {
    fetchPopularCategories();
  }, [fetchPopularCategories]);

  // 分类切换时重置数据
  useEffect(() => {
    if (currentCategory) {
      reset();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentCategory?.id]); // 只依赖 ID，避免重复触发

  // 处理分类点击
  const handleCategoryClick = useCallback(
    (categorySlug: string) => {
      navigate(`${BLOG_CATEGORY_BASE}/${categorySlug}`);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    [navigate]
  );

  return {
    popularCategories,
    currentCategory,
    isFetchingCategories,
    posts,
    isLoading,
    hasMore,
    loadMoreRef,
    handleCategoryClick,
  };
};
