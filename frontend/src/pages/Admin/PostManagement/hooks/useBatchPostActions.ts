/**
 * 批量文章操作 Hook
 * 封装批量操作的业务逻辑
 */

import {
  archivePostApi,
  deletePostApi,
  publishPostApi,
  unpublishPostApi,
} from '@/api';
import { message } from 'antd';
import { useState } from 'react';

export const useBatchPostActions = () => {
  const [isProcessing, setIsProcessing] = useState(false);

  /**
   * 批量发布文章
   */
  const batchPublish = async (ids: number[]) => {
    setIsProcessing(true);
    try {
      const results = await Promise.allSettled(
        ids.map(id => publishPostApi(id))
      );

      const successCount = results.filter(r => r.status === 'fulfilled').length;
      const failCount = results.filter(r => r.status === 'rejected').length;

      if (failCount === 0) {
        message.success(`成功发布 ${successCount} 篇文章`);
      } else {
        message.warning(
          `发布完成：成功 ${successCount} 篇，失败 ${failCount} 篇`
        );
      }

      return { successCount, failCount };
    } catch (error) {
      message.error('批量发布失败');
      throw error;
    } finally {
      setIsProcessing(false);
    }
  };

  /**
   * 批量取消发布文章
   */
  const batchUnpublish = async (ids: number[]) => {
    setIsProcessing(true);
    try {
      const results = await Promise.allSettled(
        ids.map(id => unpublishPostApi(id))
      );

      const successCount = results.filter(r => r.status === 'fulfilled').length;
      const failCount = results.filter(r => r.status === 'rejected').length;

      if (failCount === 0) {
        message.success(`成功取消发布 ${successCount} 篇文章`);
      } else {
        message.warning(
          `取消发布完成：成功 ${successCount} 篇，失败 ${failCount} 篇`
        );
      }

      return { successCount, failCount };
    } catch (error) {
      message.error('批量取消发布失败');
      throw error;
    } finally {
      setIsProcessing(false);
    }
  };

  /**
   * 批量归档文章
   */
  const batchArchive = async (ids: number[]) => {
    setIsProcessing(true);
    try {
      const results = await Promise.allSettled(
        ids.map(id => archivePostApi(id))
      );

      const successCount = results.filter(r => r.status === 'fulfilled').length;
      const failCount = results.filter(r => r.status === 'rejected').length;

      if (failCount === 0) {
        message.success(`成功归档 ${successCount} 篇文章`);
      } else {
        message.warning(
          `归档完成：成功 ${successCount} 篇，失败 ${failCount} 篇`
        );
      }

      return { successCount, failCount };
    } catch (error) {
      message.error('批量归档失败');
      throw error;
    } finally {
      setIsProcessing(false);
    }
  };

  /**
   * 批量删除文章
   */
  const batchDelete = async (ids: number[]) => {
    setIsProcessing(true);
    try {
      const results = await Promise.allSettled(
        ids.map(id => deletePostApi(id))
      );

      const successCount = results.filter(r => r.status === 'fulfilled').length;
      const failCount = results.filter(r => r.status === 'rejected').length;

      if (failCount === 0) {
        message.success(`成功删除 ${successCount} 篇文章`);
      } else {
        message.warning(
          `删除完成：成功 ${successCount} 篇，失败 ${failCount} 篇`
        );
      }

      return { successCount, failCount };
    } catch (error) {
      message.error('批量删除失败');
      throw error;
    } finally {
      setIsProcessing(false);
    }
  };

  /**
   * 执行批量操作
   */
  const executeBatchAction = async (
    action: string,
    ids: number[]
  ): Promise<{ successCount: number; failCount: number }> => {
    if (ids.length === 0) {
      message.warning('请至少选择一篇文章');
      return { successCount: 0, failCount: 0 };
    }

    switch (action) {
      case 'publish':
        return await batchPublish(ids);
      case 'unpublish':
        return await batchUnpublish(ids);
      case 'archive':
        return await batchArchive(ids);
      case 'delete':
        return await batchDelete(ids);
      // TODO: 实现其他批量操作（精选、置顶等）
      default:
        message.warning('暂不支持该操作');
        return { successCount: 0, failCount: 0 };
    }
  };

  return {
    isProcessing,
    executeBatchAction,
  };
};
