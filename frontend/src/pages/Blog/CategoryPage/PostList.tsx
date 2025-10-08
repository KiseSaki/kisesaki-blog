import { Card, PostCard } from '@/components';
import type { PublishedPostListResponse } from '@/types';
import { Folder, Loader2 } from 'lucide-react';
import type { RefObject } from 'react';

interface PostListProps {
  posts: PublishedPostListResponse[];
  isLoading: boolean;
  hasMore: boolean;
  loadMoreRef: RefObject<HTMLDivElement | null>;
}

/**
 * 文章列表组件
 * 支持网格布局和无限滚动加载
 */
export const PostList = ({
  posts,
  isLoading,
  hasMore,
  loadMoreRef,
}: PostListProps) => {
  if (!isLoading && posts.length === 0) {
    return (
      <Card className='p-12'>
        <div className='text-center text-muted-foreground space-y-2'>
          <Folder className='w-12 h-12 mx-auto opacity-50' />
          <p>该分类下暂无文章</p>
        </div>
      </Card>
    );
  }

  return (
    <div className='space-y-6'>
      {/* 文章网格 */}
      <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
        {posts.map((post, index) => (
          <PostCard key={post.id} {...post} animationDelay={index * 50} />
        ))}
      </div>

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
      {!hasMore && posts.length > 0 && (
        <div className='text-center text-sm text-muted-foreground py-4'>
          已加载全部文章
        </div>
      )}
    </div>
  );
};
