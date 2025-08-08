# KiseSaki Blog

一个基于现代技术栈构建的个人博客系统。

## 技术栈

### 前端
- React 18 + TypeScript
- Vite 构建工具
- TailwindCSS + shadcn/ui 组件库
- Zustand 状态管理
- React Router 路由管理

### 后端
- Java 17 + Spring Boot 3
- Spring Security + JWT 认证
- PostgreSQL 数据库
- Redis 缓存
- Apache Kafka 消息队列
- MyBatis-Plus ORM

## 快速开始

### 环境要求
- Node.js 18+
- Java 17+
- PostgreSQL 12+
- Redis 6+
- Apache Kafka 2.8+

### 1. 克隆项目

```bash
git clone https://github.com/KiseSaki/kisesaki-blog.git
cd kisesaki-blog
```

### 2. 配置环境变量

#### 后端配置
```bash
cd backend/blog-backend
cp .env.example .env
# 编辑 .env 文件，填入您的配置信息
```

#### 前端配置
```bash
cd frontend
cp .env.example .env.local
# 编辑 .env.local 文件，填入您的配置信息
```

### 3. 启动后端服务

```bash
cd backend/blog-backend
mvn spring-boot:run
```

### 4. 启动前端服务

```bash
cd frontend
pnpm install
pnpm dev
```

## 环境配置说明

### 后端环境变量

详细的环境变量配置说明请查看 [backend/blog-backend/ENVIRONMENT.md](backend/blog-backend/ENVIRONMENT.md)

### 前端环境变量

| 变量名                  | 说明                   | 默认值                      |
| ----------------------- | ---------------------- | --------------------------- |
| `VITE_API_URL`          | 后端 API 地址          | `http://localhost:8080/api` |
| `VITE_GITHUB_CLIENT_ID` | GitHub OAuth 客户端 ID | -                           |
| `VITE_GITEE_CLIENT_ID`  | Gitee OAuth 客户端 ID  | -                           |

## 项目结构

```
├── frontend/          # 前端项目
│   ├── src/
│   │   ├── api/       # API 调用
│   │   ├── components/ # 组件
│   │   ├── hooks/     # 自定义 Hook
│   │   ├── pages/     # 页面
│   │   ├── stores/    # Zustand 状态管理
│   │   └── types/     # TypeScript 类型定义
│   └── package.json
├── backend/           # 后端项目
│   └── blog-backend/
│       ├── src/main/java/
│       └── pom.xml
└── README.md
```

## 开发指南

### Git 分支规范

- `main`: 主分支，保持稳定
- `develop`: 开发分支
- `feature/frontend-*`: 前端功能分支
- `feature/backend-*`: 后端功能分支
- `bugfix/*`: 修复分支
- `hotfix/*`: 紧急修复分支

### 提交信息规范

采用 Conventional Commits 格式：

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

类型：
- ✨ feat: 新功能
- 🐞 fix: 修复 bug
- 📝 docs: 文档变更
- 🌈 style: 代码格式
- 🦄 refactor: 代码重构
- 🎈 perf: 性能优化
- ✅ test: 测试相关
- 🔧 build: 构建相关
- 🐎 ci: CI/CD 相关
- 🐋 chore: 其他变更

## 部署

### Docker 部署

```bash
# 构建镜像
docker-compose build

# 启动服务
docker-compose up -d
```

### Kubernetes 部署

```bash
# 应用配置
kubectl apply -f k8s/
```

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

MIT License

## 联系我们

- 作者: KiseSaki
- 邮箱: your-email@example.com
- 项目地址: https://github.com/KiseSaki/kisesaki-blog