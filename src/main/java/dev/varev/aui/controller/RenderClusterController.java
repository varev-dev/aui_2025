package dev.varev.aui.controller;

import dev.varev.aui.dto.cluster.ClusterDTO;
import dev.varev.aui.dto.cluster.ClusterListDTO;
import dev.varev.aui.dto.cluster.CreateUpdateClusterDTO;
import dev.varev.aui.entity.RenderCluster;
import dev.varev.aui.mapper.DTOMapper;
import dev.varev.aui.service.RenderClusterService;
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
    private final DTOMapper mapper;

    /**
     * GET /api/clusters
     * Retrieve all clusters (minimal info)
     */
    @GetMapping
    public ResponseEntity<List<ClusterListDTO>> getAllClusters() {
        List<ClusterListDTO> clusters = clusterService.findAll().stream()
                .map(mapper::toListDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clusters);
    }

    /**
     * GET /api/clusters/{id}
     * Retrieve single cluster by ID (full details)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClusterDTO> getCluster(@PathVariable UUID id) {
        return clusterService.findById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/clusters
     * Create new cluster
     */
    @PostMapping
    public ResponseEntity<ClusterDTO> createCluster(@RequestBody CreateUpdateClusterDTO dto) {
        RenderCluster cluster = mapper.toEntity(dto);
        RenderCluster savedCluster = clusterService.save(cluster);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCluster.getId())
                .toUri();

        return ResponseEntity.created(location).body(mapper.toDTO(savedCluster));
    }

    /**
     * PUT /api/clusters/{id}
     * Update existing cluster
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClusterDTO> updateCluster(
            @PathVariable UUID id,
            @RequestBody CreateUpdateClusterDTO dto) {

        return clusterService.findById(id)
                .map(cluster -> {
                    mapper.updateEntity(cluster, dto);
                    RenderCluster updated = clusterService.save(cluster);
                    return ResponseEntity.ok(mapper.toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/clusters/{id}
     * Delete cluster (cascades to nodes)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCluster(@PathVariable UUID id) {
        return clusterService.findById(id)
                .map(cluster -> {
                    clusterService.delete(cluster);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}