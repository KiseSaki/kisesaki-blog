/**
 * API 请求工具函数
 * 提供带加载状态的 API 请求封装
 */

import { useState, useCallback, useEffect } from 'react';

/**
 * API 请求状态接口
 */
export interface ApiState<T> {
  data: T | null;
  loading: boolean;
  error: string | null;
}

/**
 * API 请求函数类型
 */
export type ApiFunction<T, P = unknown> = (params?: P) => Promise<T>;

/**
 * 使用带加载状态的 API Hook
 * @param apiFunction API 请求函数
 * @returns 包含数据、加载状态和错误信息的对象，以及执行请求的函数
 */
export function useApiCall<T, P = unknown>(
  apiFunction: ApiFunction<T, P>
): [ApiState<T>, (params?: P) => Promise<void>] {
  const [state, setState] = useState<ApiState<T>>({
    data: null,
    loading: false,
    error: null,
  });

  const execute = useCallback(
    async (params?: P) => {
      setState(prev => ({ ...prev, loading: true, error: null }));
      
      try {
        const data = await apiFunction(params);
        setState({ data, loading: false, error: null });
      } catch (error) {
        const errorMessage = error instanceof Error ? error.message : '请求失败';
        setState(prev => ({ ...prev, loading: false, error: errorMessage }));
      }
    },
    [apiFunction]
  );

  return [state, execute];
}

/**
 * 立即执行的 API Hook
 * @param apiFunction API 请求函数
 * @param params 请求参数
 * @returns 包含数据、加载状态和错误信息的对象和刷新函数
 */
export function useApiData<T, P = unknown>(
  apiFunction: ApiFunction<T, P>,
  params?: P
): [ApiState<T>, () => void] {
  const [state, setState] = useState<ApiState<T>>({
    data: null,
    loading: true,
    error: null,
  });

  const execute = useCallback(async () => {
    setState(prev => ({ ...prev, loading: true, error: null }));
    
    try {
      const data = await apiFunction(params);
      setState({ data, loading: false, error: null });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : '请求失败';
      setState(prev => ({ ...prev, loading: false, error: errorMessage }));
    }
  }, [apiFunction, params]);

  useEffect(() => {
    execute();
  }, [execute]);

  return [state, execute];
}
