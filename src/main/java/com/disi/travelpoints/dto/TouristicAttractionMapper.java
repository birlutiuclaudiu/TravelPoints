package com.disi.travelpoints.dto;

import com.disi.travelpoints.model.entity.TouristicAttraction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class TouristicAttractionMapper {
    private final LocationMapper locationMapper;
    public TouristicAttraction touristicAttractionToEntity(TouristicAttractionDTO touristicAttractionDTO) {
        return TouristicAttraction.builder()
                .name(touristicAttractionDTO.getName())
                .category(touristicAttractionDTO.getCategory())
                .textDescription(touristicAttractionDTO.getTextDescription())
                .audioDescription(touristicAttractionDTO.getAudioDescription())
                .photo(touristicAttractionDTO.getPhoto())
                .location(locationMapper.toLocationEntity(touristicAttractionDTO.getLocation()))
                .build();
    }

    public TouristicAttractionDTO touristicAttractionToDTO(TouristicAttraction touristicAttraction) {
        return TouristicAttractionDTO.builder()
                .id(touristicAttraction.getId())
                .name(touristicAttraction.getName())
                .category(touristicAttraction.getCategory())
                .textDescription(touristicAttraction.getTextDescription())
                .audioDescription(touristicAttraction.getAudioDescription())
                .photo(touristicAttraction.getPhoto())
                .location(locationMapper.toLocationDTO(touristicAttraction.getLocation()))
                .build();
    }
}
