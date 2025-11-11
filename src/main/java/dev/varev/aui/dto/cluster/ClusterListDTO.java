package dev.varev.aui.dto.cluster;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterListDTO {
    private UUID id;
    private String name;

    public static ClusterListDTO from(UUID id, String name) {
        return ClusterListDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
}