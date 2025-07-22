## 后端项目任务流
### 🚀 **阶段一：项目基础与安全架构 (Foundation &amp; Security)**
* [ ] **1. 搭建 RBAC 实体与 Repository**
  * **路径**: `.../entity/`, `.../repository/`
  * **任务**:
    * [ ] 创建 `User`, `Role`, `Permission` 实体，并建立正确的 `@ManyToMany` 关系。
    * [ ] 在 `Permission` 中使用 `Enum` 类型管理权限点（如 `POST:CREATE`, `POST:DELETE`）。
    * [ ] 创建对应的 `UserRepository`, `RoleRepository`, `PermissionRepository`。
* [ ] **2. 配置 Spring Security (含 RBAC)**
  * **文件**: `.../config/SecurityConfig.java`
  * **任务**:
    * [ ] 配置 `SecurityFilterChain`，对不同路径（如 `/api/admin/**`）使用 `.hasRole()` 或 `.hasAuthority()` 进行接口级权限控制。
    * [ ] 公共接口（登录、注册、文章列表/详情）需显式放行。
    * [ ] 集成 `JwtAuthFilter`。
* [ ] **3. 实现统一 API 响应与全局异常处理**
  * **文件**: `.../dto/ApiResponse.java`, `.../handler/GlobalExceptionHandler.java`
  * **任务**:
    * [ ] 创建 `ApiResponse<T>` 泛型类，封装 `{ code, message, data }` 结构。
    * [ ] 创建 `@RestControllerAdvice` 注解的全局异常处理器，统一处理 `MethodArgumentNotValidException` (参数校验失败)、`AccessDeniedException` (权限不足) 等，并返回标准的 `ApiResponse` 格式。
* [ ] **4. 配置多环境与 API 文档**
  * **任务**:
    * [ ] 创建 `application-dev.yml` 和 `application-prod.yml`，管理不同环境的数据库、Redis、Kafka 配置。
    * [ ] 集成 `springdoc-openapi`，配置 Swagger UI，为所有 Controller 生成接口文档。

### 👤 **阶段二：用户认证与 OAuth2 (User Authentication &amp; OAuth2)**
* [ ] **5. 实现 AuthService (含 RBAC)**
  * **文件**: `.../service/AuthService.java`
  * **任务**:
    * [ ] **注册**: 创建新用户时，查询默认的 `USER` 角色并与之关联。
    * [ ] **登录**: 登录成功后，生成的 JWT 的 claims 中应包含用户的角色和权限信息。
* [ ] **6. 实现 OAuth2 登录流程**
  * **路径**: `.../config/`, `.../controller/`, `.../service/`
  * **任务**:
    * [ ] 使用 `spring-boot-starter-oauth2-client` 集成 GitHub/Gitee 登录。
    * [ ] 创建一个 `OAuth2Controller`，提供一个 `/oauth2/authorization/{provider}` 端点用于重定向到服务商。
    * [ ] 实现自定义的 `OAuth2UserService`，在获取到用户信息后，在本地数据库中创建或更新用户，并生成应用的 JWT 返回给前端。
* [ ] **7. 编写核心接口集成测试**
  * **路径**: `src/test/java/.../controller/`
  * **任务**:
    * [ ] 使用 `@SpringBootTest` 和 `MockMvc` 为 `/api/auth/register` 和 `/api/auth/login` 端点编写集成测试，验证参数校验、业务逻辑和安全配置是否正确。

### 📝 **阶段三：文章管理、缓存与异步事件 (Post, Cache &amp; Async Events)**
* [ ] **8. 实现 PostService (含缓存)**
  * **文件**: `.../service/PostService.java`
  * **任务**:
    * [ ] 在 `getPostBySlug` 方法上使用 `@Cacheable(value = "posts", key = "#slug")` 注解，缓存文章详情。
    * [ ] 在 `updatePost` 和 `deletePost` 方法上使用 `@CacheEvict` 注解，确保缓存同步。
    * [ ] 建立统一的 Redis Key 命名规范，如 `blog:post:{slug}`。
* [ ] **9. 实现 Kafka 异步事件**
  * **路径**: `.../config/`, `.../service/`, `.../consumer/`
  * **任务**:
    * [ ] 创建 `KafkaProducerService`，用于发送事件。
    * [ ] 在 `PostService` 的 `createPost` 方法成功后，调用 `kafkaProducerService.send("post-created-event", newPost)` 发送消息。
    * [ ] 创建 `LoggingConsumer`，使用 `@KafkaListener(topics = "post-created-event")` 注解监听消息，并将事件信息记录到日志中。
* [ ] **10. 创建日志接收端点**
  * **文件**: `.../controller/LogController.java`
  * **任务**:
    * [ ] 创建 `POST /api/logs` 端点，接收前端上报的用户行为日志。
    * [ ] 将接收到的日志数据发送到专用的 Kafka topic (如 `frontend-logs`) 进行后续处理。