package com.disi.travelpoints.dto.visit;

import com.disi.travelpoints.dto.TouristicAttractionDTO;
import com.disi.travelpoints.dto.TravelUserDTO;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.Review;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    @NotNull
    private UUID touristicAttractionDTOId;
    @NotNull
    private TravelUserDTO travelUserDTO;
    private Review review;
    private String comment;
    private Timestamp timestamp;
}
