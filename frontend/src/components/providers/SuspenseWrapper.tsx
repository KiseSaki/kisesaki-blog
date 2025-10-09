/**
 * Suspense 包装器组件
 * 为懒加载组件提供加载状态和错误边界保护
 */

import type { ErrorInfo } from '@/types/common';
import type { ReactNode } from 'react';
import React, { Suspense } from 'react';
import { Loading } from '../feedback';
import ErrorBoundary from '../feedback/ErrorBoundary';

/**
 * SuspenseWrapper 组件属性
 */
interface SuspenseWrapperProps {
  children: ReactNode;
  fallback?: ReactNode;
  enableErrorBoundary?: boolean;
  loadingText?: string;
  errorBoundaryProps?: {
    onError?: (error: Error, errorInfo: ErrorInfo) => void;
    showReload?: boolean;
    showHomeButton?: boolean;
  };
}

/**
 * Suspense 包装器组件
 *
 * 提供以下功能：
 * - 懒加载组件的加载状态
 * - 可选的错误边界保护
 * - 自定义加载界面
 *
 * @example
 * ```tsx
 * <SuspenseWrapper
 *   enableErrorBoundary={true}
 *   loadingText="正在加载页面..."
 * >
 *   <LazyComponent />
 * </SuspenseWrapper>
 * ```
 */
export const SuspenseWrapper: React.FC<SuspenseWrapperProps> = ({
  children,
  fallback,
  enableErrorBoundary = true,
  loadingText = '正在加载...',
  errorBoundaryProps = {},
}) => {
  // 默认加载组件
  const defaultFallback = fallback || <Loading text={loadingText} />;

  // 只有 Suspense 包装
  const suspenseContent = (
    <Suspense fallback={defaultFallback}>{children}</Suspense>
  );

  // 如果启用错误边界，则添加错误边界包装
  if (enableErrorBoundary) {
    return (
      <ErrorBoundary enableErrorReporting={true} {...errorBoundaryProps}>
        {suspenseContent}
      </ErrorBoundary>
    );
  }

  return suspenseContent;
};

export default SuspenseWrapper;
