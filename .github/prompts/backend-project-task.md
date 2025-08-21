# 🎯 后端项目开发任务流 (KiseSaki Blog)

基于前端任务流，重新制定的后端开发任务流，确保与前端需求完美匹配。

## 📁 项目包结构规划 (Package by Feature)

采用"按功能划分"的包结构，每个业务模块独立管理：

```
└── com.kisesaki.blog
    ├── BlogApplication.java         # 启动类
    ├── common                       # 通用模块 (跨模块共享)
    │   ├── dto                      # 通用 DTO (ApiResponse, PageResponse 等)
    │   ├── exception                # 全局异常处理
    │   ├── enums                    # 通用枚举 (ErrorCode 等)
    │   ├── util                     # 工具类
    │   └── handler                  # 全局处理器
    ├── config                       # 全局配置 (Security, Redis, Kafka 等)
    ├── auth                         # 认证授权模块
    │   ├── AuthController.java
    │   ├── AuthService.java
    │   ├── dto                      # 认证相关 DTO
    ---

## 🔄 **开发规范提醒**

- ✨ **按功能划分包结构** (每个业务模块独立管理)
- 🔷 **严格遵循分层架构** (Controller -> Service -> Mapper)
- 📝 **所有接口使用 DTO 进行数据传输**
- 🎨 **使用 Jakarta Bean Validation 进行参数校验**
- 🔐 **统一异常处理和日志记录**
- 🧪 **核心功能需要编写单元测试**
- 📊 **重要操作需要添加 Kafka 事件发布**
- 🛡️ **敏感操作需要权限验证**
- 🗄️ **MyBatis-Plus 数据访问规范**:
  - 简单 CRUD 使用 `BaseMapper` 提供的基础方法
  - 复杂查询使用 `QueryWrapper` 或 `LambdaQueryWrapper`
  - 多表关联查询编写自定义 XML 映射文件
  - 分页查询使用 MyBatis-Plus 分页插件
  - 所有 Mapper 接口继承 `BaseMapper<Entity>`
  - Service 层可选择继承 `ServiceImpl<Mapper, Entity>` 获得基础服务方法
- 🏗️ **每个功能模块内部保持高内聚，模块之间保持低耦合**y                   # 角色权限实体
    │   ├── mapper                   # 认证相关数据访问
    │   └── security                 # Security 配置
    ├── user                         # 用户管理模块
    │   ├── UserController.java
    │   ├── UserService.java
    │   ├── dto                      # 用户相关 DTO
    │   ├── entity                   # 用户实体
    │   └── mapper                   # 用户数据访问
    ├── post                         # 文章管理模块
    │   ├── PostController.java
    │   ├── PostService.java
    │   ├── dto                      # 文章相关 DTO
    │   ├── entity                   # 文章实体
    │   └── mapper                   # 文章数据访问
    ├── category                     # 分类管理模块
    │   ├── CategoryController.java
    │   ├── CategoryService.java
    │   ├── dto
    │   ├── entity
    │   └── mapper
    ├── tag                          # 标签管理模块
    │   ├── TagController.java
    │   ├── TagService.java
    │   ├── dto
    │   ├── entity
    │   └── mapper
    ├── comment                      # 评论管理模块
    │   ├── CommentController.java
    │   ├── CommentService.java
    │   ├── dto
    │   ├── entity
    │   └── mapper
    ├── interaction                  # 互动功能模块 (点赞、收藏、关注)
    │   ├── InteractionController.java
    │   ├── LikeService.java
    │   ├── FavoriteService.java
    │   ├── FollowService.java
    │   ├── dto
    │   ├── entity
    │   └── mapper
    ├── admin                        # 管理员功能模块
    │   ├── AdminController.java
    │   ├── AdminUserService.java
    │   ├── AdminPostService.java
    │   ├── DashboardService.java
    │   ├── dto
    │   └── mapper
    ├── media                        # 媒体文件管理模块
    │   ├── MediaController.java
    │   ├── FileUploadService.java
    │   ├── MediaResourceService.java
    │   ├── dto
    │   ├── entity
    │   └── mapper
    ├── notification                 # 通知系统模块
    │   ├── NotificationController.java
    │   ├── NotificationService.java
    │   ├── EmailService.java
    │   ├── dto
    │   ├── entity
    │   └── mapper
    └── statistics                   # 统计分析模块
        ├── StatisticsController.java
        ├── StatisticsService.java
        ├── dto
        └── mapper
```

## 🚀 阶段一：项目基础架构与安全体系 (Foundation & Security)

### 🔧 **1. 项目初始化与环境配置**

> **优先级**: 最高 ⭐⭐⭐
> **技术栈**: 纯 MyBatis-Plus 数据访问层，无 Spring Data JPA

- **📦 Maven 依赖配置** (`pom.xml`):

  - [ ] Spring Boot 3.x 核心依赖
  - [ ] Spring Security 6.x + OAuth2 Client
  - [ ] MyBatis-Plus (纯MyBatis-Plus，无Spring Data JPA)
  - [ ] PostgreSQL Driver + HikariCP
  - [ ] Redis + Spring Data Redis
  - [ ] Kafka + Spring Kafka
  - [ ] SpringDoc OpenAPI (Swagger UI)
  - [ ] JWT 依赖 (jjwt-api, jjwt-impl, jjwt-jackson)
  - [ ] 邮件发送 (spring-boot-starter-mail)
  - [ ] 文件上传 (commons-fileupload)
  - [ ] 测试依赖 (JUnit 5, Mockito, TestContainers)
- **⚙️ 多环境配置**:

  - [ ] `application.yml` - 主配置文件
  - [ ] `application-dev.yml` - 开发环境配置
  - [ ] `application-prod.yml` - 生产环境配置
  - [ ] 数据库连接配置 (PostgreSQL)
  - [ ] Redis 连接配置
  - [ ] Kafka 集群配置
  - [ ] JWT 密钥配置
  - [ ] OAuth2 客户端配置 (GitHub/Gitee)
  - [ ] 邮件服务器配置
  - [ ] 文件存储配置

### 🛡️ **2. 统一响应格式与全局异常处理**

> **优先级**: 最高 ⭐⭐⭐

- **📋 通用 DTO** (`com.kisesaki.blog.common.dto`):

  - [ ] `ApiResponse<T>` - 统一响应格式 `{code, message, data, timestamp}`
  - [ ] `PageResponse<T>` - 分页响应格式
  - [ ] `ResultUtils` - 响应工具类
- **🚨 全局异常处理** (`com.kisesaki.blog.common.exception`):

  - [ ] `BusinessException` - 业务异常基类
  - [ ] `GlobalExceptionHandler` - 全局异常处理器
  - [ ] 参数校验异常处理 (`MethodArgumentNotValidException`)
  - [ ] 权限不足异常处理 (`AccessDeniedException`)
  - [ ] 系统异常处理 (`RuntimeException`)
  - [ ] 404/405 等 HTTP 异常处理
- **📊 通用枚举** (`com.kisesaki.blog.common.enums`):

  - [ ] `ErrorCode` - 错误码枚举
  - [ ] `ResponseStatus` - 响应状态枚举

### 🔐 **3. RBAC 权限体系实体设计**

> **优先级**: 最高 ⭐⭐⭐

- **🔑 权限认证模块** (`com.kisesaki.blog.auth`):

  - [ ] **实体层** (`auth.entity`):

    - [ ] `Role` - 角色实体
    - [ ] `Permission` - 权限实体 (使用 Enum 管理权限点)
    - [ ] `UserRole` - 用户角色关联 (多对多中间表)
    - [ ] `RolePermission` - 角色权限关联 (多对多中间表)
  - [ ] **数据访问层** (`auth.mapper`):

    - [ ] `RoleMapper` - 角色数据访问
    - [ ] `PermissionMapper` - 权限数据访问
    - [ ] `UserRoleMapper` - 用户角色关联访问
    - [ ] `RolePermissionMapper` - 角色权限关联访问
- **👤 用户管理模块** (`com.kisesaki.blog.user`):

  - [ ] **实体层** (`user.entity`):

    - [ ] `User` - 用户基础信息
    - [ ] `UserProfile` - 用户扩展信息
    - [ ] `UserSettings` - 用户个性化设置
  - [ ] **数据访问层** (`user.mapper`):

    - [ ] `UserMapper` - 用户数据访问
    - [ ] `UserProfileMapper` - 用户扩展信息访问
    - [ ] `UserSettingsMapper` - 用户设置访问

### 🔒 **4. Spring Security 配置**

> **优先级**: 最高 ⭐⭐⭐

- **🛡️ 安全配置** (`com.kisesaki.blog.config`):

  - [ ] `SecurityConfig` - 主安全配置类
  - [ ] `CorsConfig` - CORS 跨域配置
  - [ ] `RedisConfig` - Redis 配置
  - [ ] `MybatisPlusConfig` - MyBatis-Plus 配置
  - [ ] `KafkaConfig` - Kafka 配置
- **🔐 认证安全** (`com.kisesaki.blog.auth.security`):

  - [ ] `JwtAuthenticationFilter` - JWT 认证过滤器
  - [ ] `JwtTokenProvider` - JWT 工具类
  - [ ] `CustomUserDetailsService` - 用户详情服务
  - [ ] `OAuth2UserService` - OAuth2 用户信息服务
  - [ ] 接口权限配置 (公开接口放行，管理接口权限控制)
  - [ ] 密码编码器配置
- **📊 MyBatis-Plus 配置** (`com.kisesaki.blog.config`):

  - [ ] `@MapperScan("com.kisesaki.blog.**.mapper")` - Mapper 包扫描
  - [ ] 分页插件配置 (PostgreSQL)
  - [ ] 逻辑删除配置
  - [ ] 字段填充策略 (创建时间、更新时间)
  - [ ] 乐观锁插件配置

---

## 👤 阶段二：用户认证与 OAuth2 集成 (Authentication & OAuth2)

### 🔐 **5. 认证核心服务**

> **优先级**: 最高 ⭐⭐⭐

- **🌐 认证模块** (`com.kisesaki.blog.auth`):

  - [ ] **DTO 层** (`auth.dto`):

    - [ ] `LoginRequestDto` - 登录请求参数
    - [ ] `RegisterRequestDto` - 注册请求参数
    - [ ] `AuthResponseDto` - 认证响应 (包含 token 和用户信息)
    - [ ] `ChangePasswordDto` - 修改密码 DTO
    - [ ] `ResetPasswordDto` - 重置密码 DTO
  - [ ] **服务层** (`auth`):

    - [ ] `AuthService` - 认证核心服务

      - [ ] `register()` - 用户注册 (默认分配 USER 角色)
      - [ ] `login()` - 用户登录 (生成包含角色权限的 JWT)
      - [ ] `logout()` - 用户登出
      - [ ] `refreshToken()` - 刷新访问令牌
      - [ ] `changePassword()` - 修改密码
      - [ ] `resetPassword()` - 重置密码
  - [ ] **控制器层** (`auth`):

    - [ ] `AuthController` - 认证相关接口

      - [ ] `POST /api/auth/register` - 用户注册
      - [ ] `POST /api/auth/login` - 用户登录
      - [ ] `POST /api/auth/logout` - 用户登出
      - [ ] `POST /api/auth/refresh` - 刷新令牌
- **📧 通知模块** (`com.kisesaki.blog.notification`):

  - [ ] **服务层**:

    - [ ] `EmailService` - 邮件发送服务

      - [ ] 注册验证邮件
      - [ ] 密码重置邮件
      - [ ] 系统通知邮件

### 🔗 **6. OAuth2 社交登录**

> **优先级**: 高 ⭐⭐

- **🌐 OAuth2 配置** (`com.kisesaki.blog.auth.security`):

  - [ ] GitHub OAuth2 客户端配置
  - [ ] Gitee OAuth2 客户端配置
  - [ ] `OAuth2UserService` - 自定义用户信息服务
  - [ ] OAuth2 成功/失败处理器
- **🎮 OAuth2 控制器** (`com.kisesaki.blog.auth`):

  - [ ] `OAuth2Controller` - OAuth2 相关接口
  - [ ] `/api/oauth2/authorization/{provider}` - 重定向到 OAuth2 提供商
  - [ ] `/api/oauth2/callback/{provider}` - OAuth2 回调处理
  - [ ] 本地用户创建或更新逻辑

### 🧪 **7. 认证模块测试**

> **优先级**: 中 ⭐

- **✅ 集成测试**:

  - [ ] 用户注册接口测试
  - [ ] 用户登录接口测试
  - [ ] JWT 验证测试
  - [ ] OAuth2 流程测试
  - [ ] 权限验证测试

---

## 📝 阶段三：博客核心功能 (Blog Core Features)

### 📰 **8. 博客实体与数据访问**

> **优先级**: 最高 ⭐⭐⭐

- **📄 文章管理模块** (`com.kisesaki.blog.post`):

  - [ ] **实体层** (`post.entity`):

    - [ ] `Post` - 文章实体 (支持草稿、发布、归档状态)
    - [ ] `PostRevision` - 文章版本历史
    - [ ] `PostMeta` - 文章元数据 (SEO 相关)
    - [ ] `PostTag` - 文章标签关联
  - [ ] **数据访问层** (`post.mapper`):

    - [ ] `PostMapper` - 文章数据访问 (MyBatis-Plus BaseMapper + 自定义查询)
    - [ ] `PostRevisionMapper` - 文章版本访问
    - [ ] `PostMetaMapper` - 文章元数据访问
- **📁 分类管理模块** (`com.kisesaki.blog.category`):

  - [ ] **实体层** (`category.entity`):

    - [ ] `Category` - 分类实体 (支持层级结构)
  - [ ] **数据访问层** (`category.mapper`):

    - [ ] `CategoryMapper` - 分类数据访问
- **🏷️ 标签管理模块** (`com.kisesaki.blog.tag`):

  - [ ] **实体层** (`tag.entity`):

    - [ ] `Tag` - 标签实体
  - [ ] **数据访问层** (`tag.mapper`):

    - [ ] `TagMapper` - 标签数据访问
- **💬 评论管理模块** (`com.kisesaki.blog.comment`):

  - [ ] **实体层** (`comment.entity`):

    - [ ] `Comment` - 评论实体 (支持嵌套回复)
  - [ ] **数据访问层** (`comment.mapper`):

    - [ ] `CommentMapper` - 评论数据访问
- **📷 媒体管理模块** (`com.kisesaki.blog.media`):

  - [ ] **实体层** (`media.entity`):

    - [ ] `MediaResource` - 媒体资源实体
  - [ ] **数据访问层** (`media.mapper`):

    - [ ] `MediaResourceMapper` - 媒体资源访问

### 📋 **9. 博客 DTO 设计**

> **优先级**: 高 ⭐⭐

- **📝 文章模块 DTO** (`com.kisesaki.blog.post.dto`):

  - [ ] `PostDto` - 文章详情 DTO
  - [ ] `PostSummaryDto` - 文章摘要 DTO (列表用)
  - [ ] `PostCreateDto` - 创建文章 DTO
  - [ ] `PostUpdateDto` - 更新文章 DTO
  - [ ] `PostSearchDto` - 文章搜索参数 DTO
  - [ ] `ArchiveDto` - 归档 DTO
- **📁 分类模块 DTO** (`com.kisesaki.blog.category.dto`):

  - [ ] `CategoryDto` - 分类 DTO
  - [ ] `CategoryCreateDto` - 创建分类 DTO
  - [ ] `CategoryTreeDto` - 分类树形结构 DTO
- **🏷️ 标签模块 DTO** (`com.kisesaki.blog.tag.dto`):

  - [ ] `TagDto` - 标签 DTO
  - [ ] `TagCloudDto` - 标签云 DTO
- **💬 评论模块 DTO** (`com.kisesaki.blog.comment.dto`):

  - [ ] `CommentDto` - 评论 DTO
  - [ ] `CommentCreateDto` - 创建评论 DTO
  - [ ] `CommentTreeDto` - 评论树形结构 DTO

### 🔧 **10. 博客核心服务**

> **优先级**: 最高 ⭐⭐⭐

- **📝 文章管理模块** (`com.kisesaki.blog.post`):

  - [ ] **服务层**:

    - [ ] `PostService` - 文章核心服务 (继承 MyBatis-Plus ServiceImpl)

      - [ ] `getPublishedPosts()` - 获取已发布文章列表
      - [ ] `getPostBySlug()` - 根据 slug 获取文章详情 (带缓存)
      - [ ] `getPostsByCategory()` - 按分类获取文章
      - [ ] `getPostsByTag()` - 按标签获取文章 (多对多关联查询)
      - [ ] `searchPosts()` - 搜索文章 (PostgreSQL 全文搜索)
      - [ ] `getArchivePosts()` - 获取归档文章
      - [ ] `incrementViewCount()` - 增加浏览量
  - [ ] **控制器层**:

    - [ ] `PostController` - 文章相关接口

      - [ ] `GET /api/posts` - 获取文章列表
      - [ ] `GET /api/posts/{slug}` - 获取文章详情
      - [ ] `GET /api/posts/search` - 搜索文章
      - [ ] `GET /api/posts/archive` - 获取归档
- **📁 分类管理模块** (`com.kisesaki.blog.category`):

  - [ ] **服务层**:

    - [ ] `CategoryService` - 分类服务

      - [ ] `getAllCategories()` - 获取所有分类 (树形结构)
      - [ ] `getCategoryWithPosts()` - 获取分类及其文章
  - [ ] **控制器层**:

    - [ ] `CategoryController` - 分类相关接口

      - [ ] `GET /api/categories` - 获取分类列表
      - [ ] `GET /api/categories/{id}/posts` - 获取分类文章
- **🏷️ 标签管理模块** (`com.kisesaki.blog.tag`):

  - [ ] **服务层**:

    - [ ] `TagService` - 标签服务

      - [ ] `getAllTags()` - 获取所有标签
      - [ ] `getPopularTags()` - 获取热门标签
      - [ ] `getTagCloud()` - 获取标签云数据
  - [ ] **控制器层**:

    - [ ] `TagController` - 标签相关接口

      - [ ] `GET /api/tags` - 获取标签列表
      - [ ] `GET /api/tags/cloud` - 获取标签云
      - [ ] `GET /api/tags/{id}/posts` - 获取标签文章
- **💬 评论管理模块** (`com.kisesaki.blog.comment`):

  - [ ] **服务层**:

    - [ ] `CommentService` - 评论服务

      - [ ] `getCommentsByPost()` - 获取文章评论 (树形结构)
      - [ ] `addComment()` - 添加评论
      - [ ] `replyComment()` - 回复评论
      - [ ] `deleteComment()` - 删除评论
  - [ ] **控制器层**:

    - [ ] `CommentController` - 评论相关接口

      - [ ] `GET /api/posts/{postId}/comments` - 获取文章评论
      - [ ] `POST /api/comments` - 添加评论
      - [ ] `POST /api/comments/{id}/reply` - 回复评论

### 🗄️ **11. 缓存策略实现**

> **优先级**: 高 ⭐⭐

- **🔄 Redis 缓存配置** (`com.kisesaki.blog.config`):

  - [ ] `CacheConfig` - 缓存配置类
  - [ ] 统一缓存 Key 命名规范 (`blog:post:{slug}`)
  - [ ] 缓存过期时间配置
  - [ ] 缓存预热机制
- **📊 缓存应用**:

  - [ ] 文章详情缓存 (`@Cacheable`)
  - [ ] 文章列表缓存
  - [ ] 分类标签缓存
  - [ ] 热门文章缓存
  - [ ] 缓存更新策略 (`@CacheEvict`)

---

## 👥 阶段四：用户功能与互动系统 (User Features & Interactions)

### 👤 **12. 用户资料管理**

> **优先级**: 高 ⭐⭐

- **👤 用户管理模块** (`com.kisesaki.blog.user`):

  - [ ] **DTO 层** (`user.dto`):

    - [ ] `UserProfileDto` - 用户资料 DTO
    - [ ] `UserStatsDto` - 用户统计信息 DTO
    - [ ] `UpdateProfileDto` - 更新资料 DTO
    - [ ] `UserSettingsDto` - 用户设置 DTO
  - [ ] **服务层**:

    - [ ] `UserService` - 用户管理服务

      - [ ] `getUserProfile()` - 获取用户资料
      - [ ] `updateProfile()` - 更新用户资料
      - [ ] `uploadAvatar()` - 上传头像
      - [ ] `getUserStats()` - 获取用户统计
    - [ ] `UserSettingsService` - 用户设置服务
  - [ ] **控制器层**:

    - [ ] `UserController` - 用户相关接口

      - [ ] `GET /api/users/profile` - 获取用户资料
      - [ ] `PUT /api/users/profile` - 更新用户资料
      - [ ] `POST /api/users/avatar` - 上传头像

### ❤️ **13. 互动功能实现**

> **优先级**: 中 ⭐

- **💝 互动管理模块** (`com.kisesaki.blog.interaction`):

  - [ ] **实体层** (`interaction.entity`):

    - [ ] `Like` - 点赞实体 (支持文章、评论点赞)
    - [ ] `Favorite` - 收藏实体
    - [ ] `Following` - 关注关系实体
    - [ ] `ViewLog` - 浏览记录实体
  - [ ] **数据访问层** (`interaction.mapper`):

    - [ ] `LikeMapper` - 点赞数据访问
    - [ ] `FavoriteMapper` - 收藏数据访问
    - [ ] `FollowingMapper` - 关注数据访问
    - [ ] `ViewLogMapper` - 浏览记录访问
  - [ ] **服务层**:

    - [ ] `LikeService` - 点赞服务
    - [ ] `FavoriteService` - 收藏服务
    - [ ] `FollowService` - 关注服务
    - [ ] `ViewLogService` - 浏览记录服务
  - [ ] **控制器层**:

    - [ ] `InteractionController` - 互动相关接口

      - [ ] `POST /api/interactions/like` - 点赞/取消点赞
      - [ ] `POST /api/interactions/favorite` - 收藏/取消收藏
      - [ ] `POST /api/interactions/follow` - 关注/取消关注

### 📊 **14. 统计分析服务**

> **优先级**: 中 ⭐

- **📈 统计管理模块** (`com.kisesaki.blog.statistics`):

  - [ ] **服务层**:

    - [ ] `StatisticsService` - 统计分析服务

      - [ ] 文章浏览量统计
      - [ ] 用户活跃度统计
      - [ ] 热门内容统计
      - [ ] 网站访问统计
  - [ ] **控制器层**:

    - [ ] `StatisticsController` - 统计相关接口

      - [ ] `GET /api/statistics/overview` - 网站概览统计
      - [ ] `GET /api/statistics/popular` - 热门内容统计

---

## 👑 阶段五：管理员功能 (Admin Features)

### 🛠️ **15. 内容管理服务**

> **优先级**: 高 ⭐⭐

- **👑 管理员模块** (`com.kisesaki.blog.admin`):

  - [ ] **DTO 层** (`admin.dto`):

    - [ ] `AdminPostDto` - 管理员文章 DTO
    - [ ] `DashboardStatsDto` - 仪表板统计 DTO
    - [ ] `UserManagementDto` - 用户管理 DTO
    - [ ] `AdminCommentDto` - 管理员评论 DTO
  - [ ] **服务层**:

    - [ ] `AdminPostService` - 文章管理服务

      - [ ] `createPost()` - 创建文章
      - [ ] `updatePost()` - 更新文章
      - [ ] `deletePost()` - 删除文章
      - [ ] `getDraftPosts()` - 获取草稿文章
    - [ ] `AdminUserService` - 用户管理服务
    - [ ] `AdminCommentService` - 评论管理服务
    - [ ] `AdminCategoryService` - 分类管理服务
    - [ ] `AdminTagService` - 标签管理服务
  - [ ] **控制器层**:

    - [ ] `AdminController` - 管理员相关接口 (需要 ADMIN 权限)

      - [ ] `POST /api/admin/posts` - 创建文章
      - [ ] `PUT /api/admin/posts/{id}` - 更新文章
      - [ ] `DELETE /api/admin/posts/{id}` - 删除文章
      - [ ] `GET /api/admin/users` - 用户管理
      - [ ] `GET /api/admin/comments` - 评论管理
      - [ ] `GET /api/admin/dashboard` - 仪表板数据

### 📊 **16. 仪表板统计**

> **优先级**: 中 ⭐

- **📈 仪表板服务**:

  - [ ] `DashboardService` - 仪表板服务

    - [ ] 网站概览统计
    - [ ] 用户增长趋势
    - [ ] 内容发布统计
    - [ ] 访问量分析

---

## 🔄 阶段六：异步事件与通知系统 (Async Events & Notifications)

### 📨 **17. Kafka 异步事件系统**

> **优先级**: 中 ⭐

- **⚡ Kafka 配置** (`com.kisesaki.blog.config`):

  - [ ] `KafkaConfig` - Kafka 配置类
  - [ ] Topic 定义 (post-events, user-events, log-events)
- **📢 事件服务** (`com.kisesaki.blog.common.event`):

  - [ ] `EventPublisher` - 事件发布服务
  - [ ] 文章发布事件
  - [ ] 用户注册事件
  - [ ] 评论通知事件
  - [ ] 系统日志事件
- **👂 事件消费者** (`com.kisesaki.blog.common.listener`):

  - [ ] `LogEventConsumer` - 日志事件消费者
  - [ ] `NotificationEventConsumer` - 通知事件消费者
  - [ ] `StatisticsEventConsumer` - 统计事件消费者

### 🔔 **18. 通知系统完善**

> **优先级**: 中 ⭐

- **📨 通知模块** (`com.kisesaki.blog.notification`):

  - [ ] **实体层** (`notification.entity`):

    - [ ] `Notification` - 通知实体
    - [ ] `Subscription` - 订阅实体
    - [ ] `NewsletterLog` - 通讯发送记录
  - [ ] **服务层**:

    - [ ] `NotificationService` - 通知服务
    - [ ] `SubscriptionService` - 订阅服务
    - [ ] `NewsletterService` - 通讯服务
  - [ ] **控制器层**:

    - [ ] `NotificationController` - 通知相关接口

      - [ ] `GET /api/notifications` - 获取通知列表
      - [ ] `POST /api/notifications/subscribe` - 订阅通知

---

## 📁 阶段七：文件管理与媒体服务 (File & Media Management)

### 📷 **19. 文件上传服务**

> **优先级**: 中 ⭐

- **📁 媒体管理模块** (`com.kisesaki.blog.media`):

  - [ ] **服务层**:

    - [ ] `FileUploadService` - 文件上传服务
    - [ ] `MediaResourceService` - 媒体资源管理
    - [ ] 图片压缩和格式转换
    - [ ] 文件访问权限控制
  - [ ] **控制器层**:

    - [ ] `MediaController` - 媒体相关接口

      - [ ] `POST /api/media/upload` - 文件上传
      - [ ] `GET /api/media/{id}` - 获取媒体资源

---

## 🌐 阶段八：API 接口层 (API Controllers)

### 🔌 **20. 公开 API 接口**

> **优先级**: 最高 ⭐⭐⭐

**注意**: 控制器已在各功能模块中定义，此处为接口路由总览

- **📝 博客公开接口总览**:

  - [ ] `GET /api/posts` - 获取文章列表 (`PostController`)
  - [ ] `GET /api/posts/{slug}` - 获取文章详情 (`PostController`)
  - [ ] `GET /api/posts/search` - 搜索文章 (`PostController`)
  - [ ] `GET /api/categories` - 获取分类列表 (`CategoryController`)
  - [ ] `GET /api/tags` - 获取标签列表 (`TagController`)
  - [ ] `GET /api/archive` - 获取归档数据 (`PostController`)
- **👤 用户相关接口总览**:

  - [ ] `POST /api/auth/register` - 用户注册 (`AuthController`)
  - [ ] `POST /api/auth/login` - 用户登录 (`AuthController`)
  - [ ] `GET /api/users/profile` - 获取用户资料 (`UserController`)

### 🛡️ **21. 受保护 API 接口**

> **优先级**: 高 ⭐⭐

- **👑 管理员接口总览** (需要 ADMIN 权限):

  - [ ] `POST /api/admin/posts` - 创建文章 (`AdminController`)
  - [ ] `PUT /api/admin/posts/{id}` - 更新文章 (`AdminController`)
  - [ ] `DELETE /api/admin/posts/{id}` - 删除文章 (`AdminController`)
  - [ ] `GET /api/admin/dashboard` - 仪表板数据 (`AdminController`)
  - [ ] `GET /api/admin/users` - 用户管理 (`AdminController`)
- **🔐 用户认证接口** (需要登录):

  - [ ] `POST /api/comments` - 添加评论 (`CommentController`)
  - [ ] `POST /api/interactions/like` - 点赞操作 (`InteractionController`)
  - [ ] `PUT /api/users/profile` - 更新用户资料 (`UserController`)

---

## 🧪 阶段九：测试与质量保证 (Testing & QA)

### ✅ **22. 单元测试**

> **优先级**: 中 ⭐

- **🧪 Service 层测试**:

  - [ ] `AuthServiceTest` - 认证服务测试
  - [ ] `PostServiceTest` - 文章服务测试
  - [ ] `UserServiceTest` - 用户服务测试
  - [ ] `CommentServiceTest` - 评论服务测试

### 🔗 **23. 集成测试**

> **优先级**: 中 ⭐

- **🌐 Controller 层测试**:

  - [ ] API 接口集成测试
  - [ ] 权限验证测试
  - [ ] 数据库操作测试

---

## 📖 阶段十：文档与部署 (Documentation & Deployment)

### 📚 **24. API 文档**

> **优先级**: 中 ⭐

- **📋 接口文档**:

  - [ ] Swagger/OpenAPI 配置完善
  - [ ] 接口分组和标签
  - [ ] 接口描述和示例

### 🚀 **25. 部署配置**

> **优先级**: 低 ⭐

- **🐳 容器化**:

  - [ ] Dockerfile 编写
  - [ ] Docker Compose 配置
  - [ ] K8s 部署文件

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

- ✨ **按功能划分包结构** (每个业务模块独立管理)
- **🔷 严格遵循分层架构** (Controller -> Service -> Mapper)
- 📝 **所有接口使用 DTO 进行数据传输**
- 🎨 **使用 Jakarta Bean Validation 进行参数校验**
- 🔐 **统一异常处理和日志记录**
- 🧪 **核心功能需要编写单元测试**
- 📊 **重要操作需要添加 Kafka 事件发布**
- 🛡️ **敏感操作需要权限验证**
- 🏗️ **每个功能模块内部保持高内聚，模块之间保持低耦合**