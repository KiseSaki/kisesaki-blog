-- V4__create_user_follows_table.sql
-- Flyway migration: create user_follows table
-- 与 Java 实体 UserFollow 对应，表名: user_follows

CREATE TABLE IF NOT EXISTS user_follows (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 防止重复关注: 一个 follower 对同一 following 只能存在一条记录
CREATE UNIQUE INDEX IF NOT EXISTS ux_user_follows_follower_following ON user_follows (follower_id, following_id);

-- 防止自关注
ALTER TABLE user_follows
ADD CONSTRAINT chk_user_follows_no_self_follow CHECK (follower_id <> following_id);

-- 外键约束，引用主用户表 "user" (注意双引号，因为 user 为保留字，V1 中已使用双引号)
ALTER TABLE user_follows
ADD CONSTRAINT fk_user_follows_follower_user FOREIGN KEY (follower_id) REFERENCES "user" (id) ON DELETE CASCADE,
ADD CONSTRAINT fk_user_follows_following_user FOREIGN KEY (following_id) REFERENCES "user" (id) ON DELETE CASCADE;

-- 常用索引，便于按 follower 或 following 查询
CREATE INDEX IF NOT EXISTS ix_user_follows_follower_id ON user_follows (follower_id);

CREATE INDEX IF NOT EXISTS ix_user_follows_following_id ON user_follows (following_id);

-- 备注: 使用 BIGSERIAL 让数据库生成自增 id；如果应用层使用雪花或其它策略，请调整为 BIGINT 并在应用端写入 id。