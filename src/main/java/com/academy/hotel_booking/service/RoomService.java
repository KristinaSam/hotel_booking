package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.bookingDto.BookingDto;
import com.academy.hotel_booking.dto.roomDto.RoomDto;
import com.academy.hotel_booking.model.entity.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {
    void saveRoom(RoomDto roomDto);
    void updateRoom(RoomDto roomDto);
    void deleteRoom(Integer roomId);
    List<RoomDto> findAllRooms();
    RoomDto findRoomById(Integer roomId);
    List<RoomDto> getRoomsByIds(List<Integer> roomIds);
    boolean isRoomNumberExists(Integer roomNumber);
    boolean isRoomAvailable(RoomDto room, BookingDto bookingDto);
    List<RoomDto> getAvailableRooms();
    List<RoomDto> getUnavailableRooms();
    List<RoomDto> findAvailableRoomsForBooking(LocalDate checkInDate, LocalDate checkOutDate, Integer adultsCount, Integer childrenCount);
    RoomDto convertToRoomDto(Room room);
}
