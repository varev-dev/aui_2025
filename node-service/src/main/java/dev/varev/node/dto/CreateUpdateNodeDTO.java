package dev.varev.node.dto;

import lombok.*;

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
