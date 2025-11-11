package dev.varev.aui.dto.node;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeDTO {
    private UUID id;
    private String hostname;
    private Double gpuLoad;
    private Integer temperature;
    private ClusterInfo cluster;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClusterInfo {
        private UUID id;
        private String name;
    }
}