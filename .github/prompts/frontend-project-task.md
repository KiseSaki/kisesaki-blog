# 🎯 前端项目开发任务流 (KiseSaki Blog)

## 🎨 **UI 设计策略**
> **双 UI 库架构设计理念**

- **🌟 前台博客 (Blog Frontend)**: 使用 **shadcn/ui + TailwindCSS**
  - 注重用户阅读体验和视觉美观
  - 现代化的组件设计，支持深度定制
  - 轻量级，加载性能优先
  
- **👑 后台管理 (Admin Dashboard)**: 使用 **Ant Design (antd)**
  - 专业的企业级管理界面
  - 丰富的表格、表单、图表组件
  - 成熟的交互模式，提升管理效率

## 🚀 阶段一：项目基础架构 (Foundation)

### ✅ **1. 项目初始化与配置**
> **状态**: 已完成 ✓

* **📦 依赖安装**:
  * [X] 核心依赖: `react`, `react-dom`, `typescript`, `vite`
  * [X] UI 库: `tailwindcss`, `@tailwindcss/typography`, `shadcn/ui` (前台), `antd` (后台管理)
  * [X] 状态管理: `zustand`
  * [X] 路由: `react-router-dom`
  * [X] HTTP 客户端: `axios`
  * [X] 表单: `react-hook-form`, `@hookform/resolvers`, `zod`
  * [X] 数据获取: `@tanstack/react-query`
  * [X] 工具库: `clsx`, `tailwind-merge`, `date-fns`

* **⚙️ 配置文件**:
  * [X] `tsconfig.json` - 严格 TypeScript 配置，禁用 `any` 类型
  * [X] `vite.config.ts` - Vite 构建配置，路径别名
  * [X] `tailwind.config.js` - TailwindCSS 配置
  * [X] `components.json` - shadcn/ui 组件配置
  * [X] `eslint.config.js` - ESLint 代码检查配置

### ✅ **2. 核心基础设施搭建**
> **状态**: 已完成 ✓

* **🔌 HTTP 客户端** (`src/api/client.ts`):
  * [X] Axios 实例配置，baseURL 从环境变量获取
  * [X] 请求拦截器：自动附加 JWT token
  * [X] 响应拦截器：统一错误处理和数据提取
  * [X] 加载状态管理集成

* **🗃️ 状态管理** (`src/stores/authStore.ts`):
  * [X] Zustand 认证状态管理
  * [X] 用户信息、token、角色权限管理
  * [X] localStorage 持久化
  * [X] login/logout 操作封装

* **🛣️ 路由配置** (`src/router/index.tsx`):
  * [X] React Router v6 配置
  * [X] 嵌套路由结构定义
  * [X] 私有路由权限控制 (`PrivateRoute.tsx`)
  * [X] 错误页面路由配置

* **🏗️ 布局系统** (`src/components/layout/`):
  * [X] `MainLayout.tsx` - 主布局组件
  * [X] `Header.tsx` - 导航头部
  * [X] `Footer.tsx` - 页脚信息

### ✅ **3. 全局错误处理与用户体验**
> **状态**: 已完成 ✓

* **🛡️ 错误边界** (`src/components/common/ErrorBoundary.tsx`):
  * [X] React Error Boundary 实现
  * [X] 优雅的错误展示界面
  * [X] 错误日志上报机制

* **⏳ 加载与提示** (`src/components/common/`):
  * [X] `Loading.tsx` - 加载状态组件
  * [X] `SuspenseWrapper.tsx` - Suspense 包装器
  * [X] 全局 Toast 提示集成 (shadcn/ui Toaster)

* **📄 分页组件** (`src/components/common/Pagination.tsx`):
  * [X] 基础分页组件框架
  * [ ] 完善分页逻辑和样式

---

## � 阶段二：用户认证系统 (Authentication)

### 🚧 **4. 认证 API 服务层**

* **🌐 API 类型定义** (`src/types/auth.ts`, `src/types/api.ts`):
  * [ ] `LoginRequestDto` - 登录请求参数
  * [ ] `RegisterRequestDto` - 注册请求参数  
  * [ ] `User` - 用户信息类型
  * [ ] `AuthResponse` - 认证响应类型
  * [ ] `OAuthProvider` - OAuth 提供商类型

* **🔌 认证服务** (`src/api/auth.ts`):
  * [ ] `login(credentials: LoginRequestDto)` - 用户登录
  * [ ] `register(data: RegisterRequestDto)` - 用户注册
  * [ ] `logout()` - 用户登出
  * [ ] `getProfile()` - 获取用户资料
  * [ ] `refreshToken()` - 刷新访问令牌
  * [ ] `handleOAuthCallback(provider, code)` - OAuth 回调处理

### 🚧 **5. 认证页面组件**

* **🔑 登录页面** (`src/pages/Auth/LoginPage.tsx`):
  * [ ] 响应式登录表单 (React Hook Form + Zod 校验)
  * [ ] 用户名/邮箱 + 密码登录
  * [ ] GitHub/Gitee OAuth 登录按钮
  * [ ] "记住我"、"忘记密码"功能
  * [ ] 登录状态处理和页面跳转

* **📝 注册页面** (`src/pages/Auth/RegisterPage.tsx`):
  * [ ] 注册表单设计和校验
  * [ ] 邮箱验证码功能
  * [ ] 密码强度检查
  * [ ] 用户协议和隐私政策确认

* **🔗 OAuth 回调** (`src/pages/Auth/OAuthCallbackPage.tsx`):
  * [ ] URL 参数解析 (code, state, error)
  * [ ] 调用后端 OAuth 认证接口
  * [ ] 认证成功后用户信息存储
  * [ ] 自动跳转到原页面或首页

* **🔄 密码重置** (`src/pages/Auth/`):
  * [ ] `ForgotPasswordPage.tsx` - 忘记密码页面
  * [ ] `ResetPasswordPage.tsx` - 重置密码页面
  * [ ] `EmailVerificationPage.tsx` - 邮箱验证页面

### 🚧 **6. 认证相关 Hooks**

* **🪝 认证 Hook** (`src/hooks/useAuth.ts`):
  * [ ] 封装认证状态和操作
  * [ ] 自动 token 刷新逻辑
  * [ ] 用户权限检查函数
  * [ ] 登录状态监听

---

## 📝 阶段三：博客核心功能 (Blog Core)
> **UI 库**: 使用 **shadcn/ui + TailwindCSS** 构建用户友好的前台界面

### 🚧 **7. 博客 API 服务层**

* **📊 博客类型定义** (`src/types/blog.ts`):
  * [ ] `Post` - 文章数据类型
  * [ ] `Category` - 分类类型
  * [ ] `Tag` - 标签类型
  * [ ] `Comment` - 评论类型
  * [ ] `PostListParams` - 文章列表查询参数
  * [ ] `PaginationResult<T>` - 分页结果泛型

* **🌐 博客服务** (`src/api/blog.ts`):
  * [ ] `getPosts(params)` - 获取文章列表
  * [ ] `getPostBySlug(slug)` - 获取文章详情
  * [ ] `getPostsByCategory(category)` - 按分类获取文章
  * [ ] `getPostsByTag(tag)` - 按标签获取文章
  * [ ] `searchPosts(keyword)` - 搜索文章
  * [ ] `getArchivePosts()` - 获取归档文章
  * [ ] `getCategories()` - 获取所有分类
  * [ ] `getTags()` - 获取所有标签

### � **8. 博客展示组件 (基于 shadcn/ui)**

* **📰 文章卡片** (`src/components/blog/PostCard.tsx`):
  * [ ] 文章标题、摘要、封面图显示 (shadcn/ui Card 组件)
  * [ ] 发布时间、作者、分类、标签 (shadcn/ui Badge 组件)
  * [ ] 阅读量、点赞数、评论数
  * [ ] 响应式卡片设计

* **📋 文章列表** (`src/components/blog/PostList.tsx`):
  * [ ] 文章卡片网格布局
  * [ ] 分页加载功能 (shadcn/ui Pagination 组件)
  * [ ] 加载状态和空状态处理 (shadcn/ui Skeleton)
  * [ ] 列表/网格视图切换

* **🏷️ 标签云** (`src/components/blog/TagCloud.tsx`):
  * [ ] 标签权重可视化 (shadcn/ui Badge 组件)
  * [ ] 标签点击筛选功能
  * [ ] 响应式标签布局

* **💬 评论组件** (`src/components/blog/Comment.tsx`):
  * [ ] 评论列表展示 (shadcn/ui Card 组件)
  * [ ] 嵌套回复支持
  * [ ] 评论表单和提交 (shadcn/ui Form + Input 组件)
  * [ ] 评论点赞和举报 (shadcn/ui Button 组件)

### 🚧 **9. 博客页面组件**

* **🏠 首页** (`src/pages/Blog/HomePage.tsx`):
  * [ ] 轮播图/特色文章展示
  * [ ] 最新文章列表
  * [ ] 热门文章推荐
  * [ ] 分类和标签导航
  * [ ] 页面访问埋点

* **📄 文章详情页** (`src/pages/Blog/PostDetailPage.tsx`):
  * [ ] Markdown 内容渲染 (react-markdown + rehype-highlight)
  * [ ] 文章元信息显示 (时间、分类、标签)
  * [ ] 目录导航 (TOC)
  * [ ] 上一篇/下一篇导航
  * [ ] 评论区集成
  * [ ] 文章分享功能
  * [ ] 阅读进度条

* **📂 分类页面** (`src/pages/Blog/CategoryPage.tsx`):
  * [ ] 按分类展示文章列表
  * [ ] 分类描述和统计信息
  * [ ] 子分类导航

* **🏷️ 标签页面** (`src/pages/Blog/TagPage.tsx`):
  * [ ] 按标签展示文章列表
  * [ ] 相关标签推荐

* **📚 归档页面** (`src/pages/Blog/ArchivePage.tsx`):
  * [ ] 按年份/月份归档展示
  * [ ] 时间轴样式布局
  * [ ] 归档统计信息

* **🔍 搜索页面** (`src/pages/Blog/SearchPage.tsx`):
  * [ ] 搜索框和高级筛选
  * [ ] 搜索结果列表
  * [ ] 搜索历史和热门搜索
  * [ ] 搜索结果高亮

### 🚧 **10. 博客相关 Hooks**

* **🪝 博客数据 Hook** (`src/hooks/useBlog.ts`):
  * [ ] 封装文章数据获取和缓存
  * [ ] 分页数据管理
  * [ ] 搜索状态管理

---

## 👤 阶段四：用户功能模块 (User Features)

### 🚧 **11. 用户 API 服务**

* **👥 用户类型定义** (`src/types/user.ts`):
  * [ ] `UserProfile` - 用户资料类型
  * [ ] `UserSettings` - 用户设置类型
  * [ ] `UserStats` - 用户统计信息

* **🌐 用户服务** (`src/api/user.ts`):
  * [ ] `getUserProfile(id)` - 获取用户资料
  * [ ] `updateProfile(data)` - 更新用户资料
  * [ ] `changePassword(data)` - 修改密码
  * [ ] `uploadAvatar(file)` - 上传头像
  * [ ] `getUserFavorites()` - 获取用户收藏
  * [ ] `addToFavorites(postId)` - 添加收藏
  * [ ] `removeFromFavorites(postId)` - 取消收藏

### 🚧 **12. 用户组件**

* **👤 头像组件** (`src/components/user/Avatar.tsx`):
  * [ ] 圆形头像显示
  * [ ] 默认头像处理
  * [ ] 多尺寸支持
  * [ ] 在线状态指示

* **💳 用户卡片** (`src/components/user/UserCard.tsx`):
  * [ ] 用户基本信息展示
  * [ ] 用户统计数据
  * [ ] 关注/取消关注按钮

### 🚧 **13. 用户页面**

* **👤 用户资料** (`src/pages/User/UserProfile.tsx`):
  * [ ] 用户基本信息展示
  * [ ] 用户发布的文章列表
  * [ ] 用户活动时间线

* **⚙️ 用户设置** (`src/pages/User/UserSettings.tsx`):
  * [ ] 个人信息编辑表单
  * [ ] 密码修改功能
  * [ ] 隐私设置选项
  * [ ] 通知偏好设置

* **❤️ 收藏页面** (`src/pages/User/FavoritesPage.tsx`):
  * [ ] 用户收藏文章列表
  * [ ] 收藏分类管理
  * [ ] 批量操作功能

---

## 👑 阶段五：管理员功能 (Admin Features)
> **UI 库**: 使用 **Ant Design (antd)** 构建专业的管理后台界面

### 🚧 **14. 管理员 API 服务**

* **🌐 管理员服务** (`src/api/admin.ts`):
  * [ ] `getDashboardStats()` - 获取仪表板统计
  * [ ] `getUsers()` - 获取用户列表
  * [ ] `createPost(data)` - 创建文章
  * [ ] `updatePost(id, data)` - 更新文章
  * [ ] `deletePost(id)` - 删除文章
  * [ ] `manageComments()` - 评论管理
  * [ ] `manageCategories()` - 分类管理
  * [ ] `manageTags()` - 标签管理

### 🚧 **15. 管理员组件 (基于 Ant Design)**

* **✏️ 文章编辑器** (`src/components/admin/PostEditor.tsx`):
  * [ ] 富文本编辑器集成 (使用 antd 的 Input 组件)
  * [ ] Markdown 编辑器集成
  * [ ] 实时预览功能
  * [ ] 图片上传和管理 (antd Upload 组件)
  * [ ] 文章元数据编辑 (antd Form 组件)

* **📊 统计图表** (`src/components/admin/StatChart.tsx`):
  * [ ] 访问量统计图表 (结合 antd + ECharts/Chart.js)
  * [ ] 用户增长图表
  * [ ] 文章发布统计

* **📝 数据表格** (`src/components/admin/DataTable.tsx`):
  * [ ] 基于 antd Table 组件的通用数据表格
  * [ ] 支持排序、筛选、分页
  * [ ] 批量操作功能

### 🚧 **16. 管理员页面 (Ant Design Layout)**

* **📊 管理员仪表板** (`src/pages/Admin/AdminDashboard.tsx`):
  * [ ] antd Layout 布局系统
  * [ ] 网站统计概览 (antd Card + Statistic 组件)
  * [ ] 最新活动动态 (antd Timeline 组件)
  * [ ] 快捷操作入口 (antd Button 组件)

* **📝 内容管理** (全部使用 antd 组件):
  * [ ] `PostManagement.tsx` - 文章管理 (Table + Modal + Form)
  * [ ] `CategoryManagement.tsx` - 分类管理 (Tree + Form)
  * [ ] `TagManagement.tsx` - 标签管理 (Tag + Form)
  * [ ] `CommentManagement.tsx` - 评论管理 (Table + Popconfirm)

* **👥 用户管理** (`src/pages/Admin/UserManagement.tsx`):
  * [ ] 用户列表和搜索 (antd Table + Input.Search)
  * [ ] 用户权限管理 (antd Select + Switch)
  * [ ] 用户状态控制 (antd Badge + Button)

* **⚙️ 系统设置** (`src/pages/Admin/SettingsPage.tsx`):
  * [ ] 网站基本设置 (antd Form + Input/TextArea)
  * [ ] SEO 配置 (antd Tabs + Form)
  * [ ] 系统维护选项 (antd Switch + DatePicker)

---

## 🎨 阶段六：用户体验优化 (UX Enhancement)

### 🚧 **17. 响应式设计和主题**

* **📱 响应式 Hook** (`src/hooks/useResponsive.ts`):
  * [ ] 屏幕尺寸检测
  * [ ] 断点工具函数

* **🌙 主题切换**:
  * [ ] 明暗主题支持
  * [ ] 主题持久化存储
  * [ ] 主题切换动画

### 🚧 **18. 性能优化**

* **⚡ 代码分割**:
  * [ ] 路由级代码分割
  * [ ] 组件懒加载
  * [ ] 预加载策略

* **🔄 数据缓存**:
  * [ ] React Query 缓存配置
  * [ ] 离线数据支持
  * [ ] 乐观更新

### 🚧 **19. 搜索引擎优化 (SEO)**

* **🔍 SEO 优化**:
  * [ ] 动态 meta 标签
  * [ ] Open Graph 支持
  * [ ] 结构化数据
  * [ ] XML 站点地图

---

## 📊 阶段七：数据分析和监控 (Analytics)

### 🚧 **20. 访问统计和埋点**

* **📈 分析工具** (`src/lib/analytics.ts`):
  * [ ] `trackPageView(page, properties)` - 页面访问跟踪
  * [ ] `trackEvent(event, properties)` - 事件跟踪
  * [ ] `trackUserAction(action, context)` - 用户行为跟踪
  * [ ] 与后端 Kafka 日志系统集成

* **📊 统计面板**:
  * [ ] 实时访问统计
  * [ ] 用户行为分析
  * [ ] 内容热度排行

---

## 🚧 阶段八：错误处理和容错 (Error Handling)

### 🚧 **21. 错误页面**

* **❌ 错误页面组件** (`src/pages/Error/`):
  * [ ] `NotFoundPage.tsx` - 404 页面
  * [ ] `ForbiddenPage.tsx` - 403 权限不足
  * [ ] `ServerErrorPage.tsx` - 500 服务器错误  
  * [ ] `NetworkErrorPage.tsx` - 网络连接错误

### 🚧 **22. 全局错误处理**

* **🛡️ 错误边界增强**:
  * [ ] 错误信息收集和上报
  * [ ] 错误恢复机制
  * [ ] 开发环境错误详情

---

## 🧪 阶段九：测试和质量保证 (Testing & QA)

### 🚧 **23. 单元测试**

* **🧪 组件测试**:
  * [ ] 核心组件单元测试
  * [ ] Hook 功能测试
  * [ ] API 服务测试

* **🔧 测试工具配置**:
  * [ ] Jest + React Testing Library 配置
  * [ ] Mock 数据和服务
  * [ ] 测试覆盖率报告

### 🚧 **24. 集成测试**

* **🔗 端到端测试**:
  * [ ] 用户注册登录流程
  * [ ] 文章浏览和搜索
  * [ ] 管理员操作流程

---

## 📦 阶段十：构建和部署 (Build & Deploy)

### 🚧 **25. 生产环境优化**

* **🏗️ 构建优化**:
  * [ ] Vite 生产构建配置
  * [ ] 资源压缩和优化
  * [ ] CDN 资源配置

* **🚀 部署配置**:
  * [ ] Docker 镜像构建
  * [ ] 环境变量管理
  * [ ] CI/CD 流水线集成

---

## 📝 总结和后续

### 🎯 **开发优先级**
1. **高优先级**: 阶段一到三 (基础架构 + 认证 + 博客核心)
2. **中优先级**: 阶段四到六 (用户功能 + 管理员 + UX)
3. **低优先级**: 阶段七到十 (分析 + 测试 + 部署)

### 📋 **开发规范提醒**
- ✨ 所有新功能都需要创建对应的功能分支
- 🔷 严格遵循 TypeScript 类型安全，禁用 `any`
- 📝 关键组件和函数需要添加 TSDoc 注释
- 🧪 核心功能需要编写单元测试
- 🎨 **前台组件**: 遵循 TailwindCSS + shadcn/ui 设计规范
- 👑 **后台组件**: 使用 Ant Design 组件库，保持企业级管理界面风格
- 📊 重要页面和操作需要添加埋点跟踪

### 🔄 **持续改进**
- 定期重构和优化代码结构
- 关注用户体验和性能指标
- 及时更新依赖包和安全补丁
- 收集用户反馈并迭代改进功能