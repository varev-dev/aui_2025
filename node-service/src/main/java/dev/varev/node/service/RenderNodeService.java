package dev.varev.node.service;

import dev.varev.node.entity.ClusterReference;
import dev.varev.node.entity.RenderNode;
import dev.varev.node.repo.RenderNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RenderNodeService {

    private final RenderNodeRepository nodeRepository;

    @Transactional(readOnly = true)
    public List<RenderNode> findAll() {
        return nodeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RenderNode> findById(UUID id) {
        return nodeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<RenderNode> findByClusterReference(ClusterReference cluster) {
        return nodeRepository.findByClusterReference(cluster);
    }

    @Transactional(readOnly = true)
    public List<RenderNode> findByClusterReferenceId(UUID clusterId) {
        return nodeRepository.findByClusterReferenceId(clusterId);
    }

    @Transactional
    public RenderNode save(RenderNode node) {
        return nodeRepository.save(node);
    }

    @Transactional
    public void delete(RenderNode node) {
        nodeRepository.delete(node);
    }

    @Transactional
    public void deleteById(UUID id) {
        nodeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RenderNode> findByHostname(String hostname) {
        return nodeRepository.findByHostnameContainingIgnoreCase(hostname);
    }

    @Transactional(readOnly = true)
    public List<RenderNode> findOverloadedNodes(double gpuLoadThreshold) {
        return nodeRepository.findByGpuLoadGreaterThan(gpuLoadThreshold);
    }

    @Transactional(readOnly = true)
    public List<RenderNode> findOverheatedNodes(int temperatureThreshold) {
        return nodeRepository.findByTemperatureGreaterThan(temperatureThreshold);
    }

    @Transactional(readOnly = true)
    public long count() {
        return nodeRepository.count();
    }
}