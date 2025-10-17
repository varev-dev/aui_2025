import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenderCluster implements Comparable<RenderCluster>, Serializable {
    private UUID id;
    private String name;
    private String location;
    @Builder.Default
    private List<RenderNode> nodes = new ArrayList<>();

    @Override
    public int compareTo(RenderCluster other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return "RenderCluster{name='%s', location='%s', nodes=%d}".formatted(name, location, nodes.size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RenderCluster)) return false;
        return ((RenderCluster) o).id.equals(this.id);
    }
}
