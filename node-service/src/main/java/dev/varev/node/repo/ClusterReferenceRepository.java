package dev.varev.node.repo;

import dev.varev.node.entity.ClusterReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClusterReferenceRepository extends JpaRepository<ClusterReference, UUID> {
}
