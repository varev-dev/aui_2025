package dev.varev.node.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cluster_references")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class ClusterReference implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;  // Same ID as in cluster-service

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "clusterReference", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RenderNode> nodes = new ArrayList<>();
}