/**
 * 通用类型定义
 * 包含应用中使用的通用接口、枚举等类型定义
 */

/**
 * 分页查询参数
 */
export interface PaginationParams {
    page?: number;
    limit?: number;
    sort?: string;
    order?: 'asc' | 'desc';
}

/**
 * 分页结果
 */
export interface PaginationResult<T> {
    data: T[];
    total: number;
    page: number;
    limit: number;
    totalPages: number;
}

/**
 * 错误信息接口
 */
export interface ErrorInfo {
    message: string;
    stack?: string;
    componentStack?: string;
    errorBoundary?: string;
    errorBoundaryStack?: string;
    eventType?: string;
}

/**
 * 错误日志上报数据
 */
export interface ErrorLogData {
    error: Error;
    errorInfo: ErrorInfo;
    userAgent?: string;
    url?: string;
    userId?: string;
    timestamp: number;
    level: 'error' | 'warn' | 'info';
}

/**
 * 组件状态常量
 */
export const ComponentStatus = {
    LOADING: 'loading',
    SUCCESS: 'success',
    ERROR: 'error',
    IDLE: 'idle'
} as const;

export type ComponentStatusType = typeof ComponentStatus[keyof typeof ComponentStatus];
