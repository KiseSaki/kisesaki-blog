# 🎯 后端项目开发任务流 (KiseSaki Blog)

基于前端任务流，重新制定的后端开发任务流，确保与前端需求完美匹配。

## 🚀 阶段一：项目基础架构与安全体系 (Foundation & Security)

### 🔧 **1. 项目初始化与环境配置**
> **优先级**: 最高 ⭐⭐⭐

* **📦 Maven 依赖配置** (`pom.xml`):
  * [ ] Spring Boot 3.x 核心依赖
  * [ ] Spring Security 6.x + OAuth2 Client
  * [ ] Spring Data JPA + MyBatis-Plus
  * [ ] PostgreSQL Driver + HikariCP
  * [ ] Redis + Spring Data Redis
  * [ ] Kafka + Spring Kafka
  * [ ] SpringDoc OpenAPI (Swagger UI)
  * [ ] JWT 依赖 (jjwt-api, jjwt-impl, jjwt-jackson)
  * [ ] 邮件发送 (spring-boot-starter-mail)
  * [ ] 文件上传 (commons-fileupload)
  * [ ] 测试依赖 (JUnit 5, Mockito, TestContainers)

* **⚙️ 多环境配置**:
  * [ ] `application.yml` - 主配置文件
  * [ ] `application-dev.yml` - 开发环境配置
  * [ ] `application-prod.yml` - 生产环境配置
  * [ ] 数据库连接配置 (PostgreSQL)
  * [ ] Redis 连接配置
  * [ ] Kafka 集群配置
  * [ ] JWT 密钥配置
  * [ ] OAuth2 客户端配置 (GitHub/Gitee)
  * [ ] 邮件服务器配置
  * [ ] 文件存储配置

### 🛡️ **2. 统一响应格式与全局异常处理**
> **优先级**: 最高 ⭐⭐⭐

* **📋 统一 API 响应** (`com.kisesaki.blog.common.dto`):
  * [ ] `ApiResponse<T>` - 统一响应格式 `{code, message, data, timestamp}`
  * [ ] `PageResponse<T>` - 分页响应格式
  * [ ] `ErrorCode` - 错误码枚举
  * [ ] `ResultUtils` - 响应工具类

* **🚨 全局异常处理** (`com.kisesaki.blog.common.handler`):
  * [ ] `GlobalExceptionHandler` - 全局异常处理器
  * [ ] 参数校验异常处理 (`MethodArgumentNotValidException`)
  * [ ] 权限不足异常处理 (`AccessDeniedException`)
  * [ ] 业务异常处理 (`BusinessException`)
  * [ ] 系统异常处理 (`RuntimeException`)
  * [ ] 404/405 等 HTTP 异常处理

### 🔐 **3. RBAC 权限体系实体设计**
> **优先级**: 最高 ⭐⭐⭐

* **👤 用户相关实体** (`com.kisesaki.blog.entity`):
  * [ ] `User` - 用户基础信息
  * [ ] `UserProfile` - 用户扩展信息
  * [ ] `UserSettings` - 用户个性化设置

* **🔑 权限相关实体**:
  * [ ] `Role` - 角色实体
  * [ ] `Permission` - 权限实体 (使用 Enum 管理权限点)
  * [ ] `UserRole` - 用户角色关联 (多对多中间表)
  * [ ] `RolePermission` - 角色权限关联 (多对多中间表)

* **📁 Repository 层**:
  * [ ] `UserRepository` - 用户数据访问
  * [ ] `UserProfileRepository` - 用户扩展信息访问
  * [ ] `RoleRepository` - 角色数据访问
  * [ ] `PermissionRepository` - 权限数据访问

### 🔒 **4. Spring Security 配置**
> **优先级**: 最高 ⭐⭐⭐

* **🛡️ 安全配置** (`com.kisesaki.blog.config`):
  * [ ] `SecurityConfig` - 主安全配置类
  * [ ] `JwtAuthenticationFilter` - JWT 认证过滤器
  * [ ] `JwtTokenProvider` - JWT 工具类
  * [ ] `CustomUserDetailsService` - 用户详情服务
  * [ ] 接口权限配置 (公开接口放行，管理接口权限控制)
  * [ ] CORS 跨域配置
  * [ ] 密码编码器配置

---

## 👤 阶段二：用户认证与 OAuth2 集成 (Authentication & OAuth2)

### 🔐 **5. 认证核心服务**
> **优先级**: 最高 ⭐⭐⭐

* **🌐 认证 DTO** (`com.kisesaki.blog.dto.auth`):
  * [ ] `LoginRequestDto` - 登录请求参数
  * [ ] `RegisterRequestDto` - 注册请求参数
  * [ ] `AuthResponseDto` - 认证响应 (包含 token 和用户信息)
  * [ ] `UserInfoDto` - 用户信息 DTO
  * [ ] `ChangePasswordDto` - 修改密码 DTO
  * [ ] `ResetPasswordDto` - 重置密码 DTO

* **🔧 认证服务** (`com.kisesaki.blog.service`):
  * [ ] `AuthService` - 认证核心服务
    * [ ] `register()` - 用户注册 (默认分配 USER 角色)
    * [ ] `login()` - 用户登录 (生成包含角色权限的 JWT)
    * [ ] `logout()` - 用户登出
    * [ ] `refreshToken()` - 刷新访问令牌
    * [ ] `changePassword()` - 修改密码
    * [ ] `resetPassword()` - 重置密码

* **📧 邮件服务** (`com.kisesaki.blog.service`):
  * [ ] `EmailService` - 邮件发送服务
  * [ ] 注册验证邮件
  * [ ] 密码重置邮件
  * [ ] 系统通知邮件

### 🔗 **6. OAuth2 社交登录**
> **优先级**: 高 ⭐⭐

* **🌐 OAuth2 配置**:
  * [ ] GitHub OAuth2 客户端配置
  * [ ] Gitee OAuth2 客户端配置
  * [ ] `OAuth2UserService` - 自定义用户信息服务
  * [ ] OAuth2 成功/失败处理器

* **🎮 OAuth2 控制器**:
  * [ ] `OAuth2Controller` - OAuth2 相关接口
  * [ ] `/api/oauth2/authorization/{provider}` - 重定向到 OAuth2 提供商
  * [ ] `/api/oauth2/callback/{provider}` - OAuth2 回调处理
  * [ ] 本地用户创建或更新逻辑

### 🧪 **7. 认证模块测试**
> **优先级**: 中 ⭐

* **✅ 集成测试**:
  * [ ] 用户注册接口测试
  * [ ] 用户登录接口测试
  * [ ] JWT 验证测试
  * [ ] OAuth2 流程测试
  * [ ] 权限验证测试

---

## 📝 阶段三：博客核心功能 (Blog Core Features)

### 📰 **8. 博客实体与 Repository**
> **优先级**: 最高 ⭐⭐⭐

* **📄 博客相关实体**:
  * [ ] `Post` - 文章实体 (支持草稿、发布、归档状态)
  * [ ] `PostRevision` - 文章版本历史
  * [ ] `PostMeta` - 文章元数据 (SEO 相关)
  * [ ] `Category` - 分类实体 (支持层级结构)
  * [ ] `Tag` - 标签实体
  * [ ] `PostTag` - 文章标签关联
  * [ ] `Comment` - 评论实体 (支持嵌套回复)
  * [ ] `MediaResource` - 媒体资源实体

* **📁 Repository 层**:
  * [ ] `PostRepository` - 文章数据访问 (JPA + 自定义查询)
  * [ ] `CategoryRepository` - 分类数据访问
  * [ ] `TagRepository` - 标签数据访问
  * [ ] `CommentRepository` - 评论数据访问
  * [ ] `MediaResourceRepository` - 媒体资源访问

### � **9. 博客 DTO 设计**
> **优先级**: 高 ⭐⭐

* **📋 博客 DTO** (`com.kisesaki.blog.dto.blog`):
  * [ ] `PostDto` - 文章详情 DTO
  * [ ] `PostSummaryDto` - 文章摘要 DTO (列表用)
  * [ ] `PostCreateDto` - 创建文章 DTO
  * [ ] `PostUpdateDto` - 更新文章 DTO
  * [ ] `CategoryDto` - 分类 DTO
  * [ ] `TagDto` - 标签 DTO
  * [ ] `CommentDto` - 评论 DTO
  * [ ] `PostSearchDto` - 文章搜索参数 DTO
  * [ ] `ArchiveDto` - 归档 DTO

### 🔧 **10. 博客核心服务**
> **优先级**: 最高 ⭐⭐⭐

* **📝 文章服务** (`com.kisesaki.blog.service`):
  * [ ] `PostService` - 文章核心服务
    * [ ] `getPublishedPosts()` - 获取已发布文章列表
    * [ ] `getPostBySlug()` - 根据 slug 获取文章详情 (带缓存)
    * [ ] `getPostsByCategory()` - 按分类获取文章
    * [ ] `getPostsByTag()` - 按标签获取文章
    * [ ] `searchPosts()` - 搜索文章
    * [ ] `getArchivePosts()` - 获取归档文章
    * [ ] `incrementViewCount()` - 增加浏览量

* **📁 分类标签服务**:
  * [ ] `CategoryService` - 分类服务
    * [ ] `getAllCategories()` - 获取所有分类 (树形结构)
    * [ ] `getCategoryWithPosts()` - 获取分类及其文章
  * [ ] `TagService` - 标签服务
    * [ ] `getAllTags()` - 获取所有标签
    * [ ] `getPopularTags()` - 获取热门标签
    * [ ] `getTagCloud()` - 获取标签云数据

* **💬 评论服务**:
  * [ ] `CommentService` - 评论服务
    * [ ] `getCommentsByPost()` - 获取文章评论 (树形结构)
    * [ ] `addComment()` - 添加评论
    * [ ] `replyComment()` - 回复评论
    * [ ] `deleteComment()` - 删除评论

### 🗄️ **11. 缓存策略实现**
> **优先级**: 高 ⭐⭐

* **🔄 Redis 缓存配置**:
  * [ ] `CacheConfig` - 缓存配置类
  * [ ] 统一缓存 Key 命名规范 (`blog:post:{slug}`)
  * [ ] 缓存过期时间配置
  * [ ] 缓存预热机制

* **📊 缓存应用**:
  * [ ] 文章详情缓存 (`@Cacheable`)
  * [ ] 文章列表缓存
  * [ ] 分类标签缓存
  * [ ] 热门文章缓存
  * [ ] 缓存更新策略 (`@CacheEvict`)

---

## 👥 阶段四：用户功能与互动系统 (User Features & Interactions)

### 👤 **12. 用户资料管理**
> **优先级**: 高 ⭐⭐

* **📝 用户 DTO**:
  * [ ] `UserProfileDto` - 用户资料 DTO
  * [ ] `UserStatsDto` - 用户统计信息 DTO
  * [ ] `UpdateProfileDto` - 更新资料 DTO
  * [ ] `UserSettingsDto` - 用户设置 DTO

* **🔧 用户服务**:
  * [ ] `UserService` - 用户管理服务
    * [ ] `getUserProfile()` - 获取用户资料
    * [ ] `updateProfile()` - 更新用户资料
    * [ ] `uploadAvatar()` - 上传头像
    * [ ] `getUserStats()` - 获取用户统计
  * [ ] `UserSettingsService` - 用户设置服务

### ❤️ **13. 互动功能实现**
> **优先级**: 中 ⭐

* **💝 互动实体**:
  * [ ] `Like` - 点赞实体 (支持文章、评论点赞)
  * [ ] `Favorite` - 收藏实体
  * [ ] `Following` - 关注关系实体
  * [ ] `ViewLog` - 浏览记录实体

* **🔧 互动服务**:
  * [ ] `LikeService` - 点赞服务
  * [ ] `FavoriteService` - 收藏服务
  * [ ] `FollowService` - 关注服务
  * [ ] `ViewLogService` - 浏览记录服务

### 📊 **14. 统计分析服务**
> **优先级**: 中 ⭐

* **📈 统计服务**:
  * [ ] `StatisticsService` - 统计分析服务
    * [ ] 文章浏览量统计
    * [ ] 用户活跃度统计
    * [ ] 热门内容统计
    * [ ] 网站访问统计

---

## 👑 阶段五：管理员功能 (Admin Features)

### 🛠️ **15. 内容管理服务**
> **优先级**: 高 ⭐⭐

* **📝 管理员 DTO**:
  * [ ] `AdminPostDto` - 管理员文章 DTO
  * [ ] `DashboardStatsDto` - 仪表板统计 DTO
  * [ ] `UserManagementDto` - 用户管理 DTO

* **🔧 管理员服务**:
  * [ ] `AdminPostService` - 文章管理服务
    * [ ] `createPost()` - 创建文章
    * [ ] `updatePost()` - 更新文章
    * [ ] `deletePost()` - 删除文章
    * [ ] `getDraftPosts()` - 获取草稿文章
  * [ ] `AdminUserService` - 用户管理服务
  * [ ] `AdminCommentService` - 评论管理服务
  * [ ] `AdminCategoryService` - 分类管理服务
  * [ ] `AdminTagService` - 标签管理服务

### 📊 **16. 仪表板统计**
> **优先级**: 中 ⭐

* **📈 仪表板服务**:
  * [ ] `DashboardService` - 仪表板服务
    * [ ] 网站概览统计
    * [ ] 用户增长趋势
    * [ ] 内容发布统计
    * [ ] 访问量分析

---

## 🔄 阶段六：异步事件与通知系统 (Async Events & Notifications)

### 📨 **17. Kafka 异步事件系统**
> **优先级**: 中 ⭐

* **⚡ Kafka 配置**:
  * [ ] `KafkaConfig` - Kafka 配置类
  * [ ] `KafkaProducerService` - 消息发送服务
  * [ ] Topic 定义 (post-events, user-events, log-events)

* **📢 事件发布**:
  * [ ] 文章发布事件
  * [ ] 用户注册事件
  * [ ] 评论通知事件
  * [ ] 系统日志事件

* **👂 事件消费者**:
  * [ ] `LogEventConsumer` - 日志事件消费者
  * [ ] `NotificationEventConsumer` - 通知事件消费者
  * [ ] `StatisticsEventConsumer` - 统计事件消费者

### 🔔 **18. 通知系统**
> **优先级**: 中 ⭐

* **📨 通知实体**:
  * [ ] `Notification` - 通知实体
  * [ ] `Subscription` - 订阅实体
  * [ ] `NewsletterLog` - 通讯发送记录

* **🔧 通知服务**:
  * [ ] `NotificationService` - 通知服务
  * [ ] `SubscriptionService` - 订阅服务
  * [ ] `NewsletterService` - 通讯服务

---

## 📁 阶段七：文件管理与媒体服务 (File & Media Management)

### 📷 **19. 文件上传服务**
> **优先级**: 中 ⭐

* **📁 文件服务**:
  * [ ] `FileUploadService` - 文件上传服务
  * [ ] `MediaResourceService` - 媒体资源管理
  * [ ] 图片压缩和格式转换
  * [ ] 文件访问权限控制

---

## 🌐 阶段八：API 接口层 (API Controllers)

### 🔌 **20. 公开 API 接口**
> **优先级**: 最高 ⭐⭐⭐

* **📝 博客公开接口** (`com.kisesaki.blog.controller.api`):
  * [ ] `BlogController` - 博客相关接口
    * [ ] `GET /api/posts` - 获取文章列表
    * [ ] `GET /api/posts/{slug}` - 获取文章详情
    * [ ] `GET /api/categories` - 获取分类列表
    * [ ] `GET /api/tags` - 获取标签列表
    * [ ] `GET /api/archive` - 获取归档数据
    * [ ] `GET /api/search` - 搜索文章

* **👤 用户相关接口**:
  * [ ] `UserController` - 用户相关接口
  * [ ] `AuthController` - 认证相关接口

### 🛡️ **21. 受保护 API 接口**
> **优先级**: 高 ⭐⭐

* **👑 管理员接口** (`com.kisesaki.blog.controller.admin`):
  * [ ] `AdminPostController` - 文章管理接口
  * [ ] `AdminUserController` - 用户管理接口
  * [ ] `AdminDashboardController` - 仪表板接口
  * [ ] `AdminSettingsController` - 系统设置接口

---

## 🧪 阶段九：测试与质量保证 (Testing & QA)

### ✅ **22. 单元测试**
> **优先级**: 中 ⭐

* **🧪 Service 层测试**:
  * [ ] `AuthServiceTest` - 认证服务测试
  * [ ] `PostServiceTest` - 文章服务测试
  * [ ] `UserServiceTest` - 用户服务测试

### 🔗 **23. 集成测试**
> **优先级**: 中 ⭐

* **🌐 Controller 层测试**:
  * [ ] API 接口集成测试
  * [ ] 权限验证测试
  * [ ] 数据库操作测试

---

## 📖 阶段十：文档与部署 (Documentation & Deployment)

### 📚 **24. API 文档**
> **优先级**: 中 ⭐

* **📋 接口文档**:
  * [ ] Swagger/OpenAPI 配置完善
  * [ ] 接口分组和标签
  * [ ] 接口描述和示例

### 🚀 **25. 部署配置**
> **优先级**: 低 ⭐

* **🐳 容器化**:
  * [ ] Dockerfile 编写
  * [ ] Docker Compose 配置
  * [ ] K8s 部署文件

---

## 📝 开发优先级说明

### 🔥 **第一优先级** (阶段 1-3)
- 项目基础架构搭建
- 用户认证与权限系统
- 博客核心功能实现

### ⭐ **第二优先级** (阶段 4-6)
- 用户功能模块
- 管理员功能
- 异步事件系统

### 💡 **第三优先级** (阶段 7-10)
- 文件管理
- 高级功能
- 测试与部署

---

## 🔄 **开发规范提醒**

- ✨ 严格遵循分层架构 (Controller -> Service -> Repository)
- 🔷 所有接口使用 DTO 进行数据传输
- 📝 使用 Jakarta Bean Validation 进行参数校验
- 🎨 统一异常处理和日志记录
- 🧪 核心功能需要编写单元测试
- 📊 重要操作需要添加 Kafka 事件发布
- 🔐 敏感操作需要权限验证