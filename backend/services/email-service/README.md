# Email Service - KiseSaki博客邮件微服务

## 📧 项目简介

这是KiseSaki博客的独立邮件服务，负责消费Kafka消息队列中的邮件事件并发送邮件。

## 🏗️ 架构设计

```
blog-backend (主服务)
    ↓ 发布邮件事件
  Kafka (消息队列)
    ↓ 消费邮件事件
email-service (邮件服务)
    ↓ 发送邮件
  SMTP服务器
    ↓ 投递邮件
  用户邮箱
```

## ✨ 核心功能

- **Kafka消费者**: 监听`email_notifications`和`email_high_priority_notifications`主题
- **邮件模板引擎**: 使用Thymeleaf渲染HTML邮件模板
- **重试机制**: 使用Spring Retry实现邮件发送失败自动重试
- **优先级处理**: 支持普通和高优先级邮件分别处理
- **类型化邮件**: 支持多种邮件类型（认证、通知、管理、营销）

## 📁 项目结构

```
email-service/
├── src/main/java/com/kisesaki/services/email/email_service/
│   ├── config/                     # 配置类
│   │   ├── EmailProperties.java    # 邮件配置属性
│   │   └── KafkaConsumerConfig.java # Kafka消费者配置
│   ├── kafka/
│   │   ├── consumer/
│   │   │   └── EmailEventConsumer.java # Kafka消费者
│   │   └── model/
│   │       ├── EmailMessage.java   # 邮件消息模型
│   │       └── EmailType.java      # 邮件类型枚举
│   ├── service/
│   │   ├── EmailSenderService.java # 邮件发送服务
│   │   └── EmailTemplateService.java # 模板渲染服务
│   └── EmailServiceApplication.java # 应用入口
└── src/main/resources/
    ├── application.yml             # 主配置文件
    ├── application-dev.yml         # 开发环境配置
    ├── application-prod.yml        # 生产环境配置
    └── templates/email/            # 邮件模板目录
        ├── auth/                   # 认证类邮件模板
        │   ├── email-verification.html
        │   ├── password-reset.html
        │   ├── password-changed.html
        │   └── welcome.html
        ├── notification/           # 通知类邮件模板
        │   ├── comment-reply.html
        │   ├── post-published.html
        │   ├── follow-notification.html
        │   └── weekly-digest.html
        ├── admin/                  # 管理员邮件模板
        │   ├── user-registered.html
        │   ├── system-alert.html
        │   └── content-moderation.html
        └── marketing/              # 营销类邮件模板
            ├── newsletter.html
            └── feature-announcement.html
```

## 🔧 配置说明

### 环境变量

开发环境需要配置以下环境变量（或在application-dev.yml中设置）：

```yaml
# Kafka配置
KAFKA_BOOTSTRAP_SERVERS: localhost:9092

# 邮件配置
MAIL_HOST: smtp.gmail.com
MAIL_PORT: 587
MAIL_USERNAME: your-email@gmail.com
MAIL_PASSWORD: your-app-password
EMAIL_FROM_ADDRESS: noreply@kisesaki.com
EMAIL_FROM_NAME: KiseSaki博客
```

### Gmail配置说明

如果使用Gmail发送邮件，需要：

1. 启用两步验证
2. 生成应用专用密码
3. 在配置中使用应用专用密码而非账户密码

### 配置项说明

```yaml
email:
  from:
    address: 发件人邮箱地址
    name: 发件人名称
  retry:
    max-attempts: 最大重试次数（默认3次）
    initial-interval: 初始重试间隔（默认1000ms）
    multiplier: 重试间隔倍增因子（默认2.0）
    max-interval: 最大重试间隔（默认10000ms）
  sending:
    batch-size: 批量发送大小（默认10）
    rate-limit: 每分钟发送限制（默认100）
```

## 🚀 运行服务

### 前置条件

1. Java 21+
2. Maven 3.6+
3. Kafka 2.8+ (需要先启动)
4. SMTP服务器访问权限

### 本地开发

```bash
# 1. 进入项目目录
cd backend/services/email-service

# 2. 启动Kafka（如果还没启动）
# 参考blog-backend的Kafka配置

# 3. 配置邮件服务器信息
# 编辑 src/main/resources/application-dev.yml

# 4. 启动服务
mvn spring-boot:run

# 或使用IDE直接运行EmailServiceApplication
```

### 生产部署

```bash
# 构建JAR
mvn clean package -DskipTests

# 运行
java -jar target/email-service-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --KAFKA_BOOTSTRAP_SERVERS=your-kafka-server:9092 \
  --MAIL_HOST=smtp.gmail.com \
  --MAIL_USERNAME=your-email@gmail.com \
  --MAIL_PASSWORD=your-password
```

## 📮 支持的邮件类型

### 认证类邮件

- **WELCOME**: 欢迎新用户
- **EMAIL_VERIFICATION**: 邮箱验证
- **PASSWORD_RESET**: 密码重置
- **PASSWORD_CHANGED**: 密码修改通知

### 通知类邮件

- **COMMENT_REPLY**: 评论回复通知
- **POST_PUBLISHED**: 文章发布通知
- **FOLLOW_NOTIFICATION**: 关注通知
- **WEEKLY_DIGEST**: 周报摘要

### 管理员邮件

- **USER_REGISTERED**: 新用户注册通知
- **SYSTEM_ALERT**: 系统告警
- **CONTENT_MODERATION**: 内容审核

### 营销类邮件

- **NEWSLETTER**: 博客周报
- **FEATURE_ANNOUNCEMENT**: 新功能发布

## 🔍 监控和日志

### 日志配置

日志文件位置：
- 开发环境: `logs/email-service.log`
- 生产环境: `/var/log/email-service/email-service.log`

### 关键日志

```
# 邮件发送成功
INFO - 邮件发送成功: to=user@example.com, subject=欢迎注册, type=欢迎邮件

# 邮件发送失败
ERROR - 邮件发送失败: EmailMessage[type=密码重置, to=user@example.com], 错误: Connection timeout

# Kafka消息消费
INFO - 接收到邮件事件 - Topic: email_notifications, Partition: 0, Offset: 123
```

## 🧪 测试

### 单元测试

```bash
mvn test
```

### 集成测试

可以通过blog-backend发送测试邮件事件来验证整个流程。

## 🐛 常见问题

### 1. Kafka连接失败

**问题**: `Connection to node -1 could not be established`

**解决**: 
- 检查Kafka是否已启动
- 确认`KAFKA_BOOTSTRAP_SERVERS`配置正确

### 2. 邮件发送失败

**问题**: `AuthenticationFailedException`

**解决**:
- 检查SMTP服务器配置
- 确认邮箱和密码正确
- Gmail需要使用应用专用密码

### 3. 模板渲染失败

**问题**: 找不到模板文件

**解决**:
- 确认模板文件路径正确
- 检查EmailType枚举中的templatePath配置

## 📊 性能优化

### 建议配置

- **并发消费者数量**: 3-5个（根据负载调整）
- **批量发送**: 开启批量发送可提高吞吐量
- **连接池**: 复用SMTP连接
- **异步处理**: 使用Kafka异步解耦

### 监控指标

- Kafka消费延迟
- 邮件发送成功率
- 重试次数统计
- 平均发送耗时

## 🔒 安全考虑

1. **敏感信息**: 不要在代码中硬编码邮箱密码
2. **速率限制**: 配置合理的发送频率限制
3. **内容过滤**: 对用户输入进行XSS过滤
4. **SMTP加密**: 使用TLS/SSL加密连接

## 📝 TODO

- [ ] 添加邮件发送统计功能
- [ ] 实现死信队列处理
- [ ] 支持邮件发送状态回调
- [ ] 添加更多邮件模板
- [ ] 实现邮件预览功能
- [ ] 集成邮件追踪服务

## 👥 贡献者

- KiseSaki

## 📄 License

Copyright © 2024 KiseSaki. All rights reserved.
