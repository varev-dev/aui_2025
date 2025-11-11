package dev.varev.aui.mapper;

import dev.varev.aui.dto.cluster.ClusterDTO;
import dev.varev.aui.dto.cluster.ClusterListDTO;
import dev.varev.aui.dto.cluster.CreateUpdateClusterDTO;
import dev.varev.aui.dto.node.CreateUpdateNodeDTO;
import dev.varev.aui.dto.node.NodeDTO;
import dev.varev.aui.dto.node.NodeListDTO;
import dev.varev.aui.entity.RenderCluster;
import dev.varev.aui.entity.RenderNode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DTOMapper {

    // ============ CLUSTER MAPPERS ============

    public RenderCluster toEntity(CreateUpdateClusterDTO dto) {
        return RenderCluster.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
    }

    public void updateEntity(RenderCluster cluster, CreateUpdateClusterDTO dto) {
        cluster.setName(dto.getName());
        cluster.setLocation(dto.getLocation());
    }

    public ClusterDTO toDTO(RenderCluster cluster) {
        return ClusterDTO.builder()
                .id(cluster.getId())
                .name(cluster.getName())
                .location(cluster.getLocation())
                .build();
    }

    public ClusterListDTO toListDTO(RenderCluster cluster) {
        return ClusterListDTO.builder()
                .id(cluster.getId())
                .name(cluster.getName())
                .build();
    }

    // ============ NODE MAPPERS ============

    public RenderNode toEntity(CreateUpdateNodeDTO dto, RenderCluster cluster) {
        return RenderNode.builder()
                .id(UUID.randomUUID())
                .hostname(dto.getHostname())
                .gpuLoad(dto.getGpuLoad())
                .temperature(dto.getTemperature())
                .cluster(cluster)
                .build();
    }

    public void updateEntity(RenderNode node, CreateUpdateNodeDTO dto) {
        node.setHostname(dto.getHostname());
        node.setGpuLoad(dto.getGpuLoad());
        node.setTemperature(dto.getTemperature());
    }

    public NodeDTO toDTO(RenderNode node) {
        NodeDTO.ClusterInfo clusterInfo = NodeDTO.ClusterInfo.builder()
                .id(node.getCluster().getId())
                .name(node.getCluster().getName())
                .build();

        return NodeDTO.builder()
                .id(node.getId())
                .hostname(node.getHostname())
                .gpuLoad(node.getGpuLoad())
                .temperature(node.getTemperature())
                .cluster(clusterInfo)
                .build();
    }

    public NodeListDTO toListDTO(RenderNode node) {
        return NodeListDTO.builder()
                .id(node.getId())
                .hostname(node.getHostname())
                .build();
    }
}
