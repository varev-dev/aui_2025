-- Create cluster_references table
CREATE TABLE IF NOT EXISTS cluster_references (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

-- Create render_nodes table
CREATE TABLE IF NOT EXISTS render_nodes (
    id UUID PRIMARY KEY,
    hostname VARCHAR(255) NOT NULL,
    gpu_load DOUBLE PRECISION,
    temperature INTEGER,
    cluster_id UUID NOT NULL,
    CONSTRAINT fk_cluster FOREIGN KEY (cluster_id) REFERENCES cluster_references(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_cluster_reference_name ON cluster_references(name);
CREATE INDEX idx_node_hostname ON render_nodes(hostname);
CREATE INDEX idx_node_cluster ON render_nodes(cluster_id);

-- Example data insertion
INSERT INTO cluster_references (id, name, location) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Production Cluster A', 'Data Center 1 - Warsaw')
ON CONFLICT (id) DO NOTHING;

INSERT INTO render_nodes (id, hostname, gpu_load, temperature, cluster_id) VALUES
    ('e5f6a7b8-c9d0-1234-ef01-345678901234', 'render-node-01.prod.local', 65.5, 72, 'a1b2c3d4-e5f6-7890-abcd-ef1234567890')
ON CONFLICT (id) DO NOTHING;