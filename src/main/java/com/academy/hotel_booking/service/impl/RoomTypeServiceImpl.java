package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.RoomTypeDto;
import com.academy.hotel_booking.model.entity.RoomType;
import com.academy.hotel_booking.model.repository.RoomTypeRepository;
import com.academy.hotel_booking.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    public void saveRoomType(RoomTypeDto roomTypeDto) {
        Optional<RoomType> existingRoomType = roomTypeRepository.findByName(roomTypeDto.getName());
        if (existingRoomType.isPresent()) {
            throw new RuntimeException("Room type already exists!");
        }
        RoomType roomType = new RoomType();
        roomType.setName(roomTypeDto.getName());
        roomType.setCapacity(roomTypeDto.getCapacity());
        roomType.setDescription(roomTypeDto.getDescription());
        RoomType savedRoomType = roomTypeRepository.save(roomType);
        convertToRoomTypeDto(savedRoomType);
    }

    @Override
    public void updateRoomType(RoomTypeDto roomTypeDto) {
        RoomType roomType = roomTypeRepository.findById(roomTypeDto.getId())
                .orElseThrow(() -> new RuntimeException("Room Type not found with id: " + roomTypeDto.getId()));
        roomType.setName(roomTypeDto.getName());
        roomType.setCapacity(roomTypeDto.getCapacity());
        roomType.setDescription(roomTypeDto.getDescription());
        RoomType updatedRoomType = roomTypeRepository.save(roomType);
        convertToRoomTypeDto(updatedRoomType);
    }

    @Override
    public void deleteRoomType(Integer roomTypeId) {
        if (!roomTypeRepository.existsById(roomTypeId)) {
            throw new RuntimeException("Room type not found with id: " + roomTypeId);
        }
        roomTypeRepository.deleteById(roomTypeId);
    }

    @Override
    public List<RoomTypeDto> findAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return roomTypes.stream().map(this::convertToRoomTypeDto).collect(Collectors.toList());
    }

    @Override
    public RoomTypeDto findRoomTypeById(Integer id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room Type not found with id: " + id));
        return convertToRoomTypeDto(roomType);
    }

    @Override
    public RoomTypeDto convertToRoomTypeDto(RoomType roomType) {
        RoomTypeDto dto = new RoomTypeDto();
        dto.setId(roomType.getId());
        dto.setName(roomType.getName());
        dto.setCapacity(roomType.getCapacity());
        dto.setDescription(roomType.getDescription());
        return dto;
    }

    @Override
    public RoomType convertToRoomType(RoomTypeDto roomTypeDto) {
        RoomType roomType = new RoomType();
        roomType.setId(roomTypeDto.getId());
        roomType.setName(roomTypeDto.getName());
        roomType.setCapacity(roomTypeDto.getCapacity());
        roomType.setDescription(roomTypeDto.getDescription());
        return roomType;
    }
}
