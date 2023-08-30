package com.disi.travelpoints.dto;

import com.disi.travelpoints.model.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {
    public Offer toOfferEntity(OfferDTO offerDTO) {
        return Offer.builder()
                .price(offerDTO.getPrice())
                .startDate(offerDTO.getStartDate())
                .endDate(offerDTO.getEndDate())
                .build();
    }
    public OfferDTO toOfferDTO(Offer offer) {
        return OfferDTO.builder()
                .id(offer.getId())
                .price(offer.getPrice())
                .startDate(offer.getStartDate())
                .endDate(offer.getEndDate())
                .build();
    }
}
