package dev.varev.aui.service;

import dev.varev.aui.model.RenderCluster;
import dev.varev.aui.repository.RenderClusterRepository;
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

    @Transactional(readOnly = true)
    public List<RenderCluster> findAll() {
        return clusterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RenderCluster> findById(UUID id) {
        return clusterRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<RenderCluster> findByName(String name) {
        return clusterRepository.findByName(name);
    }

    @Transactional
    public RenderCluster save(RenderCluster cluster) {
        return clusterRepository.save(cluster);
    }

    @Transactional
    public void delete(RenderCluster cluster) {
        clusterRepository.delete(cluster);
    }

    @Transactional
    public void deleteById(UUID id) {
        clusterRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return clusterRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    public long count() {
        return clusterRepository.count();
    }
}