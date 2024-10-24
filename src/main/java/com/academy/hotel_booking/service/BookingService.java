package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.BookingDto;
import com.academy.hotel_booking.dto.UserDto;
import com.academy.hotel_booking.model.entity.Booking;

import java.util.List;

public interface BookingService {

    List<BookingDto> findAllBookings();

    BookingDto findBookingById(Integer id);

    BookingDto convertToBookingDto(Booking booking);

    Booking creationBooking(BookingDto bookingDto);

    void addSelectedRoomsToBooking(BookingDto bookingDto, List<Integer> selectedRoomIds);

    void addSelectedNutritionToBooking(BookingDto bookingDto, Integer selectedNutritionId);

    void addSelectedServicesToBooking(BookingDto bookingDto, List<Integer> selectedAdditionalServiceIds);

    Boolean checkCapacityToRooms(BookingDto bookingDto);

    void confirmBooking(BookingDto bookingDto);

    void cancelBooking(BookingDto bookingDto);
    List<BookingDto> getBookingsByUsername(String username);

}
