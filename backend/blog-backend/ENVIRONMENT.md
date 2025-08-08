# 环境配置说明

## 环境变量配置

为了保护敏感信息，项目使用环境变量来管理配置。请按照以下步骤配置您的开发环境：

### 1. 复制环境变量模板

```bash
cp .env.example .env
```

### 2. 修改 .env 文件

根据您的实际环境修改 `.env` 文件中的配置：

#### 数据库配置
- `DB_URL`: PostgreSQL 数据库连接地址
- `DB_USERNAME`: 数据库用户名
- `DB_PASSWORD`: 数据库密码

#### Redis 配置
- `REDIS_HOST`: Redis 服务器地址
- `REDIS_PORT`: Redis 端口
- `REDIS_DATABASE`: Redis 数据库编号
- `REDIS_PASSWORD`: Redis 密码（可选）

#### Kafka 配置
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka 服务器地址

#### JWT 配置
- `JWT_SECRET`: JWT 签名密钥（生产环境请使用复杂的随机字符串）

#### 邮件配置
- `MAIL_USERNAME`: 邮箱用户名
- `MAIL_PASSWORD`: 邮箱应用密码

#### OAuth2 配置
- `GITHUB_CLIENT_ID`: GitHub OAuth 应用 ID
- `GITHUB_CLIENT_SECRET`: GitHub OAuth 应用密钥
- `GITEE_CLIENT_ID`: Gitee OAuth 应用 ID
- `GITEE_CLIENT_SECRET`: Gitee OAuth 应用密钥
- `GITEE_REDIRECT_URI`: Gitee OAuth 回调地址

#### 文件上传配置
- `UPLOAD_PATH`: 文件上传路径

### 3. 安全注意事项

⚠️ **重要提醒：**
- 请勿将 `.env` 文件提交到版本控制系统
- 生产环境请使用强密码和复杂的密钥
- 定期更换敏感信息
- 使用环境变量管理工具（如 Docker secrets、K8s secrets 等）来管理生产环境配置

### 4. 生产环境部署

在生产环境中，建议使用以下方式管理环境变量：

- **Docker**: 使用 `-e` 参数或 `docker-compose.yml` 的 `environment` 段
- **Kubernetes**: 使用 ConfigMap 和 Secret
- **云平台**: 使用对应平台的环境变量管理服务

### 5. 环境变量优先级

Spring Boot 的环境变量读取优先级（从高到低）：
1. 命令行参数
2. 系统环境变量
3. `.env` 文件
4. `application-{profile}.yml` 中的默认值
