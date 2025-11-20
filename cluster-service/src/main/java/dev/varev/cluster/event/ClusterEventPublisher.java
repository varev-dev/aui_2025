package dev.varev.cluster.event;

import dev.varev.cluster.entity.RenderCluster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ClusterEventPublisher {

    private final WebClient webClient;
    private final String nodeServiceUrl;

    public ClusterEventPublisher(@Value("${node.service.url}") String nodeServiceUrl) {
        this.nodeServiceUrl = nodeServiceUrl;
        this.webClient = WebClient.builder()
                .baseUrl(nodeServiceUrl)
                .build();
    }

    public void publishClusterCreated(RenderCluster cluster) {
        log.info("Publishing CLUSTER_CREATED event for cluster: {}", cluster.getId());

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CLUSTER_CREATED");
        event.put("clusterId", cluster.getId().toString());
        event.put("clusterName", cluster.getName());
        event.put("location", cluster.getLocation());

        webClient.post()
                .uri("/internal/cluster-events")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully published CLUSTER_CREATED event"))
                .doOnError(e -> log.error("Failed to publish CLUSTER_CREATED event: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    public void publishClusterUpdated(RenderCluster cluster) {
        log.info("Publishing CLUSTER_UPDATED event for cluster: {}", cluster.getId());

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CLUSTER_UPDATED");
        event.put("clusterId", cluster.getId().toString());
        event.put("clusterName", cluster.getName());
        event.put("location", cluster.getLocation());

        webClient.post()
                .uri("/internal/cluster-events")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully published CLUSTER_UPDATED event"))
                .doOnError(e -> log.error("Failed to publish CLUSTER_UPDATED event: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    public void publishClusterDeleted(UUID clusterId) {
        log.info("Publishing CLUSTER_DELETED event for cluster: {}", clusterId);

        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "CLUSTER_DELETED");
        event.put("clusterId", clusterId.toString());

        webClient.post()
                .uri("/internal/cluster-events")
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.info("Successfully published CLUSTER_DELETED event"))
                .doOnError(e -> log.error("Failed to publish CLUSTER_DELETED event: {}", e.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }
}
