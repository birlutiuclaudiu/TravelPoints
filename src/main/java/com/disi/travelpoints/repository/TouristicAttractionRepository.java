package com.disi.travelpoints.repository;

import com.disi.travelpoints.dto.TouristicAttractionDTO;
import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.model.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TouristicAttractionRepository extends JpaRepository<TouristicAttraction, UUID> {



    List<TouristicAttraction> findByCategory(Category category);
    @Query("SELECT t  from  TouristicAttraction  t WHERE " +
            "(lower(t.location.address) LIKE lower(concat('%', :location, '%')) OR " +
            " lower(t.location.country) LIKE lower(concat('%', :location, '%')) )" +
            "AND t.category=:category")
    List<TouristicAttraction> findByLocationAndCategory(String location, Category category);

    @Query("SELECT t  from  TouristicAttraction  t WHERE " +
            " lower(t.location.address) LIKE lower(concat('%', :location, '%')) OR " +
            " lower(t.location.country) LIKE lower(concat('%', :location, '%')) ")
    List<TouristicAttraction> findByLocation(String location);
}
