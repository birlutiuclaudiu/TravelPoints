package com.disi.travelpoints.repository;

import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.TravelUserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TravelUserRepository extends JpaRepository<TravelUser, UUID> {


    Optional<TravelUser> findByEmail(String email);

    Page<TravelUser> findByRole(TravelUserRole role, Pageable pageable);

    List<TravelUser> findByRole(TravelUserRole role);
}
