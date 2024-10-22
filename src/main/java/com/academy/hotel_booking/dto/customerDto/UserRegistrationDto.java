package com.academy.hotel_booking.dto.customerDto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
