import { cn } from '@/lib';
import { useState } from 'react';

interface ImageProps
  extends Omit<React.ImgHTMLAttributes<HTMLImageElement>, 'src'> {
  alt: string;
  src?: string | null;
  width?: number | string;
  height?: number | string;
  className?: string;
  /**
   * 图片加载失败或未提供 src 时显示的占位符
   * @default '暂无图片'
   */
  placeholder?: string;
  /**
   * 是否显示加载状态
   * @default false
   */
  showLoading?: boolean;
}

/**
 * 图片组件
 * 支持自定义大小、占位符、加载状态和错误处理
 */
const Image = ({
  alt,
  src,
  width,
  height,
  className,
  placeholder = '暂无图片',
  showLoading = false,
  ...rest
}: ImageProps) => {
  const [isLoading, setIsLoading] = useState(showLoading);
  const [hasError, setHasError] = useState(false);

  // 如果没有提供 src 或 src 为空/null，显示占位符
  if (!src) {
    return (
      <div
        className={cn(
          'flex items-center justify-center bg-gradient-to-br from-muted via-muted/80 to-secondary/20',
          className
        )}
        style={{ width, height }}
      >
        <span className='text-muted-foreground text-sm'>{placeholder}</span>
      </div>
    );
  }

  // 如果图片加载失败，显示占位符
  if (hasError) {
    return (
      <div
        className={cn(
          'flex items-center justify-center bg-gradient-to-br from-muted via-muted/80 to-secondary/20',
          className
        )}
        style={{ width, height }}
      >
        <span className='text-muted-foreground text-sm'>{placeholder}</span>
      </div>
    );
  }

  return (
    <>
      {isLoading && (
        <div
          className={cn(
            'flex items-center justify-center bg-muted animate-pulse',
            className
          )}
          style={{ width, height }}
        >
          <span className='text-muted-foreground text-sm'>加载中...</span>
        </div>
      )}
      <img
        alt={alt}
        src={src}
        width={width}
        height={height}
        className={cn(isLoading ? 'hidden' : '', className)}
        onLoad={() => setIsLoading(false)}
        onError={() => {
          setIsLoading(false);
          setHasError(true);
        }}
        {...rest}
      />
    </>
  );
};

export default Image;
