package com.disi.travelpoints.dto.visit;

import com.disi.travelpoints.dto.TravelUserDTO;
import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.entity.Visit;
import com.disi.travelpoints.model.enums.Review;

public final class VisitMapper {

    public static Visit toEntity(VisitDTO visitDTO){
        TouristicAttraction touristicAttraction = new TouristicAttraction();
        touristicAttraction.setId(visitDTO.getTouristicAttractionId());
        TravelUser travelUser = new TravelUser();
        travelUser.setId(visitDTO.getTravelUserId());
        return Visit.builder()
                .touristicAttraction(touristicAttraction)
                .travelUser(travelUser)
                .visitDate(visitDTO.getTimestamp())
                .comment(visitDTO.getComment())
                .review(visitDTO.getReview().ordinal())
                .build();
    }

    public static ReviewDTO toReviewDTO(Visit visit){
        return ReviewDTO.builder()
                .touristicAttractionDTOId(visit.getTouristicAttraction().getId())
                .travelUserDTO(TravelUserDTO.builder()
                        .lastName(visit.getTravelUser().getLastName())
                        .firstName(visit.getTravelUser().getFirstName())
                        .email(visit.getTravelUser().getEmail()).build())
                .timestamp(visit.getVisitDate())
                .comment(visit.getComment())
                .review(visit.getReview()!=null?Review.values()[visit.getReview()]:null)
                .build();
    }

}
