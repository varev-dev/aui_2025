package dev.varev.aui.dto;

import dev.varev.aui.model.RenderNode;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RenderNodeDTO implements Comparable<RenderNodeDTO> {

    private UUID id;
    private String hostname;
    private double gpuLoad;
    private int temperature;
    private String clusterName;

    @Override
    public int compareTo(RenderNodeDTO other) {
        return this.hostname.compareTo(other.hostname);
    }
}