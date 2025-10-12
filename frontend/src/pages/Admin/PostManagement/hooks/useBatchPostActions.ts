/**
 * 批量文章操作 Hook
 * 封装批量操作的业务逻辑，调用后端批量接口
 */

import { batchOperatePostsApi } from '@/api';
import { useState } from 'react';
import { toast } from 'sonner';

export const useBatchPostActions = () => {
  const [isProcessing, setIsProcessing] = useState(false);

  /**
   * 执行批量操作
   * @param action 操作类型
   * @param ids 文章ID列表
   */
  const executeBatchAction = async (
    action: string,
    ids: number[]
  ): Promise<{ success: boolean }> => {
    if (ids.length === 0) {
      toast.warning('请至少选择一篇文章');
      return { success: false };
    }

    setIsProcessing(true);
    try {
      // 调用后端批量操作接口
      await batchOperatePostsApi(action, ids);

      // 根据操作类型显示不同的成功提示
      const actionLabels: Record<string, string> = {
        publish: '发布',
        unpublish: '取消发布',
        archive: '归档',
        delete: '删除',
        setFeatured: '设为精选',
        unsetFeatured: '取消精选',
        setTop: '设为置顶',
        unsetTop: '取消置顶',
      };

      const label = actionLabels[action] || '操作';
      toast.success(`成功${label} ${ids.length} 篇文章`);

      return { success: true };
    } catch (error) {
      // 错误已由 httpClient 拦截器处理，这里只需标记失败
      console.error('批量操作失败:', error);
      return { success: false };
    } finally {
      setIsProcessing(false);
    }
  };

  return {
    isProcessing,
    executeBatchAction,
  };
};
