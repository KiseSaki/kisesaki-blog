import { MainLayout, SuspenseWrapper } from "@/components";
import React from "react";
import { createBrowserRouter } from "react-router";
import { PrivateRoute } from "./PrivateRoute";

// Blog相关页面
const HomePage = React.lazy(() => import('@/pages/Blog/HomePage'));
const PostDetailPage = React.lazy(() => import('@/pages/Blog/PostDetailPage'));
const CategoryPage = React.lazy(() => import('@/pages/Blog/CategoryPage'));
const TagPage = React.lazy(() => import('@/pages/Blog/TagPage'));
const ArchivePage = React.lazy(() => import('@/pages/Blog/ArchivePage'));
const SearchPage = React.lazy(() => import('@/pages/Blog/SearchPage'));

// Auth相关页面
const LoginPage = React.lazy(() => import('@/pages/Auth/LoginPage'));
const RegisterPage = React.lazy(() => import('@/pages/Auth/RegisterPage'));
const ForgotPasswordPage = React.lazy(() => import('@/pages/Auth/ForgotPasswordPage'));
const ResetPasswordPage = React.lazy(() => import('@/pages/Auth/ResetPasswordPage'));
const EmailVerificationPage = React.lazy(() => import('@/pages/Auth/EmailVerificationPage'));
const OAuthCallbackPage = React.lazy(() => import('@/pages/Auth/OAuthCallbackPage'));

// 用户相关页面
const UserProfile = React.lazy(() => import('@/pages/User/UserProfile'));
const UserSettings = React.lazy(() => import('@/pages/User/UserSettings'));
const FavoritesPage = React.lazy(() => import('@/pages/User/FavoritesPage'));

// 管理员相关页面
const AdminDashboard = React.lazy(() => import('@/pages/Admin/AdminDashboard'));
const PostManagement = React.lazy(() => import('@/pages/Admin/PostManagement'));
const CategoryManagement = React.lazy(() => import('@/pages/Admin/CategoryManagement'));
const TagManagement = React.lazy(() => import('@/pages/Admin/TagManagement'));
const CommentManagement = React.lazy(() => import('@/pages/Admin/CommentManagement'));
const UserManagement = React.lazy(() => import('@/pages/Admin/UserManagement'));
const SettingsPage = React.lazy(() => import('@/pages/Admin/SettingsPage'));

// 错误页面
const NotFoundPage = React.lazy(() => import('@/pages/Error/NotFoundPage'));
const ForbiddenPage = React.lazy(() => import('@/pages/Error/ForbiddenPage'));
const ServerErrorPage = React.lazy(() => import('@/pages/Error/ServerErrorPage'));
const NetworkErrorPage = React.lazy(() => import('@/pages/Error/NetworkErrorPage'));

/**
 * 路由配置文件
 * 定义应用的所有路由规则和页面组件映射关系
 * 包含权限控制、嵌套路由和错误处理
 */
export const router = createBrowserRouter([
    {
        path: '/',
        element: <MainLayout />,
        errorElement: <SuspenseWrapper><ServerErrorPage /></SuspenseWrapper>,
        children: [
            // 首页重定向
            {
                index: true,
                element: <SuspenseWrapper><HomePage /></SuspenseWrapper>
            },

            // Blog相关路由
            {
                path: 'blog',
                children: [
                    { index: true, element: <SuspenseWrapper><HomePage /></SuspenseWrapper> }, // 博客首页
                    { path: 'post/:id', element: <SuspenseWrapper><PostDetailPage /></SuspenseWrapper> }, // 文章详情页
                    { path: 'category/:slug', element: <SuspenseWrapper><CategoryPage /></SuspenseWrapper> }, // 分类页
                    { path: 'tag/:slug', element: <SuspenseWrapper><TagPage /></SuspenseWrapper> }, // 标签页
                    { path: 'archive', element: <SuspenseWrapper><ArchivePage /></SuspenseWrapper> }, // 归档页
                    { path: 'search', element: <SuspenseWrapper><SearchPage /></SuspenseWrapper> } // 搜索页
                ]
            },

            // Auth相关路由（公共访问）
            {
                path: 'auth',
                children: [
                    { path: 'login', element: <SuspenseWrapper><LoginPage /></SuspenseWrapper> }, // 登录页
                    { path: 'register', element: <SuspenseWrapper><RegisterPage /></SuspenseWrapper> }, // 注册页
                    { path: 'forgot-password', element: <SuspenseWrapper><ForgotPasswordPage /></SuspenseWrapper> }, // 忘记密码页
                    { path: 'reset-password', element: <SuspenseWrapper><ResetPasswordPage /></SuspenseWrapper> }, // 重置密码页
                    { path: 'verify-email', element: <SuspenseWrapper><EmailVerificationPage /></SuspenseWrapper> }, // 邮箱验证页
                    { path: 'oauth/callback', element: <SuspenseWrapper><OAuthCallbackPage /></SuspenseWrapper> } // OAuth回调页
                ]
            },

            // 用户相关路由（需要登录）
            {
                path: 'user',
                element: <PrivateRoute />,
                children: [
                    { path: 'profile', element: <SuspenseWrapper><UserProfile /></SuspenseWrapper> }, // 用户个人资料页
                    { path: 'settings', element: <SuspenseWrapper><UserSettings /></SuspenseWrapper> }, // 用户设置页
                    { path: 'favorites', element: <SuspenseWrapper><FavoritesPage /></SuspenseWrapper> } // 用户收藏页
                ]
            },

            // 管理员相关路由（需要管理员权限）
            {
                path: 'admin',
                element: <PrivateRoute requiredRoles={['ADMIN']} />,
                children: [
                    { index: true, element: <SuspenseWrapper><AdminDashboard /></SuspenseWrapper> },
                    { path: 'dashboard', element: <SuspenseWrapper><AdminDashboard /></SuspenseWrapper> }, // 管理员仪表盘
                    { path: 'posts', element: <SuspenseWrapper><PostManagement /></SuspenseWrapper> }, // 文章管理页
                    { path: 'categories', element: <SuspenseWrapper><CategoryManagement /></SuspenseWrapper> }, // 分类管理页
                    { path: 'tags', element: <SuspenseWrapper><TagManagement /></SuspenseWrapper> }, // 标签管理页
                    { path: 'comments', element: <SuspenseWrapper><CommentManagement /></SuspenseWrapper> }, // 评论管理页
                    { path: 'users', element: <SuspenseWrapper><UserManagement /></SuspenseWrapper> }, // 用户管理页
                    { path: 'settings', element: <SuspenseWrapper><SettingsPage /></SuspenseWrapper> } // 管理员设置页
                ]
            },

            // 错误页面路由
            {
                path: 'error',
                children: [
                    { path: '403', element: <SuspenseWrapper><ForbiddenPage /></SuspenseWrapper> },
                    { path: '500', element: <SuspenseWrapper><ServerErrorPage /></SuspenseWrapper> },
                    { path: 'network', element: <SuspenseWrapper><NetworkErrorPage /></SuspenseWrapper> }
                ]
            },

            // 404 页面
            {
                path: '*',
                element: <SuspenseWrapper><NotFoundPage /></SuspenseWrapper>
            }
        ]
    }
]);