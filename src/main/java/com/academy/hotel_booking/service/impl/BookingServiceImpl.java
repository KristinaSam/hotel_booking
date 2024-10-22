package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.customerDto.UserDto;
import com.academy.hotel_booking.dto.additionalServiceDto.AdditionalServiceDto;
import com.academy.hotel_booking.dto.bookingDto.BookingDto;
import com.academy.hotel_booking.dto.nutritionDto.NutritionDto;
import com.academy.hotel_booking.dto.roomDto.RoomDto;
import com.academy.hotel_booking.model.entity.*;
import com.academy.hotel_booking.model.entity.enums.BookingStatus;
import com.academy.hotel_booking.model.repository.*;
import com.academy.hotel_booking.service.BookingService;

import com.academy.hotel_booking.service.RoomTypeService;
import com.academy.hotel_booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final RoomTypeService roomTypeService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final NutritionRepository nutritionRepository;
    private final AdditionalServiceRepository additionalServiceRepository;

    @Override
    public void createBooking(Booking booking, BookingDto bookingDto) {
        if (bookingDto.getUserDto() != null) {
            User user = userDetailsService.convertToUser(bookingDto.getUserDto());
            booking.setUser(user);
        }

        List<Room> rooms = roomRepository.findAllById(bookingDto.getRoomDtos().stream()
                .map(RoomDto::getId)
                .collect(Collectors.toList()));
        booking.setRooms(rooms);

        Nutrition nutrition = nutritionRepository.findById(bookingDto.getNutritionDto().getId())
                .orElseThrow(() -> new RuntimeException("Nutrition not found"));
        booking.setNutrition(nutrition);

        List<AdditionalService> additionalServices = additionalServiceRepository.findAllById(
                bookingDto.getAdditionalServiceDtos().stream()
                        .map(AdditionalServiceDto::getId)
                        .collect(Collectors.toList())
        );
        booking.setAdditionalServices(additionalServices);

        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setAdultsCount(bookingDto.getAdultsCount());
        booking.setChildrenCount(bookingDto.getChildrenCount());

        if (booking.getId() == null) {
            booking.setBookingStatuses(List.of(BookingStatus.PENDING));
        } else {
            booking.setBookingStatuses(bookingDto.getBookingStatuses());
        }
    }

    @Override
    public void saveBooking(BookingDto bookingDto) {
        Booking booking;
        if (bookingDto.getId() != null) {
            booking = bookingRepository.findById(bookingDto.getId())
                    .orElseThrow(() -> new RuntimeException("Booking not found"));
        } else {
            booking = new Booking();
        }
        createBooking(booking, bookingDto);
        bookingRepository.save(booking);
    }

    @Override
    public void updateBooking(BookingDto bookingDto) {
        if (bookingDto.getId() == null) {
            throw new IllegalArgumentException("Booking ID must not be null");
        }
        Booking booking = bookingRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        createBooking(booking, bookingDto);
        bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        bookingRepository.delete(booking);
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

        bookingDto.setBookingStatuses(List.of(BookingStatus.PENDING));

        return bookingDto;
    }
}