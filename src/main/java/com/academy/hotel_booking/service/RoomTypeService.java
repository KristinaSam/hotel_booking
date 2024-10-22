package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.roomTypeDto.RoomTypeDto;
import com.academy.hotel_booking.model.entity.RoomType;

import java.util.List;

public interface RoomTypeService {
    void saveRoomType(RoomTypeDto roomTypeDto);
    void updateRoomType(RoomTypeDto roomTypeDto);
    void deleteRoomType(Integer id);
    List<RoomTypeDto> findAllRoomTypes();
    RoomTypeDto findRoomTypeById(Integer id);
    RoomType convertToRoomType(RoomTypeDto roomTypeDto);
    RoomTypeDto convertToRoomTypeDto(RoomType roomType);
}
