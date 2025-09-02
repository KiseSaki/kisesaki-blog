## 🎨 **UI 设计策略**

> **双 UI 库架构设计理念**

- **🌟 前台博客 (Blog Frontend)** : 使用 **shadcn/ui + TailwindCSS**

  - 注重用户阅读体验和视觉美观，现代化的组件设计，支持深度定制，轻量级，加载性能优先。
- **👑 后台管理 (Admin Dashboard)** : 使用 **Ant Design (antd)**

  - 专业的企业级管理界面，提供丰富的表格、表单、图表组件，交互成熟，提升管理效率。

---

## 🚀 阶段一：项目基础架构 (Foundation)

### ✅ **1. 项目初始化与环境配置**

> **优先级**: 最高 ⭐⭐⭐

- [ ] 使用 `Vite` 初始化 `React + TypeScript` 项目。
- [ ] 初始化 `Git` 仓库，并配置 `.gitignore` 文件。
- [ ] **安装核心依赖**:

  - [ ] 核心: `react`, `react-dom`, `typescript`
  - [ ] UI 库: `tailwindcss`, `@tailwindcss/typography`, `shadcn/ui` (前台), `antd` (后台)
  - [ ] 路由: `react-router-dom`
  - [ ] 状态管理: `zustand`
  - [ ] HTTP 客户端: `axios`
  - [ ] 数据获取: `@tanstack/react-query`
  - [ ] 表单: `react-hook-form`, `@hookform/resolvers`, `zod`
  - [ ] 图标库: `lucide-react`
  - [ ] 工具库: `clsx`, `tailwind-merge`, `date-fns`
- [ ] **配置文件**:

  - [ ] `tsconfig.json` - 严格 TypeScript 配置，禁用 `any` 类型，配置路径别名 (`@/components`等)。
  - [ ] `vite.config.ts` - Vite 构建配置，确认路径别名生效。
  - [ ] `tailwind.config.js` - TailwindCSS 配置。
  - [ ] `components.json` - shadcn/ui 组件配置。
  - [ ] `eslint.config.js` & `prettier.config.js` - ESLint 和 Prettier 代码检查与格式化配置。
- [ ] 设置多环境配置文件 (`.env`, `.env.development`, `.env.production`)。
- [ ] **补充**: 配置 `husky` 和 `lint-staged` 在 `git commit` 前自动执行代码检查和格式化。

### ✅ **2. 核心基础设施与全局组件**

> **优先级**: 最高 ⭐⭐⭐

- [ ] **定义项目目录结构**:

  ```
  /src
  ├── api/          # API 请求服务 (e.g., auth.ts, blog.ts)
  ├── components/
  │   ├── ui/       # shadcn/ui 生成的原子组件
  │   ├── common/   # 自定义通用共享组件 (Loading, Pagination, ErrorBoundary)
  │   └── layout/   # 布局组件 (MainLayout, Header, Footer)
  ├── hooks/        # 自定义 Hooks
  ├── lib/          # 工具函数 (e.g., client.ts)
  ├── pages/        # 页面级组件
  ├── providers/    # 全局 Provider (Theme, ReactQuery, Auth)
  ├── stores/       # Zustand 状态管理 (e.g., authStore.ts)
  ├── types/        # TypeScript 类型定义
  └── router/       # 路由配置 (index.tsx)
  ```
- [ ] **构建核心布局组件** (`src/components/layout/`):

  - [ ] `MainLayout.tsx` - 主布局组件，包含 `Header`, `Footer` 和内容区。
  - [ ] `Header.tsx` - 导航头部。
  - [ ] `Footer.tsx` - 页脚信息。
- [ ] **创建核心工具与 Provider**:

  - [ ] **HTTP 客户端** (`src/lib/client.ts`): Axios 实例配置，自动附加 JWT 的请求拦截器，统一错误处理的响应拦截器。
  - [ ] **状态管理** (`src/stores/authStore.ts`): Zustand 认证状态管理 (用户信息, token, 角色)，集成 `localStorage` 持久化。
  - [ ] **路由配置** (`src/router/index.tsx`): React Router v6 嵌套路由，配置私有路由 (`PrivateRoute.tsx`) 和访客路由 (`GuestRoute.tsx`)。
  - [ ] **全局 Provider** (`src/providers/`): 配置 `ReactQueryProvider`, `ThemeProvider` 等。

### ✅ **3. 全局错误处理与用户体验**

> **优先级**: 高 ⭐⭐

- [ ] **错误边界** (`src/components/common/ErrorBoundary.tsx`): React Error Boundary 实现，提供优雅的错误展示界面和错误日志上报机制。
- [ ] **加载与提示** (`src/components/common/`):

  - [ ] `Loading.tsx` - 全局或局部加载状态组件。
  - [ ] `SuspenseWrapper.tsx` - Suspense 包装器，用于代码分割。
  - [ ] 全局 Toast 提示集成 (使用 `shadcn/ui` 的 `Toaster`)。
- [ ] **分页组件** (`src/components/common/Pagination.tsx`): 完善分页逻辑和样式，使其可复用。

---

## 👤 阶段二：用户认证系统 (Authentication)

### 🚧 **4. 认证 API 服务层**

> **优先级**: 最高 ⭐⭐⭐

- [ ] **API 类型定义** (`src/types/auth.ts`, `src/types/api.ts`):

  - [ ] `LoginRequestDto`, `RegisterRequestDto`
  - [ ] `User`, `AuthResponse`, `OAuthProvider`
- [ ] **认证服务** (`src/api/auth.ts`):

  - [ ] `login(credentials: LoginRequestDto)`
  - [ ] `register(data: RegisterRequestDto)`
  - [ ] `logout()`
  - [ ] `getProfile()`
  - [ ] `refreshToken()`
  - [ ] `handleOAuthCallback(provider, code)`

### 🚧 **5. 认证页面组件**

> **优先级**: 最高 ⭐⭐⭐

- [ ] **登录页面** (`src/pages/Auth/LoginPage.tsx`):

  - [ ] 使用 `React Hook Form` + `Zod` 构建响应式登录表单。
  - [ ] 实现用户名/邮箱 + 密码登录。
  - [ ] 集成 GitHub/Gitee OAuth 登录按钮。
  - [ ] 实现 "记住我"、"忘记密码" 功能链接。
- [ ] **注册页面** (`src/pages/Auth/RegisterPage.tsx`):

  - [ ] 构建注册表单及校验，集成邮箱验证码功能和密码强度检查。
- [ ] **OAuth 回调** (`src/pages/Auth/OAuthCallbackPage.tsx`):

  - [ ] 处理 URL 参数，调用后端认证接口，成功后存储用户信息并跳转。
- [ ] **密码重置** (`src/pages/Auth/`):

  - [ ] `ForgotPasswordPage.tsx`, `ResetPasswordPage.tsx`, `EmailVerificationPage.tsx`

### 🚧 **6. 认证相关 Hooks**

> **优先级**: 高 ⭐⭐

- [ ] **认证 Hook** (`src/hooks/useAuth.ts`):

  - [ ] 封装认证状态 (`isAuthenticated`, `user`) 和操作 (`login`, `logout`)。
  - [ ] 集成自动 token 刷新逻辑。
  - [ ] 提供用户权限检查函数。

---

## 📝 阶段三：博客核心功能 (Blog Core)

> **UI 库**: 使用 **shadcn/ui + TailwindCSS** 构建用户友好的前台界面

### 🚧 **7. 博客 API 服务层**

> **优先级**: 最高 ⭐⭐⭐

- [ ] **博客类型定义** (`src/types/blog.ts`):

  - [ ] `Post`, `Category`, `Tag`, `Comment`
  - [ ] `PostListParams`, `PaginationResult<T>`
- [ ] **博客服务** (`src/api/blog.ts`):

  - [ ] `getPosts(params)`, `getPostBySlug(slug)`
  - [ ] `getPostsByCategory(category)`, `getPostsByTag(tag)`
  - [ ] `searchPosts(keyword)`, `getArchivePosts()`
  - [ ] `getCategories()`, `getTags()`

### 🚧 **8. 博客展示组件 (基于 shadcn/ui)**

> **优先级**: 最高 ⭐⭐⭐

- [ ] **文章卡片** (`src/components/blog/PostCard.tsx`): 使用 `Card` 和 `Badge` 组件展示文章元信息。
- [ ] **文章列表** (`src/components/blog/PostList.tsx`): 文章卡片网格布局，集成 `Pagination` 和 `Skeleton` 组件。
- [ ] **标签云** (`src/components/blog/TagCloud.tsx`): 使用 `Badge` 组件实现标签权重可视化。
- [ ] **评论组件** (`src/components/blog/Comment.tsx`): 评论列表、嵌套回复、评论表单。
- [ ] **Markdown 渲染器**: 使用 `react-markdown` + `rehype-highlight` 渲染文章内容。

### 🚧 **9. 博客页面组件**

> **优先级**: 最高 ⭐⭐⭐

- [ ] **首页** (`src/pages/Blog/HomePage.tsx`): 特色文章、最新文章列表、分类/标签导航。
- [ ] **文章详情页** (`src/pages/Blog/PostDetailPage.tsx`): Markdown 内容渲染、文章元信息、目录导航 (TOC)、上下篇导航、评论区。
- [ ] **分类页面** (`src/pages/Blog/CategoryPage.tsx`): 按分类展示文章列表。
- [ ] **标签页面** (`src/pages/Blog/TagPage.tsx`): 按标签展示文章列表。
- [ ] **归档页面** (`src/pages/Blog/ArchivePage.tsx`): 按时间轴样式展示文章归档。
- [ ] **搜索页面** (`src/pages/Blog/SearchPage.tsx`): 搜索框及搜索结果展示。

### 🚧 **10. 博客相关 Hooks**

> **优先级**: 高 ⭐⭐

- [ ] **博客数据 Hook** (`src/hooks/useBlog.ts`):

  - [ ] 封装文章数据获取和缓存 (`@tanstack/react-query`)。
  - [ ] 管理分页、搜索等状态。

---

## 👤 阶段四：用户功能模块 (User Features)

### 🚧 **11. 用户 API 服务**

> **优先级**: 高 ⭐⭐

- [ ] **用户类型定义** (`src/types/user.ts`):

  - [ ] `UserProfile`, `UserSettings`, `UserStats`
- [ ] **用户服务** (`src/api/user.ts`):

  - [ ] `getUserProfile(id)`, `updateProfile(data)`, `changePassword(data)`
  - [ ] `uploadAvatar(file)`
  - [ ] `getUserFavorites()`, `addToFavorites(postId)`, `removeFromFavorites(postId)`

### 🚧 **12. 用户中心页面与组件**

> **优先级**: 高 ⭐⭐

- [ ] **通用组件**: `Avatar.tsx`, `UserCard.tsx`。
- [ ] **用户资料** (`src/pages/User/UserProfile.tsx`): 展示用户基本信息、发布的文章列表。
- [ ] **用户设置** (`src/pages/User/UserSettings.tsx`): 个人信息编辑表单、密码修改、隐私设置。
- [ ] **收藏页面** (`src/pages/User/FavoritesPage.tsx`): 展示用户收藏的文章列表。
- [ ] **补充**: 对点赞、收藏等操作采用 **乐观更新 (Optimistic Updates)**  提升交互体验。

---

## 👑 阶段五：管理员功能 (Admin Features)

> **UI 库**: 使用 **Ant Design (antd)**  构建专业的管理后台界面

### 🚧 **13. 管理员 API 与布局**

> **优先级**: 高 ⭐⭐

- [ ] **管理员服务** (`src/api/admin.ts`):

  - [ ] `getDashboardStats()`, `getUsers()`
  - [ ] `createPost(data)`, `updatePost(id, data)`, `deletePost(id)`
  - [ ] `manageComments()`, `manageCategories()`, `manageTags()`
- [ ] **后台布局**:

  - [ ] 创建 `/admin` 路由组，应用独立的 `AdminLayout.tsx` (含 `antd` 侧边栏导航)。
  - [ ] 在 `PrivateRoute.tsx` 中增加管理员角色 (`ADMIN`) 校验。

### 🚧 **14. 管理员组件 (基于 Ant Design)**

> **优先级**: 高 ⭐⭐

- [ ] **文章编辑器** (`src/components/admin/PostEditor.tsx`):

  - [ ] 集成 Markdown 编辑器 (如 `Tiptap` 或 `Milkdown`) 及其实时预览。
  - [ ] 使用 `antd` 的 `Upload` 组件实现图片上传和管理。
  - [ ] 使用 `antd` 的 `Form` 组件处理文章元数据编辑。
- [ ] **统计图表** (`src/components/admin/StatChart.tsx`): 结合 `antd` 和 `ECharts/Recharts` 实现数据可视化。
- [ ] **数据表格** (`src/components/admin/DataTable.tsx`): 基于 `antd Table` 封装，支持排序、筛选、分页和批量操作。

### 🚧 **15. 管理员页面 (基于 Ant Design)**

> **优先级**: 高 ⭐⭐

- [ ] **管理员仪表板** (`src/pages/Admin/AdminDashboard.tsx`): 使用 `Card`, `Statistic`, `Timeline` 等组件展示网站统计概览。
- [ ] **内容管理**:

  - [ ] `PostManagement.tsx` (文章管理): `Table` + `Modal` + `Form`
  - [ ] `CategoryManagement.tsx` (分类管理): `Tree` + `Form`
  - [ ] `TagManagement.tsx` (标签管理): `Tag` + `Input`
  - [ ] `CommentManagement.tsx` (评论管理): `Table` + `Popconfirm`
- [ ] **用户管理** (`src/pages/Admin/UserManagement.tsx`): `Table` + `Input.Search` + `Select` + `Switch`。
- [ ] **系统设置** (`src/pages/Admin/SettingsPage.tsx`): 使用 `Form`, `Tabs`, `Switch` 等组件配置网站信息。

---

## 🎨 阶段六：用户体验优化 (UX Enhancement)

### 🚧 **16. 响应式设计和主题**

> **优先级**: 中 ⭐

- [ ] **响应式 Hook** (`src/hooks/useResponsive.ts`): 封装屏幕尺寸检测和断点工具函数。
- [ ] **主题切换**: 实现明/暗主题切换，并使用 `localStorage` 持久化。

### 🚧 **17. 性能优化**

> **优先级**: 中 ⭐

- [ ] **代码分割**: 使用 `React.lazy` 和 `Suspense` 实现路由级代码分割和组件懒加载。
- [ ] **数据缓存**: 精细化配置 `@tanstack/react-query` 的缓存策略，实现离线数据支持和乐观更新。

### 🚧 **18. 搜索引擎优化 (SEO)**

> **优先级**: 中 ⭐

- [ ] **SEO 优化**: 实现动态 `meta` 标签, `Open Graph` 支持, 结构化数据和 `XML` 站点地图。

---

## 📊 阶段七：数据分析和监控 (Analytics)

### 🚧 **19. 访问统计和埋点**

> **优先级**: 低 ⭐

- [ ] **分析工具** (`src/lib/analytics.ts`): 封装页面访问 (`trackPageView`) 和用户行为 (`trackEvent`) 的跟踪函数，与后端日志系统集成。
- [ ] **统计面板**: 在后台仪表板中集成实时访问统计、用户行为分析等模块。

---

## 🛡️ 阶段八：错误处理和容错 (Error Handling)

### 🚧 **20. 错误页面与全局处理**

> **优先级**: 中 ⭐

- [ ] **错误页面组件** (`src/pages/Error/`):

  - [ ] `NotFoundPage.tsx` (404), `ForbiddenPage.tsx` (403), `ServerErrorPage.tsx` (500)
- [ ] **错误边界增强**: 增强 `ErrorBoundary`，实现错误信息收集上报和开发环境下的错误详情展示。

---

## 🧪 阶段九：测试和质量保证 (Testing & QA)

### 🚧 **21. 单元与集成测试**

> **优先级**: 中 ⭐

- [ ] **测试工具配置**:

  - [ ] 配置 `Vitest` 和 `React Testing Library` 作为测试框架。
  - [ ] 配置 `Mock Service Worker (MSW)` 模拟 API 服务。
- [ ] **测试用例编写**:

  - [ ] 为核心组件、自定义 Hooks 和工具函数编写单元测试。
  - [ ] 为用户注册登录、文章浏览等核心流程编写集成测试。
  - [ ] 生成并关注测试覆盖率报告。

---

## 📦 阶段十：构建和部署 (Build & Deploy)

### 🚧 **22. 生产环境优化与部署**

> **优先级**: 低 ⭐

- [ ] **构建优化**:

  - [ ] 优化 `Vite` 生产构建配置，进行资源压缩和 Tree Shaking。
  - [ ] 配置 `CDN` 资源加载。
- [ ] **部署配置**:

  - [ ] 编写 `Dockerfile` 用于项目容器化。
  - [ ] 配置 `CI/CD` (如 GitHub Actions) 自动化测试、构建和部署流程。