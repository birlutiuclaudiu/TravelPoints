package com.disi.travelpoints.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "start_date", nullable = false)
    @NotNull
    private Timestamp startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull
    private Timestamp endDate;

    @Column(name = "price", nullable = false)
    @NotNull
    private Double price;

    @ManyToOne
    @JoinColumn(name="touristic_attractic_id", nullable=false)
    private TouristicAttraction touristicAttraction;
}
