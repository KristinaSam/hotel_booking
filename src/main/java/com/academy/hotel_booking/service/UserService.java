package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.UserDto;
import com.academy.hotel_booking.dto.UserRegistrationDto;
import com.academy.hotel_booking.model.entity.User;

import java.util.List;

public interface UserService {
    void registerUser(UserRegistrationDto userRegistrationDto);

    void saveUser(User user);

    void updateUser(UserDto userDto);

    List<UserDto> findAll();

    UserDto findId(Integer id);

    UserDto getCurrentUser();

    User convertToUser(UserDto userDto);

    UserDto convertToUserDto(User user);

    UserDto findByUsername(String username);
}
