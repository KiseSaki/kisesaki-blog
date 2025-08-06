# 数据库表结构规范 (Database Schema)

本文档定义了 Kisesaki Blog 项目的完整数据库表结构，基于前端任务流需求重新设计。所有表名和字段名均采用 `snake_case` 命名法，确保与前端 TypeScript 类型定义完美匹配。

## 🎯 设计原则

1. **前后端一致性**: 数据库字段设计与前端 TypeScript 类型定义保持一致
2. **RESTful API 友好**: 字段命名便于 API 响应直接映射  
3. **扩展性考虑**: 预留扩展字段，支持未来功能迭代
4. **性能优化**: 合理的索引设计和字段类型选择
5. **数据完整性**: 外键约束和业务规则约束

## 📋 前端类型映射说明

为确保前后端数据一致性，数据库字段设计严格对应前端 TypeScript 类型：

### 核心类型映射
- **用户类型**: `User`, `UserProfile`, `UserSettings` 
- **博客类型**: `Post`, `Category`, `Tag`, `Comment`, `PostListParams`, `PaginationResult<T>`
- **认证类型**: `LoginRequestDto`, `RegisterRequestDto`, `AuthResponse`, `OAuthProvider`
- **管理员类型**: `AdminPostDto`, `DashboardStatsDto`, `UserManagementDto`

### API 响应格式
```typescript
// 统一响应格式
ApiResponse<T> = {
  code: number;
  message: string;
  data: T;
  timestamp: string;
}

// 分页响应格式
PaginationResult<T> = {
  content: T[];
  page: number;
  size: number;
  total: number;
  totalPages: number;
}
```

---

## 用户与权限

为多角色、多权限体系、个性化设置与会话管理提供支持。

#### 1. `users` - 用户基础表

存储用户账号与基本认证信息，支持多种登录方式。

| 字段名              | 数据类型       | 约束                           | 描述                                       |
| :------------------ | :------------- | :----------------------------- | :----------------------------------------- |
| `id`                | `BIGSERIAL`    | `PRIMARY KEY`                  | 用户唯一ID (自增)                          |
| `username`          | `VARCHAR(50)`  | `NOT NULL`, `UNIQUE`           | 用户名                                     |
| `email`             | `VARCHAR(100)` | `NOT NULL`, `UNIQUE`           | 电子邮箱                                   |
| `password`          | `VARCHAR(255)` | `NULL`                         | 加密后的密码 (OAuth登录用户可为空)         |
| `oauth_id`          | `VARCHAR(100)` |                                | OAuth 提供商的用户ID                       |
| `oauth_provider`    | `VARCHAR(20)`  |                                | OAuth 提供商 ('github', 'gitee', 'google') |
| `account_type`      | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'local'`  | 账号类型 ('local', 'oauth')                |
| `status`            | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'active'` | 用户状态 ('active', 'inactive', 'banned')  |
| `email_verified`    | `BOOLEAN`      | `NOT NULL`, `DEFAULT FALSE`    | 邮箱是否已验证                             |
| `email_verified_at` | `TIMESTAMPTZ`  |                                | 邮箱验证时间                               |
| `last_login_at`     | `TIMESTAMPTZ`  |                                | 最后登录时间                               |
| `last_login_ip`     | `INET`         |                                | 最后登录IP                                 |
| `created_at`        | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`    | 创建时间                                   |
| `updated_at`        | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`    | 更新时间                                   |

#### 2. `user_profiles` - 用户扩展信息表

存储用户的详细个人信息，支持前端用户卡片和资料页面展示。

| 字段名             | 数据类型       | 约束                                   | 描述                                      |
| :----------------- | :------------- | :------------------------------------- | :---------------------------------------- |
| `id`               | `BIGSERIAL`    | `PRIMARY KEY`                          | 主键ID (自增)                             |
| `user_id`          | `BIGINT`       | `NOT NULL`, `UNIQUE`, `FK to users.id` | 用户ID                                    |
| `display_name`     | `VARCHAR(100)` |                                        | 显示名称                                  |
| `first_name`       | `VARCHAR(50)`  |                                        | 名字                                      |
| `last_name`        | `VARCHAR(50)`  |                                        | 姓氏                                      |
| `bio`              | `TEXT`         |                                        | 个人简介                                  |
| `avatar_url`       | `VARCHAR(255)` |                                        | 用户头像URL                               |
| `cover_image_url`  | `VARCHAR(255)` |                                        | 用户封面图URL                             |
| `website_url`      | `VARCHAR(255)` |                                        | 个人网站URL                               |
| `location`         | `VARCHAR(100)` |                                        | 所在地                                    |
| `company`          | `VARCHAR(100)` |                                        | 公司/组织                                 |
| `title`            | `VARCHAR(100)` |                                        | 职位标题                                  |
| `social_links`     | `JSONB`        |                                        | 社交媒体链接 (JSON格式)                   |
| `birth_date`       | `DATE`         |                                        | 出生日期                                  |
| `gender`           | `VARCHAR(10)`  |                                        | 性别 ('male', 'female', 'other')          |
| `timezone`         | `VARCHAR(50)`  | `DEFAULT 'UTC'`                        | 时区设置                                  |
| `language`         | `VARCHAR(10)`  | `DEFAULT 'en'`                         | 首选语言                                  |
| `theme_preference` | `VARCHAR(10)`  | `DEFAULT 'system'`                     | 主题偏好 ('light', 'dark', 'system')      |
| `privacy_level`    | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'public'`         | 隐私级别 ('public', 'friends', 'private') |
| `created_at`       | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`            | 创建时间                                  |
| `updated_at`       | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`            | 更新时间                                  |

#### 3. `roles` - 角色表

定义系统中的用户角色。

| 字段名        | 数据类型       | 约束                        | 描述              |
| :------------ | :------------- | :-------------------------- | :---------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`               | 角色唯一ID (自增) |
| `name`        | `VARCHAR(50)`  | `NOT NULL`, `UNIQUE`        | 角色名称          |
| `description` | `VARCHAR(255)` |                             | 角色描述          |
| `created_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 创建时间          |

#### 4. `permissions` - 权限表

定义系统中的权限动作。

| 字段名        | 数据类型       | 约束                        | 描述              |
| :------------ | :------------- | :-------------------------- | :---------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`               | 权限唯一ID (自增) |
| `name`        | `VARCHAR(100)` | `NOT NULL`, `UNIQUE`        | 权限名称          |
| `description` | `VARCHAR(255)` |                             | 权限描述          |
| `resource`    | `VARCHAR(50)`  | `NOT NULL`                  | 资源类型          |
| `action`      | `VARCHAR(50)`  | `NOT NULL`                  | 操作类型          |
| `created_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 创建时间          |

#### 5. `user_roles` - 用户角色关联表

实现用户与角色的多对多关系。

| 字段名        | 数据类型      | 约束                             | 描述     |
| :------------ | :------------ | :------------------------------- | :------- |
| `user_id`     | `BIGINT`      | `NOT NULL`, `FK to users.id`     | 用户ID   |
| `role_id`     | `BIGINT`      | `NOT NULL`, `FK to roles.id`     | 角色ID   |
| `assigned_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`      | 分配时间 |
|               |               | `PRIMARY KEY (user_id, role_id)` | 联合主键 |

#### 6. `role_permissions` - 角色权限关联表

实现角色与权限的多对多关系。

| 字段名          | 数据类型      | 约束                                   | 描述     |
| :-------------- | :------------ | :------------------------------------- | :------- |
| `role_id`       | `BIGINT`      | `NOT NULL`, `FK to roles.id`           | 角色ID   |
| `permission_id` | `BIGINT`      | `NOT NULL`, `FK to permissions.id`     | 权限ID   |
| `granted_at`    | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`            | 授予时间 |
|                 |               | `PRIMARY KEY (role_id, permission_id)` | 联合主键 |

#### 7. `sessions` - 用户会话表

管理用户登录会话。

| 字段名       | 数据类型       | 约束                         | 描述              |
| :----------- | :------------- | :--------------------------- | :---------------- |
| `id`         | `BIGSERIAL`    | `PRIMARY KEY`                | 会话唯一ID (自增) |
| `user_id`    | `BIGINT`       | `NOT NULL`, `FK to users.id` | 用户ID            |
| `token`      | `VARCHAR(255)` | `NOT NULL`, `UNIQUE`         | 会话令牌          |
| `ip_address` | `INET`         |                              | 客户端IP地址      |
| `user_agent` | `TEXT`         |                              | 用户代理字符串    |
| `expires_at` | `TIMESTAMPTZ`  | `NOT NULL`                   | 过期时间          |
| `created_at` | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 创建时间          |

#### 8. `user_settings` - 用户设置表

存储用户的个性化偏好设置。

| 字段名          | 数据类型       | 约束                            | 描述              |
| :-------------- | :------------- | :------------------------------ | :---------------- |
| `id`            | `BIGSERIAL`    | `PRIMARY KEY`                   | 设置唯一ID (自增) |
| `user_id`       | `BIGINT`       | `NOT NULL`, `FK to users.id`    | 用户ID            |
| `setting_key`   | `VARCHAR(100)` | `NOT NULL`                      | 设置键名          |
| `setting_value` | `TEXT`         |                                 | 设置值            |
| `updated_at`    | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`     | 更新时间          |
|                 |                | `UNIQUE (user_id, setting_key)` | 联合唯一索引      |

#### 9. `user_stats` - 用户统计表

存储用户的统计信息，支持前端用户卡片和资料页面展示。

| 字段名             | 数据类型      | 约束                                   | 描述              |
| :----------------- | :------------ | :------------------------------------- | :---------------- |
| `id`               | `BIGSERIAL`   | `PRIMARY KEY`                          | 统计唯一ID (自增) |
| `user_id`          | `BIGINT`      | `NOT NULL`, `UNIQUE`, `FK to users.id` | 用户ID            |
| `posts_count`      | `INT`         | `NOT NULL`, `DEFAULT 0`                | 发布文章数量      |
| `drafts_count`     | `INT`         | `NOT NULL`, `DEFAULT 0`                | 草稿文章数量      |
| `comments_count`   | `INT`         | `NOT NULL`, `DEFAULT 0`                | 发表评论数量      |
| `likes_count`      | `INT`         | `NOT NULL`, `DEFAULT 0`                | 获得点赞数量      |
| `views_count`      | `INT`         | `NOT NULL`, `DEFAULT 0`                | 文章总浏览量      |
| `favorites_count`  | `INT`         | `NOT NULL`, `DEFAULT 0`                | 收藏文章数量      |
| `followers_count`  | `INT`         | `NOT NULL`, `DEFAULT 0`                | 粉丝数量          |
| `following_count`  | `INT`         | `NOT NULL`, `DEFAULT 0`                | 关注数量          |
| `reputation_score` | `INT`         | `NOT NULL`, `DEFAULT 0`                | 声誉积分          |
| `last_active_at`   | `TIMESTAMPTZ` |                                        | 最后活跃时间      |
| `updated_at`       | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`            | 更新时间          |

---

## 文章管理

支持文章发布、版本控制、分类/标签管理及 SEO 扩展。

#### 10. `posts` - 文章表

存储博客文章主体信息，支持草稿、发布、归档等状态管理。

| 字段名               | 数据类型       | 约束                           | 描述                                                   |
| :------------------- | :------------- | :----------------------------- | :----------------------------------------------------- |
| `id`                 | `BIGSERIAL`    | `PRIMARY KEY`                  | 文章唯一ID (自增)                                      |
| `author_id`          | `BIGINT`       | `NOT NULL`, `FK to users.id`   | 作者ID                                                 |
| `category_id`        | `BIGINT`       | `FK to categories.id`          | 分类ID                                                 |
| `title`              | `VARCHAR(255)` | `NOT NULL`                     | 文章标题                                               |
| `slug`               | `VARCHAR(255)` | `NOT NULL`, `UNIQUE`           | 文章的URL友好别名                                      |
| `excerpt`            | `TEXT`         |                                | 文章摘要/简介                                          |
| `content`            | `TEXT`         | `NOT NULL`                     | 文章主体内容 (Markdown格式)                            |
| `html_content`       | `TEXT`         |                                | 渲染后的HTML内容 (缓存用)                              |
| `cover_image_url`    | `VARCHAR(255)` |                                | 封面图片URL                                            |
| `featured_image_url` | `VARCHAR(255)` |                                | 特色图片URL                                            |
| `status`             | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'draft'`  | 文章状态 ('draft', 'published', 'archived', 'deleted') |
| `visibility`         | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'public'` | 可见性 ('public', 'private', 'password_protected')     |
| `password`           | `VARCHAR(255)` |                                | 访问密码 (仅password_protected文章)                    |
| `view_count`         | `INT`          | `NOT NULL`, `DEFAULT 0`        | 浏览次数                                               |
| `like_count`         | `INT`          | `NOT NULL`, `DEFAULT 0`        | 点赞数量                                               |
| `comment_count`      | `INT`          | `NOT NULL`, `DEFAULT 0`        | 评论数量                                               |
| `share_count`        | `INT`          | `NOT NULL`, `DEFAULT 0`        | 分享数量                                               |
| `reading_time`       | `INT`          |                                | 预估阅读时间(分钟)                                     |
| `word_count`         | `INT`          |                                | 字数统计                                               |
| `is_featured`        | `BOOLEAN`      | `NOT NULL`, `DEFAULT FALSE`    | 是否为精选文章                                         |
| `is_top`             | `BOOLEAN`      | `NOT NULL`, `DEFAULT FALSE`    | 是否置顶                                               |
| `allow_comments`     | `BOOLEAN`      | `NOT NULL`, `DEFAULT TRUE`     | 是否允许评论                                           |
| `seo_title`          | `VARCHAR(255)` |                                | SEO标题                                                |
| `seo_description`    | `TEXT`         |                                | SEO描述                                                |
| `seo_keywords`       | `VARCHAR(255)` |                                | SEO关键词                                              |
| `published_at`       | `TIMESTAMPTZ`  |                                | 发布时间                                               |
| `scheduled_at`       | `TIMESTAMPTZ`  |                                | 计划发布时间                                           |
| `last_modified_at`   | `TIMESTAMPTZ`  |                                | 最后修改时间                                           |
| `created_at`         | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`    | 创建时间                                               |
| `updated_at`         | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`    | 更新时间                                               |

#### 10. `post_revisions` - 文章版本历史表

存储文章的历史版本，用于版本控制。

| 字段名       | 数据类型       | 约束                         | 描述              |
| :----------- | :------------- | :--------------------------- | :---------------- |
| `id`         | `BIGSERIAL`    | `PRIMARY KEY`                | 版本唯一ID (自增) |
| `post_id`    | `BIGINT`       | `NOT NULL`, `FK to posts.id` | 文章ID            |
| `version`    | `INT`          | `NOT NULL`                   | 版本号            |
| `title`      | `VARCHAR(255)` | `NOT NULL`                   | 标题              |
| `content`    | `TEXT`         | `NOT NULL`                   | 内容              |
| `summary`    | `TEXT`         |                              | 摘要              |
| `created_by` | `BIGINT`       | `NOT NULL`, `FK to users.id` | 创建者ID          |
| `created_at` | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 创建时间          |
|              |                | `UNIQUE (post_id, version)`  | 联合唯一索引      |

#### 11. `post_meta` - 文章元数据表

存储文章的SEO和自定义字段信息。

| 字段名       | 数据类型       | 约束                         | 描述                |
| :----------- | :------------- | :--------------------------- | :------------------ |
| `id`         | `BIGSERIAL`    | `PRIMARY KEY`                | 元数据唯一ID (自增) |
| `post_id`    | `BIGINT`       | `NOT NULL`, `FK to posts.id` | 文章ID              |
| `meta_key`   | `VARCHAR(100)` | `NOT NULL`                   | 元数据键名          |
| `meta_value` | `TEXT`         |                              | 元数据值            |
| `created_at` | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 创建时间            |
| `updated_at` | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 更新时间            |
|              |                | `UNIQUE (post_id, meta_key)` | 联合唯一索引        |

#### 12. `categories` - 分类表

存储文章分类，支持层级结构。

| 字段名        | 数据类型       | 约束                        | 描述              |
| :------------ | :------------- | :-------------------------- | :---------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`               | 分类唯一ID (自增) |
| `name`        | `VARCHAR(50)`  | `NOT NULL`, `UNIQUE`        | 分类名称          |
| `slug`        | `VARCHAR(50)`  | `NOT NULL`, `UNIQUE`        | 分类的URL友好别名 |
| `description` | `VARCHAR(255)` |                             | 分类描述          |
| `parent_id`   | `BIGINT`       | `FK to categories.id`       | 父分类ID (自关联) |
| `sort_order`  | `INT`          | `NOT NULL`, `DEFAULT 0`     | 排序顺序          |
| `post_count`  | `INT`          | `NOT NULL`, `DEFAULT 0`     | 文章数量          |
| `created_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 创建时间          |
| `updated_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 更新时间          |

#### 13. `tags` - 标签表

存储文章标签。

| 字段名        | 数据类型       | 约束                        | 描述                 |
| :------------ | :------------- | :-------------------------- | :------------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`               | 标签唯一ID (自增)    |
| `name`        | `VARCHAR(50)`  | `NOT NULL`, `UNIQUE`        | 标签名称             |
| `slug`        | `VARCHAR(50)`  | `NOT NULL`, `UNIQUE`        | 标签的URL友好别名    |
| `description` | `VARCHAR(255)` |                             | 标签描述             |
| `color`       | `VARCHAR(7)`   |                             | 标签颜色 (HEX格式)   |
| `post_count`  | `INT`          | `NOT NULL`, `DEFAULT 0`     | 使用该标签的文章数量 |
| `created_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 创建时间             |

#### 14. `post_tags` - 文章与标签关联表

用于实现文章和标签的多对多关系。

| 字段名       | 数据类型      | 约束                            | 描述     |
| :----------- | :------------ | :------------------------------ | :------- |
| `post_id`    | `BIGINT`      | `NOT NULL`, `FK to posts.id`    | 文章ID   |
| `tag_id`     | `BIGINT`      | `NOT NULL`, `FK to tags.id`     | 标签ID   |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`     | 创建时间 |
|              |               | `PRIMARY KEY (post_id, tag_id)` | 联合主键 |

#### 15. `search_logs` - 搜索日志表

记录用户搜索行为，支持搜索分析和热门搜索推荐。

| 字段名              | 数据类型       | 约束                        | 描述                          |
| :------------------ | :------------- | :-------------------------- | :---------------------------- |
| `id`                | `BIGSERIAL`    | `PRIMARY KEY`               | 搜索记录唯一ID (自增)         |
| `user_id`           | `BIGINT`       | `FK to users.id`            | 用户ID (可为空，支持匿名搜索) |
| `query`             | `VARCHAR(255)` | `NOT NULL`                  | 搜索关键词                    |
| `filters`           | `JSONB`        |                             | 搜索过滤条件 (JSON格式)       |
| `results_count`     | `INT`          | `NOT NULL`, `DEFAULT 0`     | 搜索结果数量                  |
| `clicked_result_id` | `BIGINT`       |                             | 点击的结果ID                  |
| `ip_address`        | `INET`         |                             | 搜索者IP地址                  |
| `user_agent`        | `TEXT`         |                             | 用户代理字符串                |
| `response_time`     | `INT`          |                             | 搜索响应时间(毫秒)            |
| `created_at`        | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 搜索时间                      |

---

## 评论与互动

评论嵌套、点赞/收藏、关注、浏览日志等功能。

#### 16. `comments` - 评论表

存储文章的评论，支持嵌套回复和丰富的交互功能。

| 字段名            | 数据类型       | 约束                             | 描述                                                 |
| :---------------- | :------------- | :------------------------------- | :--------------------------------------------------- |
| `id`              | `BIGSERIAL`    | `PRIMARY KEY`                    | 评论唯一ID (自增)                                    |
| `post_id`         | `BIGINT`       | `NOT NULL`, `FK to posts.id`     | 所属文章ID                                           |
| `user_id`         | `BIGINT`       | `NOT NULL`, `FK to users.id`     | 评论用户ID                                           |
| `parent_id`       | `BIGINT`       | `FK to comments.id`              | 父评论ID (用于实现嵌套评论)                          |
| `reply_to_id`     | `BIGINT`       | `FK to comments.id`              | 回复目标评论ID (用于@功能)                           |
| `content`         | `TEXT`         | `NOT NULL`                       | 评论内容                                             |
| `html_content`    | `TEXT`         |                                  | 渲染后的HTML内容                                     |
| `like_count`      | `INT`          | `NOT NULL`, `DEFAULT 0`          | 点赞数量                                             |
| `dislike_count`   | `INT`          | `NOT NULL`, `DEFAULT 0`          | 踩数量                                               |
| `reply_count`     | `INT`          | `NOT NULL`, `DEFAULT 0`          | 直接回复数量                                         |
| `level`           | `INT`          | `NOT NULL`, `DEFAULT 0`          | 嵌套层级 (0为顶级评论)                               |
| `path`            | `VARCHAR(255)` |                                  | 评论路径 (如: "1.3.5" 表示层级关系)                  |
| `status`          | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'approved'` | 评论状态 ('pending', 'approved', 'rejected', 'spam') |
| `is_pinned`       | `BOOLEAN`      | `NOT NULL`, `DEFAULT FALSE`      | 是否置顶                                             |
| `is_author_reply` | `BOOLEAN`      | `NOT NULL`, `DEFAULT FALSE`      | 是否为作者回复                                       |
| `ip_address`      | `INET`         |                                  | 评论者IP地址                                         |
| `user_agent`      | `TEXT`         |                                  | 用户代理字符串                                       |
| `edited_at`       | `TIMESTAMPTZ`  |                                  | 最后编辑时间                                         |
| `deleted_at`      | `TIMESTAMPTZ`  |                                  | 删除时间 (软删除)                                    |
| `created_at`      | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`      | 创建时间                                             |
| `updated_at`      | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`      | 更新时间                                             |

#### 16. `comment_reactions` - 评论反应表

存储对评论的点赞/踩等反应。

| 字段名          | 数据类型      | 约束                            | 描述                         |
| :-------------- | :------------ | :------------------------------ | :--------------------------- |
| `id`            | `BIGSERIAL`   | `PRIMARY KEY`                   | 反应唯一ID (自增)            |
| `user_id`       | `BIGINT`      | `NOT NULL`, `FK to users.id`    | 用户ID                       |
| `comment_id`    | `BIGINT`      | `NOT NULL`, `FK to comments.id` | 评论ID                       |
| `reaction_type` | `VARCHAR(20)` | `NOT NULL`                      | 反应类型 ('like', 'dislike') |
| `created_at`    | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`     | 创建时间                     |
|                 |               | `UNIQUE (user_id, comment_id)`  | 联合唯一索引                 |

#### 17. `likes` - 点赞表

通用点赞表，支持对文章、评论等的点赞。

| 字段名        | 数据类型      | 约束                                       | 描述                             |
| :------------ | :------------ | :----------------------------------------- | :------------------------------- |
| `id`          | `BIGSERIAL`   | `PRIMARY KEY`                              | 点赞唯一ID (自增)                |
| `user_id`     | `BIGINT`      | `NOT NULL`, `FK to users.id`               | 用户ID                           |
| `target_id`   | `BIGINT`      | `NOT NULL`                                 | 目标对象ID                       |
| `target_type` | `VARCHAR(50)` | `NOT NULL`                                 | 目标对象类型 ('post', 'comment') |
| `created_at`  | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`                | 创建时间                         |
|               |               | `UNIQUE (user_id, target_id, target_type)` | 联合唯一索引                     |

#### 18. `favorites` - 收藏表

存储用户收藏的文章。

| 字段名       | 数据类型      | 约束                         | 描述              |
| :----------- | :------------ | :--------------------------- | :---------------- |
| `id`         | `BIGSERIAL`   | `PRIMARY KEY`                | 收藏唯一ID (自增) |
| `user_id`    | `BIGINT`      | `NOT NULL`, `FK to users.id` | 用户ID            |
| `post_id`    | `BIGINT`      | `NOT NULL`, `FK to posts.id` | 文章ID            |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`  | 收藏时间          |
|              |               | `UNIQUE (user_id, post_id)`  | 联合唯一索引      |

#### 19. `followings` - 关注表

用户之间的关注关系。

| 字段名        | 数据类型      | 约束                                | 描述              |
| :------------ | :------------ | :---------------------------------- | :---------------- |
| `id`          | `BIGSERIAL`   | `PRIMARY KEY`                       | 关注唯一ID (自增) |
| `follower_id` | `BIGINT`      | `NOT NULL`, `FK to users.id`        | 关注者ID          |
| `followee_id` | `BIGINT`      | `NOT NULL`, `FK to users.id`        | 被关注者ID        |
| `created_at`  | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()`         | 关注时间          |
|               |               | `UNIQUE (follower_id, followee_id)` | 联合唯一索引      |

#### 21. `page_views` - 页面浏览记录表

记录网站各个页面的浏览记录，支持前端访问统计和分析。

| 字段名         | 数据类型       | 约束                        | 描述                                                    |
| :------------- | :------------- | :-------------------------- | :------------------------------------------------------ |
| `id`           | `BIGSERIAL`    | `PRIMARY KEY`               | 浏览记录唯一ID (自增)                                   |
| `post_id`      | `BIGINT`       | `FK to posts.id`            | 文章ID (仅文章页面)                                     |
| `user_id`      | `BIGINT`       | `FK to users.id`            | 用户ID (可为空，支持匿名访问)                           |
| `page_type`    | `VARCHAR(50)`  | `NOT NULL`                  | 页面类型 ('post', 'category', 'tag', 'home', 'archive') |
| `page_url`     | `VARCHAR(500)` | `NOT NULL`                  | 页面URL                                                 |
| `page_title`   | `VARCHAR(255)` |                             | 页面标题                                                |
| `referrer`     | `VARCHAR(500)` |                             | 来源页面                                                |
| `utm_source`   | `VARCHAR(100)` |                             | UTM来源                                                 |
| `utm_medium`   | `VARCHAR(100)` |                             | UTM媒介                                                 |
| `utm_campaign` | `VARCHAR(100)` |                             | UTM活动                                                 |
| `ip_address`   | `INET`         |                             | 访问者IP地址                                            |
| `user_agent`   | `TEXT`         |                             | 用户代理字符串                                          |
| `device_type`  | `VARCHAR(20)`  |                             | 设备类型 ('desktop', 'mobile', 'tablet')                |
| `browser`      | `VARCHAR(50)`  |                             | 浏览器类型                                              |
| `os`           | `VARCHAR(50)`  |                             | 操作系统                                                |
| `session_id`   | `VARCHAR(255)` |                             | 会话ID                                                  |
| `duration`     | `INT`          |                             | 页面停留时间(秒)                                        |
| `is_bounce`    | `BOOLEAN`      | `DEFAULT FALSE`             | 是否为跳出访问                                          |
| `viewed_at`    | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 浏览时间                                                |

---

## 媒体与资源

集中管理各种图片、附件及文件。

#### 21. `media_resources` - 媒体资源表

存储各种媒体文件信息。

| 字段名        | 数据类型       | 约束                         | 描述                  |
| :------------ | :------------- | :--------------------------- | :-------------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`                | 媒体资源唯一ID (自增) |
| `url`         | `VARCHAR(500)` | `NOT NULL`                   | 文件访问URL           |
| `filename`    | `VARCHAR(255)` | `NOT NULL`                   | 原始文件名            |
| `file_path`   | `VARCHAR(500)` | `NOT NULL`                   | 文件存储路径          |
| `file_type`   | `VARCHAR(100)` | `NOT NULL`                   | 文件MIME类型          |
| `file_size`   | `BIGINT`       | `NOT NULL`                   | 文件大小 (字节)       |
| `width`       | `INT`          |                              | 图片宽度 (仅图片类型) |
| `height`      | `INT`          |                              | 图片高度 (仅图片类型) |
| `alt_text`    | `VARCHAR(255)` |                              | 替代文本              |
| `description` | `TEXT`         |                              | 文件描述              |
| `uploaded_by` | `BIGINT`       | `NOT NULL`, `FK to users.id` | 上传者ID              |
| `usage_count` | `INT`          | `NOT NULL`, `DEFAULT 0`      | 使用次数              |
| `created_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 上传时间              |

---

## 通知与订阅

站内通知、邮件订阅与新闻通讯记录。

#### 22. `notifications` - 通知表

存储站内通知信息。

| 字段名       | 数据类型       | 约束                         | 描述                                             |
| :----------- | :------------- | :--------------------------- | :----------------------------------------------- |
| `id`         | `BIGSERIAL`    | `PRIMARY KEY`                | 通知唯一ID (自增)                                |
| `user_id`    | `BIGINT`       | `NOT NULL`, `FK to users.id` | 接收用户ID                                       |
| `type`       | `VARCHAR(50)`  | `NOT NULL`                   | 通知类型 ('comment', 'like', 'follow', 'system') |
| `title`      | `VARCHAR(255)` | `NOT NULL`                   | 通知标题                                         |
| `content`    | `TEXT`         |                              | 通知内容                                         |
| `payload`    | `JSONB`        |                              | 通知负载数据 (JSON格式)                          |
| `read_at`    | `TIMESTAMPTZ`  |                              | 阅读时间                                         |
| `created_at` | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 创建时间                                         |

#### 23. `subscriptions` - 订阅表

管理邮件订阅。

| 字段名              | 数据类型       | 约束                           | 描述                                                 |
| :------------------ | :------------- | :----------------------------- | :--------------------------------------------------- |
| `id`                | `BIGSERIAL`    | `PRIMARY KEY`                  | 订阅唯一ID (自增)                                    |
| `user_id`           | `BIGINT`       | `FK to users.id`               | 用户ID (可为空，支持匿名订阅)                        |
| `email`             | `VARCHAR(100)` | `NOT NULL`                     | 订阅邮箱                                             |
| `status`            | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'active'` | 订阅状态 ('active', 'unsubscribed')                  |
| `subscription_type` | `VARCHAR(50)`  | `NOT NULL`                     | 订阅类型 ('newsletter', 'comment_reply', 'new_post') |
| `token`             | `VARCHAR(255)` | `NOT NULL`, `UNIQUE`           | 订阅令牌 (用于取消订阅)                              |
| `subscribed_at`     | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`    | 订阅时间                                             |
| `unsubscribed_at`   | `TIMESTAMPTZ`  |                                | 取消订阅时间                                         |

#### 24. `newsletter_logs` - 通讯发送记录表

记录新闻通讯的发送记录。

| 字段名            | 数据类型       | 约束                                 | 描述                                   |
| :---------------- | :------------- | :----------------------------------- | :------------------------------------- |
| `id`              | `BIGSERIAL`    | `PRIMARY KEY`                        | 发送记录唯一ID (自增)                  |
| `subscription_id` | `BIGINT`       | `NOT NULL`, `FK to subscriptions.id` | 订阅ID                                 |
| `newsletter_id`   | `BIGINT`       |                                      | 通讯ID (可关联到具体的通讯内容)        |
| `subject`         | `VARCHAR(255)` | `NOT NULL`                           | 邮件主题                               |
| `content`         | `TEXT`         | `NOT NULL`                           | 邮件内容                               |
| `status`          | `VARCHAR(20)`  | `NOT NULL`                           | 发送状态 ('pending', 'sent', 'failed') |
| `sent_at`         | `TIMESTAMPTZ`  |                                      | 发送时间                               |
| `created_at`      | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`          | 创建时间                               |

---

## 审计与系统配置

操作审计、全局配置与 URL 重定向、SEO 默认设置等。

#### 25. `audit_logs` - 审计日志表

记录用户和系统的重要操作。

| 字段名         | 数据类型       | 约束                        | 描述                          |
| :------------- | :------------- | :-------------------------- | :---------------------------- |
| `id`           | `BIGSERIAL`    | `PRIMARY KEY`               | 日志唯一ID (自增)             |
| `user_id`      | `BIGINT`       | `FK to users.id`            | 操作用户ID (可为空，系统操作) |
| `action`       | `VARCHAR(100)` | `NOT NULL`                  | 操作类型                      |
| `target_table` | `VARCHAR(100)` |                             | 目标表名                      |
| `target_id`    | `BIGINT`       |                             | 目标记录ID                    |
| `old_values`   | `JSONB`        |                             | 操作前的值 (JSON格式)         |
| `new_values`   | `JSONB`        |                             | 操作后的值 (JSON格式)         |
| `ip_address`   | `INET`         |                             | 操作者IP地址                  |
| `user_agent`   | `TEXT`         |                             | 用户代理字符串                |
| `meta`         | `JSONB`        |                             | 额外的元数据 (JSON格式)       |
| `timestamp`    | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 操作时间                      |

#### 26. `system_settings` - 系统设置表

存储全局系统配置参数。

| 字段名        | 数据类型       | 约束                           | 描述                                           |
| :------------ | :------------- | :----------------------------- | :--------------------------------------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`                  | 设置唯一ID (自增)                              |
| `key`         | `VARCHAR(100)` | `NOT NULL`, `UNIQUE`           | 设置键名                                       |
| `value`       | `TEXT`         |                                | 设置值                                         |
| `type`        | `VARCHAR(20)`  | `NOT NULL`, `DEFAULT 'string'` | 值类型 ('string', 'number', 'boolean', 'json') |
| `description` | `VARCHAR(255)` |                                | 设置描述                                       |
| `is_public`   | `BOOLEAN`      | `NOT NULL`, `DEFAULT FALSE`    | 是否为公开设置                                 |
| `updated_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`    | 更新时间                                       |
| `updated_by`  | `BIGINT`       | `FK to users.id`               | 更新者ID                                       |

#### 27. `redirects` - 重定向表

管理URL重定向规则。

| 字段名        | 数据类型       | 约束                         | 描述                 |
| :------------ | :------------- | :--------------------------- | :------------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`                | 重定向唯一ID (自增)  |
| `source_path` | `VARCHAR(500)` | `NOT NULL`, `UNIQUE`         | 源路径               |
| `target_url`  | `VARCHAR(500)` | `NOT NULL`                   | 目标URL              |
| `code`        | `INT`          | `NOT NULL`, `DEFAULT 301`    | HTTP状态码 (301/302) |
| `is_active`   | `BOOLEAN`      | `NOT NULL`, `DEFAULT TRUE`   | 是否启用             |
| `hit_count`   | `INT`          | `NOT NULL`, `DEFAULT 0`      | 重定向次数           |
| `created_by`  | `BIGINT`       | `NOT NULL`, `FK to users.id` | 创建者ID             |
| `created_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 创建时间             |
| `updated_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()`  | 更新时间             |

#### 28. `seo_settings` - SEO设置表

存储全站SEO的默认配置。

| 字段名        | 数据类型       | 约束                        | 描述                                                   |
| :------------ | :------------- | :-------------------------- | :----------------------------------------------------- |
| `id`          | `BIGSERIAL`    | `PRIMARY KEY`               | SEO设置唯一ID (自增)                                   |
| `key`         | `VARCHAR(100)` | `NOT NULL`, `UNIQUE`        | SEO设置键名                                            |
| `value`       | `TEXT`         |                             | SEO设置值                                              |
| `page_type`   | `VARCHAR(50)`  |                             | 页面类型 ('global', 'home', 'post', 'category', 'tag') |
| `description` | `VARCHAR(255)` |                             | 设置描述                                               |
| `updated_at`  | `TIMESTAMPTZ`  | `NOT NULL`, `DEFAULT NOW()` | 更新时间                                               |
| `updated_by`  | `BIGINT`       | `FK to users.id`            | 更新者ID                                               |

---

## 数据库索引建议

为了提升查询性能，建议为以下字段创建索引：

### 高频查询字段
- `posts.slug` (UNIQUE)
- `posts.status`
- `posts.published_at`
- `posts.author_id`
- `posts.category_id`
- `users.email` (UNIQUE)
- `users.username` (UNIQUE)
- `categories.slug` (UNIQUE)
- `tags.slug` (UNIQUE)

### 组合索引
- `(posts.status, posts.published_at)` - 用于文章列表查询
- `(comments.post_id, comments.status)` - 用于评论查询
- `(views_log.post_id, views_log.viewed_at)` - 用于浏览统计
- `(notifications.user_id, notifications.read_at)` - 用于通知查询

### 全文搜索索引
- `posts.title`, `posts.content` - 用于文章搜索
- `users.username`, `user_profiles.display_name` - 用于用户搜索

---

## 性能优化建议

1. **分区表考虑**: 对于 `views_log` 和 `audit_logs` 等日志表，可考虑按时间分区。
2. **读写分离**: 对于高并发场景，可配置主从数据库。
3. **缓存策略**: 使用 Redis 缓存热点数据，如文章列表、用户信息等。
4. **异步处理**: 使用消息队列处理邮件发送、通知推送等耗时操作。
5. **数据归档**: 定期归档历史数据，如旧的浏览日志、审计日志等。

---

## 🚀 针对前端任务流的数据库优化

### 📊 **前端功能支持映射**

#### 认证系统支持
- **用户注册/登录**: `users` 表支持本地账号和 OAuth2 登录
- **用户资料管理**: `user_profiles` 表完整支持前端用户卡片和设置页面
- **权限控制**: RBAC 系统支持前端路由权限和组件权限控制
- **会话管理**: `sessions` 表支持 JWT 令牌管理和设备管理

#### 博客核心功能支持
- **文章展示**: `posts` 表支持前端文章卡片、列表、详情页所有字段需求
- **分类标签**: 支持前端标签云、分类导航、筛选功能
- **搜索功能**: `search_logs` 表支持搜索历史和热门搜索推荐
- **评论互动**: 完整的嵌套评论系统，支持点赞、回复、举报功能

#### 管理员功能支持
- **内容管理**: 支持草稿、发布、归档等状态管理
- **用户管理**: 完整的用户权限和状态管理
- **统计分析**: `user_stats`、`page_views` 表支持仪表板数据展示

#### 用户体验优化
- **个性化设置**: `user_settings` 表支持主题、语言等偏好设置
- **访问统计**: 详细的页面浏览和用户行为分析
- **通知系统**: 站内通知和邮件订阅完整支持

### 🔍 **前端特定查询优化索引**

```sql
-- 文章列表页查询优化
CREATE INDEX idx_posts_status_published_featured ON posts(status, published_at DESC, is_featured);
CREATE INDEX idx_posts_category_status ON posts(category_id, status, published_at DESC);
CREATE INDEX idx_posts_author_status ON posts(author_id, status, published_at DESC);

-- 搜索功能优化
CREATE INDEX idx_posts_fulltext ON posts USING gin(to_tsvector('chinese', title || ' ' || content));
CREATE INDEX idx_search_logs_query ON search_logs(query);
CREATE INDEX idx_search_logs_user_created ON search_logs(user_id, created_at DESC);

-- 评论系统优化
CREATE INDEX idx_comments_post_status_level ON comments(post_id, status, level, created_at);
CREATE INDEX idx_comments_parent_created ON comments(parent_id, created_at);
CREATE INDEX idx_comments_user_status ON comments(user_id, status, created_at DESC);

-- 用户统计优化
CREATE INDEX idx_page_views_post_date ON page_views(post_id, viewed_at);
CREATE INDEX idx_page_views_user_date ON page_views(user_id, viewed_at);
CREATE INDEX idx_page_views_type_date ON page_views(page_type, viewed_at);

-- 标签云和分类导航优化
CREATE INDEX idx_tags_post_count ON tags(post_count DESC);
CREATE INDEX idx_categories_parent_order ON categories(parent_id, sort_order);

-- 通知系统优化
CREATE INDEX idx_notifications_user_read ON notifications(user_id, read_at, created_at DESC);
CREATE INDEX idx_notifications_type_created ON notifications(type, created_at DESC);
```

### 📈 **缓存策略建议**

#### Redis 缓存键命名规范
```
# 文章相关
blog:post:{slug}                    # 文章详情
blog:posts:list:{category}:{page}   # 文章列表
blog:posts:featured                 # 精选文章
blog:posts:popular                  # 热门文章

# 用户相关
user:profile:{id}                   # 用户资料
user:stats:{id}                     # 用户统计
user:settings:{id}                  # 用户设置

# 分类标签
blog:categories:tree                # 分类树形结构
blog:tags:cloud                     # 标签云数据
blog:tags:popular                   # 热门标签

# 搜索相关
search:suggestions                  # 搜索建议
search:popular                      # 热门搜索

# 统计相关
stats:site:overview                 # 网站概览统计
stats:posts:views:{period}          # 文章浏览统计
```

#### 缓存过期策略
- **文章详情**: 1小时，内容更新时主动失效
- **文章列表**: 15分钟，新文章发布时失效
- **用户资料**: 30分钟，用户更新时失效
- **统计数据**: 5分钟，实时计算更新
- **分类标签**: 24小时，结构变更时失效

### 🔄 **数据同步策略**

#### 统计数据更新
```sql
-- 文章统计字段更新触发器
CREATE OR REPLACE FUNCTION update_post_stats()
RETURNS TRIGGER AS $$
BEGIN
    -- 更新评论数量
    IF TG_TABLE_NAME = 'comments' THEN
        UPDATE posts SET comment_count = (
            SELECT COUNT(*) FROM comments 
            WHERE post_id = COALESCE(NEW.post_id, OLD.post_id) 
            AND status = 'approved'
        ) WHERE id = COALESCE(NEW.post_id, OLD.post_id);
    END IF;
    
    -- 更新用户统计
    IF TG_TABLE_NAME = 'posts' THEN
        UPDATE user_stats SET 
            posts_count = (SELECT COUNT(*) FROM posts WHERE author_id = COALESCE(NEW.author_id, OLD.author_id) AND status = 'published'),
            drafts_count = (SELECT COUNT(*) FROM posts WHERE author_id = COALESCE(NEW.author_id, OLD.author_id) AND status = 'draft')
        WHERE user_id = COALESCE(NEW.author_id, OLD.author_id);
    END IF;
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- 创建触发器
CREATE TRIGGER trigger_update_post_stats
    AFTER INSERT OR UPDATE OR DELETE ON comments
    FOR EACH ROW EXECUTE FUNCTION update_post_stats();

CREATE TRIGGER trigger_update_user_stats
    AFTER INSERT OR UPDATE OR DELETE ON posts
    FOR EACH ROW EXECUTE FUNCTION update_post_stats();
```

---

## 🎯 **前后端集成建议**

### TypeScript 类型定义示例
```typescript
// 对应数据库 posts 表
export interface Post {
  id: number;
  authorId: number;
  categoryId?: number;
  title: string;
  slug: string;
  excerpt?: string;
  content: string;
  coverImageUrl?: string;
  status: 'draft' | 'published' | 'archived' | 'deleted';
  visibility: 'public' | 'private' | 'password_protected';
  viewCount: number;
  likeCount: number;
  commentCount: number;
  shareCount: number;
  readingTime?: number;
  wordCount?: number;
  isFeatured: boolean;
  isTop: boolean;
  allowComments: boolean;
  seoTitle?: string;
  seoDescription?: string;
  seoKeywords?: string;
  publishedAt?: string;
  scheduledAt?: string;
  createdAt: string;
  updatedAt: string;
  
  // 关联数据
  author?: User;
  category?: Category;
  tags?: Tag[];
}

// 对应数据库 users 表和 user_profiles 表
export interface User {
  id: number;
  username: string;
  email: string;
  accountType: 'local' | 'oauth';
  oauthProvider?: 'github' | 'gitee' | 'google';
  status: 'active' | 'inactive' | 'banned';
  emailVerified: boolean;
  lastLoginAt?: string;
  createdAt: string;
  updatedAt: string;
  
  // 扩展信息
  profile?: UserProfile;
  stats?: UserStats;
}

// API 响应格式
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
  timestamp: string;
}

export interface PaginationResult<T> {
  content: T[];
  page: number;
  size: number;
  total: number;
  totalPages: number;
}
```

### 数据库迁移脚本
```sql
-- 初始角色权限数据
INSERT INTO roles (name, description) VALUES 
('ADMIN', '系统管理员'),
('AUTHOR', '作者'),
('USER', '普通用户');

INSERT INTO permissions (name, description, resource, action) VALUES
('POST_CREATE', '创建文章', 'post', 'create'),
('POST_UPDATE', '更新文章', 'post', 'update'),
('POST_DELETE', '删除文章', 'post', 'delete'),
('POST_PUBLISH', '发布文章', 'post', 'publish'),
('COMMENT_MODERATE', '评论审核', 'comment', 'moderate'),
('USER_MANAGE', '用户管理', 'user', 'manage');

-- 角色权限关联
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'AUTHOR' AND p.resource = 'post';
```

---

## 📚 扩展性考虑

此数据库设计完全支持前端任务流中提到的所有功能，并预留了以下扩展空间：

- **多语言支持**: 可扩展 `post_translations` 表
- **文章协作编辑**: 基于 `post_revisions` 表实现版本控制
- **高级搜索**: 支持全文搜索和多条件筛选
- **内容审核工作流**: 扩展 `status` 字段支持审核流程
- **API 访问控制**: 可扩展 `api_tokens` 表实现 API 认证
- **插件系统**: 通过 `post_meta` 和 `user_settings` 支持插件扩展
- **多租户支持**: 可扩展支持多个博客实例
