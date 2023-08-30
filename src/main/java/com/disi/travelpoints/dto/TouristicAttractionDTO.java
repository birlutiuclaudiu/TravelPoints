package com.disi.travelpoints.dto;

import com.disi.travelpoints.model.entity.Location;
import com.disi.travelpoints.model.entity.Offer;
import com.disi.travelpoints.model.enums.Category;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TouristicAttractionDTO {
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private Category category;

    @NotNull
    private String textDescription;

    @NotNull
    private byte[] audioDescription;

    @NotNull
    private byte[] photo;

    @NotNull
    private LocationDTO location;

    private OfferDTO lastOffer;

    private Double review;

    private boolean onWishlist;

}
