/**
 * 标签相关的自定义 Hook
 * 包含标签数据获取、筛选等逻辑
 */

import { getTagCloudApi } from '@/api';
import type { TagCloudItem } from '@/types';
import { useCallback, useState } from 'react';

export const useTag = () => {
  // 标签云
  const [tagCloud, setTagCloud] = useState<TagCloudItem[]>([]);
  const [isFetchingTags, setIsFetchingTags] = useState(false);

  const fetchTagCloud = useCallback(async () => {
    if (isFetchingTags) return [];

    setIsFetchingTags(true);
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
  }, [isFetchingTags]);

  return {
    tagCloud,
    isFetchingTags,
    fetchTagCloud,
  };
};
