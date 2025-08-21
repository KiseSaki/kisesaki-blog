-- V1__create_user_tables.sql
-- Flyway migration: create user, user_settings, user_profiles tables
-- 注意: Java 实体中 @TableName("user")，因此这里使用带引号的 "user" 表名以避免与 PostgreSQL 保留字冲突。

CREATE TABLE IF NOT EXISTS "user" (
    id BIGINT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password TEXT,
    oauth_id VARCHAR(255),
    oauth_provider VARCHAR(50),
    account_type VARCHAR(20) NOT NULL DEFAULT 'local',
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    email_verified_at TIMESTAMPTZ,
    last_login_at TIMESTAMPTZ,
    last_login_ip INET,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS ux_user_username ON "user" (username);

CREATE UNIQUE INDEX IF NOT EXISTS ux_user_email ON "user" (email);

CREATE INDEX IF NOT EXISTS ix_user_oauth_provider_oauth_id ON "user" (oauth_provider, oauth_id);

-- user_settings 表
CREATE TABLE IF NOT EXISTS user_settings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    setting_key VARCHAR(150) NOT NULL,
    setting_value TEXT,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE user_settings
ADD CONSTRAINT fk_user_settings_user FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS ix_user_settings_user_id ON user_settings (user_id);

CREATE UNIQUE INDEX IF NOT EXISTS ux_user_settings_user_key ON user_settings (user_id, setting_key);

-- user_profiles 表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    display_name VARCHAR(200),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    bio TEXT,
    avatar_url TEXT,
    cover_image_url TEXT,
    website_url TEXT,
    location VARCHAR(200),
    company VARCHAR(200),
    title VARCHAR(200),
    social_links JSONB,
    birth_date DATE,
    gender VARCHAR(20),
    timezone VARCHAR(100),
    language VARCHAR(20),
    theme_preference VARCHAR(20),
    privacy_level VARCHAR(20),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

ALTER TABLE user_profiles
ADD CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

CREATE INDEX IF NOT EXISTS ix_user_profiles_user_id ON user_profiles (user_id);

-- Notes:
-- 1) "user" 为保留字，且 Java 映射为 @TableName("user")；此处使用双引号确保表名精确匹配。
-- 2) User.id 采用应用端生成（雪花/ASSIGN_ID），因此没有 DEFAULT 或 sequence。如果希望数据库生成，需改为 BIGSERIAL 并调整应用逻辑。
-- 3) 在大表上创建索引时，建议在独立迁移中使用 CREATE INDEX CONCURRENTLY，以避免长时间锁表；CONCURRENTLY 不能在事务中执行，Flyway 的默认事务可能需要单独处理该语句。
-- 4) 如果想把每个表放单独的 migration 文件（便于回滚与增量部署），可拆分为 V1__create_user.sql V2__create_user_settings.sql 等。