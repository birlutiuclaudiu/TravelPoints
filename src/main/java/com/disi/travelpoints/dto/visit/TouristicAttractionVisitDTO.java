package com.disi.travelpoints.dto.visit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TouristicAttractionVisitDTO {
    private String touristicAttractionName;
    private Long numberOfVisits;
}
