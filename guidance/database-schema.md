# 数据库表结构规范 (Database Schema)

本文档定义了 Kisesaki Blog 项目的完整数据库表结构。所有表名和字段名均采用 `snake_case` 命名法。

这是一个成熟、功能齐全的个人博客「完全体」数据库表设计，涵盖用户与权限、文章管理、互动系统、媒体资源、通知订阅，以及审计与系统配置等各个方面。

---

## 用户与权限

为多角色、多权限体系、个性化设置与会话管理提供支持。

#### 1. `users` - 用户表

存储用户账号与基本认证信息。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 用户唯一ID (自增) |
| `username` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | 用户名 |
| `email` | `VARCHAR(100)` | `NOT NULL`, `UNIQUE` | 电子邮箱 |
| `password` | `VARCHAR(255)` | `NOT NULL` | 加密后的密码 |
| `status` | `VARCHAR(20)` | `NOT NULL`, `DEFAULT 'active'` | 用户状态 ('active', 'inactive', 'banned') |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |

#### 2. `user_profiles` - 用户扩展信息表

存储用户的详细个人信息。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 主键ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK to users.id` | 用户ID |
| `display_name` | `VARCHAR(100)` | | 显示名称 |
| `bio` | `TEXT` | | 个人简介 |
| `avatar_url` | `VARCHAR(255)` | | 用户头像URL |
| `website_url` | `VARCHAR(255)` | | 个人网站URL |
| `location` | `VARCHAR(100)` | | 所在地 |
| `social_links` | `JSONB` | | 社交媒体链接 (JSON格式) |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |

#### 3. `roles` - 角色表

定义系统中的用户角色。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 角色唯一ID (自增) |
| `name` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | 角色名称 |
| `description` | `VARCHAR(255)` | | 角色描述 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |

#### 4. `permissions` - 权限表

定义系统中的权限动作。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 权限唯一ID (自增) |
| `name` | `VARCHAR(100)` | `NOT NULL`, `UNIQUE` | 权限名称 |
| `description` | `VARCHAR(255)` | | 权限描述 |
| `resource` | `VARCHAR(50)` | `NOT NULL` | 资源类型 |
| `action` | `VARCHAR(50)` | `NOT NULL` | 操作类型 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |

#### 5. `user_roles` - 用户角色关联表

实现用户与角色的多对多关系。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 用户ID |
| `role_id` | `BIGINT` | `NOT NULL`, `FK to roles.id` | 角色ID |
| `assigned_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 分配时间 |
| | | `PRIMARY KEY (user_id, role_id)` | 联合主键 |

#### 6. `role_permissions` - 角色权限关联表

实现角色与权限的多对多关系。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `role_id` | `BIGINT` | `NOT NULL`, `FK to roles.id` | 角色ID |
| `permission_id` | `BIGINT` | `NOT NULL`, `FK to permissions.id` | 权限ID |
| `granted_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 授予时间 |
| | | `PRIMARY KEY (role_id, permission_id)` | 联合主键 |

#### 7. `sessions` - 用户会话表

管理用户登录会话。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 会话唯一ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 用户ID |
| `token` | `VARCHAR(255)` | `NOT NULL`, `UNIQUE` | 会话令牌 |
| `ip_address` | `INET` | | 客户端IP地址 |
| `user_agent` | `TEXT` | | 用户代理字符串 |
| `expires_at` | `TIMESTAMPTZ` | `NOT NULL` | 过期时间 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |

#### 8. `user_settings` - 用户设置表

存储用户的个性化偏好设置。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 设置唯一ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 用户ID |
| `setting_key` | `VARCHAR(100)` | `NOT NULL` | 设置键名 |
| `setting_value` | `TEXT` | | 设置值 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |
| | | `UNIQUE (user_id, setting_key)` | 联合唯一索引 |

---

## 文章管理

支持文章发布、版本控制、分类/标签管理及 SEO 扩展。

#### 9. `posts` - 文章表

存储博客文章主体信息。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 文章唯一ID (自增) |
| `author_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 作者ID |
| `category_id` | `BIGINT` | `FK to categories.id` | 分类ID |
| `title` | `VARCHAR(255)` | `NOT NULL` | 文章标题 |
| `slug` | `VARCHAR(255)` | `NOT NULL`, `UNIQUE` | 文章的URL友好别名 |
| `summary` | `TEXT` | | 文章摘要 |
| `content` | `TEXT` | `NOT NULL` | 文章主体内容 (Markdown格式) |
| `cover_image_url` | `VARCHAR(255)` | | 封面图片URL |
| `status` | `VARCHAR(20)` | `NOT NULL`, `DEFAULT 'draft'` | 文章状态 ('draft', 'published', 'archived') |
| `view_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 浏览次数 |
| `like_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 点赞数量 |
| `comment_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 评论数量 |
| `is_featured` | `BOOLEAN` | `NOT NULL`, `DEFAULT FALSE` | 是否为精选文章 |
| `is_top` | `BOOLEAN` | `NOT NULL`, `DEFAULT FALSE` | 是否置顶 |
| `published_at` | `TIMESTAMPTZ` | | 发布时间 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |

#### 10. `post_revisions` - 文章版本历史表

存储文章的历史版本，用于版本控制。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 版本唯一ID (自增) |
| `post_id` | `BIGINT` | `NOT NULL`, `FK to posts.id` | 文章ID |
| `version` | `INT` | `NOT NULL` | 版本号 |
| `title` | `VARCHAR(255)` | `NOT NULL` | 标题 |
| `content` | `TEXT` | `NOT NULL` | 内容 |
| `summary` | `TEXT` | | 摘要 |
| `created_by` | `BIGINT` | `NOT NULL`, `FK to users.id` | 创建者ID |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| | | `UNIQUE (post_id, version)` | 联合唯一索引 |

#### 11. `post_meta` - 文章元数据表

存储文章的SEO和自定义字段信息。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 元数据唯一ID (自增) |
| `post_id` | `BIGINT` | `NOT NULL`, `FK to posts.id` | 文章ID |
| `meta_key` | `VARCHAR(100)` | `NOT NULL` | 元数据键名 |
| `meta_value` | `TEXT` | | 元数据值 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |
| | | `UNIQUE (post_id, meta_key)` | 联合唯一索引 |

#### 12. `categories` - 分类表

存储文章分类，支持层级结构。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 分类唯一ID (自增) |
| `name` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | 分类名称 |
| `slug` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | 分类的URL友好别名 |
| `description` | `VARCHAR(255)` | | 分类描述 |
| `parent_id` | `BIGINT` | `FK to categories.id` | 父分类ID (自关联) |
| `sort_order` | `INT` | `NOT NULL`, `DEFAULT 0` | 排序顺序 |
| `post_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 文章数量 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |

#### 13. `tags` - 标签表

存储文章标签。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 标签唯一ID (自增) |
| `name` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | 标签名称 |
| `slug` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | 标签的URL友好别名 |
| `description` | `VARCHAR(255)` | | 标签描述 |
| `color` | `VARCHAR(7)` | | 标签颜色 (HEX格式) |
| `post_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 使用该标签的文章数量 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |

#### 14. `post_tags` - 文章与标签关联表

用于实现文章和标签的多对多关系。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `post_id` | `BIGINT` | `NOT NULL`, `FK to posts.id` | 文章ID |
| `tag_id` | `BIGINT` | `NOT NULL`, `FK to tags.id` | 标签ID |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| | | `PRIMARY KEY (post_id, tag_id)` | 联合主键 |

---

## 评论与互动

评论嵌套、点赞/收藏、关注、浏览日志等功能。

#### 15. `comments` - 评论表

存储文章的评论，支持嵌套回复。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 评论唯一ID (自增) |
| `post_id` | `BIGINT` | `NOT NULL`, `FK to posts.id` | 所属文章ID |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 评论用户ID |
| `parent_id` | `BIGINT` | `FK to comments.id` | 父评论ID (用于实现嵌套评论) |
| `content` | `TEXT` | `NOT NULL` | 评论内容 |
| `like_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 点赞数量 |
| `reply_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 回复数量 |
| `status` | `VARCHAR(20)` | `NOT NULL`, `DEFAULT 'approved'` | 评论状态 ('pending', 'approved', 'rejected') |
| `ip_address` | `INET` | | 评论者IP地址 |
| `user_agent` | `TEXT` | | 用户代理字符串 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |

#### 16. `comment_reactions` - 评论反应表

存储对评论的点赞/踩等反应。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 反应唯一ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 用户ID |
| `comment_id` | `BIGINT` | `NOT NULL`, `FK to comments.id` | 评论ID |
| `reaction_type` | `VARCHAR(20)` | `NOT NULL` | 反应类型 ('like', 'dislike') |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| | | `UNIQUE (user_id, comment_id)` | 联合唯一索引 |

#### 17. `likes` - 点赞表

通用点赞表，支持对文章、评论等的点赞。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 点赞唯一ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 用户ID |
| `target_id` | `BIGINT` | `NOT NULL` | 目标对象ID |
| `target_type` | `VARCHAR(50)` | `NOT NULL` | 目标对象类型 ('post', 'comment') |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| | | `UNIQUE (user_id, target_id, target_type)` | 联合唯一索引 |

#### 18. `favorites` - 收藏表

存储用户收藏的文章。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 收藏唯一ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 用户ID |
| `post_id` | `BIGINT` | `NOT NULL`, `FK to posts.id` | 文章ID |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 收藏时间 |
| | | `UNIQUE (user_id, post_id)` | 联合唯一索引 |

#### 19. `followings` - 关注表

用户之间的关注关系。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 关注唯一ID (自增) |
| `follower_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 关注者ID |
| `followee_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 被关注者ID |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 关注时间 |
| | | `UNIQUE (follower_id, followee_id)` | 联合唯一索引 |

#### 20. `views_log` - 浏览日志表

记录文章的浏览记录。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 浏览记录唯一ID (自增) |
| `post_id` | `BIGINT` | `NOT NULL`, `FK to posts.id` | 文章ID |
| `user_id` | `BIGINT` | `FK to users.id` | 用户ID (可为空，支持匿名访问) |
| `ip_address` | `INET` | | 访问者IP地址 |
| `user_agent` | `TEXT` | | 用户代理字符串 |
| `referrer` | `VARCHAR(255)` | | 来源页面 |
| `viewed_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 浏览时间 |

---

## 媒体与资源

集中管理各种图片、附件及文件。

#### 21. `media_resources` - 媒体资源表

存储各种媒体文件信息。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 媒体资源唯一ID (自增) |
| `url` | `VARCHAR(500)` | `NOT NULL` | 文件访问URL |
| `filename` | `VARCHAR(255)` | `NOT NULL` | 原始文件名 |
| `file_path` | `VARCHAR(500)` | `NOT NULL` | 文件存储路径 |
| `file_type` | `VARCHAR(100)` | `NOT NULL` | 文件MIME类型 |
| `file_size` | `BIGINT` | `NOT NULL` | 文件大小 (字节) |
| `width` | `INT` | | 图片宽度 (仅图片类型) |
| `height` | `INT` | | 图片高度 (仅图片类型) |
| `alt_text` | `VARCHAR(255)` | | 替代文本 |
| `description` | `TEXT` | | 文件描述 |
| `uploaded_by` | `BIGINT` | `NOT NULL`, `FK to users.id` | 上传者ID |
| `usage_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 使用次数 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 上传时间 |

---

## 通知与订阅

站内通知、邮件订阅与新闻通讯记录。

#### 22. `notifications` - 通知表

存储站内通知信息。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 通知唯一ID (自增) |
| `user_id` | `BIGINT` | `NOT NULL`, `FK to users.id` | 接收用户ID |
| `type` | `VARCHAR(50)` | `NOT NULL` | 通知类型 ('comment', 'like', 'follow', 'system') |
| `title` | `VARCHAR(255)` | `NOT NULL` | 通知标题 |
| `content` | `TEXT` | | 通知内容 |
| `payload` | `JSONB` | | 通知负载数据 (JSON格式) |
| `read_at` | `TIMESTAMPTZ` | | 阅读时间 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |

#### 23. `subscriptions` - 订阅表

管理邮件订阅。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 订阅唯一ID (自增) |
| `user_id` | `BIGINT` | `FK to users.id` | 用户ID (可为空，支持匿名订阅) |
| `email` | `VARCHAR(100)` | `NOT NULL` | 订阅邮箱 |
| `status` | `VARCHAR(20)` | `NOT NULL`, `DEFAULT 'active'` | 订阅状态 ('active', 'unsubscribed') |
| `subscription_type` | `VARCHAR(50)` | `NOT NULL` | 订阅类型 ('newsletter', 'comment_reply', 'new_post') |
| `token` | `VARCHAR(255)` | `NOT NULL`, `UNIQUE` | 订阅令牌 (用于取消订阅) |
| `subscribed_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 订阅时间 |
| `unsubscribed_at` | `TIMESTAMPTZ` | | 取消订阅时间 |

#### 24. `newsletter_logs` - 通讯发送记录表

记录新闻通讯的发送记录。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 发送记录唯一ID (自增) |
| `subscription_id` | `BIGINT` | `NOT NULL`, `FK to subscriptions.id` | 订阅ID |
| `newsletter_id` | `BIGINT` | | 通讯ID (可关联到具体的通讯内容) |
| `subject` | `VARCHAR(255)` | `NOT NULL` | 邮件主题 |
| `content` | `TEXT` | `NOT NULL` | 邮件内容 |
| `status` | `VARCHAR(20)` | `NOT NULL` | 发送状态 ('pending', 'sent', 'failed') |
| `sent_at` | `TIMESTAMPTZ` | | 发送时间 |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |

---

## 审计与系统配置

操作审计、全局配置与 URL 重定向、SEO 默认设置等。

#### 25. `audit_logs` - 审计日志表

记录用户和系统的重要操作。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 日志唯一ID (自增) |
| `user_id` | `BIGINT` | `FK to users.id` | 操作用户ID (可为空，系统操作) |
| `action` | `VARCHAR(100)` | `NOT NULL` | 操作类型 |
| `target_table` | `VARCHAR(100)` | | 目标表名 |
| `target_id` | `BIGINT` | | 目标记录ID |
| `old_values` | `JSONB` | | 操作前的值 (JSON格式) |
| `new_values` | `JSONB` | | 操作后的值 (JSON格式) |
| `ip_address` | `INET` | | 操作者IP地址 |
| `user_agent` | `TEXT` | | 用户代理字符串 |
| `meta` | `JSONB` | | 额外的元数据 (JSON格式) |
| `timestamp` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 操作时间 |

#### 26. `system_settings` - 系统设置表

存储全局系统配置参数。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 设置唯一ID (自增) |
| `key` | `VARCHAR(100)` | `NOT NULL`, `UNIQUE` | 设置键名 |
| `value` | `TEXT` | | 设置值 |
| `type` | `VARCHAR(20)` | `NOT NULL`, `DEFAULT 'string'` | 值类型 ('string', 'number', 'boolean', 'json') |
| `description` | `VARCHAR(255)` | | 设置描述 |
| `is_public` | `BOOLEAN` | `NOT NULL`, `DEFAULT FALSE` | 是否为公开设置 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |
| `updated_by` | `BIGINT` | `FK to users.id` | 更新者ID |

#### 27. `redirects` - 重定向表

管理URL重定向规则。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | 重定向唯一ID (自增) |
| `source_path` | `VARCHAR(500)` | `NOT NULL`, `UNIQUE` | 源路径 |
| `target_url` | `VARCHAR(500)` | `NOT NULL` | 目标URL |
| `code` | `INT` | `NOT NULL`, `DEFAULT 301` | HTTP状态码 (301/302) |
| `is_active` | `BOOLEAN` | `NOT NULL`, `DEFAULT TRUE` | 是否启用 |
| `hit_count` | `INT` | `NOT NULL`, `DEFAULT 0` | 重定向次数 |
| `created_by` | `BIGINT` | `NOT NULL`, `FK to users.id` | 创建者ID |
| `created_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 创建时间 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |

#### 28. `seo_settings` - SEO设置表

存储全站SEO的默认配置。

| 字段名 | 数据类型 | 约束 | 描述 |
| :--- | :--- | :--- | :--- |
| `id` | `BIGSERIAL` | `PRIMARY KEY` | SEO设置唯一ID (自增) |
| `key` | `VARCHAR(100)` | `NOT NULL`, `UNIQUE` | SEO设置键名 |
| `value` | `TEXT` | | SEO设置值 |
| `page_type` | `VARCHAR(50)` | | 页面类型 ('global', 'home', 'post', 'category', 'tag') |
| `description` | `VARCHAR(255)` | | 设置描述 |
| `updated_at` | `TIMESTAMPTZ` | `NOT NULL`, `DEFAULT NOW()` | 更新时间 |
| `updated_by` | `BIGINT` | `FK to users.id` | 更新者ID |

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

## 扩展性考虑

此数据库设计支持以下扩展：
- 多语言支持 (可扩展 `post_translations` 表)
- 文章协作编辑 (基于 `post_revisions` 表)
- 内容审核工作流 (扩展 `posts.status` 字段)
- 高级权限控制 (基于角色权限系统)
- 统计分析功能 (基于各种日志表)
- API 访问控制 (可扩展 `api_tokens` 表)
