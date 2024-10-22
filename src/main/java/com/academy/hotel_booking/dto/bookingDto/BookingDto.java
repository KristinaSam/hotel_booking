package com.academy.hotel_booking.dto.bookingDto;


import com.academy.hotel_booking.dto.additionalServiceDto.AdditionalServiceDto;
import com.academy.hotel_booking.dto.customerDto.UserDto;
import com.academy.hotel_booking.dto.nutritionDto.NutritionDto;
import com.academy.hotel_booking.dto.roomDto.RoomDto;
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
    private List<BookingStatus> bookingStatuses;
}
