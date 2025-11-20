package dev.varev.cluster.service;

import dev.varev.cluster.entity.RenderCluster;
import dev.varev.cluster.event.ClusterEventPublisher;
import dev.varev.cluster.repo.RenderClusterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RenderClusterService {

    private final RenderClusterRepository clusterRepository;
    private final ClusterEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<RenderCluster> findAll() {
        return clusterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RenderCluster> findById(UUID id) {
        return clusterRepository.findById(id);
    }

    @Transactional
    public RenderCluster save(RenderCluster cluster) {
        boolean isNew = cluster.getId() == null || !clusterRepository.existsById(cluster.getId());
        RenderCluster saved = clusterRepository.save(cluster);

        // Publish event
        if (isNew) {
            eventPublisher.publishClusterCreated(saved);
        } else {
            eventPublisher.publishClusterUpdated(saved);
        }

        return saved;
    }

    @Transactional
    public RenderCluster update(UUID id, RenderCluster cluster) {
        RenderCluster existing = clusterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cluster not found"));

        existing.setName(cluster.getName());
        existing.setLocation(cluster.getLocation());

        RenderCluster updated = clusterRepository.save(existing);
        eventPublisher.publishClusterUpdated(updated);

        return updated;
    }

    @Transactional
    public void delete(UUID id) {
        if (clusterRepository.existsById(id)) {
            clusterRepository.deleteById(id);
            // Publish event AFTER deletion
            eventPublisher.publishClusterDeleted(id);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return clusterRepository.existsById(id);
    }
}
