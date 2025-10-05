import { cn } from '@/lib/utils';
import { cva, type VariantProps } from 'class-variance-authority';
import { X } from 'lucide-react';
import React from 'react';

/**
 * Tag 组件样式变体定义
 * 支持多种颜色、大小和样式变体
 */
const tagVariants = cva(
  'inline-flex items-center gap-1 font-medium transition-all duration-200 select-none',
  {
    variants: {
      variant: {
        solid: '',
        outlined: 'border-2 bg-transparent',
        soft: '',
        light: '',
      },
      color: {
        default: '',
        primary: '',
        success: '',
        warning: '',
        error: '',
        info: '',
        purple: '',
        pink: '',
        orange: '',
        cyan: '',
        teal: '',
        indigo: '',
        violet: '',
        rose: '',
      },
      size: {
        sm: 'px-2 py-0.5 text-xs rounded',
        md: 'px-2.5 py-1 text-sm rounded-md',
        lg: 'px-3 py-1.5 text-base rounded-lg',
      },
    },
    compoundVariants: [
      // Solid 变体颜色
      {
        variant: 'solid',
        color: 'default',
        className: 'bg-gray-500 text-white hover:bg-gray-600',
      },
      {
        variant: 'solid',
        color: 'primary',
        className: 'bg-blue-500 text-white hover:bg-blue-600',
      },
      {
        variant: 'solid',
        color: 'success',
        className: 'bg-green-500 text-white hover:bg-green-600',
      },
      {
        variant: 'solid',
        color: 'warning',
        className: 'bg-yellow-500 text-white hover:bg-yellow-600',
      },
      {
        variant: 'solid',
        color: 'error',
        className: 'bg-red-500 text-white hover:bg-red-600',
      },
      {
        variant: 'solid',
        color: 'info',
        className: 'bg-cyan-500 text-white hover:bg-cyan-600',
      },
      {
        variant: 'solid',
        color: 'purple',
        className: 'bg-purple-500 text-white hover:bg-purple-600',
      },
      {
        variant: 'solid',
        color: 'pink',
        className: 'bg-pink-500 text-white hover:bg-pink-600',
      },
      {
        variant: 'solid',
        color: 'orange',
        className: 'bg-orange-500 text-white hover:bg-orange-600',
      },
      {
        variant: 'solid',
        color: 'cyan',
        className: 'bg-cyan-500 text-white hover:bg-cyan-600',
      },
      {
        variant: 'solid',
        color: 'teal',
        className: 'bg-teal-500 text-white hover:bg-teal-600',
      },
      {
        variant: 'solid',
        color: 'indigo',
        className: 'bg-indigo-500 text-white hover:bg-indigo-600',
      },
      {
        variant: 'solid',
        color: 'violet',
        className: 'bg-violet-500 text-white hover:bg-violet-600',
      },
      {
        variant: 'solid',
        color: 'rose',
        className: 'bg-rose-500 text-white hover:bg-rose-600',
      },

      // Outlined 变体颜色
      {
        variant: 'outlined',
        color: 'default',
        className: 'border-gray-500 text-gray-700 hover:bg-gray-50',
      },
      {
        variant: 'outlined',
        color: 'primary',
        className: 'border-blue-500 text-blue-700 hover:bg-blue-50',
      },
      {
        variant: 'outlined',
        color: 'success',
        className: 'border-green-500 text-green-700 hover:bg-green-50',
      },
      {
        variant: 'outlined',
        color: 'warning',
        className: 'border-yellow-500 text-yellow-700 hover:bg-yellow-50',
      },
      {
        variant: 'outlined',
        color: 'error',
        className: 'border-red-500 text-red-700 hover:bg-red-50',
      },
      {
        variant: 'outlined',
        color: 'info',
        className: 'border-cyan-500 text-cyan-700 hover:bg-cyan-50',
      },
      {
        variant: 'outlined',
        color: 'purple',
        className: 'border-purple-500 text-purple-700 hover:bg-purple-50',
      },
      {
        variant: 'outlined',
        color: 'pink',
        className: 'border-pink-500 text-pink-700 hover:bg-pink-50',
      },
      {
        variant: 'outlined',
        color: 'orange',
        className: 'border-orange-500 text-orange-700 hover:bg-orange-50',
      },
      {
        variant: 'outlined',
        color: 'cyan',
        className: 'border-cyan-500 text-cyan-700 hover:bg-cyan-50',
      },
      {
        variant: 'outlined',
        color: 'teal',
        className: 'border-teal-500 text-teal-700 hover:bg-teal-50',
      },
      {
        variant: 'outlined',
        color: 'indigo',
        className: 'border-indigo-500 text-indigo-700 hover:bg-indigo-50',
      },
      {
        variant: 'outlined',
        color: 'violet',
        className: 'border-violet-500 text-violet-700 hover:bg-violet-50',
      },
      {
        variant: 'outlined',
        color: 'rose',
        className: 'border-rose-500 text-rose-700 hover:bg-rose-50',
      },

      // Soft 变体颜色（柔和背景）
      {
        variant: 'soft',
        color: 'default',
        className: 'bg-gray-100 text-gray-700 hover:bg-gray-200',
      },
      {
        variant: 'soft',
        color: 'primary',
        className: 'bg-blue-100 text-blue-700 hover:bg-blue-200',
      },
      {
        variant: 'soft',
        color: 'success',
        className: 'bg-green-100 text-green-700 hover:bg-green-200',
      },
      {
        variant: 'soft',
        color: 'warning',
        className: 'bg-yellow-100 text-yellow-700 hover:bg-yellow-200',
      },
      {
        variant: 'soft',
        color: 'error',
        className: 'bg-red-100 text-red-700 hover:bg-red-200',
      },
      {
        variant: 'soft',
        color: 'info',
        className: 'bg-cyan-100 text-cyan-700 hover:bg-cyan-200',
      },
      {
        variant: 'soft',
        color: 'purple',
        className: 'bg-purple-100 text-purple-700 hover:bg-purple-200',
      },
      {
        variant: 'soft',
        color: 'pink',
        className: 'bg-pink-100 text-pink-700 hover:bg-pink-200',
      },
      {
        variant: 'soft',
        color: 'orange',
        className: 'bg-orange-100 text-orange-700 hover:bg-orange-200',
      },
      {
        variant: 'soft',
        color: 'cyan',
        className: 'bg-cyan-100 text-cyan-700 hover:bg-cyan-200',
      },
      {
        variant: 'soft',
        color: 'teal',
        className: 'bg-teal-100 text-teal-700 hover:bg-teal-200',
      },
      {
        variant: 'soft',
        color: 'indigo',
        className: 'bg-indigo-100 text-indigo-700 hover:bg-indigo-200',
      },
      {
        variant: 'soft',
        color: 'violet',
        className: 'bg-violet-100 text-violet-700 hover:bg-violet-200',
      },
      {
        variant: 'soft',
        color: 'rose',
        className: 'bg-rose-100 text-rose-700 hover:bg-rose-200',
      },

      // Light 变体颜色（更浅的背景）
      {
        variant: 'light',
        color: 'default',
        className: 'bg-gray-50 text-gray-600 hover:bg-gray-100',
      },
      {
        variant: 'light',
        color: 'primary',
        className: 'bg-blue-50 text-blue-600 hover:bg-blue-100',
      },
      {
        variant: 'light',
        color: 'success',
        className: 'bg-green-50 text-green-600 hover:bg-green-100',
      },
      {
        variant: 'light',
        color: 'warning',
        className: 'bg-yellow-50 text-yellow-600 hover:bg-yellow-100',
      },
      {
        variant: 'light',
        color: 'error',
        className: 'bg-red-50 text-red-600 hover:bg-red-100',
      },
      {
        variant: 'light',
        color: 'info',
        className: 'bg-cyan-50 text-cyan-600 hover:bg-cyan-100',
      },
      {
        variant: 'light',
        color: 'purple',
        className: 'bg-purple-50 text-purple-600 hover:bg-purple-100',
      },
      {
        variant: 'light',
        color: 'pink',
        className: 'bg-pink-50 text-pink-600 hover:bg-pink-100',
      },
      {
        variant: 'light',
        color: 'orange',
        className: 'bg-orange-50 text-orange-600 hover:bg-orange-100',
      },
      {
        variant: 'light',
        color: 'cyan',
        className: 'bg-cyan-50 text-cyan-600 hover:bg-cyan-100',
      },
      {
        variant: 'light',
        color: 'teal',
        className: 'bg-teal-50 text-teal-600 hover:bg-teal-100',
      },
      {
        variant: 'light',
        color: 'indigo',
        className: 'bg-indigo-50 text-indigo-600 hover:bg-indigo-100',
      },
      {
        variant: 'light',
        color: 'violet',
        className: 'bg-violet-50 text-violet-600 hover:bg-violet-100',
      },
      {
        variant: 'light',
        color: 'rose',
        className: 'bg-rose-50 text-rose-600 hover:bg-rose-100',
      },
    ],
    defaultVariants: {
      variant: 'soft',
      color: 'default',
      size: 'md',
    },
  }
);

/**
 * Tag 组件属性接口
 */
export interface TagProps
  extends Omit<React.HTMLAttributes<HTMLSpanElement>, 'color'>,
    VariantProps<typeof tagVariants> {
  /**
   * Tag 的内容
   */
  children: React.ReactNode;

  /**
   * 是否可关闭
   */
  closable?: boolean;

  /**
   * 关闭时的回调
   */
  onClose?: (e: React.MouseEvent<HTMLElement>) => void;

  /**
   * 是否显示圆点（左侧小圆点装饰）
   */
  dot?: boolean;

  /**
   * 左侧图标
   */
  icon?: React.ReactNode;
}

/**
 * Tag 标签组件
 *
 * @example
 * ```tsx
 * // 基础用法
 * <Tag>Default</Tag>
 *
 * // 不同颜色
 * <Tag color="primary">Primary</Tag>
 * <Tag color="success">Success</Tag>
 * <Tag color="warning">Warning</Tag>
 *
 * // 不同变体
 * <Tag variant="solid" color="primary">Solid</Tag>
 * <Tag variant="outlined" color="success">Outlined</Tag>
 * <Tag variant="soft" color="warning">Soft</Tag>
 * <Tag variant="light" color="error">Light</Tag>
 *
 * // 不同大小
 * <Tag size="sm">Small</Tag>
 * <Tag size="md">Medium</Tag>
 * <Tag size="lg">Large</Tag>
 *
 * // 可关闭的标签
 * <Tag closable onClose={() => console.log('closed')}>Closable</Tag>
 *
 * // 带图标
 * <Tag icon={<StarIcon />}>With Icon</Tag>
 *
 * // 带圆点
 * <Tag dot color="success">Online</Tag>
 * ```
 */
export const Tag = React.forwardRef<HTMLSpanElement, TagProps>(
  (
    {
      className,
      variant,
      color,
      size,
      children,
      closable,
      onClose,
      dot,
      icon,
      ...props
    },
    ref
  ) => {
    const handleClose = (e: React.MouseEvent<HTMLElement>) => {
      e.stopPropagation();
      onClose?.(e);
    };

    return (
      <span
        ref={ref}
        className={cn(tagVariants({ variant, color, size }), className)}
        {...props}
      >
        {/* 左侧圆点 */}
        {dot && (
          <span
            className={cn(
              'inline-block w-1.5 h-1.5 rounded-full',
              // 根据当前颜色自动匹配圆点颜色
              color === 'primary' && 'bg-blue-500',
              color === 'success' && 'bg-green-500',
              color === 'warning' && 'bg-yellow-500',
              color === 'error' && 'bg-red-500',
              color === 'info' && 'bg-cyan-500',
              color === 'purple' && 'bg-purple-500',
              color === 'pink' && 'bg-pink-500',
              color === 'orange' && 'bg-orange-500',
              color === 'cyan' && 'bg-cyan-500',
              color === 'teal' && 'bg-teal-500',
              color === 'indigo' && 'bg-indigo-500',
              color === 'violet' && 'bg-violet-500',
              color === 'rose' && 'bg-rose-500',
              color === 'default' && 'bg-gray-500'
            )}
          />
        )}

        {/* 左侧图标 */}
        {icon && (
          <span className={cn('inline-flex', size === 'sm' && 'text-xs')}>
            {icon}
          </span>
        )}

        {/* 标签内容 */}
        <span>{children}</span>

        {/* 关闭按钮 */}
        {closable && (
          <button
            type='button'
            onClick={handleClose}
            className={cn(
              'inline-flex items-center justify-center rounded-full',
              'hover:bg-black/10 transition-colors',
              'focus:outline-none focus:ring-1 focus:ring-current',
              size === 'sm' && 'w-3 h-3',
              size === 'md' && 'w-3.5 h-3.5',
              size === 'lg' && 'w-4 h-4'
            )}
            aria-label='关闭标签'
          >
            <X
              className={cn(
                size === 'sm' && 'w-2.5 h-2.5',
                size === 'md' && 'w-3 h-3',
                size === 'lg' && 'w-3.5 h-3.5'
              )}
            />
          </button>
        )}
      </span>
    );
  }
);

Tag.displayName = 'Tag';

/**
 * TagGroup 标签组组件
 * 用于展示一组标签
 */
export interface TagGroupProps extends React.HTMLAttributes<HTMLDivElement> {
  /**
   * 标签组的内容
   */
  children: React.ReactNode;

  /**
   * 标签之间的间距
   */
  gap?: 'sm' | 'md' | 'lg';
}

export const TagGroup = React.forwardRef<HTMLDivElement, TagGroupProps>(
  ({ className, children, gap = 'md', ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={cn(
          'flex flex-wrap items-center',
          gap === 'sm' && 'gap-1',
          gap === 'md' && 'gap-2',
          gap === 'lg' && 'gap-3',
          className
        )}
        {...props}
      >
        {children}
      </div>
    );
  }
);

TagGroup.displayName = 'TagGroup';
