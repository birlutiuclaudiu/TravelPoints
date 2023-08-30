package com.disi.travelpoints.repository;

import com.disi.travelpoints.model.entity.Location;
import com.disi.travelpoints.model.entity.TouristicAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Query("SELECT l FROM Location l where l.touristicAttraction = :touristicAttraction")
    Location findByTouristicAttraction(TouristicAttraction touristicAttraction);
}
