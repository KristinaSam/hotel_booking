package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.bookingDto.BookingDto;
import com.academy.hotel_booking.dto.roomDto.RoomDto;
import com.academy.hotel_booking.model.entity.Booking;
import com.academy.hotel_booking.model.entity.Room;
import com.academy.hotel_booking.model.repository.BookingRepository;
import com.academy.hotel_booking.model.repository.RoomRepository;
import com.academy.hotel_booking.service.RoomService;
import com.academy.hotel_booking.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomTypeService roomTypeService;

    @Override
    public void saveRoom(RoomDto roomDto) {
        if (roomRepository.existsByNumber(roomDto.getNumber())) {
            throw new RuntimeException("Room number already exists: " + roomDto.getNumber());
        }
        Room room = new Room();
        room.setRoomType(roomTypeService.convertToRoomType(roomDto.getRoomTypeDto()));
        room.setNumber(roomDto.getNumber());
        room.setAvailability(roomDto.getAvailability());
        room.setPrice(roomDto.getPrice());
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(RoomDto roomDto) {
        Room existingRoom = roomRepository.findById(roomDto.getId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomDto.getId()));
        existingRoom.setRoomType(roomTypeService.convertToRoomType(roomDto.getRoomTypeDto()));
        existingRoom.setNumber(roomDto.getNumber());
        existingRoom.setAvailability(roomDto.getAvailability());
        existingRoom.setPrice(roomDto.getPrice());

        roomRepository.save(existingRoom);
    }

    @Override
    public void deleteRoom(Integer roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
        roomRepository.deleteById(roomId);
    }

    @Override
    public List<RoomDto> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(this::convertToRoomDto).toList();
    }

    @Override
    public RoomDto findRoomById(Integer roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            return convertToRoomDto(room);
        } else {
            throw new RuntimeException("Room not found with id: " + roomId);
        }
    }
    public List<RoomDto> getRoomsByIds(List<Integer> roomIds) {
        List<Room> rooms = roomRepository.findAllById(roomIds);
        return rooms.stream().map(this::convertToRoomDto).collect(Collectors.toList());
    }

    @Override
    public boolean isRoomNumberExists(Integer roomNumber) {
        return roomRepository.existsByNumber(roomNumber);
    }



    @Override
    public List<RoomDto> getAvailableRooms() {
        List<Room> rooms = roomRepository.findByAvailabilityTrue();
        return rooms.stream().map(this::convertToRoomDto).collect(Collectors.toList());
    }

    @Override
    public List<RoomDto> getUnavailableRooms() {
        List<Room> rooms = roomRepository.findByAvailabilityFalse();
        return rooms.stream().map(this::convertToRoomDto).collect(Collectors.toList());
    }

    @Override
    public RoomDto convertToRoomDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomTypeDto(roomTypeService.convertToRoomTypeDto(room.getRoomType()));
        roomDto.setNumber(room.getNumber());
        roomDto.setAvailability(room.getAvailability());
        roomDto.setPrice(room.getPrice());
        return roomDto;
    }

    @Override
    public boolean isRoomAvailable(RoomDto room, BookingDto bookingDto) {
        List<Booking> bookings = bookingRepository.findByRooms_Id(room.getId());
        for (Booking booking : bookings) {
            LocalDate existingCheckInDate = booking.getCheckInDate();
            LocalDate existingCheckOutDate = booking.getCheckOutDate();
            if ((bookingDto.getCheckInDate().isBefore(existingCheckOutDate) && bookingDto.getCheckOutDate().isAfter(existingCheckInDate))) {
                return false;                                                                  //может ошибку бросить, комната недоступна!!!
            }
        }
        return true;
    }
    @Override
    public List<RoomDto> findAvailableRoomsForBooking(LocalDate checkInDate, LocalDate checkOutDate, Integer adultsCount, Integer childrenCount) {
        List<Room> allAvailableRooms = roomRepository.findByAvailabilityTrue();
        List<Room> filteredRooms = allAvailableRooms.stream()
                .filter(room -> {
                    boolean isBooked = room.getBookingList().stream().anyMatch(booking ->
                            (booking.getCheckInDate().isBefore(checkOutDate) && booking.getCheckOutDate().isAfter(checkInDate))
                    );
                    return !isBooked;
                })
                .filter(room -> {
                    return room.getRoomType().getCapacity() >= (adultsCount + childrenCount);
                })
                .toList();
        return filteredRooms.stream()
                .map(this::convertToRoomDto)
                .collect(Collectors.toList());
    }

    public int calculateTotalCapacity(List<Integer> selectedRoomIds) {
        return roomRepository.findAllById(selectedRoomIds)
                .stream()
                .mapToInt(room -> room.getRoomType().getCapacity())
                .sum();
    }
}
