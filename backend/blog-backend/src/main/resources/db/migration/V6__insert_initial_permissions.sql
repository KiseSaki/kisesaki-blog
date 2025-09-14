-- V6__insert_initial_permissions.sql
-- Flyway migration: Insert initial permission data
-- Generated based on requirement specification

-- 文章相关权限 (post:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'post:create',
        '创建文章权限',
        'post',
        'create'
    ),
    (
        'post:edit:own',
        '编辑自己文章的权限',
        'post',
        'edit:own'
    ),
    (
        'post:edit:all',
        '编辑所有文章的权限',
        'post',
        'edit:all'
    ),
    (
        'post:delete:own',
        '删除自己文章的权限',
        'post',
        'delete:own'
    ),
    (
        'post:delete:all',
        '删除所有文章的权限',
        'post',
        'delete:all'
    ),
    (
        'post:publish',
        '发布文章权限',
        'post',
        'publish'
    ),
    (
        'post:manage',
        '文章管理权限（包括设置精选、置顶等）',
        'post',
        'manage'
    ),
    (
        'post:revision:view',
        '查看文章版本历史权限',
        'post',
        'revision:view'
    ),
    (
        'post:revision:restore',
        '恢复文章版本权限',
        'post',
        'revision:restore'
    );

-- 用户相关权限 (user:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'user:manage',
        '用户管理权限',
        'user',
        'manage'
    ),
    (
        'user:ban',
        '封禁用户权限',
        'user',
        'ban'
    ),
    (
        'user:unban',
        '解封用户权限',
        'user',
        'unban'
    );

-- 角色权限管理 (role:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'role:view',
        '查看角色权限',
        'role',
        'view'
    ),
    (
        'role:create',
        '创建角色权限',
        'role',
        'create'
    ),
    (
        'role:edit',
        '编辑角色权限',
        'role',
        'edit'
    ),
    (
        'role:delete',
        '删除角色权限',
        'role',
        'delete'
    ),
    (
        'role:assign',
        '分配角色权限',
        'role',
        'assign'
    );

-- 权限管理 (permission:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'permission:view',
        '查看权限权限',
        'permission',
        'view'
    ),
    (
        'permission:create',
        '创建权限权限',
        'permission',
        'create'
    ),
    (
        'permission:edit',
        '编辑权限权限',
        'permission',
        'edit'
    ),
    (
        'permission:delete',
        '删除权限权限',
        'permission',
        'delete'
    );

-- 分类管理权限 (category:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'category:view',
        '查看分类权限',
        'category',
        'view'
    ),
    (
        'category:create',
        '创建分类权限',
        'category',
        'create'
    ),
    (
        'category:edit',
        '编辑分类权限',
        'category',
        'edit'
    ),
    (
        'category:delete',
        '删除分类权限',
        'category',
        'delete'
    );

-- 标签管理权限 (tag:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'tag:create',
        '创建标签权限',
        'tag',
        'create'
    ),
    (
        'tag:edit',
        '编辑标签权限',
        'tag',
        'edit'
    ),
    (
        'tag:delete',
        '删除标签权限',
        'tag',
        'delete'
    ),
    (
        'tag:manage',
        '标签管理权限（包括审核）',
        'tag',
        'manage'
    );

-- 评论相关权限 (comment:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'comment:create',
        '发表评论权限',
        'comment',
        'create'
    ),
    (
        'comment:manage',
        '评论管理权限',
        'comment',
        'manage'
    ),
    (
        'comment:moderate',
        '评论审核权限',
        'comment',
        'moderate'
    ),
    (
        'comment:delete',
        '删除评论权限',
        'comment',
        'delete'
    );

-- 文件管理权限 (file:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'file:upload',
        '文件上传权限',
        'file',
        'upload'
    ),
    (
        'file:manage',
        '文件管理权限',
        'file',
        'manage'
    ),
    (
        'file:delete',
        '文件删除权限',
        'file',
        'delete'
    );

-- 统计分析权限 (analytics:* & stats:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'analytics:view',
        '查看统计数据权限',
        'analytics',
        'view'
    ),
    (
        'stats:view',
        '查看统计信息权限',
        'stats',
        'view'
    );

-- 通知权限 (notification:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'notification:send',
        '发送通知权限',
        'notification',
        'send'
    ),
    (
        'notification:manage',
        '通知管理权限',
        'notification',
        'manage'
    );

-- 订阅权限 (subscription:* & newsletter:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'subscription:manage',
        '订阅管理权限',
        'subscription',
        'manage'
    ),
    (
        'newsletter:send',
        '发送新闻通讯权限',
        'newsletter',
        'send'
    ),
    (
        'newsletter:manage',
        '新闻通讯管理权限',
        'newsletter',
        'manage'
    );

-- 系统管理权限 (system:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'system:manage',
        '系统管理权限',
        'system',
        'manage'
    ),
    (
        'system:monitor',
        '系统监控权限',
        'system',
        'monitor'
    ),
    (
        'system:backup',
        '系统备份权限',
        'system',
        'backup'
    ),
    (
        'system:restore',
        '系统恢复权限',
        'system',
        'restore'
    );

-- SEO管理权限 (seo:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'seo:manage',
        'SEO管理权限',
        'seo',
        'manage'
    );

-- 重定向管理权限 (redirect:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'redirect:manage',
        '重定向管理权限',
        'redirect',
        'manage'
    ),
    (
        'redirect:create',
        '创建重定向权限',
        'redirect',
        'create'
    ),
    (
        'redirect:edit',
        '编辑重定向权限',
        'redirect',
        'edit'
    ),
    (
        'redirect:delete',
        '删除重定向权限',
        'redirect',
        'delete'
    );

-- 审计权限 (audit:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'audit:view',
        '查看审计日志权限',
        'audit',
        'view'
    ),
    (
        'audit:export',
        '导出审计日志权限',
        'audit',
        'export'
    );

-- 举报管理权限 (report:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'report:manage',
        '举报管理权限',
        'report',
        'manage'
    ),
    (
        'report:action',
        '举报处理权限',
        'report',
        'action'
    );

-- 搜索管理权限 (search:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'search:manage',
        '搜索管理权限',
        'search',
        'manage'
    );

-- API令牌权限 (token:*)
INSERT INTO
    permissions (
        name,
        description,
        resource,
        "action"
    )
VALUES (
        'token:manage',
        'API令牌管理权限',
        'token',
        'manage'
    );

-- End of migration