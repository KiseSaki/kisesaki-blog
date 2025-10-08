import { Card, PostCard } from '@/components';
import type { PublishedPostListResponse } from '@/types';
import { Calendar, Loader2 } from 'lucide-react';
import type { RefObject } from 'react';
import { useMemo } from 'react';

interface ArchivePostListProps {
  posts: PublishedPostListResponse[];
  selectedYear?: number;
  selectedMonth?: number;
  isLoading: boolean;
  hasMore: boolean;
  loadMoreRef: RefObject<HTMLDivElement | null>;
}

/**
 * 归档文章列表组件
 * 按年份/月份分组展示文章，支持无限滚动
 */
export const ArchivePostList = ({
  posts,
  selectedYear,
  selectedMonth,
  isLoading,
  hasMore,
  loadMoreRef,
}: ArchivePostListProps) => {
  // 按年份和月份分组文章
  const groupedPosts = useMemo(() => {
    const groups: Record<string, PublishedPostListResponse[]> = {};

    posts.forEach(post => {
      const date = new Date(post.publishedAt);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;

      // 根据筛选条件过滤
      if (selectedYear && year !== selectedYear) return;
      if (selectedMonth && month !== selectedMonth) return;

      const key = `${year}-${month.toString().padStart(2, '0')}`;
      if (!groups[key]) groups[key] = [];
      groups[key].push(post);
    });

    // 按时间倒序排序
    return Object.keys(groups)
      .sort((a, b) => b.localeCompare(a))
      .reduce(
        (acc, key) => {
          acc[key] = groups[key];
          return acc;
        },
        {} as Record<string, PublishedPostListResponse[]>
      );
  }, [posts, selectedYear, selectedMonth]);

  const groupKeys = Object.keys(groupedPosts);

  if (!isLoading && groupKeys.length === 0) {
    return (
      <Card className='p-12'>
        <div className='text-center text-muted-foreground space-y-2'>
          <Calendar className='w-12 h-12 mx-auto opacity-50' />
          <p>该时间段暂无文章</p>
        </div>
      </Card>
    );
  }

  return (
    <div className='space-y-8'>
      {groupKeys.map(key => {
        const [year, month] = key.split('-');
        const groupPosts = groupedPosts[key];

        return (
          <section key={key} className='space-y-4'>
            {/* 时间标题 */}
            <div className='flex items-center gap-3'>
              <div className='flex items-center gap-2 px-4 py-2 bg-primary/10 text-primary rounded-lg'>
                <Calendar className='w-4 h-4' />
                <h2 className='text-lg font-semibold'>
                  {year} 年 {parseInt(month)} 月
                </h2>
              </div>
              <div className='h-px flex-1 bg-gradient-to-r from-border to-transparent' />
            </div>

            {/* 文章网格 */}
            <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
              {groupPosts.map((post, index) => (
                <PostCard key={post.id} {...post} animationDelay={index * 50} />
              ))}
            </div>
          </section>
        );
      })}

      {/* 加载更多触发器 */}
      {hasMore && (
        <div
          ref={loadMoreRef}
          className='flex items-center justify-center py-8'
        >
          {isLoading && (
            <div className='flex items-center gap-2 text-muted-foreground'>
              <Loader2 className='w-5 h-5 animate-spin' />
              <span>加载更多文章...</span>
            </div>
          )}
        </div>
      )}

      {/* 已加载全部 */}
      {!hasMore && groupKeys.length > 0 && (
        <div className='text-center text-sm text-muted-foreground py-4'>
          已加载全部文章
        </div>
      )}
    </div>
  );
};
