import axios, {
  type AxiosError,
  type AxiosInstance,
  type AxiosRequestConfig,
  type AxiosResponse,
  type InternalAxiosRequestConfig,
} from "axios";
import { toast } from "sonner";
import { useAuthStore } from "../stores/authStore";

/**
 * API 响应数据结构
 */
interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}

/**
 * HTTP 客户端类
 */
class HttpClient {
  private instance: AxiosInstance;

  constructor() {
    this.instance = axios.create({
      baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
      timeout: 10000,
      headers: {
        "Content-Type": "application/json",
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
        const token = useAuthStore.getState().token;

        if (token && !this.isPublicUrl(config.url)) {
          config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
      },
      (error) => Promise.reject(error)
    );

    // 响应拦截器
    this.instance.interceptors.response.use(
      <T>(response: AxiosResponse<ApiResponse<T>>): T => {
        // 可以在这里添加全局成功处理逻辑
        return response.data.data;
      },
      (error: AxiosError<ApiResponse>) => {
        this.handleError(error);
        return Promise.reject(error);
      }
    );
  }

  /**
   * 判断是否为公开 URL（不需要 token）
   */
  private isPublicUrl(url?: string): boolean {
    const publicUrls = ["/auth/login", "/auth/register", "/auth/refresh"];
    return publicUrls.some((publicUrl) => url?.includes(publicUrl));
  }

  /**
   * 统一错误处理
   */
  private handleError(error: AxiosError<ApiResponse>) {
    let errorMessage = "请求失败，请稍后重试";

    if (error.response?.data?.message) {
      errorMessage = error.response.data.message;
    } else if (error.message) {
      errorMessage = error.message;
    }

    // 显示错误提示
    toast.error(errorMessage);

    // 状态码处理
    switch (error.response?.status) {
      case 401:
        this.handleUnauthorized();
        break;
      case 403:
        window.location.href = "/403";
        break;
      case 404:
        // 可以选择是否全局处理 404
        break;
      case 500:
        // 服务器错误处理
        break;
    }
  }

  /**
   * 处理未授权错误
   */
  private handleUnauthorized() {
    useAuthStore.getState().logout();

    // 避免在登录页重复跳转
    if (window.location.pathname !== "/login") {
      window.location.href = "/login";
    }
  }

  // ============ HTTP 方法封装 ============

  /**
   * GET 请求
   */
  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.get(url, config);
  }

  /**
   * POST 请求
   */
  async post<T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return this.instance.post(url, data, config);
  }

  /**
   * PUT 请求
   */
  async put<T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return this.instance.put(url, data, config);
  }

  /**
   * PATCH 请求
   */
  async patch<T>(
    url: string,
    data?: unknown,
    config?: AxiosRequestConfig
  ): Promise<T> {
    return this.instance.patch(url, data, config);
  }

  /**
   * DELETE 请求
   */
  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.delete(url, config);
  }

  /**
   * 通用请求方法
   */
  async request<T>(config: AxiosRequestConfig): Promise<T> {
    return this.instance.request(config);
  }

  /**
   * 上传文件
   */
  async upload<T>(
    url: string,
    file: File,
    onProgress?: (progress: number) => void
  ): Promise<T> {
    const formData = new FormData();
    formData.append("file", file);

    return this.instance.post(url, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress: (progressEvent) => {
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
      responseType: "blob",
    });

    const blob = new Blob([response.data]);
    const downloadUrl = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = downloadUrl;
    link.download = filename || "download";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(downloadUrl);
  }
}

// 导出单例实例
export const httpClient = new HttpClient();
export default httpClient;
