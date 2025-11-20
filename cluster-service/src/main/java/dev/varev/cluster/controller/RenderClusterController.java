package dev.varev.cluster.controller;

import dev.varev.cluster.dto.ClusterDTO;
import dev.varev.cluster.dto.ClusterListDTO;
import dev.varev.cluster.dto.CreateUpdateClusterDTO;
import dev.varev.cluster.entity.RenderCluster;
import dev.varev.cluster.service.RenderClusterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clusters")
@RequiredArgsConstructor
public class RenderClusterController {

    private final RenderClusterService clusterService;

    @GetMapping
    public ResponseEntity<List<ClusterListDTO>> getAllClusters() {
        List<ClusterListDTO> clusters = clusterService.findAll().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clusters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClusterDTO> getCluster(@PathVariable UUID id) {
        return clusterService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClusterDTO> createCluster(@RequestBody CreateUpdateClusterDTO dto) {
        RenderCluster cluster = RenderCluster.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .location(dto.getLocation())
                .build();

        RenderCluster saved = clusterService.save(cluster);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClusterDTO> updateCluster(
            @PathVariable UUID id,
            @RequestBody CreateUpdateClusterDTO dto) {

        if (!clusterService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        RenderCluster cluster = RenderCluster.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .build();

        RenderCluster updated = clusterService.update(id, cluster);
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable UUID id) {
        if (!clusterService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clusterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Mapper methods
    private ClusterDTO toDTO(RenderCluster cluster) {
        return ClusterDTO.builder()
                .id(cluster.getId())
                .name(cluster.getName())
                .location(cluster.getLocation())
                .build();
    }

    private ClusterListDTO toListDTO(RenderCluster cluster) {
        return ClusterListDTO.builder()
                .id(cluster.getId())
                .name(cluster.getName())
                .build();
    }
}
