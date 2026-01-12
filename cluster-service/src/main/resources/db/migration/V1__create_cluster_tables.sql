-- Create render_clusters table
CREATE TABLE IF NOT EXISTS render_clusters (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

-- Create indexes
CREATE INDEX idx_cluster_name ON render_clusters(name);

-- Example data insertion
INSERT INTO render_clusters (id, name, location) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Production Cluster A', 'Data Center 1 - Warsaw'),
    ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Development Cluster B', 'Data Center 2 - Gdansk')
ON CONFLICT (id) DO NOTHING;