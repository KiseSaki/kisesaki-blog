-- Create enum type for comment report status
CREATE TYPE comment_report_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'IGNORED');

-- Create comment_reports table
CREATE TABLE IF NOT EXISTS comment_reports (
    id BIGSERIAL PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    reporter_id BIGINT NOT NULL,
    reason VARCHAR(100) NOT NULL,
    description TEXT,
    status comment_report_status NOT NULL DEFAULT 'PENDING',
    handled_by BIGINT,
    handled_at TIMESTAMP WITH TIME ZONE,
    handle_note TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

-- Foreign key constraints (assumes comments and "user" tables exist)
ALTER TABLE comment_reports
    ADD CONSTRAINT fk_comment_reports_comment
    FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE;

ALTER TABLE comment_reports
    ADD CONSTRAINT fk_comment_reports_reporter
    FOREIGN KEY (reporter_id) REFERENCES "user"(id) ON DELETE SET NULL;

ALTER TABLE comment_reports
    ADD CONSTRAINT fk_comment_reports_handled_by
    FOREIGN KEY (handled_by) REFERENCES "user"(id) ON DELETE SET NULL;

-- Indexes to speed up common queries
CREATE INDEX IF NOT EXISTS idx_comment_reports_comment_id ON comment_reports(comment_id);
CREATE INDEX IF NOT EXISTS idx_comment_reports_reporter_id ON comment_reports(reporter_id);
CREATE INDEX IF NOT EXISTS idx_comment_reports_status ON comment_reports(status);
CREATE INDEX IF NOT EXISTS idx_comment_reports_created_at ON comment_reports(created_at);

-- Trigger function to update updated_at on row modification
CREATE OR REPLACE FUNCTION trg_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to call function
DROP TRIGGER IF EXISTS trg_comment_reports_updated_at ON comment_reports;
CREATE TRIGGER trg_comment_reports_updated_at
BEFORE UPDATE ON comment_reports
FOR EACH ROW
EXECUTE FUNCTION trg_set_updated_at();

-- Optional: materialized view or view for reporting (example view for reported comments count)
CREATE OR REPLACE VIEW v_reported_comments_count AS
SELECT COUNT(DISTINCT comment_id) AS reported_count
FROM comment_reports cr
JOIN comments c ON c.id = cr.comment_id
WHERE c.deleted_at IS NULL;
