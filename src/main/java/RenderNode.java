import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenderNode implements Comparable<RenderNode>, Serializable {
    private UUID id;
    private String hostname;
    private double gpuLoad;
    private int temperature;
    private RenderCluster cluster;

    @Override
    public int compareTo(RenderNode other) {
        return this.hostname.compareToIgnoreCase(other.hostname);
    }

    @Override
    public String toString() {
        return "RenderNode{host='%s', load=%.1f%%, temp=%d°C, cluster='%s'}"
                .formatted(hostname, gpuLoad, temperature, cluster != null ? cluster.getName() : "N/A");
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RenderNode)) return false;
        return ((RenderNode) o).id.equals(this.id);
    }
}