---
mode: agent
description: "这是一个为 AI Agent 设计的前端项目任务清单。每项任务都是一个具体的、可执行的编码步骤，旨在指导 Agent 高效、准确地生成代码。"
---

### 🚀 **阶段一：项目基础与核心服务 (Foundation & Core Services)**

- [ ] **1. 封装 Axios API 客户端**
    - **文件**: `src/api/client.ts`
    - **任务**:
        - [ ] 初始化 Axios 实例，`baseURL` 从环境变量 `import.meta.env.VITE_API_URL` 获取。
        - [ ] 实现 **请求拦截器**：从 Zustand store 中获取 JWT token，并附加到 `Authorization` 请求头。
        - [ ] 实现 **响应拦截器**：
            - 成功时 (2xx): 直接返回 `response.data.data`。
            - 失败时: 使用 `sonner` 或类似库显示 `error.response.data.message` 错误提示，并 `Promise.reject(error)`。

- [ ] **2. 创建用户认证 Store**
    - **文件**: `src/stores/authStore.ts`
    - **任务**:
        - [ ] 定义 `AuthState` 接口，包含 `user: User | null` 和 `token: string | null`。
        - [ ] 使用 Zustand 创建 `useAuthStore`，包含 `user` 和 `token` 状态。
        - [ ] 创建 `setUserAndToken(user: User, token: string)` 和 `logout()` action。
        - [ ] 使用 Zustand 的 `persist` 中间件将 `token` 和 `user` 信息持久化到 `localStorage`。

- [ ] **3. 配置路由与核心布局**
    - **文件**: `src/router/index.tsx`
    - **任务**:
        - [ ] 使用 `createBrowserRouter` 创建路由。
        - [ ] 创建 `MainLayout.tsx` (`src/components/layout/`) 作为根布局，包含 `Header`、`Footer` 和 `<Outlet />`。
        - [ ] 创建 `PrivateRoute.tsx` (`src/router/`)，检查 `useAuthStore` 中是否存在 `token`，若无则重定向到 `/login`。
        - [ ] 定义路由结构：`'/'` (首页) 和 `'/login'` (登录页) 使用 `MainLayout`，后台管理路由 (如 `'/admin'`) 使用 `PrivateRoute` 包裹。

- [ ] **4. 实现主题切换功能**
    - **文件**: `src/hooks/useTheme.ts`
    - **任务**:
        - [ ] 创建一个自定义 Hook，用于管理和切换 `light` / `dark` / `system` 主题。
        - [ ] 该 Hook 应能将主题状态持久化到 `localStorage`。
        - [ ] 在 `App.tsx` 中调用此 Hook，并将主题对应的 class (如 `dark`) 应用到 `<html>` 根元素。
        - [ ] 在 `Header.tsx` 中添加一个切换按钮来调用此 Hook 中的切换函数。

### 👤 **阶段二：用户认证功能 (User Authentication)**

- [ ] **5. 创建认证 API 服务**
    - **文件**: `src/api/auth.ts`
    - **任务**:
        - [ ] 在 `src/types/auth.ts` 中定义 `LoginRequestDto` 和 `RegisterRequestDto` 类型。
        - [ ] 创建 `login(data: LoginRequestDto)` 函数，调用 `client.post('/auth/login', data)`。
        - [ ] 创建 `register(data: RegisterRequestDto)` 函数，调用 `client.post('/auth/register', data)`。
        - [ ] 创建 `getProfile()` 函数，调用 `client.get('/users/me')`。

- [ ] **6. 实现登录页面**
    - **文件**: `src/pages/LoginPage.tsx`
    - **任务**:
        - [ ] 使用 `shadcn/ui` 的 `Card`, `Input`, `Button`, `Label` 构建登录表单。
        - [ ] 集成 `react-hook-form` 和 `zod` 进行表单状态管理和客户端校验。
        - [ ] 表单提交时，调用 `login` API 服务。
        - [ ] 登录成功后，调用 `useAuthStore` 的 `setUserAndToken` action，并使用 `react-router-dom` 的 `useNavigate` 跳转到首页或后台。

- [ ] **7. 实现注册页面**
    - **文件**: `src/pages/RegisterPage.tsx`
    - **任务**:
        - [ ] 结构与登录页面类似，构建注册表单。
        - [ ] 表单提交时，调用 `register` API 服务。
        - [ ] 注册成功后，显示成功提示并跳转到登录页。

### 📝 **阶段三：博客文章展示 (Blog Post Display)**

- [ ] **8. 创建文章 API 服务**
    - **文件**: `src/api/post.ts`
    - **任务**:
        - [ ] 在 `src/types/post.ts` 中定义 `Post`, `Category`, `Tag` 等类型。
        - [ ] 创建 `getPosts(params: { page: number, limit: number })` 函数。
        - [ ] 创建 `getPostBySlug(slug: string)` 函数。
        - [ ] 创建 `getCategories()` 和 `getTags()` 函数。

- [ ] **9. 实现首页文章列表**
    - **文件**: `src/pages/HomePage.tsx`
    - **任务**:
        - [ ] 使用 `react-query` 或类似库的 `useQuery` 来调用 `getPosts` API。
        - [ ] 创建 `PostCard.tsx` (`src/components/common/`) 用于展示单篇文章的摘要信息。
        - [ ] 在页面中循环渲染 `PostCard` 列表。
        - [ ] 添加 `Pagination` 组件 (`shadcn/ui`)，并将其与 `getPosts` 的分页参数关联。

- [ ] **10. 实现文章详情页**
    - **文件**: `src/pages/PostDetailPage.tsx` (动态路由 `/post/:slug`)
    - **任务**:
        - [ ] 从 URL 中获取 `slug` 参数。
        - [ ] 使用 `useQuery` 调用 `getPostBySlug(slug)` API。
        - [ ] 创建 `MarkdownRenderer.tsx` (`src/components/common/`)，使用 `react-markdown` 和 `rehype-highlight` 渲染文章内容。
        - [ ] 在页面中展示文章标题、元数据（作者、日期）和渲染后的 Markdown 内容。

---