package dev.varev.node.repo;

import dev.varev.node.entity.ClusterReference;
import dev.varev.node.entity.RenderNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RenderNodeRepository extends JpaRepository<RenderNode, UUID> {

    List<RenderNode> findByClusterReference(ClusterReference cluster);

    List<RenderNode> findByClusterReferenceId(UUID clusterId);

    List<RenderNode> findByHostnameContainingIgnoreCase(String hostname);

    List<RenderNode> findByGpuLoadGreaterThan(double gpuLoad);

    List<RenderNode> findByTemperatureGreaterThan(int temperature);
}