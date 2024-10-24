package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.UserDto;
import com.academy.hotel_booking.dto.AdditionalServiceDto;
import com.academy.hotel_booking.dto.BookingDto;
import com.academy.hotel_booking.dto.NutritionDto;
import com.academy.hotel_booking.dto.RoomDto;
import com.academy.hotel_booking.model.entity.*;
import com.academy.hotel_booking.model.entity.enums.BookingStatus;
import com.academy.hotel_booking.model.repository.*;
import com.academy.hotel_booking.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final AdditionalServiceService additionalServiceService;
    private final NutritionService nutritionService;
    private final RoomService roomService;
    private final RoomTypeService roomTypeService;
    private final UserDetailsServiceImpl userDetailsService;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto deleteBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        bookingRepository.delete(booking);
        return null;
    }

    @Override
    public List<BookingDto> findAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto findBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        return convertToBookingDto(booking);
    }

    @Override
    public BookingDto convertToBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());

        if (booking.getUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(booking.getUser().getId());
            userDto.setUsername(booking.getUser().getUsername());
            userDto.setFirstName(booking.getUser().getFirstName());
            userDto.setLastName(booking.getUser().getLastName());
            userDto.setEmail(booking.getUser().getEmail());
            userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
            bookingDto.setUserDto(userDto);
        }
        List<RoomDto> roomDtos = booking.getRooms().stream()
                .map(room -> new RoomDto(
                        room.getId(),
                        roomTypeService.convertToRoomTypeDto(room.getRoomType()),
                        room.getNumber(),
                        room.getAvailability(),
                        room.getPrice()))
                .collect(Collectors.toList());
        bookingDto.setRoomDtos(roomDtos);

        NutritionDto nutritionDto = new NutritionDto(
                booking.getNutrition().getId(),
                booking.getNutrition().getName(),
                booking.getNutrition().getDescription(),
                booking.getNutrition().getPrice());
        bookingDto.setNutritionDto(nutritionDto);

        bookingDto.setCheckInDate(booking.getCheckInDate());
        bookingDto.setCheckOutDate(booking.getCheckOutDate());
        bookingDto.setAdultsCount(booking.getAdultsCount());
        bookingDto.setChildrenCount(booking.getChildrenCount());

        List<AdditionalServiceDto> additionalServiceDtos = booking.getAdditionalServices().stream()
                .map(service -> new AdditionalServiceDto(
                        service.getId(),
                        service.getName(),
                        service.getDescription(),
                        service.getPrice()))
                .collect(Collectors.toList());
        bookingDto.setAdditionalServiceDtos(additionalServiceDtos);

        bookingDto.setBookingStatus(BookingStatus.PENDING);

        return bookingDto;
    }

    @Override
    public Booking creationBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        UserDto currentUser = userDetailsService.getCurrentUser();
        booking.setUser(userDetailsService.convertToUser(currentUser));
        bookingDto.setUserDto(currentUser);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setAdultsCount(bookingDto.getAdultsCount());
        booking.setChildrenCount(bookingDto.getChildrenCount());
        booking.setBookingStatus(BookingStatus.PENDING);
        Booking savedBooking = bookingRepository.save(booking);
        bookingDto.setId(savedBooking.getId());
        return savedBooking;

    }

    @Override
    public void addSelectedRoomsToBooking(BookingDto bookingDto, List<Integer> selectedRoomIds) {
        Booking booking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingDto.getId()));
        List<RoomDto> selectedRooms = roomService.getRoomsByIds(selectedRoomIds);
        for (RoomDto roomDto : selectedRooms) {
            Room room = roomService.convertToRoom(roomDto);
            booking.getRooms().add(room);
        }
        bookingRepository.save(booking);
    }

    @Override
    public void addSelectedNutritionToBooking(BookingDto bookingDto, Integer selectedNutritionId) {
        Booking booking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingDto.getId()));
        NutritionDto nutritionDto = nutritionService.findNutritionById(selectedNutritionId);
        booking.setNutrition(nutritionService.convertToNutrition(nutritionDto));
        bookingRepository.save(booking);
    }

    public void addSelectedServicesToBooking(BookingDto bookingDto, List<Integer> selectedAdditionalServiceIds) {
        Booking booking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingDto.getId()));
        List<AdditionalServiceDto> selectedAdditionalServices = additionalServiceService.findAllAdditionalServicesByIds(selectedAdditionalServiceIds);
        for (AdditionalServiceDto additionalServiceDto : selectedAdditionalServices) {
            AdditionalService additionalService = additionalServiceService.convertToAdditionalService(additionalServiceDto);
            booking.getAdditionalServices().add(additionalService);
        }
        bookingRepository.save(booking);
    }

    @Override
    public Boolean checkCapacityToRooms(BookingDto bookingDto) {
        List<RoomDto> selectedRooms = bookingDto.getRoomDtos();
        int totalCapacity = selectedRooms.stream()
                .mapToInt(room -> room.getRoomTypeDto().getCapacity())
                .sum();
        int totalGuests = bookingDto.getAdultsCount() + bookingDto.getChildrenCount();
        return totalCapacity <= totalGuests;
    }

    @Override
    public void confirmBooking(BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingDto.getId()));
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingDto.getId()));
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public List<BookingDto> getBookingsByUsername(String username) {
        List<Booking> bookings = bookingRepository.findByUserUsername(username);

        return bookings.stream()
                .map(this::convertToBookingDto) // Используем метод преобразования
                .collect(Collectors.toList());
    }
}
