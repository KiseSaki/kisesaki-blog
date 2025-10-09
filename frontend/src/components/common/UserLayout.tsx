import { cn } from '@/lib';
import type { ReactNode } from 'react';

interface UserLayoutProps {
  // 页面标题
  title: string;

  // 页面描述（可选）
  description?: string;

  // 页面内容
  children: ReactNode;

  // 自定义容器类名
  className?: string;

  // 标题右侧的操作区域（可选）
  extra?: ReactNode;

  // 是否显示返回按钮（可选）
  showBack?: boolean;

  // 返回按钮点击事件
  onBack?: () => void;
}

/**
 * 用户中心页面布局组件
 * 为所有 /user/* 路由提供统一的页面容器和标题样式
 *
 * @example
 * ```tsx
 * <UserLayout title="个人资料" description="查看和编辑你的个人信息">
 *   <div>页面内容</div>
 * </UserLayout>
 * ```
 */
export const UserLayout = ({
  title,
  description,
  children,
  className,
  extra,
  showBack = false,
  onBack,
}: UserLayoutProps) => {
  return (
    <div className={cn('min-h-screen', className)}>
      {/* 页面头部 */}
      <div>
        <div className='container mx-auto px-4 py-6 sm:px-6 lg:px-8'>
          <div className='flex items-center justify-between'>
            <div className='flex-1 min-w-0'>
              {/* 返回按钮 */}
              {showBack && (
                <button
                  onClick={onBack}
                  className='inline-flex items-center text-sm text-muted-foreground hover:text-foreground mb-2 transition-colors'
                  aria-label='返回上一页'
                >
                  <svg
                    className='w-4 h-4 mr-1'
                    fill='none'
                    strokeLinecap='round'
                    strokeLinejoin='round'
                    strokeWidth='2'
                    viewBox='0 0 24 24'
                    stroke='currentColor'
                  >
                    <path d='M15 19l-7-7 7-7' />
                  </svg>
                  返回
                </button>
              )}

              {/* 标题 */}
              <h1 className='text-2xl sm:text-3xl font-bold text-foreground tracking-tight'>
                {title}
              </h1>

              {/* 描述 */}
              {description && (
                <p className='mt-2 text-sm text-muted-foreground max-w-2xl'>
                  {description}
                </p>
              )}
            </div>

            {/* 操作区域 */}
            {extra && <div className='ml-4 flex-shrink-0'>{extra}</div>}
          </div>
        </div>
      </div>

      {/* 页面内容 */}
      <div className='container mx-auto px-4 py-6 sm:px-6 lg:px-8'>
        <div className='space-y-6'>{children}</div>
      </div>
    </div>
  );
};
