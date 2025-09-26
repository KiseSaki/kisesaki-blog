/**
 * 错误边界组件
 * 捕获 React 组件树中的错误并显示友好的错误界面
 */

import type { ErrorInfo, ErrorLogData } from '@/types/common';
import { AlertTriangle, Home, RefreshCw } from 'lucide-react';
import type { ReactNode } from 'react';
import React, { Component } from 'react';
import { toast } from 'sonner';

/**
 * 错误边界组件的属性接口
 */
export interface ErrorBoundaryProps {
  children: ReactNode; // 子组件
  fallback?: ReactNode; // 自定义错误界面
  onError?: (error: Error, errorInfo: ErrorInfo) => void; // 错误处理回调
  showReload?: boolean; // 是否显示重新加载按钮
  showHomeButton?: boolean; // 是否显示返回首页按钮
  enableErrorReporting?: boolean; // 是否启用错误日志上报
}

/**
 * 错误边界组件的状态接口
 */
interface ErrorBoundaryState {
  hasError: boolean;
  error: Error | null;
  errorInfo: ErrorInfo | null;
}

/**
 * 错误日志上报函数
 */
const reportError = async (errorData: ErrorLogData): Promise<void> => {
  try {
    // 在实际项目中，这里会调用后端API上报错误
    // await api.post('/api/logs/error', errorData);
    console.error('[ErrorBoundary] Error reported:', errorData);

    // 开发环境下显示详细错误信息
    if (import.meta.env.DEV) {
      console.group('🐛 Error Boundary Caught Error');
      console.error('Error:', errorData.error);
      console.error('Error Info:', errorData.errorInfo);
      console.error('User Agent:', errorData.userAgent);
      console.error('URL:', errorData.url);
      console.groupEnd();
    }
  } catch (reportingError) {
    console.error('[ErrorBoundary] Failed to report error:', reportingError);
  }
};

/**
 * React 错误边界组件
 *
 * 功能特性：
 * - 捕获子组件树中的 JavaScript 错误
 * - 显示友好的错误界面
 * - 提供错误恢复机制（重新加载、返回首页）
 * - 自动错误日志上报
 * - 开发环境下显示详细错误信息
 *
 * @example
 * ```tsx
 * <ErrorBoundary
 *   onError={(error, errorInfo) => console.log('Error caught:', error)}
 *   enableErrorReporting={true}
 * >
 *   <App />
 * </ErrorBoundary>
 * ```
 */
export class ErrorBoundary extends Component<
  ErrorBoundaryProps,
  ErrorBoundaryState
> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = {
      hasError: false,
      error: null,
      errorInfo: null,
    };
  }

  /**
   * 静态方法：从错误中派生状态
   */
  static getDerivedStateFromError(error: Error): Partial<ErrorBoundaryState> {
    return {
      hasError: true,
      error,
    };
  }

  /**
   * 捕获错误并进行处理
   */
  componentDidCatch(error: Error, errorInfo: React.ErrorInfo): void {
    const { onError, enableErrorReporting = true } = this.props;

    // 构建错误信息对象
    const errorInfoData: ErrorInfo = {
      message: error.message,
      stack: error.stack,
      componentStack: errorInfo.componentStack || undefined,
      errorBoundary: this.constructor.name,
      errorBoundaryStack: errorInfo.componentStack || undefined,
      eventType: 'react_error_boundary',
    };

    // 更新状态
    this.setState({
      errorInfo: errorInfoData,
    });

    // 调用外部错误处理函数
    if (onError) {
      onError(error, errorInfoData);
    }

    // 显示错误提示
    toast.error('应用发生了错误，请稍后重试');

    // 错误日志上报
    if (enableErrorReporting) {
      const errorLogData: ErrorLogData = {
        error,
        errorInfo: errorInfoData,
        userAgent: navigator.userAgent,
        url: window.location.href,
        userId: this.getUserId(),
        timestamp: Date.now(),
        level: 'error',
      };

      reportError(errorLogData).catch(console.error);
    }
  }

  /**
   * 获取当前用户ID（如果已登录）
   */
  private getUserId(): string | undefined {
    try {
      // 从 localStorage 中获取用户信息
      const authData = localStorage.getItem('auth-store');
      if (authData) {
        const parsed = JSON.parse(authData);
        return parsed?.state?.user?.id;
      }
    } catch (error) {
      console.warn('Failed to get user ID:', error);
    }
    return undefined;
  }

  /**
   * 重新加载页面
   */
  private handleReload = (): void => {
    window.location.reload();
  };

  /**
   * 返回首页
   */
  private handleGoHome = (): void => {
    window.location.href = '/';
  };

  /**
   * 重置错误状态
   */
  private handleRetry = (): void => {
    this.setState({
      hasError: false,
      error: null,
      errorInfo: null,
    });
  };

  render(): ReactNode {
    const {
      children,
      fallback,
      showReload = true,
      showHomeButton = true,
    } = this.props;
    const { hasError, error, errorInfo } = this.state;

    if (hasError) {
      // 如果提供了自定义错误界面，则使用它
      if (fallback) {
        return fallback;
      }

      // 默认错误界面
      return (
        <div className='min-h-screen flex items-center justify-center bg-gray-50 px-4 py-12 sm:px-6 lg:px-8'>
          <div className='max-w-md w-full space-y-8 text-center'>
            <div>
              <AlertTriangle className='mx-auto h-12 w-12 text-red-500' />
              <h2 className='mt-6 text-3xl font-extrabold text-gray-900'>
                出错了
              </h2>
              <p className='mt-2 text-sm text-gray-600'>
                抱歉，应用遇到了一个意外错误
              </p>
            </div>

            {/* 开发环境下显示错误详情 */}
            {import.meta.env.DEV && error && (
              <div className='bg-red-50 border border-red-200 rounded-md p-4 text-left'>
                <h3 className='text-sm font-medium text-red-800 mb-2'>
                  错误详情 (仅开发环境可见):
                </h3>
                <pre className='text-xs text-red-700 whitespace-pre-wrap break-words max-h-32 overflow-y-auto'>
                  {error.message}
                  {error.stack && `\n\n${error.stack}`}
                </pre>
                {errorInfo?.componentStack && (
                  <details className='mt-2'>
                    <summary className='text-xs text-red-600 cursor-pointer'>
                      组件堆栈
                    </summary>
                    <pre className='text-xs text-red-600 whitespace-pre-wrap break-words mt-1'>
                      {errorInfo.componentStack}
                    </pre>
                  </details>
                )}
              </div>
            )}

            {/* 操作按钮 */}
            <div className='flex flex-col sm:flex-row gap-3 justify-center'>
              <button
                onClick={this.handleRetry}
                className='inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors'
              >
                <RefreshCw className='w-4 h-4 mr-2' />
                重试
              </button>

              {showReload && (
                <button
                  onClick={this.handleReload}
                  className='inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors'
                >
                  <RefreshCw className='w-4 h-4 mr-2' />
                  重新加载
                </button>
              )}

              {showHomeButton && (
                <button
                  onClick={this.handleGoHome}
                  className='inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors'
                >
                  <Home className='w-4 h-4 mr-2' />
                  返回首页
                </button>
              )}
            </div>

            <p className='text-xs text-gray-500'>
              如果问题持续存在，请联系技术支持
            </p>
          </div>
        </div>
      );
    }

    return children;
  }
}

export default ErrorBoundary;
