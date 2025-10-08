import { BlogLayout } from '@/components';
import { Folder } from 'lucide-react';
import { useParams } from 'react-router';
import { CategorySidebar } from './CategorySidebar';
import { PostList } from './PostList';
import { useCategoryPage } from './useCategoryPage';

/**
 * 博客分类页面
 * 按分类展示文章列表，支持分类筛选和无限滚动加载
 */
const CategoryPage = () => {
  const { slug } = useParams<{ slug: string }>();

  const {
    popularCategories,
    currentCategory,
    isFetchingCategories,
    posts,
    isLoading,
    hasMore,
    loadMoreRef,
    handleCategoryClick,
  } = useCategoryPage(slug);

  // 加载分类数据中
  if (isFetchingCategories) {
    return (
      <BlogLayout>
        <div className='flex items-center justify-center min-h-[60vh]'>
          <div className='text-center space-y-4'>
            <div className='animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto' />
            <p className='text-muted-foreground'>加载分类数据中...</p>
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
            文章分类
          </h1>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        {currentCategory && (
          <div className='flex items-center gap-3 text-sm text-muted-foreground'>
            <Folder className='w-4 h-4 text-primary' />
            <span className='font-medium text-foreground'>
              {currentCategory.name}
            </span>
            <span>•</span>
            <span>{posts.length} 篇文章已加载</span>
          </div>
        )}
      </section>

      <div className='grid grid-cols-1 lg:grid-cols-4 gap-8'>
        {/* 左侧：分类侧边栏 */}
        <CategorySidebar
          categories={popularCategories}
          currentSlug={slug}
          onCategoryClick={handleCategoryClick}
        />

        {/* 右侧：文章列表 */}
        <main className='lg:col-span-3'>
          {!currentCategory ? (
            <div className='p-12 text-center text-muted-foreground space-y-2'>
              <Folder className='w-12 h-12 mx-auto opacity-50' />
              <p>请选择一个分类查看文章</p>
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

export default CategoryPage;
