package dev.varev.node.event;

import dev.varev.node.entity.ClusterReference;
import dev.varev.node.service.ClusterReferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal/cluster-events")
@RequiredArgsConstructor
@Slf4j
public class ClusterEventListener {

    private final ClusterReferenceService clusterReferenceService;

    @PostMapping
    public ResponseEntity<Void> handleClusterEvent(@RequestBody Map<String, Object> event) {
        String eventType = (String) event.get("eventType");

        log.info("Received event: {}", eventType);

        switch (eventType) {
            case "CLUSTER_CREATED" -> handleClusterCreated(event);
            case "CLUSTER_UPDATED" -> handleClusterUpdated(event);
            case "CLUSTER_DELETED" -> handleClusterDeleted(event);
            default -> log.warn("Unknown event type: {}", eventType);
        }

        return ResponseEntity.ok().build();
    }

    private void handleClusterCreated(Map<String, Object> event) {
        UUID clusterId = UUID.fromString((String) event.get("clusterId"));
        String clusterName = (String) event.get("clusterName");
        String location = (String) event.get("location");

        log.info("Creating cluster reference: {} - {}", clusterId, clusterName);

        ClusterReference clusterRef = ClusterReference.builder()
                .id(clusterId)
                .name(clusterName)
                .location(location)
                .build();

        clusterReferenceService.save(clusterRef);
        log.info("Cluster reference created successfully");
    }

    private void handleClusterUpdated(Map<String, Object> event) {
        UUID clusterId = UUID.fromString((String) event.get("clusterId"));
        String clusterName = (String) event.get("clusterName");
        String location = (String) event.get("location");

        log.info("Updating cluster reference: {} - {}", clusterId, clusterName);

        clusterReferenceService.findById(clusterId).ifPresent(clusterRef -> {
            clusterRef.setName(clusterName);
            clusterRef.setLocation(location);
            clusterReferenceService.save(clusterRef);
            log.info("Cluster reference updated successfully");
        });
    }

    private void handleClusterDeleted(Map<String, Object> event) {
        UUID clusterId = UUID.fromString((String) event.get("clusterId"));

        log.info("Deleting cluster reference and all its nodes: {}", clusterId);

        // This will cascade delete all nodes belonging to this cluster
        clusterReferenceService.deleteById(clusterId);
        log.info("Cluster reference and nodes deleted successfully");
    }
}