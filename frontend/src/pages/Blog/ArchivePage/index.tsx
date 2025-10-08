import { BlogLayout } from '@/components';
import { Calendar } from 'lucide-react';
import { ArchivePostList } from './ArchivePostList';
import { YearMonthSidebar } from './YearMonthSidebar';
import { useArchive } from './useArchive';

/**
 * 博客归档页面
 * 按时间线展示所有文章，支持年份/月份筛选和无限滚动
 */
const ArchivePage = () => {
  const {
    posts,
    isLoading,
    hasMore,
    loadMoreRef,
    selectedYear,
    selectedMonth,
    filteredCount,
    handleYearClick,
    handleMonthClick,
  } = useArchive();

  return (
    <BlogLayout>
      {/* 页面标题 */}
      <section className='space-y-4'>
        <div className='flex items-center justify-between'>
          <h1 className='text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
            文章归档
          </h1>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        <div className='flex items-center gap-3 text-sm text-muted-foreground'>
          <Calendar className='w-4 h-4 text-primary' />
          <span>
            {selectedYear && selectedMonth
              ? `${selectedYear} 年 ${selectedMonth} 月`
              : selectedYear
                ? `${selectedYear} 年`
                : '全部时间'}
          </span>
          <span>•</span>
          <span>
            {filteredCount} / {posts.length} 篇文章
          </span>
        </div>
      </section>

      <div className='grid grid-cols-1 lg:grid-cols-4 gap-8'>
        {/* 左侧：时间轴侧边栏 */}
        <YearMonthSidebar
          posts={posts}
          selectedYear={selectedYear}
          selectedMonth={selectedMonth}
          onYearClick={handleYearClick}
          onMonthClick={handleMonthClick}
        />

        {/* 右侧：归档文章列表 */}
        <main className='lg:col-span-3'>
          <ArchivePostList
            posts={posts}
            selectedYear={selectedYear}
            selectedMonth={selectedMonth}
            isLoading={isLoading}
            hasMore={hasMore}
            loadMoreRef={loadMoreRef}
          />
        </main>
      </div>
    </BlogLayout>
  );
};

export default ArchivePage;
