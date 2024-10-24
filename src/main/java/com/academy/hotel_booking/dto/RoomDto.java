package com.academy.hotel_booking.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Integer id;
    private RoomTypeDto roomTypeDto;
    private Integer number;
    private Boolean availability;
    private Double price;
}
