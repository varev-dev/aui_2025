package dev.varev.node.controller;

import dev.varev.node.dto.CreateUpdateNodeDTO;
import dev.varev.node.dto.NodeDTO;
import dev.varev.node.dto.NodeListDTO;
import dev.varev.node.entity.RenderNode;
import dev.varev.node.service.ClusterReferenceService;
import dev.varev.node.service.RenderNodeService;
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
    private final ClusterReferenceService clusterReferenceService;

    @GetMapping("/api/nodes")
    public ResponseEntity<List<NodeListDTO>> getAllNodes() {
        List<NodeListDTO> nodes = nodeService.findAll().stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(nodes);
    }

    @GetMapping("/api/nodes/{id}")
    public ResponseEntity<NodeDTO> getNode(@PathVariable UUID id) {
        return nodeService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/clusters/{clusterId}/nodes")
    public ResponseEntity<List<NodeListDTO>> getClusterNodes(@PathVariable UUID clusterId) {
        if (!clusterReferenceService.existsById(clusterId)) {
            return ResponseEntity.notFound().build();
        }

        List<NodeListDTO> nodes = nodeService.findByClusterReferenceId(clusterId).stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(nodes);
    }

    @GetMapping("/api/clusters/{clusterId}/nodes/{nodeId}")
    public ResponseEntity<NodeDTO> getClusterNode(
            @PathVariable UUID clusterId,
            @PathVariable UUID nodeId) {

        if (!clusterReferenceService.existsById(clusterId)) {
            return ResponseEntity.notFound().build();
        }

        return nodeService.findById(nodeId)
                .filter(node -> node.getClusterReference().getId().equals(clusterId))
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/clusters/{clusterId}/nodes")
    public ResponseEntity<NodeDTO> createNode(
            @PathVariable UUID clusterId,
            @RequestBody CreateUpdateNodeDTO dto) {

        return clusterReferenceService.findById(clusterId)
                .map(clusterRef -> {
                    RenderNode node = RenderNode.builder()
                            .id(UUID.randomUUID())
                            .hostname(dto.getHostname())
                            .gpuLoad(dto.getGpuLoad())
                            .temperature(dto.getTemperature())
                            .clusterReference(clusterRef)
                            .build();

                    RenderNode saved = nodeService.save(node);

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(saved.getId())
                            .toUri();

                    return ResponseEntity.created(location).body(toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/clusters/{clusterId}/nodes/{nodeId}")
    public ResponseEntity<NodeDTO> updateNode(
            @PathVariable UUID clusterId,
            @PathVariable UUID nodeId,
            @RequestBody CreateUpdateNodeDTO dto) {

        if (!clusterReferenceService.existsById(clusterId)) {
            return ResponseEntity.notFound().build();
        }

        return nodeService.findById(nodeId)
                .filter(node -> node.getClusterReference().getId().equals(clusterId))
                .map(node -> {
                    node.setHostname(dto.getHostname());
                    node.setGpuLoad(dto.getGpuLoad());
                    node.setTemperature(dto.getTemperature());

                    RenderNode updated = nodeService.save(node);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/clusters/{clusterId}/nodes/{nodeId}")
    public ResponseEntity<Void> deleteNode(
            @PathVariable UUID clusterId,
            @PathVariable UUID nodeId) {

        if (!clusterReferenceService.existsById(clusterId)) {
            return ResponseEntity.notFound().build();
        }

        return nodeService.findById(nodeId)
                .filter(node -> node.getClusterReference().getId().equals(clusterId))
                .map(node -> {
                    nodeService.delete(node);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/nodes/{id}")
    public ResponseEntity<Void> deleteNodeById(@PathVariable UUID id) {
        return nodeService.findById(id)
                .map(node -> {
                    nodeService.delete(node);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Mapper methods
    private NodeDTO toDTO(RenderNode node) {
        NodeDTO.ClusterInfo clusterInfo = NodeDTO.ClusterInfo.builder()
                .id(node.getClusterReference().getId())
                .name(node.getClusterReference().getName())
                .build();

        return NodeDTO.builder()
                .id(node.getId())
                .hostname(node.getHostname())
                .gpuLoad(node.getGpuLoad())
                .temperature(node.getTemperature())
                .cluster(clusterInfo)
                .build();
    }

    private NodeListDTO toListDTO(RenderNode node) {
        return NodeListDTO.builder()
                .id(node.getId())
                .hostname(node.getHostname())
                .build();
    }
}