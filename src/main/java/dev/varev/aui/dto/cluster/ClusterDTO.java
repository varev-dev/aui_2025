package dev.varev.aui.dto.cluster;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterDTO {
    private UUID id;
    private String name;
    private String location;
}