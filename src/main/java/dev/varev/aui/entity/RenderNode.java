package dev.varev.aui.entity;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "render_nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "cluster")
public class RenderNode implements Comparable<RenderNode>, Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "hostname", nullable = false)
    private String hostname;

    @Column(name = "gpu_load")
    private double gpuLoad;

    @Column(name = "temperature")
    private int temperature;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id", nullable = false)
    private RenderCluster cluster;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public int compareTo(RenderNode other) {
        return this.hostname.compareTo(other.hostname);
    }
}