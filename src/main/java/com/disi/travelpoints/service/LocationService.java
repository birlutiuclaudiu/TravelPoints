package com.disi.travelpoints.service;


import com.disi.travelpoints.dto.LocationDTO;
import com.disi.travelpoints.dto.LocationMapper;
import com.disi.travelpoints.model.entity.Location;
import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LocationService {
    private final LocationMapper locationMapper;
    private final LocationRepository locationRepository;
    public Location addLocation(LocationDTO locationDTO) {
        return locationRepository.save(locationMapper.toLocationEntity(locationDTO));
    }

    public Location modifyLocationForTouristicAttraction(LocationDTO locationDTO, TouristicAttraction touristicAttraction) {
        Location location = locationRepository.findByTouristicAttraction(touristicAttraction);
        location.setAddress(locationDTO.getAddress());
        location.setCountry(locationDTO.getCountry());
        location.setLongitude(locationDTO.getLongitude());
        location.setLatitude(locationDTO.getLatitude());
        return locationRepository.save(location);
    }
}
