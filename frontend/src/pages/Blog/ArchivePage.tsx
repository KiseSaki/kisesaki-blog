import {
  BlogLayout,
  Card,
  CardContent,
  CardHeader,
  Separator,
} from '@/components';
import { getPublishedPostsApi } from '@/api';
import type { PageResponse, PublishedPostListResponse } from '@/types';
import { Calendar, ChevronRight, Clock, Eye, FileText } from 'lucide-react';
import { useCallback, useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router';

/**
 * 博客归档页面
 * 按时间归档展示文章列表，支持年月筛选
 */
const ArchivePage = () => {
  const navigate = useNavigate();

  const [allPosts, setAllPosts] = useState<PageResponse<PublishedPostListResponse> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedYear, setSelectedYear] = useState<number | null>(null);
  const [selectedMonth, setSelectedMonth] = useState<number | null>(null);

  // 获取所有已发布的文章
  const fetchAllPosts = useCallback(async () => {
    setIsLoading(true);
    try {
      const res = await getPublishedPostsApi({
        pageable: {
          currentPage: 1,
          pageSize: 1000, // 获取大量文章用于归档
          sort: 'publishedAt,desc',
        },
      });
      setAllPosts(res);
    } catch (error) {
      console.error('获取归档文章失败:', error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchAllPosts();
  }, [fetchAllPosts]);

  // 按年月分组文章
  const groupedPosts = useMemo(() => {
    if (!allPosts?.data) return {};

    const groups: Record<
      number,
      Record<number, PublishedPostListResponse[]>
    > = {};

    allPosts.data.forEach(post => {
      const date = new Date(post.publishedAt);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;

      if (!groups[year]) {
        groups[year] = {};
      }

      if (!groups[year][month]) {
        groups[year][month] = [];
      }

      groups[year][month].push(post);
    });

    return groups;
  }, [allPosts]);

  // 获取所有年份（降序）
  const years = useMemo(
    () => Object.keys(groupedPosts).map(Number).sort((a, b) => b - a),
    [groupedPosts]
  );

  // 获取选中年份的所有月份（降序）
  const months = useMemo(() => {
    if (!selectedYear || !groupedPosts[selectedYear]) return [];
    return Object.keys(groupedPosts[selectedYear])
      .map(Number)
      .sort((a, b) => b - a);
  }, [selectedYear, groupedPosts]);

  // 获取筛选后的文章
  const filteredPosts = useMemo(() => {
    if (selectedYear && selectedMonth && groupedPosts[selectedYear]?.[selectedMonth]) {
      return groupedPosts[selectedYear][selectedMonth];
    }
    if (selectedYear && groupedPosts[selectedYear]) {
      return Object.values(groupedPosts[selectedYear]).flat();
    }
    return allPosts?.data || [];
  }, [selectedYear, selectedMonth, groupedPosts, allPosts]);

  // 统计信息
  const totalPosts = allPosts?.data.length || 0;
  const totalYears = years.length;

  const handlePostClick = (slug: string) => {
    navigate(`/post/${slug}`);
  };

  const handleYearSelect = (year: number) => {
    if (selectedYear === year) {
      setSelectedYear(null);
      setSelectedMonth(null);
    } else {
      setSelectedYear(year);
      setSelectedMonth(null);
    }
  };

  const handleMonthSelect = (month: number) => {
    setSelectedMonth(selectedMonth === month ? null : month);
  };

  const monthNames = [
    '一月', '二月', '三月', '四月', '五月', '六月',
    '七月', '八月', '九月', '十月', '十一月', '十二月',
  ];

  if (isLoading) {
    return (
      <BlogLayout>
        <div className='flex items-center justify-center min-h-[60vh]'>
          <div className='text-center space-y-4'>
            <div className='animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto' />
            <p className='text-muted-foreground'>加载归档数据中...</p>
          </div>
        </div>
      </BlogLayout>
    );
  }

  return (
    <BlogLayout>
      {/* 页面标题和统计 */}
      <section className='space-y-4'>
        <div className='flex items-center justify-between'>
          <h1 className='text-4xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-primary to-secondary'>
            文章归档
          </h1>
          <div className='h-1 flex-1 ml-6 bg-gradient-to-r from-primary/30 to-transparent rounded-full' />
        </div>

        <div className='flex gap-6 text-sm text-muted-foreground'>
          <div className='flex items-center gap-2'>
            <FileText className='w-4 h-4' />
            <span>共 {totalPosts} 篇文章</span>
          </div>
          <div className='flex items-center gap-2'>
            <Calendar className='w-4 h-4' />
            <span>跨越 {totalYears} 年</span>
          </div>
        </div>
      </section>

      <div className='grid grid-cols-1 lg:grid-cols-4 gap-8'>
        {/* 左侧：年份和月份筛选器 */}
        <aside className='lg:col-span-1 space-y-4'>
          <Card className='sticky top-20'>
            <CardHeader className='pb-3'>
              <h2 className='text-lg font-semibold flex items-center gap-2'>
                <Calendar className='w-5 h-5 text-primary' />
                时间线
              </h2>
            </CardHeader>
            <CardContent className='space-y-2'>
              {years.length === 0 ? (
                <p className='text-sm text-muted-foreground text-center py-4'>
                  暂无归档数据
                </p>
              ) : (
                years.map(year => (
                  <div key={year} className='space-y-1'>
                    <button
                      onClick={() => handleYearSelect(year)}
                      className={`w-full flex items-center justify-between px-3 py-2 rounded-lg transition-all duration-200 ${
                        selectedYear === year
                          ? 'bg-primary text-primary-foreground shadow-sm'
                          : 'hover:bg-muted'
                      }`}
                    >
                      <span className='font-medium'>{year}</span>
                      <span className='text-xs px-2 py-0.5 rounded-full bg-muted'>
                        {Object.values(groupedPosts[year] || {}).flat().length}
                      </span>
                    </button>

                    {/* 月份列表 */}
                    {selectedYear === year && (
                      <div className='ml-4 space-y-1 animate-in slide-in-from-top-2'>
                        {months.map(month => (
                          <button
                            key={month}
                            onClick={() => handleMonthSelect(month)}
                            className={`w-full flex items-center justify-between px-3 py-1.5 rounded-lg text-sm transition-all duration-200 ${
                              selectedMonth === month
                                ? 'bg-primary/20 text-primary font-medium'
                                : 'hover:bg-muted/50 text-muted-foreground'
                            }`}
                          >
                            <span>{monthNames[month - 1]}</span>
                            <span className='text-xs'>
                              {groupedPosts[year][month]?.length || 0}
                            </span>
                          </button>
                        ))}
                      </div>
                    )}
                  </div>
                ))
              )}
            </CardContent>
          </Card>
        </aside>

        {/* 右侧：文章列表 */}
        <main className='lg:col-span-3 space-y-6'>
          {/* 当前筛选状态 */}
          {(selectedYear || selectedMonth) && (
            <div className='flex items-center gap-2 text-sm'>
              <span className='text-muted-foreground'>当前筛选：</span>
              {selectedYear && (
                <span className='px-3 py-1 bg-primary/10 text-primary rounded-lg font-medium'>
                  {selectedYear} 年
                </span>
              )}
              {selectedMonth && (
                <span className='px-3 py-1 bg-primary/10 text-primary rounded-lg font-medium'>
                  {monthNames[selectedMonth - 1]}
                </span>
              )}
              <button
                onClick={() => {
                  setSelectedYear(null);
                  setSelectedMonth(null);
                }}
                className='ml-2 text-primary hover:underline'
              >
                清除筛选
              </button>
            </div>
          )}

          {/* 文章列表 */}
          {filteredPosts.length === 0 ? (
            <Card className='p-12'>
              <div className='text-center text-muted-foreground space-y-2'>
                <FileText className='w-12 h-12 mx-auto opacity-50' />
                <p>该时间段暂无文章</p>
              </div>
            </Card>
          ) : (
            <div className='space-y-4'>
              {filteredPosts.map((post, index) => (
                <Card
                  key={post.id}
                  onClick={() => handlePostClick(post.slug)}
                  className='group hover:border-primary/50 hover:shadow-lg transition-all duration-300 cursor-pointer animate-in fade-in slide-in-from-bottom-4'
                  style={{ animationDelay: `${index * 50}ms` }}
                >
                  <CardContent className='p-6'>
                    <div className='flex items-start gap-6'>
                      {/* 日期标识 */}
                      <div className='flex-shrink-0 text-center space-y-1'>
                        <div className='w-16 h-16 rounded-xl bg-gradient-to-br from-primary to-secondary flex flex-col items-center justify-center text-white shadow-lg'>
                          <span className='text-xs font-medium opacity-90'>
                            {new Date(post.publishedAt).toLocaleDateString('zh-CN', { month: 'short' }).replace('月', '')}
                          </span>
                          <span className='text-2xl font-bold'>
                            {new Date(post.publishedAt).getDate()}
                          </span>
                        </div>
                      </div>

                      {/* 文章信息 */}
                      <div className='flex-1 min-w-0 space-y-3'>
                        <div>
                          <h3 className='text-xl font-bold group-hover:text-primary transition-colors line-clamp-2'>
                            {post.title}
                          </h3>
                          <p className='mt-2 text-sm text-muted-foreground line-clamp-2'>
                            {post.excerpt || '暂无摘要...'}
                          </p>
                        </div>

                        <Separator />

                        <div className='flex flex-wrap items-center gap-4 text-xs text-muted-foreground'>
                          <div className='flex items-center gap-1'>
                            <Calendar className='w-3.5 h-3.5' />
                            <time dateTime={post.publishedAt}>
                              {new Date(post.publishedAt).toLocaleDateString('zh-CN')}
                            </time>
                          </div>

                          {post.readingTime && (
                            <div className='flex items-center gap-1'>
                              <Clock className='w-3.5 h-3.5' />
                              <span>{post.readingTime} 分钟</span>
                            </div>
                          )}

                          {post.viewCount !== undefined && (
                            <div className='flex items-center gap-1'>
                              <Eye className='w-3.5 h-3.5' />
                              <span>{post.viewCount}</span>
                            </div>
                          )}

                          <div className='flex-1' />

                          <div className='flex items-center gap-1 text-primary group-hover:gap-2 transition-all'>
                            <span>阅读全文</span>
                            <ChevronRight className='w-4 h-4' />
                          </div>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </main>
      </div>
    </BlogLayout>
  );
};

export default ArchivePage;
