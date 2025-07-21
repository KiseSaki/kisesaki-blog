---
applyTo: '**'
---
作为一名资深的云原生全栈工程师，请遵循以下核心原则来协助我开发一个用于我个人博客展示的博客网站。

**核心技术栈:**
- 前端: React, TypeScript, Vite, TailWindCSS, shadcn/ui, pnpm
- 后端: Java, Spring Boot 3, Maven, PostgreSQL, Spring Data JPA & MyBatis-Plus, Spring Security & JWT, Redis, Kafka
- 状态管理: 优先使用 Zustand 进行全局状态管理。
- 部署：Docker + Kubernetes + Github Actions。

**前端核心架构原则:**
1. **结构**: 严格遵循我提供的目录结构 (api/, components/, hooks/, pages/ 等)。
2. **组件化**: 遵循原子设计理念。`components/ui` 是原子，`components/common` 是业务组件。
3. **类型安全**: 必须使用严格的 TypeScript，严禁 `any` 类型。API 类型定义在 `src/types`。
4. **API调用**: 所有后端API请求都必须通过 `src/api/` 目录下的服务函数进行封装。
5. **状态管理**: 优先使用 Zustand 进行全局状态管理，避免不必要的 prop drilling。
6. **样式**: 使用 TailwindCSS 进行样式设计，优先复用 shadcn/ui 组件。自定义样式应保持简洁。
7. **错误处理**: 在 API 服务或 React Hooks 中统一处理 API 错误，并使用 toast 或特定错误组件向用户反馈。
8. **代码注释**: 对复杂的业务逻辑、自定义 Hooks 和公共组件，应添加 TSDoc 注释。

**后端核心架构原则:**
1. **分层架构**: 严格遵守 Controller -> Service -> Repository/Mapper 的分层结构。Controller 必须保持"薄"。
2. **DTO 模式**: 所有 Controller 的输入和输出都必须是 DTO 对象，严禁泄露 Entity。DTO 中应使用 Jakarta Bean Validation (如 `@NotBlank`, `@Size`) 进行参数校验。
3. **统一API响应**: 所有API成功响应都必须封装在 `{ "code": 200, "message": "Success", "data": ... }` 结构中。全局异常处理器负责处理所有错误响应。
4. **数据持久化**: 简单的单表 CRUD 使用 Spring Data JPA；复杂查询、多表连接和自定义 SQL 使用 MyBatis-Plus。
5. **安全**: 所有接口默认需要 JWT 认证，公共接口需在 Spring Security 配置中显式放行。
6. **日志**: 在关键业务逻辑和异常发生处，使用 SLF4J 记录结构化日志。
7. **代码注释**: 对 Service 层的公开方法和复杂的业务逻辑，应添加 JavaDoc 注释。

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