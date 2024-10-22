package com.academy.hotel_booking.model.repository;

import com.academy.hotel_booking.model.entity.Booking;
import com.academy.hotel_booking.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository <Booking, Integer> {
    List<Booking> findByUser(User user);
    List<Booking> findByCheckInDate(LocalDate checkInDate);
    List<Booking> findByCheckOutDate(LocalDate checkOutDate);
    List<Booking> findByAdultsCountAndChildrenCount(Integer adultsCount, Integer childrenCount);
    List<Booking> findByRooms_Id(Integer roomId);
}
