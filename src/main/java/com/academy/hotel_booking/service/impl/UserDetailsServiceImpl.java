package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.customerDto.UserDto;
import com.academy.hotel_booking.dto.customerDto.UserRegistrationDto;
import com.academy.hotel_booking.exception.UserNotFoundException;
import com.academy.hotel_booking.exception.UserAlreadyExistsException;
import com.academy.hotel_booking.model.entity.enums.Role;
import com.academy.hotel_booking.model.entity.User;
import com.academy.hotel_booking.model.repository.UserRepository;
import com.academy.hotel_booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {               //правильно

        if (userRepository.findByUsername(userRegistrationDto.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByEmail(userRegistrationDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email is already in use");
        }
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setEmail(userRegistrationDto.getEmail());
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setRoles(List.of(Role.ROLE_CUSTOMER));
        saveUser(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserDto userDto) {                                   //правильно
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));
        existingUser.setUsername(userDto.getUsername());
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(existingUser);
    }

    @Override
    public List<UserDto> findAll() {                          //правильно
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> {
            UserDto userDto = convertToUserDto(user);
            userDtos.add(userDto);
        });
        return userDtos;
    }

    @Override
    public UserDto findId(Integer id) {          //правильно
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToUserDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {                 //правильно
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    @Override
    public UserDto getCurrentUser() {                                    // правильно
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                User user = userRepository.findByUsername(username);
                if (user != null) {
                    return convertToUserDto(user);
                }
            }
        }
        throw new UserNotFoundException("Current user not found!");
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("User with name с именем " + username + " not found!");
        }
        return convertToUserDto(user);
    }


    @Override
    public UserDto convertToUserDto(User user) {                        //правильно
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

    @Override
    public User convertToUser(UserDto userDto) {                   //правильно
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }
}





