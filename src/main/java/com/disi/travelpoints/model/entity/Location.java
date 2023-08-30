package com.disi.travelpoints.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "country", nullable = false)
    @NotNull
    @Size(min = 1, max = 35)
    private String country;

    @Column(name = "address", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String address;

    @Column(name = "long", nullable = false)
    @NotNull
    private Double longitude;

    @Column(name = "lat", nullable = false)
    @NotNull
    private Double latitude;

    @OneToOne(mappedBy = "location")
    private TouristicAttraction touristicAttraction;
}
