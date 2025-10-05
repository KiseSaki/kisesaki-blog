import type { PopularCategoryResponse } from '@/types';
import { Folder } from 'lucide-react';

interface CategoryCloudProps {
  categories: PopularCategoryResponse[];
}

export const CategoryCloud = ({ categories }: CategoryCloudProps) => {
  return (
    <div className='flex flex-wrap gap-3'>
      {categories.map(category => (
        <button
          key={category.id}
          className='group inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-card border border-border hover:border-primary hover:bg-primary/5 transition-all duration-300 cursor-pointer'
          onClick={() => {
            // TODO: 导航到分类页面
            console.log('Navigate to category:', category.slug);
          }}
        >
          <Folder className='w-4 h-4 text-muted-foreground group-hover:text-primary transition-colors' />
          <span className='text-sm font-medium group-hover:text-primary transition-colors'>
            {category.name}
          </span>
          <span className='text-xs text-muted-foreground bg-muted px-2 py-0.5 rounded-full'>
            {category.postCount}
          </span>
        </button>
      ))}
    </div>
  );
};
