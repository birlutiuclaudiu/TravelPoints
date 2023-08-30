package com.disi.travelpoints.repository;

import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.model.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {

    @Query("SELECT avg(v.review) FROM Visit v WHERE v.touristicAttraction =:touristicAttraction")
    Double findReview(TouristicAttraction touristicAttraction);

    List<Visit> findAllByTouristicAttractionId(UUID touristicAttractionId);
}
