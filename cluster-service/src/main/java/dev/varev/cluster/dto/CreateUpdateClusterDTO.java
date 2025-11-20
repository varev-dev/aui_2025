package dev.varev.cluster.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateClusterDTO {
    private String name;
    private String location;
}
