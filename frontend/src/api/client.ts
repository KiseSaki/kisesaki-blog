import axios, { type AxiosInstance, type AxiosResponse, type AxiosError } from 'axios';
import { toast } from 'sonner';
import { useAuthStore } from '../stores/authStore';

/**
 * API 响应数据结构
 */
interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}

/**
 * 创建 Axios 实例
 * baseURL 从环境变量获取
 */
const client: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

/**
 * 请求拦截器
 * 自动添加 JWT token 到请求头
 */
client.interceptors.request.use(
  (config) => {
    // 从 Zustand store 中获取 token
    const token = useAuthStore.getState().token;
    
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * 响应拦截器
 * 统一处理响应数据和错误
 */
client.interceptors.response.use(
  <T>(response: AxiosResponse<ApiResponse<T>>): T => {
    // 成功响应，直接返回 data 字段，剥离外层封装
    return response.data.data;
  },
  (error: AxiosError<ApiResponse>) => {
    // 处理错误响应
    let errorMessage = '请求失败，请稍后重试';
    
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message;
    } else if (error.message) {
      errorMessage = error.message;
    }
    
    // 使用 sonner 显示错误提示
    toast.error(errorMessage);
    
    // 特殊状态码处理
    if (error.response?.status === 401) {
      // 401 未授权：清除认证信息并重定向到登录页
      useAuthStore.getState().logout();
      window.location.href = '/login';
    } else if (error.response?.status === 403) {
      // 403 权限不足：重定向到403页面
      window.location.href = '/403';
    }
    
    return Promise.reject(error);
  }
);

export default client;
