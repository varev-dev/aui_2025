package dev.varev.node.dto;

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
}
