-- V2__create_rbac_tables.sql
-- Flyway migration: create RBAC (Role-Based Access Control) tables
-- 创建角色、权限、用户角色关联、角色权限关联表

-- 角色表
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 权限表
CREATE TABLE IF NOT EXISTS permissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    resource VARCHAR(50) NOT NULL,
    action VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 用户角色关联表（多对多）
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, role_id)
);

-- 角色权限关联表（多对多）
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    granted_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (role_id, permission_id)
);

-- 外键约束
ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE user_roles
ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE;

ALTER TABLE role_permissions
ADD CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE;

ALTER TABLE role_permissions
ADD CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE;

-- 索引优化
CREATE INDEX IF NOT EXISTS ix_roles_name ON roles (name);

CREATE INDEX IF NOT EXISTS ix_permissions_name ON permissions (name);

CREATE INDEX IF NOT EXISTS ix_permissions_resource ON permissions (resource);

CREATE INDEX IF NOT EXISTS ix_permissions_resource_action ON permissions (resource, action);

CREATE INDEX IF NOT EXISTS ix_user_roles_user_id ON user_roles (user_id);

CREATE INDEX IF NOT EXISTS ix_user_roles_role_id ON user_roles (role_id);

CREATE INDEX IF NOT EXISTS ix_role_permissions_role_id ON role_permissions (role_id);

CREATE INDEX IF NOT EXISTS ix_role_permissions_permission_id ON role_permissions (permission_id);

-- 初始化基础角色数据
INSERT INTO
    roles (name, description)
VALUES ('ADMIN', '系统管理员'),
    ('AUTHOR', '作者'),
    ('USER', '普通用户')
ON CONFLICT (name) DO NOTHING;

-- 初始化基础权限数据
INSERT INTO
    permissions (
        name,
        description,
        resource,
        action
    )
VALUES
    -- 文章管理权限
    (
        'POST_CREATE',
        '创建文章',
        'post',
        'create'
    ),
    (
        'POST_UPDATE',
        '更新文章',
        'post',
        'update'
    ),
    (
        'POST_DELETE',
        '删除文章',
        'post',
        'delete'
    ),
    (
        'POST_PUBLISH',
        '发布文章',
        'post',
        'publish'
    ),
    (
        'POST_VIEW_ALL',
        '查看所有文章',
        'post',
        'view_all'
    ),

-- 评论管理权限
(
    'COMMENT_CREATE',
    '创建评论',
    'comment',
    'create'
),
(
    'COMMENT_DELETE',
    '删除评论',
    'comment',
    'delete'
),
(
    'COMMENT_MODERATE',
    '评论审核',
    'comment',
    'moderate'
),

-- 用户管理权限
(
    'USER_MANAGE',
    '用户管理',
    'user',
    'manage'
),
(
    'USER_VIEW_ALL',
    '查看用户列表',
    'user',
    'view_all'
),
(
    'USER_BAN',
    '禁用用户',
    'user',
    'ban'
),

-- 分类管理权限
(
    'CATEGORY_CREATE',
    '创建分类',
    'category',
    'create'
),
(
    'CATEGORY_UPDATE',
    '更新分类',
    'category',
    'update'
),
(
    'CATEGORY_DELETE',
    '删除分类',
    'category',
    'delete'
),

-- 标签管理权限
(
    'TAG_CREATE',
    '创建标签',
    'tag',
    'create'
),
(
    'TAG_UPDATE',
    '更新标签',
    'tag',
    'update'
),
(
    'TAG_DELETE',
    '删除标签',
    'tag',
    'delete'
),

-- 媒体管理权限
(
    'MEDIA_UPLOAD',
    '上传媒体文件',
    'media',
    'upload'
),
(
    'MEDIA_DELETE',
    '删除媒体文件',
    'media',
    'delete'
),
(
    'MEDIA_VIEW_ALL',
    '查看所有媒体文件',
    'media',
    'view_all'
),

-- 系统管理权限
(
    'SYSTEM_STATS_VIEW',
    '系统统计查看',
    'system',
    'stats_view'
),
(
    'SYSTEM_CONFIG_MANAGE',
    '系统配置管理',
    'system',
    'config_manage'
),

-- 仪表板权限
(
    'DASHBOARD_ADMIN_ACCESS',
    '管理员仪表板访问',
    'dashboard',
    'admin_access'
)
ON CONFLICT (name) DO NOTHING;

-- 初始化角色权限关联（管理员拥有所有权限）
INSERT INTO
    role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE
    r.name = 'ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 作者权限配置
INSERT INTO
    role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE
    r.name = 'AUTHOR'
    AND p.name IN (
        'POST_CREATE',
        'POST_UPDATE',
        'POST_PUBLISH',
        'COMMENT_CREATE',
        'COMMENT_DELETE',
        'CATEGORY_CREATE',
        'TAG_CREATE',
        'TAG_UPDATE',
        'MEDIA_UPLOAD'
    )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- 普通用户权限配置
INSERT INTO
    role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE
    r.name = 'USER'
    AND p.name IN (
        'COMMENT_CREATE',
        'MEDIA_UPLOAD'
    )
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- Notes:
-- 1) 使用 BIGSERIAL 为 roles 和 permissions 表生成自增主键
-- 2) user_roles 和 role_permissions 使用联合主键确保唯一性
-- 3) 所有关联表都设置了适当的外键约束和级联删除
-- 4) 创建了性能优化索引，特别是查询频繁的字段
-- 5) 使用 ON CONFLICT DO NOTHING 避免重复插入初始数据
-- 6) 预设了三种基础角色和相应的权限分配