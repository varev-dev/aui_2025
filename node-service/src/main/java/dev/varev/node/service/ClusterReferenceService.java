package dev.varev.node.service;

import dev.varev.node.entity.ClusterReference;
import dev.varev.node.repo.ClusterReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClusterReferenceService {

    private final ClusterReferenceRepository clusterReferenceRepository;

    @Transactional(readOnly = true)
    public List<ClusterReference> findAll() {
        return clusterReferenceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ClusterReference> findById(UUID id) {
        return clusterReferenceRepository.findById(id);
    }

    @Transactional
    public ClusterReference save(ClusterReference clusterReference) {
        return clusterReferenceRepository.save(clusterReference);
    }

    @Transactional
    public void deleteById(UUID id) {
        clusterReferenceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return clusterReferenceRepository.existsById(id);
    }
}