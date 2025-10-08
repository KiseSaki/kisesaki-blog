import { Card, CardContent, CardHeader, CategoryCloud } from '@/components';
import type { PopularCategoryResponse } from '@/types';
import { Folder } from 'lucide-react';

interface CategorySidebarProps {
  categories: PopularCategoryResponse[];
  currentSlug?: string;
  onCategoryClick: (slug: string) => void;
}

/**
 * 分类侧边栏组件
 * 展示分类列表和分类云，支持分类切换
 */
export const CategorySidebar = ({
  categories,
  currentSlug,
  onCategoryClick,
}: CategorySidebarProps) => {
  return (
    <aside className='lg:col-span-1 space-y-4'>
      {/* 分类列表 */}
      <Card className='sticky top-20'>
        <CardHeader className='pb-3'>
          <h2 className='text-lg font-semibold flex items-center gap-2'>
            <Folder className='w-5 h-5 text-primary' />
            所有分类
          </h2>
        </CardHeader>
        <CardContent className='space-y-2'>
          {categories.length === 0 ? (
            <p className='text-sm text-muted-foreground text-center py-4'>
              暂无分类
            </p>
          ) : (
            categories.map(category => (
              <button
                key={category.id}
                onClick={() => onCategoryClick(category.slug)}
                className={`w-full flex items-center justify-between px-4 py-3 rounded-lg transition-all duration-200 ${
                  category.slug === currentSlug
                    ? 'bg-primary text-primary-foreground shadow-sm'
                    : 'hover:bg-muted'
                }`}
              >
                <span className='font-medium'>{category.name}</span>
                <span className='text-xs px-2 py-0.5 rounded-full bg-muted'>
                  {category.postCount}
                </span>
              </button>
            ))
          )}
        </CardContent>
      </Card>

      {/* 分类云 */}
      <Card>
        <CardHeader className='pb-3'>
          <h3 className='text-sm font-semibold text-muted-foreground'>
            快速浏览
          </h3>
        </CardHeader>
        <CardContent>
          <CategoryCloud categories={categories} />
        </CardContent>
      </Card>
    </aside>
  );
};
