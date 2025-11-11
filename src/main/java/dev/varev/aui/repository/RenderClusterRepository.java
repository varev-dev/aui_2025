package dev.varev.aui.repository;

import dev.varev.aui.entity.RenderCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RenderClusterRepository extends JpaRepository<RenderCluster, UUID> {

    Optional<RenderCluster> findByName(String name);

    boolean existsByName(String name);
}