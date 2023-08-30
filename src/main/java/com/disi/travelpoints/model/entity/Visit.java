package com.disi.travelpoints.model.entity;

import com.disi.travelpoints.model.enums.Review;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "touristic_attraction_id", nullable = false)
    private TouristicAttraction touristicAttraction;

    @ManyToOne
    @JoinColumn(name = "travel_user_id", nullable = false)
    private TravelUser travelUser;

    @Column(name = "visit_date", nullable = false)
    @NotNull
    private Timestamp visitDate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "review")
    private Integer review;

}
