package com.academy.hotel_booking.dto.roomTypeDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDto {
    private Integer id;
    private String name;
    private Integer capacity;
    private String description;
}
