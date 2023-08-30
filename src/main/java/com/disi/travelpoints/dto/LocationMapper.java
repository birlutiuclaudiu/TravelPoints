package com.disi.travelpoints.dto;

import com.disi.travelpoints.model.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public Location toLocationEntity(LocationDTO locationDTO) {
        return Location.builder()
                .country(locationDTO.getCountry())
                .address(locationDTO.getAddress())
                .longitude(locationDTO.getLongitude())
                .latitude(locationDTO.getLatitude())
                .build();
    }
    public LocationDTO toLocationDTO(Location location) {
        return LocationDTO.builder()
                .country(location.getCountry())
                .address(location.getAddress())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
    }
}
