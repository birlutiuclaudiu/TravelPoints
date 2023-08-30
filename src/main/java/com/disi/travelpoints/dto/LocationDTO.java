package com.disi.travelpoints.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LocationDTO {

    @NotNull
    private String country;

    @NotNull
    private String address;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;
}
