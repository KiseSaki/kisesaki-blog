// API 端点配置
// 定义所有后端 API 接口的 URL 路径和相关配置

// 公开 API 路径（不需要认证）
export const PUBLIC_API_URLS = [
  // 静态资源和系统接口
  '/swagger-resources',
  '/webjars',
  '/favicon.ico',
  '/error',
  '/uploads',
  '/files',

  // 认证系统接口
  '/auth/register',
  '/auth/login',
  '/auth/forgot-password',
  '/auth/reset-password',
  '/auth/verify-email',
  '/auth/resend-verification',
  '/auth/oauth',

  // 文章系统接口（公开访问）
  '/posts',
  '/categories',
  '/tags',

  // 用户系统接口（公开信息）
  '/users',

  // 评论系统接口（公开）
  '/comments',

  // 搜索和统计接口（公开）
  '/search',
  '/stats/overview',
  '/analytics/view',
  '/analytics/event',
  '/analytics/popular',

  // 通知订阅接口（部分公开）
  '/subscriptions/newsletter',
  '/subscriptions/unsubscribe',
  '/subscriptions/verify',

  // 系统配置接口（公开）
  '/settings/public',
  '/seo/meta',
];

// API 基础配置
export const API_CONFIG = {
  // 默认请求超时时间(ms)
  DEFAULT_TIMEOUT: 10000,
  // 文件上传超时时间(ms)
  UPLOAD_TIMEOUT: 30000,
  // 分页默认大小
  DEFAULT_PAGE_SIZE: 10,
  // 最大分页大小
  MAX_PAGE_SIZE: 100,
  // 重试次数
  RETRY_COUNT: 3,
  // 重试间隔(ms)
  RETRY_DELAY: 1000,
} as const;

// 文件上传相关配置
export const UPLOAD_CONFIG = {
  // 最大文件大小（字节）
  MAX_FILE_SIZE: 10 * 1024 * 1024, // 10MB
  // 允许的图片类型
  ALLOWED_IMAGE_TYPES: ['image/jpeg', 'image/png', 'image/gif', 'image/webp'],
  // 允许的文档类型
  ALLOWED_DOCUMENT_TYPES: [
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'application/vnd.ms-excel',
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  ],
  // 允许的所有文件类型
  get ALLOWED_ALL_TYPES() {
    return [...this.ALLOWED_IMAGE_TYPES, ...this.ALLOWED_DOCUMENT_TYPES];
  },
} as const;

// 分页相关配置
export const PAGINATION_CONFIG = {
  // 默认页码
  DEFAULT_CURRENT_PAGE: 1,
  // 默认页面大小
  DEFAULT_PAGE_SIZE: 10,
  // 可选的页面大小
  PAGE_SIZE_OPTIONS: [10, 20, 50, 100],
  // 最大页面大小
  MAX_PAGE_SIZE: 100,
  // 默认排序
  DEFAULT_SORT: 'createdAt,desc',
} as const;

// 环境变量配置
export const ENV_CONFIG = {
  // API 基础 URL
  API_BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  // 网站基础 URL
  SITE_BASE_URL: import.meta.env.VITE_SITE_URL || 'http://localhost:5173',
  // 是否为开发环境
  IS_DEV: import.meta.env.DEV,
  // 是否为生产环境
  IS_PROD: import.meta.env.PROD,
  // 应用版本
  APP_VERSION: import.meta.env.VITE_APP_VERSION || '1.0.0',
} as const;

// 错误重试配置
export const RETRY_CONFIG = {
  // 可重试的 HTTP 状态码
  RETRYABLE_STATUS_CODES: [408, 429, 500, 502, 503, 504],
  // 可重试的错误类型
  RETRYABLE_ERROR_TYPES: ['timeout', 'network'],
  // 最大重试次数
  MAX_RETRY_COUNT: 3,
  // 基础延迟时间（毫秒）
  BASE_DELAY: 1000,
  // 延迟倍数（指数退避）
  DELAY_MULTIPLIER: 2,
  // 最大延迟时间（毫秒）
  MAX_DELAY: 10000,
} as const;
