/**
 * 标签相关的自定义 Hook
 * 包含标签数据获取、筛选等逻辑
 */

import { getTagCloudApi, getTagListApi } from '@/api';
import type {
  PageResponse,
  TagCloudItem,
  TagListParams,
  TagListResponse,
} from '@/types';
import { useCallback, useState } from 'react';

export const useTag = () => {
  // 标签云
  const [tagCloud, setTagCloud] = useState<TagCloudItem[]>([]);
  const [isFetchingTags, setIsFetchingTags] = useState(false);

  const fetchTagCloud = useCallback(async () => {
    setIsFetchingTags(prev => {
      if (prev) return prev;
      return true;
    });

    try {
      const res = await getTagCloudApi();
      setTagCloud(res);
      return res;
    } catch (error) {
      console.error('获取标签云失败:', error);
      return [];
    } finally {
      setIsFetchingTags(false);
    }
  }, []);

  // 分页获取标签
  const [pageTags, setPageTags] =
    useState<PageResponse<TagListResponse> | null>(null);
  const [isFetchingPageTags, setIsFetchingPageTags] = useState(false);

  const fetchPageTags = useCallback(
    async (params: TagListParams, shouldAppend = false) => {
      setIsFetchingPageTags(prev => {
        if (prev) return prev;
        return true;
      });

      try {
        const res = await getTagListApi(params);

        // 如果需要追加数据（用于无限滚动）
        if (shouldAppend) {
          setPageTags(prev => {
            if (!prev) return res;
            return {
              ...res,
              data: [...prev.data, ...res.data],
            };
          });
        } else {
          setPageTags(res);
        }

        return res;
      } catch (error) {
        console.error('获取标签失败:', error);
        return [];
      } finally {
        setIsFetchingPageTags(false);
      }
    },
    [] // 移除 pageTags 依赖，使用函数式更新
  );

  return {
    tagCloud,
    isFetchingTags,
    fetchTagCloud,

    pageTags,
    isFetchingPageTags,
    fetchPageTags,
  };
};
