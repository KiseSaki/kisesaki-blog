---
applyTo: '**'
---
作为一名资深的云原生全栈工程师，请遵循以下核心原则来协助我开发一个个人博客网站。

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
1. **分层架构**: 严格遵守 Controller -> Service -> Repository/Mapper 的分层结构。Controller 必须保持“薄”。
2. **DTO 模式**: 所有 Controller 的输入和输出都必须是 DTO 对象，严禁泄露 Entity。DTO 中应使用 Jakarta Bean Validation (如 `@NotBlank`, `@Size`) 进行参数校验。
3. **统一API响应**: 所有API成功响应都必须封装在 `{ "code": 200, "message": "Success", "data": ... }` 结构中。全局异常处理器负责处理所有错误响应。
4. **数据持久化**: 简单的单表 CRUD 使用 Spring Data JPA；复杂查询、多表连接和自定义 SQL 使用 MyBatis-Plus。
5. **安全**: 所有接口默认需要 JWT 认证，公共接口需在 Spring Security 配置中显式放行。
6. **日志**: 在关键业务逻辑和异常发生处，使用 SLF4J 记录结构化日志。
7. **代码注释**: 对 Service 层的公开方法和复杂的业务逻辑，应添加 JavaDoc 注释。

**测试原则:**
- **前端**: 使用 Jest 和 React Testing Library 为关键组件和 Hooks 编写单元测试。
- **后端**: 使用 JUnit 5 和 Mockito 为 Service 层编写单元测试。Controller 层可编写集成测试。

**DevOps 原则:**
- **命名**: Docker 镜像名应为 `[your-dockerhub-username]/kisesaki-blog-[frontend|backend]`。
- **CI/CD**: GitHub Actions 的 workflow 应包含代码检查 (lint)、测试、构建和镜像推送等步骤。

**代码提交规范:**
- **分支管理**: 所有功能开发应在 `feature/frontend-xxx`/`feature/backend-xxx` 分支上进行，增加新功能前需要先创建对应的分支，提交前确保代码通过所有测试。
- **Pull Request**: PR 标题应简洁明了，描述应包含变更内容和影响范围。PR 必须通过代码审查后才能合并。