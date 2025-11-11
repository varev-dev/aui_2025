package dev.varev.aui.controller;

import dev.varev.aui.dto.node.CreateUpdateNodeDTO;
import dev.varev.aui.dto.node.NodeDTO;
import dev.varev.aui.dto.node.NodeListDTO;
import dev.varev.aui.mapper.DTOMapper;
import dev.varev.aui.entity.RenderNode;
import dev.varev.aui.service.RenderClusterService;
import dev.varev.aui.service.RenderNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RenderNodeController {

    private final RenderNodeService nodeService;
    private final RenderClusterService clusterService;
    private final DTOMapper mapper;

    /**
     * GET /api/nodes
     * Retrieve all nodes from all clusters (minimal info)
     */
    @GetMapping("/api/nodes")
    public ResponseEntity<List<NodeListDTO>> getAllNodes() {
        List<NodeListDTO> nodes = nodeService.findAll().stream()
                .map(mapper::toListDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(nodes);
    }

    /**
     * GET /api/nodes/{id}
     * Retrieve single node by ID (full details)
     */
    @GetMapping("/api/nodes/{id}")
    public ResponseEntity<NodeDTO> getNode(@PathVariable UUID id) {
        return nodeService.findById(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/clusters/{clusterId}/nodes
     * Retrieve all nodes from specific cluster
     * - 200 OK with empty list if cluster exists but has no nodes
     * - 404 NOT FOUND if cluster doesn't exist
     */
    @GetMapping("/api/clusters/{clusterId}/nodes")
    public ResponseEntity<List<NodeListDTO>> getClusterNodes(@PathVariable UUID clusterId) {
        return clusterService.findById(clusterId)
                .map(cluster -> {
                    List<NodeListDTO> nodes = nodeService.findByCluster(cluster).stream()
                            .map(mapper::toListDTO)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(nodes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/clusters/{clusterId}/nodes/{nodeId}
     * Retrieve specific node from specific cluster (full details)
     */
    @GetMapping("/api/clusters/{clusterId}/nodes/{nodeId}")
    public ResponseEntity<NodeDTO> getClusterNode(
            @PathVariable UUID clusterId,
            @PathVariable UUID nodeId) {

        // Verify cluster exists
        if (clusterService.findById(clusterId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return nodeService.findById(nodeId)
                .filter(node -> node.getCluster().getId().equals(clusterId))
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/clusters/{clusterId}/nodes
     * Create new node in specific cluster
     * - 404 NOT FOUND if cluster doesn't exist
     * - 201 CREATED with Location header if successful
     */
    @PostMapping("/api/clusters/{clusterId}/nodes")
    public ResponseEntity<NodeDTO> createNode(
            @PathVariable UUID clusterId,
            @RequestBody CreateUpdateNodeDTO dto) {

        return clusterService.findById(clusterId)
                .map(cluster -> {
                    RenderNode node = mapper.toEntity(dto, cluster);
                    RenderNode savedNode = nodeService.save(node);

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(savedNode.getId())
                            .toUri();

                    return ResponseEntity.created(location).body(mapper.toDTO(savedNode));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/clusters/{clusterId}/nodes/{nodeId}
     * Update existing node in specific cluster
     * - Does NOT change cluster assignment
     */
    @PutMapping("/api/clusters/{clusterId}/nodes/{nodeId}")
    public ResponseEntity<NodeDTO> updateNode(
            @PathVariable UUID clusterId,
            @PathVariable UUID nodeId,
            @RequestBody CreateUpdateNodeDTO dto) {

        // Verify cluster exists
        if (clusterService.findById(clusterId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return nodeService.findById(nodeId)
                .filter(node -> node.getCluster().getId().equals(clusterId))
                .map(node -> {
                    mapper.updateEntity(node, dto);
                    RenderNode updated = nodeService.save(node);
                    return ResponseEntity.ok(mapper.toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/clusters/{clusterId}/nodes/{nodeId}
     * Delete node from specific cluster
     */
    @DeleteMapping("/api/clusters/{clusterId}/nodes/{nodeId}")
    public ResponseEntity<Void> deleteNode(
            @PathVariable UUID clusterId,
            @PathVariable UUID nodeId) {

        // Verify cluster exists
        if (clusterService.findById(clusterId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return nodeService.findById(nodeId)
                .filter(node -> node.getCluster().getId().equals(clusterId))
                .map(node -> {
                    nodeService.delete(node);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/nodes/{id}
     * Delete node by ID (alternative endpoint)
     */
    @DeleteMapping("/api/nodes/{id}")
    public ResponseEntity<Void> deleteNodeById(@PathVariable UUID id) {
        return nodeService.findById(id)
                .map(node -> {
                    nodeService.delete(node);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}