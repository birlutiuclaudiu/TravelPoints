package com.disi.travelpoints.dto.visit;

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
public class VisitDTO {

    private UUID touristicAttractionId;
    private UUID travelUserId;
    private Review review;
    private String comment;
    private Timestamp timestamp;
    private Integer hour;
}
