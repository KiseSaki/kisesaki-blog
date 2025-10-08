import { Card, CardContent, CardHeader } from '@/components';
import { Calendar } from 'lucide-react';
import { useMemo } from 'react';

interface YearMonthSidebarProps {
  posts: Array<{ publishedAt: string }>;
  selectedYear?: number;
  selectedMonth?: number;
  onYearClick: (year: number) => void;
  onMonthClick: (year: number, month: number) => void;
}

/**
 * 年份/月份侧边栏组件
 * 用于归档页的时间筛选导航
 */
export const YearMonthSidebar = ({
  posts,
  selectedYear,
  selectedMonth,
  onYearClick,
  onMonthClick,
}: YearMonthSidebarProps) => {
  // 按年份和月份统计文章数量
  const archiveStats = useMemo(() => {
    const stats: Record<number, Record<number, number>> = {};

    posts.forEach(post => {
      const date = new Date(post.publishedAt);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;

      if (!stats[year]) stats[year] = {};
      stats[year][month] = (stats[year][month] || 0) + 1;
    });

    return stats;
  }, [posts]);

  const years = Object.keys(archiveStats)
    .map(Number)
    .sort((a, b) => b - a);

  return (
    <aside className='lg:col-span-1'>
      <Card className='sticky top-20'>
        <CardHeader className='pb-3'>
          <h2 className='text-lg font-semibold flex items-center gap-2'>
            <Calendar className='w-5 h-5 text-primary' />
            时间轴
          </h2>
        </CardHeader>
        <CardContent className='space-y-1'>
          {years.length === 0 ? (
            <p className='text-sm text-muted-foreground text-center py-4'>
              暂无归档
            </p>
          ) : (
            years.map(year => {
              const yearPostCount = Object.values(archiveStats[year]).reduce(
                (sum, count) => sum + count,
                0
              );

              return (
                <div key={year} className='space-y-1'>
                  {/* 年份按钮 */}
                  <button
                    onClick={() => onYearClick(year)}
                    className={`w-full flex items-center justify-between px-3 py-2 rounded-lg transition-all duration-200 ${
                      selectedYear === year && !selectedMonth
                        ? 'bg-primary text-primary-foreground shadow-sm font-semibold'
                        : 'hover:bg-muted'
                    }`}
                  >
                    <span className='font-medium'>{year} 年</span>
                    <span className='text-xs px-2 py-0.5 rounded-full bg-muted'>
                      {yearPostCount}
                    </span>
                  </button>

                  {/* 月份按钮（仅当选中该年份时显示）*/}
                  {selectedYear === year && (
                    <div className='pl-4 space-y-1'>
                      {Object.keys(archiveStats[year])
                        .map(Number)
                        .sort((a, b) => b - a)
                        .map(month => (
                          <button
                            key={month}
                            onClick={() => onMonthClick(year, month)}
                            className={`w-full flex items-center justify-between px-3 py-1.5 rounded-md text-sm transition-all duration-200 ${
                              selectedMonth === month
                                ? 'bg-primary/20 text-primary font-medium'
                                : 'hover:bg-muted/50'
                            }`}
                          >
                            <span>{month} 月</span>
                            <span className='text-xs text-muted-foreground'>
                              {archiveStats[year][month]}
                            </span>
                          </button>
                        ))}
                    </div>
                  )}
                </div>
              );
            })
          )}
        </CardContent>
      </Card>
    </aside>
  );
};
