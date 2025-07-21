---
mode: agent
description: "这是一个为 AI Agent 设计的后端项目任务清单。每项任务都是一个具体的、可执行的编码步骤，旨在指导 Agent 高效、准确地生成代码。"
---

### 🚀 **阶段一：项目基础与安全架构 (Foundation & Security)**

- [ ] **1. 定义 JPA 实体**
    - **路径**: `src/main/java/com/kisesaki/blog/entity/`
    - **任务**:
        - [ ] 创建 `User.java`, `Role.java`, `Permission.java` 实体类。
        - [ ] 使用 `@Entity`, `@Table`, `@Id`, `@Column`, `@ManyToMany` 等 JPA 注解，严格按照 `database-schema.md` 定义字段和关系。
        - [ ] 创建一个 `BaseEntity.java`，包含 `createdAt` 和 `updatedAt` 字段，并使用 `@MappedSuperclass`。

- [ ] **2. 创建 JPA Repository**
    - **路径**: `src/main/java/com/kisesaki/blog/repository/`
    - **任务**:
        - [ ] 创建 `UserRepository.java` 接口，继承 `JpaRepository<User, Long>`。
        - [ ] 在 `UserRepository` 中添加 `Optional<User> findByUsername(String username)` 方法。
        - [ ] 创建 `RoleRepository.java` 接口。

- [ ] **3. 配置 Spring Security**
    - **文件**: `src/main/java/com/kisesaki/blog/config/SecurityConfig.java`
    - **任务**:
        - [ ] 创建 `SecurityFilterChain` bean。
        - [ ] 配置 `http.authorizeHttpRequests()`，使用 `.requestMatchers("/api/auth/**").permitAll()` 放行公共端点，其他所有请求 `.anyRequest().authenticated()`。
        - [ ] 定义 `PasswordEncoder` bean，返回 `BCryptPasswordEncoder` 实例。
        - [ ] 定义 `AuthenticationManager` bean。

- [ ] **4. 实现 JWT 工具类与过滤器**
    - **文件**: `src/main/java/com/kisesaki/blog/util/JwtUtil.java`
    - **任务**:
        - [ ] 创建 `generateToken(UserDetails userDetails)` 方法。
        - [ ] 创建 `validateToken(String token, UserDetails userDetails)` 方法。
        - [ ] 创建 `extractUsername(String token)` 方法。
    - **文件**: `src/main/java/com/kisesaki/blog/filter/JwtAuthFilter.java`
    - **任务**:
        - [ ] 继承 `OncePerRequestFilter`。
        - [ ] 在 `doFilterInternal` 中，从请求头解析 JWT，验证 token，如果有效，则从数据库加载 `UserDetails` 并创建 `UsernamePasswordAuthenticationToken`，最后设置到 `SecurityContextHolder`。
        - [ ] 在 `SecurityConfig` 中将此过滤器添加到 `UsernamePasswordAuthenticationFilter` 之前。

### 👤 **阶段二：用户认证 API (User Authentication API)**

- [ ] **5. 创建 DTOs**
    - **路径**: `src/main/java/com/kisesaki/blog/dto/`
    - **任务**:
        - [ ] 创建 `RegisterRequestDto.java` 和 `LoginRequestDto.java`，使用 `@NotBlank` 等 Jakarta Bean Validation 注解。
        - [ ] 创建 `AuthResponseDto.java` 用于返回 token。
        - [ ] 创建 `UserDto.java` 用于返回用户信息。

- [ ] **6. 实现 UserDetailsService**
    - **文件**: `src/main/java/com/kisesaki/blog/service/impl/UserDetailsServiceImpl.java`
    - **任务**:
        - [ ] 实现 `UserDetailsService` 接口。
        - [ ] 重写 `loadUserByUsername` 方法，通过 `UserRepository` 查询用户，并将其转换为 Spring Security 的 `User` 对象（包含权限信息）。

- [ ] **7. 实现 AuthService**
    - **文件**: `src/main/java/com/kisesaki/blog/service/AuthService.java`
    - **任务**:
        - [ ] 创建 `register(RegisterRequestDto dto)` 方法：检查用户名/邮箱是否已存在，对密码加密，保存新用户。
        - [ ] 创建 `login(LoginRequestDto dto)` 方法：使用 `AuthenticationManager` 验证用户凭证，成功后调用 `JwtUtil` 生成 token。

- [ ] **8. 创建 AuthController**
    - **文件**: `src/main/java/com/kisesaki/blog/controller/AuthController.java`
    - **任务**:
        - [ ] 创建 `/api/auth/register` 端点，调用 `AuthService.register`。
        - [ ] 创建 `/api/auth/login` 端点，调用 `AuthService.login`。
        - [ ] 确保对 DTO 使用 `@Valid` 注解以触发校验。

### 📝 **阶段三：文章管理 API (Post Management API)**

- [ ] **9. 定义文章相关实体与 Repository**
    - **路径**: `entity/` 和 `repository/`
    - **任务**:
        - [ ] 创建 `Post.java`, `Category.java`, `Tag.java`, `Comment.java` 实体。
        - [ ] 创建对应的 `PostRepository`, `CategoryRepository`, `TagRepository`。

- [ ] **10. 定义文章相关 DTOs**
    - **路径**: `src/main/java/com/kisesaki/blog/dto/`
    - **任务**:
        - [ ] 创建 `CreatePostDto.java` 和 `UpdatePostDto.java`。
        - [ ] 创建 `PostDetailDto.java` 和 `PostSummaryDto.java` 用于不同场景的数据返回。

- [ ] **11. 实现 PostMapper (MyBatis-Plus)**
    - **文件**: `src/main/java/com/kisesaki/blog/mapper/PostMapper.java`
    - **任务**:
        - [ ] 继承 `BaseMapper<Post>`。
        - [ ] 编写一个自定义方法 `findPosts(Page<Post> page, @Param("query") PostQuery query)`，使用 XML 或注解实现复杂的分页和条件查询（按分类、标签等）。

- [ ] **12. 实现 PostService**
    - **文件**: `src/main/java/com/kisesaki/blog/service/PostService.java`
    - **任务**:
        - [ ] 实现 `createPost(CreatePostDto dto)`。
        - [ ] 实现 `getPostBySlug(String slug)`。
        - [ ] 实现 `getPosts(PostQuery query)`，调用 `PostMapper` 进行查询。
        - [ ] 实现 `updatePost(Long id, UpdatePostDto dto)` 和 `deletePost(Long id)`。

- [ ] **13. 创建 PostController**
    - **文件**: `src/main/java/com/kisesaki/blog/controller/PostController.java`
    - **任务**:
        - [ ] 创建 `GET /api/posts` 和 `GET /api/posts/{slug}` 公共端点。
        - [ ] 创建 `POST /api/admin/posts`, `PUT /api/admin/posts/{id}`, `DELETE /api/admin/posts/{id}` 等受保护的管理端点。

---