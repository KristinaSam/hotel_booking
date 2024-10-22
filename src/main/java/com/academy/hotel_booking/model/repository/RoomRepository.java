package com.academy.hotel_booking.model.repository;

import com.academy.hotel_booking.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByIdIn(List<Integer> ids);
    List<Room> findByAvailabilityTrue();
    List<Room> findByAvailabilityFalse();
    boolean existsByNumber(Integer number);
    List<Room> findAllById(Iterable<Integer> selectedRoomIds);

}
