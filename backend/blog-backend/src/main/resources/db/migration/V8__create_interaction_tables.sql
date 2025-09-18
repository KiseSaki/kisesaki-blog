-- V8__create_interaction_tables.sql
-- Flyway migration: Create tables for comments and user interactions
-- Generated for blog interaction features including comments, likes, favorites, follows, and page views

-- =============================================================================
-- 1. 评论表 (comments)
-- =============================================================================
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT,
    reply_to_id BIGINT,
    content TEXT NOT NULL,
    html_content TEXT,
    like_count INT NOT NULL DEFAULT 0,
    dislike_count INT NOT NULL DEFAULT 0,
    reply_count INT NOT NULL DEFAULT 0,
    level INT NOT NULL DEFAULT 0,
    path VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'approved',
    is_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    is_author_reply BOOLEAN NOT NULL DEFAULT FALSE,
    ip_address INET,
    user_agent TEXT,
    edited_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 添加外键约束
ALTER TABLE comments
ADD CONSTRAINT fk_comments_post_id FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

ALTER TABLE comments
ADD CONSTRAINT fk_comments_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE comments
ADD CONSTRAINT fk_comments_parent_id FOREIGN KEY (parent_id) REFERENCES comments (id) ON DELETE CASCADE;

ALTER TABLE comments
ADD CONSTRAINT fk_comments_reply_to_id FOREIGN KEY (reply_to_id) REFERENCES comments (id) ON DELETE SET NULL;

-- 添加约束检查
ALTER TABLE comments
ADD CONSTRAINT ck_comments_status CHECK (
    status IN (
        'pending',
        'approved',
        'rejected',
        'spam'
    )
);

ALTER TABLE comments
ADD CONSTRAINT ck_comments_level CHECK (level >= 0);

-- 创建索引
CREATE INDEX ix_comments_post_id ON comments (post_id);

CREATE INDEX ix_comments_user_id ON comments (user_id);

CREATE INDEX ix_comments_parent_id ON comments (parent_id);

CREATE INDEX ix_comments_reply_to_id ON comments (reply_to_id);

CREATE INDEX ix_comments_status ON comments (status);

CREATE INDEX ix_comments_created_at ON comments (created_at DESC);

CREATE INDEX ix_comments_level ON comments (level);

CREATE INDEX ix_comments_is_pinned ON comments (is_pinned);

CREATE INDEX ix_comments_deleted_at ON comments (deleted_at);

-- =============================================================================
-- 2. 评论反应表 (comment_reactions)
-- =============================================================================
CREATE TABLE comment_reactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    comment_id BIGINT NOT NULL,
    reaction_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, comment_id)
);

-- 添加外键约束
ALTER TABLE comment_reactions
ADD CONSTRAINT fk_comment_reactions_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE comment_reactions
ADD CONSTRAINT fk_comment_reactions_comment_id FOREIGN KEY (comment_id) REFERENCES comments (id) ON DELETE CASCADE;

-- 添加约束检查
ALTER TABLE comment_reactions
ADD CONSTRAINT ck_comment_reactions_reaction_type CHECK (
    reaction_type IN ('like', 'dislike')
);

-- 创建索引
CREATE INDEX ix_comment_reactions_user_id ON comment_reactions (user_id);

CREATE INDEX ix_comment_reactions_comment_id ON comment_reactions (comment_id);

CREATE INDEX ix_comment_reactions_reaction_type ON comment_reactions (reaction_type);

-- =============================================================================
-- 3. 点赞表 (likes)
-- =============================================================================
CREATE TABLE likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (
        user_id,
        target_id,
        target_type
    )
);

-- 添加外键约束
ALTER TABLE likes
ADD CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

-- 添加约束检查
ALTER TABLE likes
ADD CONSTRAINT ck_likes_target_type CHECK (
    target_type IN ('post', 'comment')
);

-- 创建索引
CREATE INDEX ix_likes_user_id ON likes (user_id);

CREATE INDEX ix_likes_target_id_type ON likes (target_id, target_type);

CREATE INDEX ix_likes_target_type ON likes (target_type);

CREATE INDEX ix_likes_created_at ON likes (created_at DESC);

-- =============================================================================
-- 4. 收藏表 (favorites)
-- =============================================================================
CREATE TABLE favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, post_id)
);

-- 添加外键约束
ALTER TABLE favorites
ADD CONSTRAINT fk_favorites_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE favorites
ADD CONSTRAINT fk_favorites_post_id FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE;

-- 创建索引
CREATE INDEX ix_favorites_user_id ON favorites (user_id);

CREATE INDEX ix_favorites_post_id ON favorites (post_id);

CREATE INDEX ix_favorites_created_at ON favorites (created_at DESC);

-- =============================================================================
-- 5. 关注表 (followings)
-- =============================================================================
CREATE TABLE followings (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    followee_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (follower_id, followee_id)
);

-- 添加外键约束
ALTER TABLE followings
ADD CONSTRAINT fk_followings_follower_id FOREIGN KEY (follower_id) REFERENCES "user" (id) ON DELETE CASCADE;

ALTER TABLE followings
ADD CONSTRAINT fk_followings_followee_id FOREIGN KEY (followee_id) REFERENCES "user" (id) ON DELETE CASCADE;

-- 添加约束检查 (防止自己关注自己)
ALTER TABLE followings
ADD CONSTRAINT ck_followings_no_self_follow CHECK (follower_id != followee_id);

-- 创建索引
CREATE INDEX ix_followings_follower_id ON followings (follower_id);

CREATE INDEX ix_followings_followee_id ON followings (followee_id);

CREATE INDEX ix_followings_created_at ON followings (created_at DESC);

-- =============================================================================
-- 6. 页面浏览记录表 (page_views)
-- =============================================================================
CREATE TABLE page_views (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT,
    user_id BIGINT,
    page_type VARCHAR(50) NOT NULL,
    page_url VARCHAR(500) NOT NULL,
    page_title VARCHAR(255),
    referrer VARCHAR(500),
    utm_source VARCHAR(100),
    utm_medium VARCHAR(100),
    utm_campaign VARCHAR(100),
    ip_address INET,
    user_agent TEXT,
    device_type VARCHAR(20),
    browser VARCHAR(50),
    os VARCHAR(50),
    session_id VARCHAR(255),
    duration INT,
    is_bounce BOOLEAN DEFAULT FALSE,
    viewed_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 添加外键约束
ALTER TABLE page_views
ADD CONSTRAINT fk_page_views_post_id FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE SET NULL;

ALTER TABLE page_views
ADD CONSTRAINT fk_page_views_user_id FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE SET NULL;

-- 添加约束检查
ALTER TABLE page_views
ADD CONSTRAINT ck_page_views_page_type CHECK (
    page_type IN (
        'post',
        'category',
        'tag',
        'home',
        'archive'
    )
);

ALTER TABLE page_views
ADD CONSTRAINT ck_page_views_device_type CHECK (
    device_type IS NULL
    OR device_type IN ('desktop', 'mobile', 'tablet')
);

ALTER TABLE page_views
ADD CONSTRAINT ck_page_views_duration CHECK (
    duration IS NULL
    OR duration >= 0
);

-- 创建索引
CREATE INDEX ix_page_views_post_id ON page_views (post_id);

CREATE INDEX ix_page_views_user_id ON page_views (user_id);

CREATE INDEX ix_page_views_page_type ON page_views (page_type);

CREATE INDEX ix_page_views_viewed_at ON page_views (viewed_at DESC);

CREATE INDEX ix_page_views_session_id ON page_views (session_id);

CREATE INDEX ix_page_views_ip_address ON page_views (ip_address);

CREATE INDEX ix_page_views_page_url ON page_views (page_url);

CREATE INDEX ix_page_views_utm_source ON page_views (utm_source);

-- =============================================================================
-- 创建触发器函数：自动更新 comments 表的 updated_at 字段
-- =============================================================================
CREATE OR REPLACE FUNCTION update_comments_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器
CREATE TRIGGER tr_comments_update_updated_at
    BEFORE UPDATE ON comments
    FOR EACH ROW
    EXECUTE FUNCTION update_comments_updated_at();

-- =============================================================================
-- 创建触发器函数：自动更新评论计数
-- =============================================================================
CREATE OR REPLACE FUNCTION update_comment_reply_count()
RETURNS TRIGGER AS $$
BEGIN
    -- 当插入新评论时，更新父评论的回复数量
    IF TG_OP = 'INSERT' AND NEW.parent_id IS NOT NULL THEN
        UPDATE comments 
        SET reply_count = reply_count + 1 
        WHERE id = NEW.parent_id;
    END IF;
    
    -- 当删除评论时，更新父评论的回复数量
    IF TG_OP = 'DELETE' AND OLD.parent_id IS NOT NULL THEN
        UPDATE comments 
        SET reply_count = reply_count - 1 
        WHERE id = OLD.parent_id;
    END IF;
    
    -- 当软删除评论时，更新父评论的回复数量
    IF TG_OP = 'UPDATE' AND OLD.deleted_at IS NULL AND NEW.deleted_at IS NOT NULL AND NEW.parent_id IS NOT NULL THEN
        UPDATE comments 
        SET reply_count = reply_count - 1 
        WHERE id = NEW.parent_id;
    END IF;
    
    -- 当恢复软删除评论时，更新父评论的回复数量
    IF TG_OP = 'UPDATE' AND OLD.deleted_at IS NOT NULL AND NEW.deleted_at IS NULL AND NEW.parent_id IS NOT NULL THEN
        UPDATE comments 
        SET reply_count = reply_count + 1 
        WHERE id = NEW.parent_id;
    END IF;
    
    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    ELSE
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器
CREATE TRIGGER tr_comments_update_reply_count
    AFTER INSERT OR UPDATE OR DELETE ON comments
    FOR EACH ROW
    EXECUTE FUNCTION update_comment_reply_count();

-- =============================================================================
-- 创建触发器函数：自动更新文章的评论数量
-- =============================================================================
CREATE OR REPLACE FUNCTION update_post_comment_count()
RETURNS TRIGGER AS $$
BEGIN
    -- 当插入新评论时，更新文章的评论数量
    IF TG_OP = 'INSERT' THEN
        UPDATE posts 
        SET comment_count = comment_count + 1 
        WHERE id = NEW.post_id;
    END IF;
    
    -- 当删除评论时，更新文章的评论数量
    IF TG_OP = 'DELETE' THEN
        UPDATE posts 
        SET comment_count = comment_count - 1 
        WHERE id = OLD.post_id;
    END IF;
    
    -- 当软删除评论时，更新文章的评论数量
    IF TG_OP = 'UPDATE' AND OLD.deleted_at IS NULL AND NEW.deleted_at IS NOT NULL THEN
        UPDATE posts 
        SET comment_count = comment_count - 1 
        WHERE id = NEW.post_id;
    END IF;
    
    -- 当恢复软删除评论时，更新文章的评论数量
    IF TG_OP = 'UPDATE' AND OLD.deleted_at IS NOT NULL AND NEW.deleted_at IS NULL THEN
        UPDATE posts 
        SET comment_count = comment_count + 1 
        WHERE id = NEW.post_id;
    END IF;
    
    IF TG_OP = 'DELETE' THEN
        RETURN OLD;
    ELSE
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器
CREATE TRIGGER tr_comments_update_post_comment_count
    AFTER INSERT OR UPDATE OR DELETE ON comments
    FOR EACH ROW
    EXECUTE FUNCTION update_post_comment_count();

-- =============================================================================
-- 添加表注释
-- =============================================================================
COMMENT ON TABLE comments IS '评论表，支持嵌套评论和丰富的交互功能';

COMMENT ON TABLE comment_reactions IS '评论反应表，存储对评论的点赞/踩等反应';

COMMENT ON TABLE likes IS '通用点赞表，支持对文章、评论等的点赞';

COMMENT ON TABLE favorites IS '收藏表，存储用户收藏的文章';

COMMENT ON TABLE followings IS '关注表，用户之间的关注关系';

COMMENT ON TABLE page_views IS '页面浏览记录表，记录网站各个页面的浏览记录';

-- =============================================================================
-- 添加字段注释
-- =============================================================================

-- comments 表字段注释
COMMENT ON COLUMN comments.id IS '评论唯一ID (自增)';

COMMENT ON COLUMN comments.post_id IS '所属文章ID';

COMMENT ON COLUMN comments.user_id IS '评论用户ID';

COMMENT ON COLUMN comments.parent_id IS '父评论ID (用于实现嵌套评论)';

COMMENT ON COLUMN comments.reply_to_id IS '回复目标评论ID (用于@功能)';

COMMENT ON COLUMN comments.content IS '评论内容';

COMMENT ON COLUMN comments.html_content IS '渲染后的HTML内容';

COMMENT ON COLUMN comments.like_count IS '点赞数量';

COMMENT ON COLUMN comments.dislike_count IS '踩数量';

COMMENT ON COLUMN comments.reply_count IS '直接回复数量';

COMMENT ON COLUMN comments.level IS '嵌套层级 (0为顶级评论)';

COMMENT ON COLUMN comments.path IS '评论路径 (如: "1.3.5" 表示层级关系)';

COMMENT ON COLUMN comments.status IS '评论状态 (pending, approved, rejected, spam)';

COMMENT ON COLUMN comments.is_pinned IS '是否置顶';

COMMENT ON COLUMN comments.is_author_reply IS '是否为作者回复';

COMMENT ON COLUMN comments.ip_address IS '评论者IP地址';

COMMENT ON COLUMN comments.user_agent IS '用户代理字符串';

COMMENT ON COLUMN comments.edited_at IS '最后编辑时间';

COMMENT ON COLUMN comments.deleted_at IS '删除时间 (软删除)';

COMMENT ON COLUMN comments.created_at IS '创建时间';

COMMENT ON COLUMN comments.updated_at IS '更新时间';

-- comment_reactions 表字段注释
COMMENT ON COLUMN comment_reactions.id IS '反应唯一ID (自增)';

COMMENT ON COLUMN comment_reactions.user_id IS '用户ID';

COMMENT ON COLUMN comment_reactions.comment_id IS '评论ID';

COMMENT ON COLUMN comment_reactions.reaction_type IS '反应类型 (like, dislike)';

COMMENT ON COLUMN comment_reactions.created_at IS '创建时间';

-- likes 表字段注释
COMMENT ON COLUMN likes.id IS '点赞唯一ID (自增)';

COMMENT ON COLUMN likes.user_id IS '用户ID';

COMMENT ON COLUMN likes.target_id IS '目标对象ID';

COMMENT ON COLUMN likes.target_type IS '目标对象类型 (post, comment)';

COMMENT ON COLUMN likes.created_at IS '创建时间';

-- favorites 表字段注释
COMMENT ON COLUMN favorites.id IS '收藏唯一ID (自增)';

COMMENT ON COLUMN favorites.user_id IS '用户ID';

COMMENT ON COLUMN favorites.post_id IS '文章ID';

COMMENT ON COLUMN favorites.created_at IS '收藏时间';

-- followings 表字段注释
COMMENT ON COLUMN followings.id IS '关注唯一ID (自增)';

COMMENT ON COLUMN followings.follower_id IS '关注者ID';

COMMENT ON COLUMN followings.followee_id IS '被关注者ID';

COMMENT ON COLUMN followings.created_at IS '关注时间';

-- page_views 表字段注释
COMMENT ON COLUMN page_views.id IS '浏览记录唯一ID (自增)';

COMMENT ON COLUMN page_views.post_id IS '文章ID (仅文章页面)';

COMMENT ON COLUMN page_views.user_id IS '用户ID (可为空，支持匿名访问)';

COMMENT ON COLUMN page_views.page_type IS '页面类型 (post, category, tag, home, archive)';

COMMENT ON COLUMN page_views.page_url IS '页面URL';

COMMENT ON COLUMN page_views.page_title IS '页面标题';

COMMENT ON COLUMN page_views.referrer IS '来源页面';

COMMENT ON COLUMN page_views.utm_source IS 'UTM来源';

COMMENT ON COLUMN page_views.utm_medium IS 'UTM媒介';

COMMENT ON COLUMN page_views.utm_campaign IS 'UTM活动';

COMMENT ON COLUMN page_views.ip_address IS '访问者IP地址';

COMMENT ON COLUMN page_views.user_agent IS '用户代理字符串';

COMMENT ON COLUMN page_views.device_type IS '设备类型 (desktop, mobile, tablet)';

COMMENT ON COLUMN page_views.browser IS '浏览器类型';

COMMENT ON COLUMN page_views.os IS '操作系统';

COMMENT ON COLUMN page_views.session_id IS '会话ID';

COMMENT ON COLUMN page_views.duration IS '页面停留时间(秒)';

COMMENT ON COLUMN page_views.is_bounce IS '是否为跳出访问';

COMMENT ON COLUMN page_views.viewed_at IS '浏览时间';

-- End of migration