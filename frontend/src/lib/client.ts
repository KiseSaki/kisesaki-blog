import { API_CONFIG, ENV_CONFIG, LOGIN_LINK, PAGINATION_CONFIG } from '@/config';
import axios, {
  type AxiosError,
  type AxiosInstance,
  type AxiosRequestConfig,
  type AxiosResponse,
  type InternalAxiosRequestConfig,
} from 'axios';
import { toast } from 'sonner';
import { useAuthStore } from '../stores/authStore';
import type {
  ApiRequestConfig,
  ApiResponse,
  ErrorCodeType,
  PageableParams,
} from '../types';
import {
  ERROR_MESSAGES,
  ErrorCategory,
  ErrorCode,
  getErrorCategory,
  isLoginRequiredError,
} from '../types';

/**
 * HTTP 客户端配置
 */
interface HttpClientConfig {
  // 基础 URL
  baseURL?: string;
  // 请求超时时间(ms)
  timeout?: number;
  // 是否启用全局错误提示
  enableGlobalErrorToast?: boolean;
  // 是否启用认证重定向
  enableAuthRedirect?: boolean;
}

/**
 * HTTP 客户端类
 */
class HttpClient {
  private instance: AxiosInstance;
  private config: HttpClientConfig;

  constructor(config: HttpClientConfig = {}) {
    this.config = {
      baseURL: ENV_CONFIG.API_BASE_URL,
      timeout: API_CONFIG.DEFAULT_TIMEOUT,
      enableGlobalErrorToast: true,
      enableAuthRedirect: true,
      ...config,
    };

    this.instance = axios.create({
      baseURL: this.config.baseURL,
      timeout: this.config.timeout,
      headers: {
        'Content-Type': 'application/json',
      },
      // 自定义参数序列化器，支持对象展开为点分隔格式
      paramsSerializer: {
        serialize: (params: Record<string, unknown>) => {
          return this.serializeParams(params).toString();
        },
      },
    });

    this.initInterceptors();
  }

  /**
   * 初始化拦截器
   */
  private initInterceptors() {
    // 请求拦截器
    this.instance.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const token = useAuthStore.getState().token?.accessToken;
        config.headers.Authorization = `Bearer ${token}`;

        return config;
      },
      error => Promise.reject(error)
    );

    // 响应拦截器
    this.instance.interceptors.response.use(
      <T>(response: AxiosResponse<ApiResponse<T>>): T => {
        const responseData = response.data;
        const config = response.config as InternalAxiosRequestConfig & {
          metadata?: ApiRequestConfig;
        };

        // 检查业务状态码，判断业务是否成功
        if (responseData.code !== 200 || responseData.success === false) {
          // 业务失败，创建错误对象并抛出
          const error = new Error(
            responseData.message || '请求失败'
          ) as AxiosError<ApiResponse<unknown>>;
          error.response = response as AxiosResponse<ApiResponse<unknown>>;
          error.config = response.config;
          error.isAxiosError = true;
          error.name = 'AxiosError';

          // 处理业务错误
          this.handleError(error, config?.metadata);
          throw error;
        }

        // 业务成功，调用成功回调
        if (config.metadata?.onSuccess) {
          config.metadata.onSuccess(responseData.data);
        }
        return responseData.data;
      },
      (error: AxiosError<ApiResponse<unknown>>) => {
        const config = error.config as InternalAxiosRequestConfig & {
          metadata?: ApiRequestConfig;
        };
        this.handleError(error, config?.metadata);
        return Promise.reject(error);
      }
    );
  }

  /**
   * 统一错误处理
   */
  private handleError(
    error: AxiosError<ApiResponse<unknown>>,
    config?: ApiRequestConfig
  ) {
    let errorMessage = '请求失败，请稍后重试';
    let errorCode: ErrorCodeType | undefined;

    // 从响应中获取错误码和消息
    if (error.response?.data) {
      const responseData = error.response.data;
      errorCode = responseData.code as ErrorCodeType;
      errorMessage =
        responseData.message || ERROR_MESSAGES[errorCode] || errorMessage;
    } else if (error.message) {
      errorMessage = error.message;
    }

    // 如果配置了自定义错误处理，则调用
    if (config?.onError) {
      const businessError = errorCode
        ? (new Error(errorMessage) as Error & { errorCode?: ErrorCodeType })
        : new Error(errorMessage);
      if (errorCode) {
        (businessError as Error & { errorCode: ErrorCodeType }).errorCode =
          errorCode;
      }
      config.onError(businessError);
      return;
    }

    // 根据错误分类处理
    if (errorCode) {
      const category = getErrorCategory(errorCode);

      // 认证错误处理
      if (category === ErrorCategory.AUTH) {
        this.handleAuthError(errorCode, config);
        return; // 认证错误不显示通用错误提示
      }
    }

    // 显示错误提示（除非配置为静默或禁用错误提示）
    if (
      config?.showError !== false &&
      config?.silent !== true &&
      this.config.enableGlobalErrorToast
    ) {
      toast.error(errorMessage);
    }

    // 注意：不在此处处理页面跳转
    // 页面级别的错误（如访问被禁止的页面）应该由路由层面处理
    // API 请求的错误（如提交评论失败）应该由业务逻辑处理，不应跳转页面
  }

  /**
   * 处理认证相关错误
   */
  private handleAuthError(errorCode: ErrorCodeType, config?: ApiRequestConfig) {
    // 需要重新登录的错误
    if (isLoginRequiredError(errorCode)) {
      this.handleUnauthorized();
      if (
        config?.showError !== false &&
        config?.silent !== true &&
        this.config.enableGlobalErrorToast
      ) {
        toast.error(ERROR_MESSAGES[errorCode] || '登录已过期，请重新登录');
      }
      return;
    }

    // 其他认证错误
    if (
      config?.showError !== false &&
      config?.silent !== true &&
      this.config.enableGlobalErrorToast
    ) {
      switch (errorCode) {
        case ErrorCode.ACCESS_DENIED:
          toast.error('权限不足');
          break;
        case ErrorCode.LOGIN_FAILED:
          toast.error('登录失败，请检查用户名和密码');
          break;
        case ErrorCode.ACCOUNT_DISABLED:
          toast.error('账户已被禁用，请联系管理员');
          break;
        case ErrorCode.ACCOUNT_LOCKED:
          toast.error('账户已被锁定，请稍后重试');
          break;
        default:
          toast.error(ERROR_MESSAGES[errorCode] || '认证失败');
      }
    }
  }

  /**
   * 处理未授权错误
   */
  private handleUnauthorized() {
    useAuthStore.getState().logout();

    // 避免在登录页重复跳转
    if (window.location.pathname !== LOGIN_LINK) {
      window.location.href = LOGIN_LINK;
    }
  }

  /**
   * 序列化查询参数
   * 处理数组、对象、空值等特殊情况
   */
  private serializeParams(params: Record<string, unknown>): URLSearchParams {
    const searchParams = new URLSearchParams();

    Object.entries(params).forEach(([key, value]) => {
      if (value == null || value === '') {
        return; // 跳过空值
      }

      if (Array.isArray(value)) {
        // 数组参数：tags=["vue", "react"] -> tags=vue&tags=react
        value.forEach(item => {
          if (item != null && item !== '') {
            searchParams.append(key, String(item));
          }
        });
      } else if (typeof value === 'object' && !Array.isArray(value)) {
        // 对象参数：展开为点分隔的参数
        // 例如：pageable: { currentPage: 1, pageSize: 10 } -> pageable.currentPage=1&pageable.pageSize=10
        Object.entries(value as Record<string, unknown>).forEach(
          ([subKey, subValue]) => {
            if (subValue != null && subValue !== '') {
              searchParams.append(`${key}.${subKey}`, String(subValue));
            }
          }
        );
      } else {
        // 普通参数
        searchParams.append(key, String(value));
      }
    });

    return searchParams;
  }

  /**
   * 构建分页查询的 URL
   */
  private buildPageableUrl(
    baseUrl: string,
    params: PageableParams & Record<string, unknown>
  ): string {
    const searchParams = this.serializeParams(params);
    const queryString = searchParams.toString();
    return queryString ? `${baseUrl}?${queryString}` : baseUrl;
  }

  // ============ HTTP 方法封装 ============

  /**
   * GET 请求
   */
  async get<T>(
    url: string,
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    return this.request<T>({ ...config, method: 'GET', url });
  }

  /**
   * POST 请求
   */
  async post<T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    return this.request<T>({ ...config, method: 'POST', url, data });
  }

  /**
   * PUT 请求
   */
  async put<T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    return this.request<T>({ ...config, method: 'PUT', url, data });
  }

  /**
   * PATCH 请求
   */
  async patch<T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    return this.request<T>({ ...config, method: 'PATCH', url, data });
  }

  /**
   * DELETE 请求
   */
  async delete<T>(
    url: string,
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    return this.request<T>({ ...config, method: 'DELETE', url });
  }

  /**
   * 通用请求方法
   */
  async request<T>(config: AxiosRequestConfig & ApiRequestConfig): Promise<T> {
    // 提取 ApiRequestConfig 的配置
    const {
      showLoading,
      showError,
      silent,
      onError,
      onSuccess,
      ...axiosConfig
    } = config;
    const apiConfig: ApiRequestConfig = {
      showLoading,
      showError,
      silent,
      onError,
      onSuccess,
    };

    // 将 ApiRequestConfig 作为元数据附加到请求配置中
    const requestConfig = {
      ...axiosConfig,
      metadata: apiConfig,
    } as InternalAxiosRequestConfig & { metadata: ApiRequestConfig };

    return this.instance.request(requestConfig);
  }

  // ============ 分页查询便捷方法 ============

  /**
   * 分页查询 GET 请求
   * 自动处理查询参数的序列化和 URL 构建
   *
   * @param url 基础 URL
   * @param params 分页和查询参数
   * @param config 额外的请求配置
   * @returns 分页响应数据
   */
  async getPageable<T>(
    url: string,
    params: PageableParams & Record<string, unknown> = {},
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    // 设置默认分页参数
    const defaultParams: PageableParams = {
      currentPage: PAGINATION_CONFIG.DEFAULT_CURRENT_PAGE,
      pageSize: PAGINATION_CONFIG.DEFAULT_PAGE_SIZE,
      sort: PAGINATION_CONFIG.DEFAULT_SORT,
      includeTotal: true,
    };

    const mergedParams = { ...defaultParams, ...params };
    const requestUrl = this.buildPageableUrl(url, mergedParams);

    return this.get<T>(requestUrl, config);
  }

  /**
   * 搜索查询（通常也是分页的）
   *
   * @param url 搜索 URL
   * @param searchParams 搜索参数
   * @param config 额外的请求配置
   * @returns 搜索结果
   */
  async search<T>(
    url: string,
    searchParams: Record<string, unknown> = {},
    config?: AxiosRequestConfig & ApiRequestConfig
  ): Promise<T> {
    return this.getPageable<T>(url, searchParams, config);
  }

  // ============ 文件相关方法 ============

  /**
   * 上传文件
   */
  async upload<T>(
    url: string,
    file: File,
    onProgress?: (progress: number) => void
  ): Promise<T> {
    const formData = new FormData();
    formData.append('file', file);

    return this.instance.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: progressEvent => {
        if (onProgress && progressEvent.total) {
          const progress = (progressEvent.loaded / progressEvent.total) * 100;
          onProgress(Math.round(progress));
        }
      },
    });
  }

  /**
   * 批量上传文件
   */
  async uploadMultiple<T>(
    url: string,
    files: File[],
    onProgress?: (progress: number) => void
  ): Promise<T> {
    const formData = new FormData();
    files.forEach((file, index) => {
      formData.append(`files[${index}]`, file);
    });

    return this.instance.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: progressEvent => {
        if (onProgress && progressEvent.total) {
          const progress = (progressEvent.loaded / progressEvent.total) * 100;
          onProgress(Math.round(progress));
        }
      },
    });
  }

  /**
   * 下载文件
   */
  async download(url: string, filename?: string): Promise<void> {
    const response = await this.instance.get(url, {
      responseType: 'blob',
    });

    const blob = new Blob([response.data]);
    const downloadUrl = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = downloadUrl;
    link.download = filename || 'download';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(downloadUrl);
  }

  // ============ 高级功能方法 ============

  /**
   * 并发请求
   */
  async concurrent<T extends readonly unknown[]>(
    requests: readonly [...{ [K in keyof T]: Promise<T[K]> }]
  ): Promise<T> {
    return Promise.all(requests) as Promise<T>;
  }

  /**
   * 取消请求的方法
   */
  createCancelToken() {
    return axios.CancelToken.source();
  }

  /**
   * 检查请求是否被取消
   */
  isCancel(error: unknown): boolean {
    return axios.isCancel(error);
  }
}

// 导出单例实例
export const httpClient = new HttpClient();

// 导出类和配置接口供高级用法
export { HttpClient, type HttpClientConfig };
export default httpClient;
