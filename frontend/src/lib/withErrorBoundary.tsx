import type { ErrorBoundaryProps } from '@/components/common/ErrorBoundary';
import ErrorBoundary from '@/components/common/ErrorBoundary';
import React from 'react';

/**
 * 高阶组件：为组件添加错误边界
 * 
 * @example
 * ```tsx
 * const SafeComponent = withErrorBoundary(MyComponent, {
 *   onError: (error, errorInfo) => console.log('Error in MyComponent:', error),
 *   enableErrorReporting: true
 * });
 * ```
 */
export function withErrorBoundary<P extends object>(
    Component: React.ComponentType<P>,
    errorBoundaryProps?: Omit<ErrorBoundaryProps, 'children'>
) {
    const WrappedComponent = (props: P) => (
        <ErrorBoundary {...errorBoundaryProps}>
            <Component {...props} />
        </ErrorBoundary>
    );

    WrappedComponent.displayName = `withErrorBoundary(${Component.displayName || Component.name})`;

    return WrappedComponent;
}
