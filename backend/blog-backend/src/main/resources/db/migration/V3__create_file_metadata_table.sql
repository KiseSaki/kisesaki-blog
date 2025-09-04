-- Active: 1754550827822@@116.62.27.195@35432@blog@public
-- Create file_metadata table for storing media file information
CREATE TABLE file_metadata (
    id BIGSERIAL PRIMARY KEY,
    url VARCHAR(500) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    width INT,
    height INT,
    alt_text VARCHAR(255),
    description TEXT,
    uploaded_by BIGINT NOT NULL REFERENCES "user" (id),
    usage_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);