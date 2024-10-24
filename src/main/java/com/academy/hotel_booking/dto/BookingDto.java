package com.academy.hotel_booking.dto;


import com.academy.hotel_booking.model.entity.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Integer id;
    private UserDto userDto;
    private List<RoomDto> roomDtos;
    private NutritionDto nutritionDto;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer adultsCount;
    private Integer childrenCount;
    private List<AdditionalServiceDto> additionalServiceDtos;
    private BookingStatus bookingStatus;
}
