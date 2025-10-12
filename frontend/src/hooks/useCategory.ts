/**
 * 分类相关的自定义 Hook
 * 包含分类数据获取、筛选等逻辑
 */

import { getCategoryListApi, getPopularCategoriesApi } from '@/api';
import type {
  CategoryQueryParams,
  CategoryTreeResponse,
  PageResponse,
  PopularCategoryResponse,
} from '@/types';
import { useCallback, useState } from 'react';

export const useCategory = () => {
  // 热门分类
  const [popularCategories, setPopularCategories] = useState<
    PopularCategoryResponse[]
  >([]);
  const [isFetchingCategories, setIsFetchingCategories] = useState(false);

  const fetchPopularCategories = useCallback(async (limit = 10) => {
    setIsFetchingCategories(prev => {
      if (prev) return prev;
      return true;
    });

    try {
      const res = await getPopularCategoriesApi({ limit });
      setPopularCategories(res);
      return res;
    } catch (error) {
      console.error('获取热门分类失败:', error);
      return [];
    } finally {
      setIsFetchingCategories(false);
    }
  }, []);

  // 分页获取分类
  const [pageCategories, setPageCategories] =
    useState<PageResponse<CategoryTreeResponse> | null>(null);
  const [isFetchingPageCategories, setIsFetchingPageCategories] =
    useState(false);

  const fetchPageCategories = useCallback(
    async (params: CategoryQueryParams, shouldAppend = false) => {
      setIsFetchingPageCategories(prev => {
        if (prev) return prev;
        return true;
      });

      try {
        const res = await getCategoryListApi(params);

        // 如果需要追加数据（用于无限滚动）
        if (shouldAppend) {
          setPageCategories(prev => {
            if (!prev) return res;
            return {
              ...res,
              data: [...prev.data, ...res.data],
            };
          });
        } else {
          setPageCategories(res);
        }

        return res;
      } catch (error) {
        console.error('获取分类失败:', error);
        return [];
      } finally {
        setIsFetchingPageCategories(false);
      }
    },
    [] // 移除 pageCategories 依赖，使用函数式更新
  );

  return {
    // 热门分类
    popularCategories,
    isFetchingCategories,
    fetchPopularCategories,

    // 分页分类
    pageCategories,
    isFetchingPageCategories,
    fetchPageCategories,
  };
};
