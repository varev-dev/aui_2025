import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenderNodeDTO implements Comparable<RenderNodeDTO> {
    private UUID id;
    private String hostname;
    private double gpuLoad;
    private int temperature;
    private String clusterName;

    public static RenderNodeDTO from(RenderNode node) {
        return RenderNodeDTO.builder()
                .id(node.getId())
                .hostname(node.getHostname())
                .gpuLoad(node.getGpuLoad())
                .temperature(node.getTemperature())
                .clusterName(node.getCluster().getName())
                .build();
    }

    @Override
    public int compareTo(RenderNodeDTO other) {
        return this.hostname.compareToIgnoreCase(other.hostname);
    }

    @Override
    public String toString() {
        return "RenderNodeDTO{host='%s', cluster='%s', load=%.1f%%, temp=%d°C}"
                .formatted(hostname, clusterName, gpuLoad, temperature);
    }
}
