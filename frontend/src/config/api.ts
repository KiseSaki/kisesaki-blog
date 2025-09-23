// API 端点配置
// 定义所有后端 API 接口的 URL 路径和相关配置
// 基于 SecurityConfig.java 的实际权限配置

/**
 * 完全公开的 API 路径（不需要认证的接口）
 * 这些接口在 Spring Security 中被配置为 permitAll()
 */
export const PUBLIC_API_URLS = [
  // ==================== 静态资源和系统接口 ====================
  // Swagger 文档接口
  '/swagger-ui/**',
  '/v3-docs/**',
  '/swagger-resources/**',
  '/webjars/**',
  // 静态资源
  '/favicon.ico',
  '/error',
  '/uploads/**', // 文件访问路径
  '/files/**',

  // ==================== 认证系统接口 ====================
  // 本地认证：完全公开
  '/auth/register',
  '/auth/login',
  '/auth/refresh',
  '/auth/forgot-password',
  '/auth/reset-password',
  '/auth/verify-email',
  '/auth/resend-verification',
  // OAuth2 认证：完全公开
  '/auth/oauth/*/authorize',
  '/auth/oauth/*/callback',

  // ==================== 文章系统接口（公开访问） ====================
  // 公开访问的文章接口（PostQueryService）
  '/posts', // GET 方法
  '/posts/*', // GET 方法
  '/posts/slug/*', // GET 方法
  '/categories/*/posts', // GET 方法
  '/tags/*/posts', // GET 方法
  '/posts/featured', // GET 方法
  '/posts/recent', // GET 方法
  '/posts/popular', // GET 方法
  '/posts/search', // GET 方法
  // 文章浏览统计：公开
  '/posts/*/view', // POST 方法

  // ==================== 分类标签接口（公开访问） ====================
  // 分类公开接口
  '/categories', // GET 方法
  '/categories/*', // GET 方法
  '/categories/slug/*', // GET 方法
  '/categories/popular', // GET 方法
  // 标签公开接口
  '/tags', // GET 方法
  '/tags/*', // GET 方法
  '/tags/slug/*', // GET 方法
  '/tags/popular', // GET 方法
  '/tags/cloud', // GET 方法

  // ==================== 用户系统接口（公开信息） ====================
  // 用户公开信息
  '/users/*', // GET 方法
  '/users/*/profile', // GET 方法
  '/users/*/posts', // GET 方法
  '/users/*/stats', // GET 方法
  '/users/search', // GET 方法
  '/users/popular', // GET 方法
  '/users/recent', // GET 方法

  // ==================== 评论系统接口（公开访问） ====================
  // 公开访问评论
  '/posts/*/comments', // GET 方法
  '/comments/*', // GET 方法
  '/comments/*/replies', // GET 方法

  // ==================== 点赞收藏关注接口（公开查看） ====================
  // 公开查看点赞收藏
  '/posts/*/likes', // GET 方法
  '/posts/*/favorites', // GET 方法
  '/users/*/likes', // GET 方法
  '/users/*/favorites', // GET 方法
  '/users/*/followers', // GET 方法
  '/users/*/following', // GET 方法

  // ==================== 搜索和统计接口（公开） ====================
  // 搜索：公开
  '/search/**',
  // 公开统计
  '/stats/overview', // GET 方法
  // 浏览统计：公开
  '/analytics/view',
  '/analytics/event',
  '/analytics/popular',
  '/posts/*/views', // GET 方法

  // ==================== 通知订阅接口（部分公开） ====================
  // 订阅：部分公开
  '/subscriptions/newsletter',
  '/subscriptions/unsubscribe/*',
  '/subscriptions/verify',

  // ==================== 系统配置接口（公开） ====================
  // 公开系统设置
  '/settings/public', // GET 方法
  // SEO 元数据：公开
  '/seo/meta/*', // GET 方法
];

/**
 * 需要登录认证的 API 路径
 * 这些接口需要 JWT Token，但不需要特定角色权限
 */
export const AUTHENTICATED_API_URLS = [
  // ==================== 认证系统接口 ====================
  // 会话管理：需要登录
  '/auth/logout',
  '/auth/sessions/**',
  '/auth/me',
  // OAuth2 绑定：需要登录
  '/auth/oauth/*/bind',
  '/auth/oauth/*/unbind',
  '/auth/oauth/linked',

  // ==================== 文章系统接口 ====================
  // 我的文章相关：需要登录
  '/posts/my/**',
  '/posts/*/preview',
  // 文章交互：点赞需要登录
  '/posts/*/like',

  // ==================== 分类标签接口 ====================
  // 标签搜索：需要登录
  '/tags/search', // GET 方法
  '/tags/my',

  // ==================== 用户系统接口 ====================
  // 用户资料管理：需要登录
  '/users/profile',
  '/users/avatar',
  '/users/cover',
  '/users/password',
  '/users/dashboard',
  '/users/settings/**',
  '/users/account', // DELETE 方法
  // 用户关注功能：需要登录
  '/users/*/follow',
  '/users/following/posts',
  '/users/recommendations',

  // ==================== 评论系统接口 ====================
  // 评论操作：需要登录
  '/comments/my',
  '/comments/*/report',
  // 评论点赞：需要登录
  '/comments/*/like', // POST 方法
  '/comments/*/dislike', // POST 方法
  // 评论编辑删除：需要登录（具体权限在Service层判断）
  '/comments/*', // PUT/DELETE 方法

  // ==================== 点赞收藏关注接口 ====================
  // 点赞收藏操作：需要登录
  '/posts/*/favorite',
  '/users/favorites',

  // ==================== 媒体资源接口 ====================
  // 查看文件：需要登录
  '/media/**', // GET 方法

  // ==================== 通知订阅接口 ====================
  // 通知：需要登录
  '/notifications/**',
  '/subscriptions/**', // 除了公开的订阅接口

  // ==================== 举报接口 ====================
  // 举报：需要登录
  '/reports/**',
];

/**
 * 需要特定角色权限的 API 路径
 * 根据 RBAC 角色权限模型定义的接口访问控制
 */
export const ROLE_BASED_API_URLS = {
  // ==================== 内容创作权限 (ADMIN, EDITOR, AUTHOR) ====================
  CONTENT_CREATION: [
    '/posts', // POST 方法 - 创建文章
    '/posts/*', // PUT 方法 - 编辑文章
    '/posts/*/publish', // PUT 方法 - 发布文章
    '/posts/*/unpublish', // PUT 方法 - 取消发布
    '/posts/*', // DELETE 方法 - 删除文章
    '/posts/*/duplicate', // POST 方法 - 复制文章
    '/posts/*/meta', // 文章元数据管理
    '/posts/*/meta/*',
    '/tags', // POST 方法 - 创建标签
    '/media/**', // POST/DELETE 方法 - 文件上传删除
  ],

  // ==================== 内容管理权限 (ADMIN, EDITOR) ====================
  CONTENT_MANAGEMENT: [
    '/posts/*/revisions/*/restore', // POST 方法 - 版本恢复
    '/admin/posts/**', // 文章管理接口
    '/admin/categories/**', // 分类管理接口
    '/admin/tags/**', // 标签管理接口
    '/admin/comments/**', // 评论管理接口
    '/admin/media/**', // 媒体管理接口
  ],

  // ==================== 版本查看权限 (ADMIN, EDITOR, AUTHOR) ====================
  VERSION_VIEW: [
    '/posts/*/revisions/**', // GET 方法 - 查看文章版本
  ],

  // ==================== 评论创建权限 (ADMIN, EDITOR, AUTHOR, USER) ====================
  COMMENT_CREATION: [
    '/posts/*/comments', // POST 方法 - 创建评论
  ],

  // ==================== 系统管理权限 (ADMIN) ====================
  SYSTEM_ADMIN: [
    '/admin/users/**', // 用户管理接口
    '/admin/system/**', // 系统管理接口
    '/admin/settings/**', // 设置管理接口
    '/admin/analytics/**', // 分析管理接口
    '/admin/reports/**', // 报告管理接口
    '/admin/audit/**', // 审计管理接口
    '/admin/**', // 其他管理员接口
  ],
};

/**
 * 角色权限定义
 * 基于后端 RBAC 模型的角色层级定义
 */
export const USER_ROLES = {
  ADMIN: 'ADMIN', // 管理员，拥有所有权限，可以管理系统配置、用户、审计等
  EDITOR: 'EDITOR', // 编辑者，可以管理内容相关功能（文章、分类、标签、评论、媒体）
  AUTHOR: 'AUTHOR', // 作者，可以创建和管理自己的文章、标签
  USER: 'USER', // 普通用户，可以评论、点赞、收藏等基础交互功能
} as const;

// 定义角色类型
export type UserRole = (typeof USER_ROLES)[keyof typeof USER_ROLES];

/**
 * 权限检查工具函数
 * 用于在前端检查用户是否有访问特定接口的权限
 */
export const API_PERMISSIONS = {
  // 检查是否为公开接口
  isPublicApi: (path: string): boolean => {
    return PUBLIC_API_URLS.some(pattern => {
      // 支持通配符匹配
      const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
      return regex.test(path);
    });
  },

  // 检查是否需要登录
  requiresAuth: (path: string): boolean => {
    return (
      AUTHENTICATED_API_URLS.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      }) || API_PERMISSIONS.requiresRole(path)
    );
  },

  // 检查是否需要特定角色
  requiresRole: (path: string): boolean => {
    return Object.values(ROLE_BASED_API_URLS).some(urls =>
      urls.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    );
  },

  // 检查用户是否有访问权限
  hasPermission: (path: string, userRoles: UserRole[] = []): boolean => {
    // 公开接口无需权限
    if (API_PERMISSIONS.isPublicApi(path)) {
      return true;
    }

    // 需要登录但无角色要求
    if (
      API_PERMISSIONS.requiresAuth(path) &&
      !API_PERMISSIONS.requiresRole(path)
    ) {
      return userRoles.length > 0; // 只要登录即可
    }

    // 需要特定角色权限
    if (userRoles.includes(USER_ROLES.ADMIN)) {
      return true; // 管理员拥有所有权限
    }

    // 检查内容创作权限 (ADMIN, EDITOR, AUTHOR)
    if (
      ROLE_BASED_API_URLS.CONTENT_CREATION.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      const allowedRoles = [
        USER_ROLES.ADMIN,
        USER_ROLES.EDITOR,
        USER_ROLES.AUTHOR,
      ] as const;
      return userRoles.some(role =>
        allowedRoles.some(allowedRole => allowedRole === role)
      );
    }

    // 检查内容管理权限 (ADMIN, EDITOR)
    if (
      ROLE_BASED_API_URLS.CONTENT_MANAGEMENT.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      const allowedRoles = [USER_ROLES.ADMIN, USER_ROLES.EDITOR] as const;
      return userRoles.some(role =>
        allowedRoles.some(allowedRole => allowedRole === role)
      );
    }

    // 检查版本查看权限 (ADMIN, EDITOR, AUTHOR)
    if (
      ROLE_BASED_API_URLS.VERSION_VIEW.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      const allowedRoles = [
        USER_ROLES.ADMIN,
        USER_ROLES.EDITOR,
        USER_ROLES.AUTHOR,
      ] as const;
      return userRoles.some(role =>
        allowedRoles.some(allowedRole => allowedRole === role)
      );
    }

    // 检查评论创建权限 (ADMIN, EDITOR, AUTHOR, USER)
    if (
      ROLE_BASED_API_URLS.COMMENT_CREATION.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      const allowedRoles = Object.values(USER_ROLES);
      return userRoles.some(role =>
        allowedRoles.some(allowedRole => allowedRole === role)
      );
    }

    // 检查系统管理权限 (ADMIN)
    if (
      ROLE_BASED_API_URLS.SYSTEM_ADMIN.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      return userRoles.includes(USER_ROLES.ADMIN);
    }

    // 默认需要登录
    return userRoles.length > 0;
  },

  // 获取接口所需的最低角色
  getRequiredRoles: (path: string): UserRole[] | ['AUTHENTICATED'] => {
    if (API_PERMISSIONS.isPublicApi(path)) {
      return [];
    }

    if (
      ROLE_BASED_API_URLS.SYSTEM_ADMIN.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      return [USER_ROLES.ADMIN];
    }

    if (
      ROLE_BASED_API_URLS.CONTENT_MANAGEMENT.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      return [USER_ROLES.ADMIN, USER_ROLES.EDITOR];
    }

    if (
      ROLE_BASED_API_URLS.CONTENT_CREATION.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      }) ||
      ROLE_BASED_API_URLS.VERSION_VIEW.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      return [USER_ROLES.ADMIN, USER_ROLES.EDITOR, USER_ROLES.AUTHOR];
    }

    if (
      ROLE_BASED_API_URLS.COMMENT_CREATION.some(pattern => {
        const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
        return regex.test(path);
      })
    ) {
      return Object.values(USER_ROLES);
    }

    // 默认需要登录
    return ['AUTHENTICATED'];
  },
} as const;

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

/**
 * HTTP 方法定义
 */
export const HTTP_METHODS = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE',
  PATCH: 'PATCH',
} as const;

export type HttpMethod = (typeof HTTP_METHODS)[keyof typeof HTTP_METHODS];

/**
 * API 工具函数
 */
export const API_UTILS = {
  /**
   * 构建完整的 API URL
   * @param path API 路径
   * @returns 完整的 API URL
   */
  buildApiUrl: (path: string): string => {
    const baseUrl = ENV_CONFIG.API_BASE_URL.replace(/\/$/, '');
    const cleanPath = path.startsWith('/') ? path : `/${path}`;
    return `${baseUrl}${cleanPath}`;
  },

  /**
   * 检查 HTTP 状态码是否可重试
   * @param status HTTP 状态码
   * @returns 是否可重试
   */
  isRetryableStatus: (status: number): boolean => {
    return (RETRY_CONFIG.RETRYABLE_STATUS_CODES as readonly number[]).includes(
      status
    );
  },

  /**
   * 计算重试延迟时间
   * @param attempt 当前重试次数
   * @returns 延迟时间（毫秒）
   */
  calculateRetryDelay: (attempt: number): number => {
    const delay =
      RETRY_CONFIG.BASE_DELAY *
      Math.pow(RETRY_CONFIG.DELAY_MULTIPLIER, attempt - 1);
    return Math.min(delay, RETRY_CONFIG.MAX_DELAY);
  },

  /**
   * 验证文件类型
   * @param file 文件对象
   * @param allowedTypes 允许的文件类型
   * @returns 是否允许上传
   */
  validateFileType: (
    file: File,
    allowedTypes: readonly string[] = UPLOAD_CONFIG.ALLOWED_ALL_TYPES
  ): boolean => {
    return allowedTypes.includes(file.type);
  },

  /**
   * 验证文件大小
   * @param file 文件对象
   * @param maxSize 最大文件大小（字节）
   * @returns 是否符合大小限制
   */
  validateFileSize: (
    file: File,
    maxSize: number = UPLOAD_CONFIG.MAX_FILE_SIZE
  ): boolean => {
    return file.size <= maxSize;
  },

  /**
   * 格式化文件大小
   * @param bytes 字节数
   * @returns 格式化后的文件大小字符串
   */
  formatFileSize: (bytes: number): string => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  },
} as const;

/**
 * 使用示例：
 *
 * // 检查接口权限
 * const userRoles: UserRole[] = ['AUTHOR'];
 * const canCreatePost = API_PERMISSIONS.hasPermission('/posts', userRoles); // true
 * const canManageUsers = API_PERMISSIONS.hasPermission('/admin/users/123', userRoles); // false
 *
 * // 检查文件上传
 * const file = new File(['content'], 'test.jpg', { type: 'image/jpeg' });
 * const isValidType = API_UTILS.validateFileType(file, UPLOAD_CONFIG.ALLOWED_IMAGE_TYPES);
 * const isValidSize = API_UTILS.validateFileSize(file);
 *
 * // 构建 API URL
 * const apiUrl = API_UTILS.buildApiUrl('/posts/123');
 *
 * // 获取所需角色
 * const requiredRoles = API_PERMISSIONS.getRequiredRoles('/admin/posts/123');
 */
