import { useAuthStore } from '@/stores/authStore';
import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router';

/**
 * 私有路由组件属性接口
 */
interface PrivateRouteProps {
    requiredRoles?: string[]; // 需要的角色权限列表，如果为空则只需要登录即可
    redirectTo?: string; // 重定向路径，默认为登录页
}

/**
 * 私有路由组件
 * 用于权限控制，验证用户登录状态和角色权限
 * 
 * @param requiredRoles - 需要的角色权限列表
 * @param redirectTo - 重定向路径，默认为登录页
 */
export const PrivateRoute: React.FC<PrivateRouteProps> = ({
    requiredRoles = [],
    redirectTo = '/auth/login'
}) => {
    const location = useLocation();
    const { user, token, roles } = useAuthStore();

    // 检查用户是否已登录
    const isAuthenticated = !!token && !!user;

    // 检查用户是否具有所需权限
    const hasRequiredRole = () => {
        // 如果没有指定需要的角色，则只需要登录即可
        if (requiredRoles.length === 0) {
            return true;
        }

        // 检查用户是否具有任一所需角色
        return requiredRoles.some(role => roles.includes(role));
    };

    // 如果用户未登录，重定向到登录页面并保存当前路径
    if (!isAuthenticated) {
        return (
            <Navigate
                to={redirectTo}
                state={{ from: location.pathname }}
                replace
            />
        );
    }

    // 如果用户已登录但没有所需权限，重定向到 403 页面
    if (!hasRequiredRole()) {
        return <Navigate to="/error/403" replace />;
    }

    // 用户已登录且具有所需权限，渲染子路由
    return <Outlet />;
};

export default PrivateRoute;
