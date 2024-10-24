package com.academy.hotel_booking.model.repository;

import com.academy.hotel_booking.model.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Integer> {
    Optional<Nutrition> findByName(String name);
}
