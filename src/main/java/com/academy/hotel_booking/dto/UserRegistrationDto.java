package com.academy.hotel_booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDto {
    private Integer id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Username is required")
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
