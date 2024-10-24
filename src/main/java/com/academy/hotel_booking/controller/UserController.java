package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.UserDto;
import com.academy.hotel_booking.dto.UserRegistrationDto;
import com.academy.hotel_booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userRegistrationDto, Model model) {
        try {
            userService.registerUser(userRegistrationDto);
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return "register";
        }
        model.addAttribute("message", "Welcome to our Hotel Booking System!");
        return "customer/success";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal.getName();
        UserDto user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "customer/dashboard";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/dashboard/{userId}")
    public String getUserProfile(@PathVariable Integer userId, Model model) {
        UserDto userDto = userService.findId(userId);
        if (userDto == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        model.addAttribute("user", userDto);
        return "customer/dashboard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users")
    public String getUsers(Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/usersList";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users/{id}")
    public String getUser(@PathVariable Integer id, Model model ) {
        UserDto user = userService.findId(id);
        model.addAttribute("user", user);
        return "admin/userDetails";

    }


}
