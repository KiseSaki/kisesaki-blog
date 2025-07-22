## 前端项目任务流
### 🚀 **阶段一：项目基础与核心服务 (Foundation &amp; Core Services)**

* [X] **1. 项目初始化与目录结构搭建**
  * **任务**:
    * [X] 使用 `pnpm create vite` 初始化 React + TypeScript 项目。
    * [X] 安装核心依赖: `tailwindcss`, `shadcn/ui`, `zustand`, `axios`, `react-router-dom`。
    * [X] 按照规范创建初始目录结构: `src/api`, `src/components/ui`, `src/components/common`, `src/components/layout`, `src/hooks`, `src/pages`, `src/stores`, `src/lib`, `src/config`, `src/types`。
    * [X] 配置 `tsconfig.json`，设置严格模式并**禁止** **`any`** **类型**。
* [ ] **2. 封装 Axios API 客户端**
  * **文件**: `src/api/client.ts`
  * **任务**:
    * [ ] 初始化 Axios 实例，`baseURL` 从环境变量 `import.meta.env.VITE_API_URL` 获取。
    * [ ] **请求拦截器**: 从 `authStore` 获取 JWT token，附加到 `Authorization` 请求头。
    * [ ] **响应拦截器**:
      * 成功时 (2xx): 直接返回 `response.data.data`，剥离外层封装。
      * 失败时: 使用 `sonner` (来自 shadcn/ui) 显示 `error.response.data.message` 错误提示，并 `Promise.reject(error)`。
    * [ ] **请求加载状态**: 封装的 API 服务函数应能返回 `loading` 状态，或考虑使用 `swr`/`react-query`。
* [ ] **3. 创建用户认证 Store (Zustand)**
  * **文件**: `src/stores/authStore.ts`
  * **任务**:
    * [ ] 定义 `AuthState` 接口，包含 `user: User | null`, `token: string | null`, `roles: string[]`。
    * [ ] 使用 Zustand 创建 `useAuthStore`，包含上述状态。
    * [ ] 创建 `login(user: User, token: string)` 和 `logout()` action。`login` action 需要同时设置用户信息、token 和角色。
    * [ ] 使用 Zustand 的 `persist` 中间件将 `token` 和 `user` 信息持久化到 `localStorage`。
* [ ] **4. 配置路由与权限控制**
  * **文件**: `src/router/index.tsx`
  * **任务**:
    * [ ] 使用 `createBrowserRouter` 创建路由。
    * [ ] 创建 `MainLayout.tsx` (`src/components/layout/`) 作为根布局，包含 `Header`、`Footer` 和 `<Outlet />`。
    * [ ] 创建 `PrivateRoute.tsx` (`src/router/`)，检查 `useAuthStore` 中的 `token` 和 `user.roles`，若不满足权限要求则重定向到 `/login` 或 `/403` 页面。
    * [ ] 定义路由结构，使用 `PrivateRoute` 包裹需要认证和特定角色的路由。
* [ ] **5. 实现全局错误边界与提示**
  * **文件**: `src/components/common/GlobalErrorBoundary.tsx`
  * **任务**:
    * [ ] 创建一个 React Error Boundary 组件，用于捕获渲染过程中的严重错误。
    * [ ] 在 `App.tsx` 的顶层使用该组件包裹路由。
    * [ ] 在根组件中集成 `shadcn/ui` 的 `<Toaster />`，为全局错误提示做准备。
### 👤 **阶段二：用户认证功能 (User Authentication)**
* [ ] **6. 创建认证 API 服务**
  * **文件**: `src/api/auth.ts`, `src/types/api.ts`
  * **任务**:
    * [ ] 在 `src/types/api.ts` 中定义 `LoginRequestDto`, `RegisterRequestDto`, `User`, `AuthResponse` 等类型。
    * [ ] 创建 `login(data: LoginRequestDto)` 函数。
    * [ ] 创建 `register(data: RegisterRequestDto)` 函数。
    * [ ] 创建 `getProfile()` 函数，用于获取当前登录用户信息。
    * [ ] 创建 `handleOAuthCallback(provider: 'github' | 'gitee', code: string)` 函数，用于处理 OAuth 回调。
* [ ] **7. 实现登录页面 (含 OAuth)**
  * **文件**: `src/pages/LoginPage.tsx`
  * **任务**:
    * [ ] 使用 `shadcn/ui` 构建包含用户名、密码的登录表单。
    * [ ] 集成 `react-hook-form` 和 `zod` 进行表单状态管理和客户端校验。
    * [ ] 表单提交时，调用 `login` API，成功后更新 `authStore` 并跳转。
    * [ ] 添加 "使用 GitHub 登录" 和 "使用 Gitee 登录" 的按钮，点击后重定向到后端指定的 OAuth 地址。
* [ ] **8. 实现 OAuth 回调处理**
  * **文件**: `src/pages/OAuthCallbackPage.tsx`
  * **任务**:
    * [ ] 创建一个页面用于处理从 GitHub/Gitee 回调的请求。
    * [ ] 在页面加载时，从 URL 中解析出 `code` 参数。
    * [ ] 调用 `handleOAuthCallback` API，将 `code` 发送到后端换取 JWT。
    * [ ] 成功后，将获取的用户信息和 token 存入 `authStore`，并跳转到首页。
### 📝 **阶段三：博客文章与日志上报 (Blog &amp; Analytics)**
* [ ] **9. 创建文章 API 服务**
  * **文件**: `src/api/post.ts`
  * **任务**:
    * [ ] 在 `src/types/api.ts` 中定义 `Post`, `Category`, `Tag`, `Pagination` 等类型。
    * [ ] 创建 `getPosts(params: { page: number, limit: number })` 函数。
    * [ ] 创建 `getPostBySlug(slug: string)` 函数。
* [ ] **10. 实现首页文章列表**
  * **文件**: `src/pages/HomePage.tsx`
  * **任务**:
    * [ ] 使用 `swr` 或 `react-query` 的 `useQuery` Hook 来调用 `getPosts` API，以管理数据获取、缓存和加载状态。
    * [ ] 创建 `PostCard.tsx` (`src/components/common/`) 用于展示单篇文章摘要。
    * [ ] 页面加载时，调用 `trackView('HomePage')` 上报埋点日志。
* [ ] **11. 实现文章详情页**
  * **文件**: `src/pages/PostDetailPage.tsx` (动态路由 `/post/:slug`)
  * **任务**:
    * [ ] 使用 `useQuery` 调用 `getPostBySlug(slug)` API。
    * [ ] 创建 `MarkdownRenderer.tsx` (`src/components/common/`)，使用 `react-markdown` 和 `rehype-highlight` 渲染文章内容。
    * [ ] 页面加载时，调用 `trackView('PostDetailPage', { slug })` 上报埋点日志。
* [ ] **12. 实现日志上报工具**
  * **文件**: `src/lib/analytics.ts`
  * **任务**:
    * [ ] 创建 `trackView(pageName: string, properties?: object)` 函数。
    * [ ] 创建 `reportEvent(eventName: string, properties?: object)` 函数。
    * [ ] 函数内部将日志信息通过一个专用的 API 端点（如 `/api/logs`）发送到后端 Kafka。