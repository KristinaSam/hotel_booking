package com.academy.hotel_booking.model.repository;

import com.academy.hotel_booking.model.entity.AdditionalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdditionalServiceRepository extends JpaRepository<AdditionalService, Integer> {
    Optional<AdditionalService> findByName(String name);

}
