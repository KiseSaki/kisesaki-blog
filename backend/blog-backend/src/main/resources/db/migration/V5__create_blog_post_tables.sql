-- V5__create_blog_post_tables.sql
-- Flyway migration: create blog-related tables (posts, categories, tags, post_tags, post_meta, post_revisions)
-- Generated based on Java entity classes under com.kisesaki.blog.content.*

-- 1) posts 表
CREATE TABLE IF NOT EXISTS posts (
    id BIGSERIAL PRIMARY KEY,
    author_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255),
    excerpt TEXT,
    content TEXT NOT NULL,
    html_content TEXT,
    cover_image_url VARCHAR(1024),
    featured_image_url VARCHAR(1024),
    status VARCHAR(32) NOT NULL DEFAULT 'draft',
    visibility VARCHAR(32) NOT NULL DEFAULT 'public',
    password VARCHAR(255),
    view_count INTEGER NOT NULL DEFAULT 0,
    like_count INTEGER NOT NULL DEFAULT 0,
    comment_count INTEGER NOT NULL DEFAULT 0,
    share_count INTEGER NOT NULL DEFAULT 0,
    reading_time INTEGER,
    word_count INTEGER,
    is_featured BOOLEAN NOT NULL DEFAULT FALSE,
    is_top BOOLEAN NOT NULL DEFAULT FALSE,
    allow_comments BOOLEAN NOT NULL DEFAULT TRUE,
    seo_title VARCHAR(255),
    seo_description VARCHAR(512),
    seo_keywords VARCHAR(512),
    published_at TIMESTAMPTZ,
    scheduled_at TIMESTAMPTZ,
    last_modified_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 唯一索引：slug
CREATE UNIQUE INDEX IF NOT EXISTS ux_posts_slug ON posts (slug);

-- 索引：author, category, status, visibility
CREATE INDEX IF NOT EXISTS ix_posts_author_id ON posts (author_id);

CREATE INDEX IF NOT EXISTS ix_posts_category_id ON posts (category_id);

CREATE INDEX IF NOT EXISTS ix_posts_status ON posts (status);

CREATE INDEX IF NOT EXISTS ix_posts_visibility ON posts (visibility);

-- 外键：引用 "user" 表（注意 user 在项目中用双引号）
ALTER TABLE posts
ADD CONSTRAINT fk_posts_author_user FOREIGN KEY (author_id) REFERENCES "user" (id) ON DELETE SET NULL;

-- 如果存在 categories 表，再添加外键约束（安全起见用 ALTER TABLE 添加）
ALTER TABLE posts
ADD CONSTRAINT fk_posts_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL;

-- 2) categories 表
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    slug VARCHAR(128),
    description TEXT,
    parent_id BIGINT,
    sort_order INTEGER DEFAULT 0,
    post_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_categories_slug ON categories (slug);

CREATE INDEX IF NOT EXISTS ix_categories_parent_id ON categories (parent_id);

ALTER TABLE categories
ADD CONSTRAINT fk_categories_parent FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE SET NULL;

-- 3) tags 表
CREATE TABLE IF NOT EXISTS tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    slug VARCHAR(128),
    description TEXT,
    color VARCHAR(32),
    post_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_tags_slug ON tags (slug);

-- 4) post_tags 关联表
CREATE TABLE IF NOT EXISTS post_tags (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (post_id, tag_id)
);

CREATE INDEX IF NOT EXISTS ix_post_tags_post_id ON post_tags (post_id);

CREATE INDEX IF NOT EXISTS ix_post_tags_tag_id ON post_tags (tag_id);

ALTER TABLE post_tags
ADD CONSTRAINT fk_post_tags_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_tags_tag FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE;

-- 5) post_meta 表
CREATE TABLE IF NOT EXISTS post_meta (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    meta_key VARCHAR(191) NOT NULL,
    meta_value TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_post_meta_post_key ON post_meta (post_id, meta_key);

CREATE INDEX IF NOT EXISTS ix_post_meta_post_id ON post_meta (post_id);

ALTER TABLE post_meta
ADD CONSTRAINT fk_post_meta_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

-- 6) post_revisions 表
CREATE TABLE IF NOT EXISTS post_revisions (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    version INTEGER NOT NULL,
    title VARCHAR(255),
    content TEXT,
    summary TEXT,
    created_by BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_post_revisions_post_version ON post_revisions (post_id, version);

CREATE INDEX IF NOT EXISTS ix_post_revisions_post_id ON post_revisions (post_id);

ALTER TABLE post_revisions
ADD CONSTRAINT fk_post_revisions_post FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
ADD CONSTRAINT fk_post_revisions_created_by_user FOREIGN KEY (created_by) REFERENCES "user" (id) ON DELETE SET NULL;

-- End of migration