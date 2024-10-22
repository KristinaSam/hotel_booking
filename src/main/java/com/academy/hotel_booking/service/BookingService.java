package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.bookingDto.BookingDto;
import com.academy.hotel_booking.model.entity.Booking;

import java.util.List;

public interface BookingService {
//    List<Booking> getAllBookings();
//    void saveBooking(Booking booking);
//    Booking getBookingById(Integer id);
//    void deleteBookingById(Integer id);
//    List<Booking> getBookingsByClientId(Integer clientId);
//    List<Booking> getBookingsByAdditionalService(AdditionalService additionalService);
//    //List<Booking> getBookingsByNutrition(Nutrition nutrition);
//    Integer countOfDaysBooked (Booking booking);
//   double totalCostOfBooking (Booking booking);

    void createBooking(Booking booking, BookingDto bookingDto);
    void saveBooking(BookingDto bookingDto);
    void updateBooking(BookingDto bookingDto);
    void deleteBooking(Integer id);
    List<BookingDto> findAllBookings();
    BookingDto findBookingById(Integer id);
    BookingDto convertToBookingDto(Booking booking);
}
