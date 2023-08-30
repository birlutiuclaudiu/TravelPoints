package com.disi.travelpoints.repository;

import com.disi.travelpoints.model.entity.Offer;
import com.disi.travelpoints.model.entity.TouristicAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {
    List<Offer> findByTouristicAttractionId(UUID touristicAttractionId);

    Optional<Offer> findTopByTouristicAttractionOrderByEndDate(TouristicAttraction touristicAttraction);
}
