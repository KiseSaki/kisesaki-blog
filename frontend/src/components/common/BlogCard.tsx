import { cn } from '@/lib/utils';

/**
 * BlogCard 组件属性
 */
interface BlogCardProps {
  // 子元素
  children: React.ReactNode;
  // 额外的类名
  className?: string;
}

/**
 * 博客内容卡片组件
 * 提供统一的卡片样式容器，用于博客详情页等场景
 */
export const BlogCard = ({ children, className }: BlogCardProps) => {
  return (
    <div className={cn('p-6 rounded-2xl bg-theme-card-background', className)}>
      {children}
    </div>
  );
};
