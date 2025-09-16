-- V7__update_tags_table_add_approval_fields.sql
-- Flyway migration: Update tags table to support approval workflow and enhanced features
-- Generated based on tag management API requirements

-- 添加新字段到 tags 表
ALTER TABLE tags
ADD COLUMN updated_at TIMESTAMPTZ DEFAULT now(),
ADD COLUMN created_by BIGINT,
ADD COLUMN is_approved BOOLEAN DEFAULT FALSE,
ADD COLUMN approval_status VARCHAR(20) DEFAULT 'pending',
ADD COLUMN approved_by BIGINT,
ADD COLUMN approved_at TIMESTAMPTZ,
ADD COLUMN approval_note VARCHAR(500),
ADD COLUMN last_used_at TIMESTAMPTZ,
ADD COLUMN popularity_score DOUBLE PRECISION DEFAULT 0.0;

-- 添加约束：审核状态只能是指定值
ALTER TABLE tags
ADD CONSTRAINT ck_tags_approval_status CHECK (
    approval_status IN (
        'pending',
        'approved',
        'rejected'
    )
);

-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS ix_tags_created_by ON tags (created_by);

CREATE INDEX IF NOT EXISTS ix_tags_approval_status ON tags (approval_status);

CREATE INDEX IF NOT EXISTS ix_tags_approved_by ON tags (approved_by);

CREATE INDEX IF NOT EXISTS ix_tags_is_approved ON tags (is_approved);

CREATE INDEX IF NOT EXISTS ix_tags_last_used_at ON tags (last_used_at);

CREATE INDEX IF NOT EXISTS ix_tags_popularity_score ON tags (popularity_score DESC);

CREATE INDEX IF NOT EXISTS ix_tags_name ON tags (name);

-- 添加外键约束：关联用户表
ALTER TABLE tags
ADD CONSTRAINT fk_tags_created_by_user FOREIGN KEY (created_by) REFERENCES "user" (id) ON DELETE SET NULL;

ALTER TABLE tags
ADD CONSTRAINT fk_tags_approved_by_user FOREIGN KEY (approved_by) REFERENCES "user" (id) ON DELETE SET NULL;

-- 更新现有数据：将所有现有标签设为已审核通过
UPDATE tags
SET
    updated_at = now(),
    is_approved = TRUE,
    approval_status = 'approved',
    approved_at = created_at,
    last_used_at = created_at,
    popularity_score = COALESCE(post_count * 1.0, 0.0)
WHERE
    id IS NOT NULL;

-- 创建触发器函数：自动更新 updated_at 字段
CREATE OR REPLACE FUNCTION update_tags_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器：在更新时自动更新 updated_at
CREATE TRIGGER tr_tags_update_updated_at
    BEFORE UPDATE ON tags
    FOR EACH ROW
    EXECUTE FUNCTION update_tags_updated_at();

-- 创建触发器函数：自动更新热度权重
CREATE OR REPLACE FUNCTION update_tag_popularity_score()
RETURNS TRIGGER AS $$
BEGIN
    -- 基于文章数量计算热度权重，可以根据需要调整算法
    NEW.popularity_score = COALESCE(NEW.post_count * 1.0, 0.0);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器：在更新 post_count 时自动更新热度权重
CREATE TRIGGER tr_tags_update_popularity_score
    BEFORE UPDATE OF post_count ON tags
    FOR EACH ROW
    EXECUTE FUNCTION update_tag_popularity_score();

-- 添加注释
COMMENT ON COLUMN tags.updated_at IS '标签更新时间';

COMMENT ON COLUMN tags.created_by IS '创建者用户ID';

COMMENT ON COLUMN tags.is_approved IS '是否已审核通过';

COMMENT ON COLUMN tags.approval_status IS '审核状态：pending-待审核, approved-已通过, rejected-已拒绝';

COMMENT ON COLUMN tags.approved_by IS '审核者用户ID';

COMMENT ON COLUMN tags.approved_at IS '审核时间';

COMMENT ON COLUMN tags.approval_note IS '审核备注';

COMMENT ON COLUMN tags.last_used_at IS '最近使用时间（最后一次被文章使用的时间）';

COMMENT ON COLUMN tags.popularity_score IS '热度权重（基于文章数量、阅读量等计算）';

-- End of migration