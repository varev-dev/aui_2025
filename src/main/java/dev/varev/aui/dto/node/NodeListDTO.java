package dev.varev.aui.dto.node;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeListDTO {
    private UUID id;
    private String hostname;

    public static NodeListDTO from(UUID id, String hostname) {
        return NodeListDTO.builder()
                .id(id)
                .hostname(hostname)
                .build();
    }
}