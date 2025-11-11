package dev.varev.aui.bootstrap;

import dev.varev.aui.entity.RenderCluster;
import dev.varev.aui.entity.RenderNode;
import dev.varev.aui.service.RenderClusterService;
import dev.varev.aui.service.RenderNodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RenderClusterService clusterService;
    private final RenderNodeService nodeService;

    @Bean
    @Order(1)
    public CommandLineRunner initializeData() {
        return args -> {
            log.info("Starting data initialization...");

            // Create render clusters
            RenderCluster cluster1 = RenderCluster.builder()
                    .id(UUID.randomUUID())
                    .name("Production Cluster A")
                    .location("Data Center 1 - Warsaw")
                    .build();

            RenderCluster cluster2 = RenderCluster.builder()
                    .id(UUID.randomUUID())
                    .name("Development Cluster B")
                    .location("Data Center 2 - Gdansk")
                    .build();

            RenderCluster cluster3 = RenderCluster.builder()
                    .id(UUID.randomUUID())
                    .name("Backup Cluster C")
                    .location("Data Center 3 - Krakow")
                    .build();

            clusterService.save(cluster1);
            clusterService.save(cluster2);
            clusterService.save(cluster3);

            log.info("Created {} render clusters", clusterService.count());

            // Create render nodes for cluster 1
            RenderNode node1 = RenderNode.builder()
                    .id(UUID.randomUUID())
                    .hostname("render-node-01.prod.local")
                    .gpuLoad(65.5)
                    .temperature(72)
                    .cluster(cluster1)
                    .build();

            RenderNode node2 = RenderNode.builder()
                    .id(UUID.randomUUID())
                    .hostname("render-node-02.prod.local")
                    .gpuLoad(82.3)
                    .temperature(78)
                    .cluster(cluster1)
                    .build();

            RenderNode node3 = RenderNode.builder()
                    .id(UUID.randomUUID())
                    .hostname("render-node-03.prod.local")
                    .gpuLoad(45.8)
                    .temperature(65)
                    .cluster(cluster1)
                    .build();

            // Create render nodes for cluster 2
            RenderNode node4 = RenderNode.builder()
                    .id(UUID.randomUUID())
                    .hostname("render-node-01.dev.local")
                    .gpuLoad(35.2)
                    .temperature(60)
                    .cluster(cluster2)
                    .build();

            RenderNode node5 = RenderNode.builder()
                    .id(UUID.randomUUID())
                    .hostname("render-node-02.dev.local")
                    .gpuLoad(50.0)
                    .temperature(68)
                    .cluster(cluster2)
                    .build();

            // Create render nodes for cluster 3
            RenderNode node6 = RenderNode.builder()
                    .id(UUID.randomUUID())
                    .hostname("render-node-01.backup.local")
                    .gpuLoad(15.7)
                    .temperature(55)
                    .cluster(cluster3)
                    .build();

            nodeService.save(node1);
            nodeService.save(node2);
            nodeService.save(node3);
            nodeService.save(node4);
            nodeService.save(node5);
            nodeService.save(node6);

            log.info("Created {} render nodes", nodeService.count());
            log.info("Data initialization completed successfully!");
        };
    }
}