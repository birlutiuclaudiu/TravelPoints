package com.disi.travelpoints.repository;

import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {
    @Query("SELECT w FROM Wishlist w where w.touristicAttraction = :touristicAttraction AND w.user= :travelUser")
    Wishlist findByTouristicAttractionAnUser(TouristicAttraction touristicAttraction, TravelUser travelUser);

    @Query("SELECT w FROM Wishlist w where w.touristicAttraction.id = :touristicAttractionId")
    List<Wishlist> findByTouristicAttractionId(UUID touristicAttractionId);

    @Query("SELECT w FROM Wishlist w where w.user =:travelUser")
    Set<Wishlist> findByTravelUser(TravelUser travelUser);
}
