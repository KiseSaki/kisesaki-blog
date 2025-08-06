/**
 * Loading 加载组件
 * 通用的加载状态显示组件
 */

import { Loader2 } from 'lucide-react';
import type { ReactNode } from 'react';
import React from 'react';

/**
 * Loading 组件属性
 */
interface LoadingProps {
    /** 加载提示文本 */
    text?: string;
    /** 组件大小 */
    size?: 'sm' | 'md' | 'lg';
    /** 是否显示为全屏加载 */
    fullscreen?: boolean;
    /** 自定义类名 */
    className?: string;
    /** 自定义图标 */
    icon?: ReactNode;
    /** 是否显示背景遮罩 */
    overlay?: boolean;
}

/**
 * 大小配置
 */
const sizeConfig = {
    sm: {
        spinner: 'h-4 w-4',
        text: 'text-xs',
        spacing: 'space-y-2',
        container: 'min-h-[100px]'
    },
    md: {
        spinner: 'h-6 w-6',
        text: 'text-sm',
        spacing: 'space-y-3',
        container: 'min-h-[150px]'
    },
    lg: {
        spinner: 'h-8 w-8',
        text: 'text-base',
        spacing: 'space-y-4',
        container: 'min-h-[200px]'
    }
};

/**
 * Loading 加载组件
 * 
 * 提供多种加载状态的显示方式：
 * - 默认行内加载
 * - 全屏遮罩加载
 * - 不同尺寸的加载效果
 * - 自定义加载图标和文本
 * 
 * @example
 * ```tsx
 * // 基础用法
 * <Loading text="正在加载..." />
 * 
 * // 全屏加载
 * <Loading fullscreen overlay text="正在处理请求..." />
 * 
 * // 小尺寸加载
 * <Loading size="sm" text="加载中" />
 * ```
 */
export const Loading: React.FC<LoadingProps> = ({
    text = "正在加载...",
    size = 'md',
    fullscreen = false,
    className = '',
    icon,
    overlay = false
}) => {
    const config = sizeConfig[size];

    // 默认旋转图标
    const defaultIcon = (
        <Loader2 className={`animate-spin ${config.spinner} text-blue-600`} />
    );

    // 加载内容
    const loadingContent = (
        <div className={`flex flex-col items-center justify-center ${config.spacing} ${className}`}>
            {icon || defaultIcon}
            {text && (
                <p className={`${config.text} text-gray-600 font-medium`}>
                    {text}
                </p>
            )}
        </div>
    );

    // 全屏加载
    if (fullscreen) {
        return (
            <div className={`fixed inset-0 z-50 flex items-center justify-center ${overlay ? 'bg-white/80 backdrop-blur-sm' : 'bg-white'
                }`}>
                {loadingContent}
            </div>
        );
    }

    // 普通加载
    return (
        <div className={`flex items-center justify-center ${config.container} ${className}`}>
            {loadingContent}
        </div>
    );
};

/**
 * 行内加载组件 - 用于按钮等小空间
 */
export const InlineLoading: React.FC<{
    text?: string;
    className?: string;
}> = ({ text, className = '' }) => (
    <div className={`flex items-center space-x-2 ${className}`}>
        <Loader2 className="h-4 w-4 animate-spin" />
        {text && <span className="text-sm">{text}</span>}
    </div>
);

/**
 * 页面加载组件 - 用于页面级别的加载状态
 */
export const PageLoading: React.FC<{
    title?: string;
    description?: string;
}> = ({
    title = "正在加载页面...",
    description = "请稍候，正在为您准备内容"
}) => (
        <div className="flex flex-col items-center justify-center min-h-[60vh] space-y-6">
            <div className="flex flex-col items-center space-y-4">
                <Loader2 className="h-12 w-12 animate-spin text-blue-600" />
                <div className="text-center space-y-2">
                    <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
                    <p className="text-sm text-gray-600 max-w-md">{description}</p>
                </div>
            </div>
        </div>
    );

/**
 * 卡片加载组件 - 用于卡片内容的加载状态
 */
export const CardLoading: React.FC<{
    lines?: number;
    showAvatar?: boolean;
}> = ({ lines = 3, showAvatar = false }) => (
    <div className="animate-pulse">
        <div className="space-y-4">
            {showAvatar && (
                <div className="flex items-center space-x-3">
                    <div className="rounded-full bg-gray-300 h-10 w-10"></div>
                    <div className="space-y-2">
                        <div className="h-4 bg-gray-300 rounded w-24"></div>
                        <div className="h-3 bg-gray-300 rounded w-16"></div>
                    </div>
                </div>
            )}
            <div className="space-y-3">
                {Array.from({ length: lines }).map((_, i) => (
                    <div
                        key={i}
                        className={`h-4 bg-gray-300 rounded ${i === lines - 1 ? 'w-3/4' : 'w-full'
                            }`}
                    ></div>
                ))}
            </div>
        </div>
    </div>
);

export default Loading;
