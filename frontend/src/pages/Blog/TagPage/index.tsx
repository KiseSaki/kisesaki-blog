import { BlogLayout } from '@/components';
import { Hash } from 'lucide-react';
import { useParams } from 'react-router';
import { PostList } from '../CategoryPage/PostList';
import { TagSidebar } from './TagSidebar';
import { useTagPage } from './useTagPage';

/**
 * 博客标签页面
 * 按标签展示文章列表，支持标签筛选和无限滚动加载
 */
const TagPage = () => {
  const { slug } = useParams<{ slug: string }>();

  const {
    tagCloud,
    currentTag,
    isFetchingTags,
    posts,
    isLoading,
    hasMore,
    loadMoreRef,
  } = useTagPage(slug);

  // 加载标签数据中
  if (isFetchingTags) {
    return (
      <BlogLayout>
        <div className='flex items-center justify-center min-h-[60vh]'>
          <div className='text-center space-y-4'>
            <div className='animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto' />
            <p className='text-muted-foreground'>加载标签数据中...</p>
          </div>
        </div>
      </BlogLayout>
    );
  }

  return (
    <BlogLayout>
      {/* 页面标题 */}
      <section className='space-y-4'>
        <div className='flex items-center justify-between'>
          <h1 className='text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
            文章标签
          </h1>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        {currentTag && (
          <div className='flex items-center gap-3 text-sm text-muted-foreground'>
            <Hash className='w-4 h-4 text-primary' />
            <span
              className='font-medium'
              style={{ color: currentTag.color || undefined }}
            >
              {currentTag.name}
            </span>
            <span>•</span>
            <span>{posts.length} 篇文章已加载</span>
          </div>
        )}
      </section>

      <div className='grid grid-cols-1 lg:grid-cols-4 gap-8'>
        {/* 左侧：标签侧边栏 */}
        <TagSidebar tags={tagCloud} />

        {/* 右侧：文章列表 */}
        <main className='lg:col-span-3'>
          {!currentTag ? (
            <div className='p-12 text-center text-muted-foreground space-y-2'>
              <Hash className='w-12 h-12 mx-auto opacity-50' />
              <p>请从标签云中选择一个标签查看文章</p>
            </div>
          ) : (
            <PostList
              posts={posts}
              isLoading={isLoading}
              hasMore={hasMore}
              loadMoreRef={loadMoreRef}
            />
          )}
        </main>
      </div>
    </BlogLayout>
  );
};

export default TagPage;
