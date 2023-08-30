package com.disi.travelpoints.repository;

import com.disi.travelpoints.model.entity.AccountTokenValidation;
import com.disi.travelpoints.model.entity.TravelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenValidationRepository extends JpaRepository<AccountTokenValidation, UUID> {
    Optional<AccountTokenValidation> findByToken(String token);


    void deleteByTravelUser(TravelUser travelUser);
}
