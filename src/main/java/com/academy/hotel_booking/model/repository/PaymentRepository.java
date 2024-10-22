package com.academy.hotel_booking.model.repository;

import com.academy.hotel_booking.model.entity.Booking;
import com.academy.hotel_booking.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBooking(Booking booking);

    List<Payment> findByBooking_CheckInDate(Date checkInDateTime);
    List<Payment> findByTotalCost(Double totalCost);


}
