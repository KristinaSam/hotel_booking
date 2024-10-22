package com.academy.hotel_booking.dto.roomDto;

import com.academy.hotel_booking.dto.roomTypeDto.RoomTypeDto;
import com.academy.hotel_booking.model.entity.RoomType;
import jdk.jfr.Registered;
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
