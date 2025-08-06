/**
 * Suspense 包装器组件
 * 为懒加载组件提供加载状态和错误边界保护
 */

import type { ErrorInfo } from '@/types/common';
import type { ReactNode } from 'react';
import React, { Suspense } from 'react';
import ErrorBoundary from './ErrorBoundary';

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
 * 默认加载组件
 */
const DefaultLoading: React.FC<{ text: string }> = ({ text }) => (
    <div className="flex items-center justify-center min-h-[200px]">
        <div className="flex flex-col items-center space-y-4">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            <p className="text-sm text-gray-600">{text}</p>
        </div>
    </div>
);

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
    loadingText = "正在加载...",
    errorBoundaryProps = {}
}) => {
    // 默认加载组件
    const defaultFallback = fallback || <DefaultLoading text={loadingText} />;

    // 只有 Suspense 包装
    const suspenseContent = (
        <Suspense fallback={defaultFallback}>
            {children}
        </Suspense>
    );

    // 如果启用错误边界，则添加错误边界包装
    if (enableErrorBoundary) {
        return (
            <ErrorBoundary
                enableErrorReporting={true}
                {...errorBoundaryProps}
            >
                {suspenseContent}
            </ErrorBoundary>
        );
    }

    return suspenseContent;
};

export default SuspenseWrapper;