// API 类型定义文件
// 定义所有后端接口的请求和响应类型
// 与后端 ApiResponse 结构保持一致
export interface ApiResponse<T> {
  // 业务状态码
  code: number;
  // 响应消息
  message: string;
  // 响应数据
  data: T;
  // 请求时间戳
  timestamp: number;
  // 是否成功
  success: boolean;
  // HTTP 状态
  httpStatus: string;
  // 是否为客户端错误 (4xx)
  clientError: boolean;
  // 是否为服务器错误 (5xx)
  serverError: boolean;
}

// 错误码常量
// 与后端 ErrorCode 枚举保持一致
export const ErrorCode = {
  // ========== 通用错误码 ==========
  SUCCESS: 200,
  SYSTEM_ERROR: 500,
  PARAM_ERROR: 400,
  NOT_FOUND: 404,
  METHOD_NOT_ALLOWED: 405,
  REQUEST_TIMEOUT: 408,
  TOO_MANY_REQUESTS: 429,

  // ========== 业务错误码 ==========
  BUSINESS_ERROR: 4000,

  // ========== 认证授权错误码 ==========
  UNAUTHORIZED: 401,
  ACCESS_DENIED: 403,
  TOKEN_EXPIRED: 4001,
  TOKEN_INVALID: 4002,
  LOGIN_FAILED: 4003,
  USER_NOT_FOUND: 4004,
  PASSWORD_ERROR: 4005,
  ACCOUNT_DISABLED: 4006,
  ACCOUNT_LOCKED: 4007,

  // ========== 用户相关错误码 ==========
  USER_ALREADY_EXISTS: 4010,
  EMAIL_ALREADY_EXISTS: 4011,
  USERNAME_ALREADY_EXISTS: 4012,
  INVALID_EMAIL_FORMAT: 4013,
  WEAK_PASSWORD: 4014,

  // ========== 文章相关错误码 ==========
  POST_NOT_FOUND: 4020,
  POST_SLUG_ALREADY_EXISTS: 4021,
  POST_TITLE_EMPTY: 4022,
  POST_CONTENT_EMPTY: 4023,
  POST_CATEGORY_NOT_FOUND: 4024,
  POST_ACCESS_DENIED: 4025,
  POST_SLUG_INVALID_FORMAT: 4026,
  POST_SLUG_TOO_LONG: 4027,
  POST_PASSWORD_REQUIRED: 4028,
  POST_INVALID_VISIBILITY: 4029,

  // ========== 评论相关错误码 ==========
  COMMENT_NOT_FOUND: 4030,
  COMMENT_CONTENT_EMPTY: 4031,
  COMMENT_ACCESS_DENIED: 4032,
  PARENT_COMMENT_NOT_FOUND: 4033,

  // ========== 文件上传错误码 ==========
  FILE_UPLOAD_FAILED: 4040,
  FILE_TOO_LARGE: 4041,
  FILE_TYPE_NOT_ALLOWED: 4042,
  FILE_NOT_FOUND: 4043,

  // ========== 数据验证错误码 ==========
  VALIDATION_FAILED: 4050,
  REQUIRED_FIELD_MISSING: 4051,
  INVALID_DATA_FORMAT: 4052,
  DATA_LENGTH_EXCEEDED: 4053,

  // ========== 外部服务错误码 ==========
  EXTERNAL_SERVICE_ERROR: 5001,
  DATABASE_ERROR: 5002,
  REDIS_ERROR: 5003,
  EMAIL_SEND_FAILED: 5004,
  SMS_SEND_FAILED: 5005,
} as const;

export type ErrorCodeType = (typeof ErrorCode)[keyof typeof ErrorCode];

// 错误码消息映射
// 提供用户友好的错误消息
export const ERROR_MESSAGES: Record<ErrorCodeType, string> = {
  // 通用错误码
  [ErrorCode.SUCCESS]: '操作成功',
  [ErrorCode.SYSTEM_ERROR]: '系统繁忙，请稍后重试',
  [ErrorCode.PARAM_ERROR]: '参数错误',
  [ErrorCode.NOT_FOUND]: '请求的资源不存在',
  [ErrorCode.METHOD_NOT_ALLOWED]: '请求方法不支持',
  [ErrorCode.REQUEST_TIMEOUT]: '请求超时，请检查网络连接',
  [ErrorCode.TOO_MANY_REQUESTS]: '请求过于频繁，请稍后再试',

  // 业务错误码
  [ErrorCode.BUSINESS_ERROR]: '业务处理失败',

  // 认证授权错误码
  [ErrorCode.UNAUTHORIZED]: '请先登录',
  [ErrorCode.ACCESS_DENIED]: '权限不足',
  [ErrorCode.TOKEN_EXPIRED]: '登录已过期，请重新登录',
  [ErrorCode.TOKEN_INVALID]: '登录状态无效，请重新登录',
  [ErrorCode.LOGIN_FAILED]: '登录失败，请检查用户名和密码',
  [ErrorCode.USER_NOT_FOUND]: '用户不存在',
  [ErrorCode.PASSWORD_ERROR]: '密码错误',
  [ErrorCode.ACCOUNT_DISABLED]: '账户已被禁用',
  [ErrorCode.ACCOUNT_LOCKED]: '账户已被锁定',

  // 用户相关错误码
  [ErrorCode.USER_ALREADY_EXISTS]: '用户已存在',
  [ErrorCode.EMAIL_ALREADY_EXISTS]: '邮箱已被注册',
  [ErrorCode.USERNAME_ALREADY_EXISTS]: '用户名已存在',
  [ErrorCode.INVALID_EMAIL_FORMAT]: '邮箱格式不正确',
  [ErrorCode.WEAK_PASSWORD]: '密码强度不足',

  // 文章相关错误码
  [ErrorCode.POST_NOT_FOUND]: '文章不存在',
  [ErrorCode.POST_SLUG_ALREADY_EXISTS]: '文章链接已存在',
  [ErrorCode.POST_TITLE_EMPTY]: '文章标题不能为空',
  [ErrorCode.POST_CONTENT_EMPTY]: '文章内容不能为空',
  [ErrorCode.POST_CATEGORY_NOT_FOUND]: '文章分类不存在',
  [ErrorCode.POST_ACCESS_DENIED]: '无权访问该文章',
  [ErrorCode.POST_SLUG_INVALID_FORMAT]: '文章链接格式无效',
  [ErrorCode.POST_SLUG_TOO_LONG]: '文章链接过长',
  [ErrorCode.POST_PASSWORD_REQUIRED]: '该文章需要密码访问',
  [ErrorCode.POST_INVALID_VISIBILITY]: '文章可见性设置无效',

  // 评论相关错误码
  [ErrorCode.COMMENT_NOT_FOUND]: '评论不存在',
  [ErrorCode.COMMENT_CONTENT_EMPTY]: '评论内容不能为空',
  [ErrorCode.COMMENT_ACCESS_DENIED]: '无权操作该评论',
  [ErrorCode.PARENT_COMMENT_NOT_FOUND]: '父评论不存在',

  // 文件上传错误码
  [ErrorCode.FILE_UPLOAD_FAILED]: '文件上传失败',
  [ErrorCode.FILE_TOO_LARGE]: '文件大小超出限制',
  [ErrorCode.FILE_TYPE_NOT_ALLOWED]: '不支持的文件类型',
  [ErrorCode.FILE_NOT_FOUND]: '文件不存在',

  // 数据验证错误码
  [ErrorCode.VALIDATION_FAILED]: '数据验证失败',
  [ErrorCode.REQUIRED_FIELD_MISSING]: '必填字段缺失',
  [ErrorCode.INVALID_DATA_FORMAT]: '数据格式无效',
  [ErrorCode.DATA_LENGTH_EXCEEDED]: '数据长度超出限制',

  // 外部服务错误码
  [ErrorCode.EXTERNAL_SERVICE_ERROR]: '外部服务异常',
  [ErrorCode.DATABASE_ERROR]: '数据库异常',
  [ErrorCode.REDIS_ERROR]: '缓存服务异常',
  [ErrorCode.EMAIL_SEND_FAILED]: '邮件发送失败',
  [ErrorCode.SMS_SEND_FAILED]: '短信发送失败',
};

// 分页查询参数基础接口
// 完全对应后端 PageableParams，用于 GET 请求的 URL 参数
export interface PageableParams {
  // 当前页码（从1开始）
  currentPage?: number;
  // 每页大小
  pageSize?: number;
  // 排序规则，格式: "field,asc|desc"
  sort?: string;
  // 是否返回总数
  includeTotal?: boolean;
  // 开始时间（用于时间范围查询），格式: YYYY-MM-DD HH:mm:ss
  startTime?: string;
  // 结束时间（用于时间范围查询），格式: YYYY-MM-DD HH:mm:ss
  endTime?: string;
  // 查询指定日期的数据，格式: YYYY-MM-DD
  date?: string;
}

// 分页响应数据结构
// 完全对应后端 PageResponse
export interface PageResponse<T> {
  // 当前页码（从1开始）
  currentPage: number;
  // 每页大小
  pageSize: number;
  // 总记录数
  totalRecords: number;
  // 总页数
  totalPages: number;
  // 是否为第一页
  first: boolean;
  // 是否为最后一页
  last: boolean;
  // 是否为空页（无数据）
  empty: boolean;
  // 数据列表
  data: T[];
}

// API 请求配置接口
// 扩展 axios 的请求配置
export interface ApiRequestConfig {
  // 是否显示加载状态
  showLoading?: boolean;
  // 是否显示错误提示
  showError?: boolean;
  // 是否静默请求（不显示任何提示）
  silent?: boolean;
  // 自定义错误处理函数
  onError?: (error: Error) => void;
  // 自定义成功处理函数
  onSuccess?: (data: unknown) => void;
}

// 业务异常接口
// 对应后端 BusinessException
export interface BusinessException extends Error {
  // 错误码
  errorCode: ErrorCodeType;
  // 错误消息
  message: string;
  // 格式化参数
  args?: unknown[];
  // 原始错误
  cause?: Error;
}

// 错误分类
export const ErrorCategory = {
  // 通用错误
  GENERAL: 'general',
  // 认证授权错误
  AUTH: 'auth',
  // 用户相关错误
  USER: 'user',
  // 文章相关错误
  POST: 'post',
  // 评论相关错误
  COMMENT: 'comment',
  // 文件上传错误
  FILE: 'file',
  // 数据验证错误
  VALIDATION: 'validation',
  // 外部服务错误
  EXTERNAL: 'external',
} as const;

export type ErrorCategoryType =
  (typeof ErrorCategory)[keyof typeof ErrorCategory];

// 错误码分类映射
export const ERROR_CODE_CATEGORIES: Record<ErrorCodeType, ErrorCategoryType> = {
  // 通用错误码
  [ErrorCode.SUCCESS]: ErrorCategory.GENERAL,
  [ErrorCode.SYSTEM_ERROR]: ErrorCategory.GENERAL,
  [ErrorCode.PARAM_ERROR]: ErrorCategory.GENERAL,
  [ErrorCode.NOT_FOUND]: ErrorCategory.GENERAL,
  [ErrorCode.METHOD_NOT_ALLOWED]: ErrorCategory.GENERAL,
  [ErrorCode.REQUEST_TIMEOUT]: ErrorCategory.GENERAL,
  [ErrorCode.TOO_MANY_REQUESTS]: ErrorCategory.GENERAL,
  [ErrorCode.BUSINESS_ERROR]: ErrorCategory.GENERAL,

  // 认证授权错误码
  [ErrorCode.UNAUTHORIZED]: ErrorCategory.AUTH,
  [ErrorCode.ACCESS_DENIED]: ErrorCategory.AUTH,
  [ErrorCode.TOKEN_EXPIRED]: ErrorCategory.AUTH,
  [ErrorCode.TOKEN_INVALID]: ErrorCategory.AUTH,
  [ErrorCode.LOGIN_FAILED]: ErrorCategory.AUTH,
  [ErrorCode.USER_NOT_FOUND]: ErrorCategory.AUTH,
  [ErrorCode.PASSWORD_ERROR]: ErrorCategory.AUTH,
  [ErrorCode.ACCOUNT_DISABLED]: ErrorCategory.AUTH,
  [ErrorCode.ACCOUNT_LOCKED]: ErrorCategory.AUTH,

  // 用户相关错误码
  [ErrorCode.USER_ALREADY_EXISTS]: ErrorCategory.USER,
  [ErrorCode.EMAIL_ALREADY_EXISTS]: ErrorCategory.USER,
  [ErrorCode.USERNAME_ALREADY_EXISTS]: ErrorCategory.USER,
  [ErrorCode.INVALID_EMAIL_FORMAT]: ErrorCategory.USER,
  [ErrorCode.WEAK_PASSWORD]: ErrorCategory.USER,

  // 文章相关错误码
  [ErrorCode.POST_NOT_FOUND]: ErrorCategory.POST,
  [ErrorCode.POST_SLUG_ALREADY_EXISTS]: ErrorCategory.POST,
  [ErrorCode.POST_TITLE_EMPTY]: ErrorCategory.POST,
  [ErrorCode.POST_CONTENT_EMPTY]: ErrorCategory.POST,
  [ErrorCode.POST_CATEGORY_NOT_FOUND]: ErrorCategory.POST,
  [ErrorCode.POST_ACCESS_DENIED]: ErrorCategory.POST,
  [ErrorCode.POST_SLUG_INVALID_FORMAT]: ErrorCategory.POST,
  [ErrorCode.POST_SLUG_TOO_LONG]: ErrorCategory.POST,
  [ErrorCode.POST_PASSWORD_REQUIRED]: ErrorCategory.POST,
  [ErrorCode.POST_INVALID_VISIBILITY]: ErrorCategory.POST,

  // 评论相关错误码
  [ErrorCode.COMMENT_NOT_FOUND]: ErrorCategory.COMMENT,
  [ErrorCode.COMMENT_CONTENT_EMPTY]: ErrorCategory.COMMENT,
  [ErrorCode.COMMENT_ACCESS_DENIED]: ErrorCategory.COMMENT,
  [ErrorCode.PARENT_COMMENT_NOT_FOUND]: ErrorCategory.COMMENT,

  // 文件上传错误码
  [ErrorCode.FILE_UPLOAD_FAILED]: ErrorCategory.FILE,
  [ErrorCode.FILE_TOO_LARGE]: ErrorCategory.FILE,
  [ErrorCode.FILE_TYPE_NOT_ALLOWED]: ErrorCategory.FILE,
  [ErrorCode.FILE_NOT_FOUND]: ErrorCategory.FILE,

  // 数据验证错误码
  [ErrorCode.VALIDATION_FAILED]: ErrorCategory.VALIDATION,
  [ErrorCode.REQUIRED_FIELD_MISSING]: ErrorCategory.VALIDATION,
  [ErrorCode.INVALID_DATA_FORMAT]: ErrorCategory.VALIDATION,
  [ErrorCode.DATA_LENGTH_EXCEEDED]: ErrorCategory.VALIDATION,

  // 外部服务错误码
  [ErrorCode.EXTERNAL_SERVICE_ERROR]: ErrorCategory.EXTERNAL,
  [ErrorCode.DATABASE_ERROR]: ErrorCategory.EXTERNAL,
  [ErrorCode.REDIS_ERROR]: ErrorCategory.EXTERNAL,
  [ErrorCode.EMAIL_SEND_FAILED]: ErrorCategory.EXTERNAL,
  [ErrorCode.SMS_SEND_FAILED]: ErrorCategory.EXTERNAL,
};

// 创建业务异常
export function createBusinessException(
  errorCode: ErrorCodeType,
  customMessage?: string,
  args?: unknown[]
): BusinessException {
  const message = customMessage || ERROR_MESSAGES[errorCode];
  const error = new Error(message) as BusinessException;
  error.errorCode = errorCode;
  error.args = args;
  return error;
}

// 判断是否为业务异常
export function isBusinessException(
  error: unknown
): error is BusinessException {
  return error instanceof Error && 'errorCode' in error;
}

// 获取错误分类
export function getErrorCategory(errorCode: ErrorCodeType): ErrorCategoryType {
  return ERROR_CODE_CATEGORIES[errorCode] || ErrorCategory.GENERAL;
}

// 判断是否为认证相关错误
export function isAuthError(errorCode: ErrorCodeType): boolean {
  return getErrorCategory(errorCode) === ErrorCategory.AUTH;
}

// 判断是否需要重新登录的错误
export function isLoginRequiredError(errorCode: ErrorCodeType): boolean {
  return [
    ErrorCode.UNAUTHORIZED,
    ErrorCode.TOKEN_EXPIRED,
    ErrorCode.TOKEN_INVALID,
  ].includes(errorCode as never);
}
