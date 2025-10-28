package dev.varev.aui.repository;

import dev.varev.aui.model.RenderCluster;
import dev.varev.aui.model.RenderNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RenderNodeRepository extends JpaRepository<RenderNode, UUID> {

    List<RenderNode> findByCluster(RenderCluster cluster);

    List<RenderNode> findByClusterId(UUID clusterId);

    List<RenderNode> findByHostnameContainingIgnoreCase(String hostname);

    List<RenderNode> findByGpuLoadGreaterThan(double gpuLoad);

    List<RenderNode> findByTemperatureGreaterThan(int temperature);
}