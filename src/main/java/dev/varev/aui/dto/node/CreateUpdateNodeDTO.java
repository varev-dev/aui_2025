package dev.varev.aui.dto.node;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateNodeDTO {
    private String hostname;
    private Double gpuLoad;
    private Integer temperature;
}