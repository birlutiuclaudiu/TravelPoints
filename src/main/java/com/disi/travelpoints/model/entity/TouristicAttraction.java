package com.disi.travelpoints.model.entity;


import com.disi.travelpoints.model.enums.Category;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "touristic_attractions")
public class TouristicAttraction {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name", nullable = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @Column(name = "category", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "text_description", nullable = false)
    @NotNull
    @Size(min = 1, max = 10000)
    private String textDescription;

    @Column(name = "audio_description", nullable = false)
    @NotNull
    private byte[] audioDescription;

    @Column(name = "photo")
    private byte[] photo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    @OneToMany(mappedBy = "touristicAttraction")
    private Set<Offer> offers;

    @OneToMany(mappedBy = "touristicAttraction", cascade = CascadeType.ALL)
    private List<Visit> visits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Wishlist> wishlists;
}
