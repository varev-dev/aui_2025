package dev.varev.aui.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "render_clusters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "nodes")
public class RenderCluster implements Comparable<RenderCluster>, Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RenderNode> nodes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public int compareTo(RenderCluster other) {
        return this.name.compareTo(other.name);
    }

    public void addNode(RenderNode node) {
        nodes.add(node);
        node.setCluster(this);
    }

    public void removeNode(RenderNode node) {
        nodes.remove(node);
        node.setCluster(null);
    }
}