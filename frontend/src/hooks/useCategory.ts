/**
 * 分类相关的自定义 Hook
 * 包含分类数据获取、筛选等逻辑
 */

import { getPopularCategoriesApi } from '@/api';
import type { PopularCategoryResponse } from '@/types';
import { useCallback, useState } from 'react';

export const useCategory = () => {
  // 热门分类
  const [popularCategories, setPopularCategories] = useState<
    PopularCategoryResponse[]
  >([]);
  const [isFetchingCategories, setIsFetchingCategories] = useState(false);

  const fetchPopularCategories = useCallback(
    async (limit = 10) => {
      if (isFetchingCategories) return [];

      setIsFetchingCategories(true);
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
    },
    [isFetchingCategories]
  );

  return {
    popularCategories,
    isFetchingCategories,
    fetchPopularCategories,
  };
};
