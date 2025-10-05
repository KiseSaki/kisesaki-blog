import { cn } from '@/lib/utils';
import { cva, type VariantProps } from 'class-variance-authority';
import { X } from 'lucide-react';
import React, { useMemo } from 'react';

/**
 * 预定义的颜色列表，用于随机选择
 */
const PRESET_COLORS = [
  'primary',
  'success',
  'warning',
  'error',
  'info',
  'purple',
  'pink',
  'orange',
  'cyan',
  'teal',
  'indigo',
  'violet',
  'rose',
] as const;

/**
 * 根据字符串生成一致的随机颜色
 * 确保相同的输入总是返回相同的颜色
 */
const getRandomColor = (seed?: string): (typeof PRESET_COLORS)[number] => {
  if (!seed) {
    return PRESET_COLORS[Math.floor(Math.random() * PRESET_COLORS.length)];
  }
  // 使用简单的哈希函数确保相同的 seed 总是得到相同的颜色
  let hash = 0;
  for (let i = 0; i < seed.length; i++) {
    hash = seed.charCodeAt(i) + ((hash << 5) - hash);
  }
  const index = Math.abs(hash) % PRESET_COLORS.length;
  return PRESET_COLORS[index];
};

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

type PresetColor = VariantProps<typeof tagVariants>['color'];

/**
 * Tag 组件属性接口
 */
export interface TagProps
  extends Omit<React.HTMLAttributes<HTMLSpanElement>, 'color'> {
  /**
   * Tag 的内容
   */
  children: React.ReactNode;

  /**
   * 标签颜色
   * - 预定义颜色：'default' | 'primary' | 'success' | 'warning' | 'error' 等
   * - 自定义颜色：任意 CSS 颜色值（如 '#ff0000', 'rgb(255, 0, 0)'）
   * - 'default' 时会随机选择一个预定义颜色
   */
  color?: PresetColor | string;

  /**
   * 标签变体
   */
  variant?: VariantProps<typeof tagVariants>['variant'];

  /**
   * 标签大小
   */
  size?: VariantProps<typeof tagVariants>['size'];

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

  /**
   * 随机颜色的种子值（用于确保相同内容的标签颜色一致）
   * 当 color 为 'default' 时有效
   */
  colorSeed?: string;
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
 * // 自定义颜色
 * <Tag color="#ff0000">Custom Red</Tag>
 * <Tag color="rgb(0, 255, 0)">Custom Green</Tag>
 * <Tag color="#8b5cf6" variant="solid">Custom Purple</Tag>
 *
 * // 随机颜色（color='default' 时自动随机）
 * <Tag>Random Tag 1</Tag>
 * <Tag>Random Tag 2</Tag>
 * // 使用相同文本会得到相同颜色
 * <Tag>React</Tag>
 * <Tag>React</Tag>
 * // 使用 colorSeed 确保颜色一致性
 * <Tag colorSeed="unique-id-123">Tag A</Tag>
 * <Tag colorSeed="unique-id-123">Tag B</Tag>
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
      variant = 'soft',
      color = 'default',
      size,
      children,
      closable,
      onClose,
      dot,
      icon,
      colorSeed,
      style,
      ...props
    },
    ref
  ) => {
    // 处理颜色逻辑
    const { computedColor, isCustomColor, customColorValue } = useMemo(() => {
      // 如果是 default，随机选择颜色
      if (color === 'default') {
        // 使用 colorSeed 或 children 的字符串值作为种子
        const seed =
          colorSeed || (typeof children === 'string' ? children : undefined);
        return {
          computedColor: getRandomColor(seed),
          isCustomColor: false,
          customColorValue: undefined,
        };
      }

      // 检查是否是预定义颜色
      const presetColors: string[] = [
        'default',
        'primary',
        'success',
        'warning',
        'error',
        'info',
        'purple',
        'pink',
        'orange',
        'cyan',
        'teal',
        'indigo',
        'violet',
        'rose',
      ];

      if (color && presetColors.includes(color)) {
        return {
          computedColor: color as PresetColor,
          isCustomColor: false,
          customColorValue: undefined,
        };
      }

      // 自定义颜色
      return {
        computedColor: 'default' as PresetColor,
        isCustomColor: true,
        customColorValue: color,
      };
    }, [color, children, colorSeed]);

    // 自定义颜色的内联样式
    const customStyle = useMemo(() => {
      if (!isCustomColor || !customColorValue) return style;

      const baseStyle: React.CSSProperties = { ...style };

      if (variant === 'solid') {
        baseStyle.backgroundColor = customColorValue;
        baseStyle.color = '#ffffff';
      } else if (variant === 'outlined') {
        baseStyle.borderColor = customColorValue;
        baseStyle.color = customColorValue;
      } else if (variant === 'soft') {
        // 使用自定义颜色的浅色版本
        baseStyle.backgroundColor = `${customColorValue}20`; // 20% 透明度
        baseStyle.color = customColorValue;
      } else if (variant === 'light') {
        baseStyle.backgroundColor = `${customColorValue}10`; // 10% 透明度
        baseStyle.color = customColorValue;
      }

      return baseStyle;
    }, [isCustomColor, customColorValue, variant, style]);

    const handleClose = (e: React.MouseEvent<HTMLElement>) => {
      e.stopPropagation();
      onClose?.(e);
    };

    return (
      <span
        ref={ref}
        className={cn(
          tagVariants({
            variant,
            color: computedColor,
            size,
          }),
          isCustomColor && variant === 'outlined' && 'border-2',
          className
        )}
        style={customStyle}
        {...props}
      >
        {/* 左侧圆点 */}
        {dot && (
          <span
            className={cn(
              'inline-block w-1.5 h-1.5 rounded-full',
              !isCustomColor && [
                // 根据当前颜色自动匹配圆点颜色
                computedColor === 'primary' && 'bg-blue-500',
                computedColor === 'success' && 'bg-green-500',
                computedColor === 'warning' && 'bg-yellow-500',
                computedColor === 'error' && 'bg-red-500',
                computedColor === 'info' && 'bg-cyan-500',
                computedColor === 'purple' && 'bg-purple-500',
                computedColor === 'pink' && 'bg-pink-500',
                computedColor === 'orange' && 'bg-orange-500',
                computedColor === 'cyan' && 'bg-cyan-500',
                computedColor === 'teal' && 'bg-teal-500',
                computedColor === 'indigo' && 'bg-indigo-500',
                computedColor === 'violet' && 'bg-violet-500',
                computedColor === 'rose' && 'bg-rose-500',
                computedColor === 'default' && 'bg-gray-500',
              ]
            )}
            style={
              isCustomColor && customColorValue
                ? { backgroundColor: customColorValue }
                : undefined
            }
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
