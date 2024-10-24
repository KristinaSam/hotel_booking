package com.academy.hotel_booking.model.repository;

import com.academy.hotel_booking.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository <Booking, Integer> {
    List<Booking> findByRooms_Id(Integer roomId);
    List<Booking> findByUserUsername(String username);
}
