package com.academy.hotel_booking.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "room")
@ToString(exclude = "roomType")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", nullable = false)
    @JsonManagedReference
    private RoomType roomType;

    @Column(name = "room_number", nullable = false, unique = true)
    private Integer number;

    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "rooms")
    @JsonBackReference
    private List<Booking> bookingList;
}
