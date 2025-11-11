package dev.varev.aui.dto.cluster;

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