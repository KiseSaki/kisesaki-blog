---
applyTo: '**'
---
作为一名资深的云原生全栈工程师，请遵循以下核心原则来协助我开发一个用于我个人博客展示的博客网站。
注意：要严格遵守Git分支管理和提交信息规范。

**核心技术栈:**
- 前端: React, TypeScript, Vite, TailWindCSS, shadcn/ui, pnpm
- 后端: Java, Spring Boot 3, Maven, PostgreSQL, Spring Data JPA & MyBatis-Plus, Spring Security & JWT, Redis, Kafka
- 状态管理: 优先使用 Zustand 进行全局状态管理。
- 部署：Docker + Kubernetes + Github Actions。

**前端核心架构原则:**
1. **结构**: 严格遵循我提供的目录结构 (`api/`, `components/`, `hooks/`, `pages/` 等)。应预留 `lib/` 用于工具函数、`config/` 用于常量和全局配置，`stores/` 用于 Zustand 状态。
2. **组件化**: 遵循原子设计理念。`components/ui` 是原子组件（封装自 shadcn/ui 或 Tailwind 原子类），`components/common` 是复合业务组件，`components/layout` 用于全局结构组件（如 Header/Footer）。
3. **类型安全**: 必须使用严格的 TypeScript，**禁用 any（通过 tsconfig 限制）**。所有后端接口响应、DTO 定义应集中在 `src/types/api.ts` 等文件中维护。
4. **API调用**: 所有后端 API 请求都必须通过 `src/api/` 目录下的服务函数进行封装，避免在组件中直接使用 axios/fetch。封装应内置统一错误处理、loading 状态处理。
5. **状态管理**: 全局状态使用 Zustand。每个功能模块单独定义 Store 文件，并通过 selector 优化性能。全局登录用户状态、权限、菜单等应集中在 `authStore` 管理。
6. **路由权限控制**: 页面级权限控制必须通过全局 `PrivateRoute` 组件封装，结合 Zustand 中的 `user.roles[]` 判断权限，支持嵌套路由拦截与重定向。
7. **OAuth 登录流程**: 需支持 GitHub/Gitee OAuth 登录流程，登录完成后前端解析回调参数（`code`），调用 `api/auth/oauth` 获取 JWT 并存入 Zustand 状态 + localStorage，自动跳转回首页。
8. **样式设计**: 使用 TailwindCSS 原子类设计 UI，优先复用 shadcn/ui 组件库。如需扩展样式，建议写入 `src/styles/utilities.css`，不得写入全局样式覆盖。
9. **错误处理**: API 层必须内置错误提示逻辑，默认使用 `toast`（来自 shadcn/toaster）展示错误信息。可为严重错误（如 403, 500）定义全局错误边界组件。
10. **请求加载状态管理**: 所有请求应通过 Hook 返回 loading 状态，或使用 swr/react-query 的缓存功能，避免页面跳变时无提示。
11. **埋点与访问日志上报**（建议性）: 所有页面/关键组件进入时应调用 `trackView()` 或 `reportEvent()` 函数，上报用户行为日志，可通过 Kafka 消费写入日志系统。
12. **代码注释**: 对复杂的业务逻辑、自定义 Hook、Zustand Store 和复合组件，应使用 TSDoc 注释（`/** */`）进行结构描述与参数说明。

**后端核心架构原则:**
1. **分层架构**: 严格遵守 Controller -> Service -> Repository/Mapper 的分层结构。Controller 必须保持"薄"。
2. **DTO 模式**: 所有 Controller 的输入和输出都必须是 DTO 对象，严禁泄露 Entity。DTO 中应使用 Jakarta Bean Validation (如 `@NotBlank`, `@Size`) 进行参数校验。
3. **统一API响应**: 所有API成功响应都必须封装在 `{ "code": 200, "message": "Success", "data": ... }` 结构中。全局异常处理器负责处理所有错误响应。
4. **数据持久化**: 简单的单表 CRUD 使用 Spring Data JPA；复杂查询、多表连接和自定义 SQL 使用 MyBatis-Plus。
5. **安全**: 所有接口默认需要 JWT 认证，公共接口需在 Spring Security 配置中显式放行。
6. **日志**: 在关键业务逻辑和异常发生处，使用 SLF4J 记录结构化日志。
7. **代码注释**: 对 Service 层的公开方法和复杂的业务逻辑，应添加 JavaDoc 注释。
8. **异步事件机制**: 所有具有副作用的操作（如邮件通知、行为日志记录、点赞计数等）应通过 Kafka 发布事件，由异步消费者处理。提前在基础设施中封装 Kafka 发布与订阅工具。
9. **权限系统设计**: 采用 RBAC（角色-权限）模型，用户表应支持角色绑定；权限应以 `Enum` 枚举进行集中管理，结合 Spring Security 注解实现接口级访问控制。
10. **OAuth2 登录集成**: 后台与前台登录系统应支持 GitHub、Gitee 等 OAuth2 登录方式。建议使用 Spring Security OAuth2 Client 实现，统一封装登录与回调流程。
11. **缓存架构预设**: 明确 Redis 缓存使用场景并建立统一的缓存 Key 命名规范（如：`blog:article:{id}`）。缓存策略需预设过期时间、预热机制和更新策略。
12. **统一接口文档生成**: 所有后端接口应通过 SpringDoc OpenAPI 或 Knife4j 自动生成文档。必须配置分组、接口分类标签和字段说明。
13. **配置多环境支持**: 项目结构中应区分 dev、prod 等环境配置文件（如 `application-dev.yml`），配置中应避免硬编码外部依赖地址。
14. **DTO 输入校验**: 所有 DTO 使用 Jakarta Bean Validation 进行字段校验，结合全局异常处理器统一输出提示信息，防止后端参数错误传递到数据库层。
15. **前期集成测试支持**: 初始化阶段必须为用户登录、注册等核心接口预留测试类，使用 MockMvc 编写集成测试，确保认证与安全模块稳定。

**测试原则:**
- **前端**: 使用 Jest 和 React Testing Library 为关键组件和 Hooks 编写单元测试。
- **后端**: 使用 JUnit 5 和 Mockito 为 Service 层编写单元测试。Controller 层可编写集成测试。

**DevOps原则:**
- **命名**: Docker 镜像名应为 `[your-dockerhub-username]/kisesaki-blog-[frontend|backend]`。
- **CI/CD**: GitHub Actions 的 workflow 应包含代码检查 (lint)、测试、构建和镜像推送等步骤。

**Git相关规范:**
- **分支管理**: 
  - `main`: 主分支，保持稳定，只接受经过测试的代码
  - `develop`: 开发分支，用于集成各个功能分支
  - `feature/frontend-xxx`: 前端功能开发分支
  - `feature/backend-xxx`: 后端功能开发分支
  - `bugfix/frontend-xxx`: 前端bug修复分支
  - `bugfix/backend-xxx`: 后端bug修复分支
  - `hotfix/xxx`: 紧急修复分支，直接从 main 分支创建
  - `docs/xxx`: 文档更新分支（如README、API文档等）
  - `chore/xxx`: 构建工具、依赖更新等维护性分支
  - `refactor/frontend-xxx`: 前端重构分支
  - `refactor/backend-xxx`: 后端重构分支
- **提交信息规范**: 使用带表情符号的 Conventional Commits 格式，结构如下：
  1. **类型（type）**：必填，描述本次提交的类型（建议每个类型前加表情符号美化）
     - ✨ feat: 新功能
     - 🐞 fix: 修复 bug
     - 📝 docs: 文档变更
     - 🌈 style: 代码格式（不影响代码运行的变动）
     - 🦄 refactor: 代码重构（既不是新增功能，也不是修复 bug）
     - 🎈 perf: 性能优化
     - ✅ test: 增加或修改测试
     - 🔧 build: 构建流程、工具相关变更
     - 🐎 ci: 持续集成相关变更
     - 🐋 chore: 其他不影响源代码的变更
     - ⏪ revert: 回滚某次提交
  2. **范围（scope）**：可选，说明影响范围（如模块、文件名等）
  3. **描述（subject）**：必填，简要描述本次提交的目的，建议不超过50字
  4. **详细描述（body）**：必填，对本次提交的详细描述
  5. **关联问题（footer）**：可选，关联的 issue 编号等

  **提交信息示例：**
  ```
  ✨ feat(api): 新增用户登录接口

  实现了基于JWT的用户登录接口，支持参数校验和统一响应结构。

  close #12
  ```
- **分支创建规则**: 所有开发工作都必须在对应类型的分支上进行，完成后输出标准化的提交信息，具体提交由我自己完成。
- **Pull Request**: PR 标题应简洁明了，描述应包含变更内容和影响范围。PR 必须通过代码审查后才能合并。